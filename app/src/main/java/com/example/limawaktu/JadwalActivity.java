package com.example.limawaktu;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class JadwalActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView jamSubuh;
    private TextView jamDzuhur;
    private TextView jamAsar;
    private TextView jamMaghrib;
    private TextView jamIsya;
    private TextView tanggal;
    private TextView nextSholat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jadwal_sholat);
        ImageButton btnBack = findViewById(R.id.btnBack);
        jamSubuh = findViewById(R.id.jamSubuh);
        jamDzuhur = findViewById(R.id.jamDuhur);
        jamAsar = findViewById(R.id.jamAsar);
        jamMaghrib = findViewById(R.id.jamMaghrib);
        jamIsya = findViewById(R.id.jamIsya);
        tanggal = findViewById(R.id.tanggal);
        nextSholat = findViewById(R.id.namaSholat);
        btnBack.setOnClickListener(this);
        try {
            loadJadwal();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
        }
    }

    private void loadJadwal() throws JSONException {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df2 = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        String formattedDate2 = df2.format(c);
        tanggal.setText(formattedDate2);

        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate3 = df3.format(c);

        API.get("format/json/jadwal/kota/783/tanggal/"+formattedDate3, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("TES1", response.toString());
                try {
                    JSONObject jadwal = response.getJSONObject("jadwal");
                    Log.d("TES4", jadwal.toString());
                    JSONObject data = jadwal.getJSONObject("data");
                    String subuh = data.getString("subuh");
                    String dzuhur = data.getString("dzuhur");
                    String ashar = data.getString("ashar");
                    String maghrib = data.getString("maghrib");
                    String isya = data.getString("isya");
                    jamSubuh.setText(subuh);
                    jamDzuhur.setText(dzuhur);
                    jamAsar.setText(ashar);
                    jamMaghrib.setText(maghrib);
                    jamIsya.setText(isya);

                    MyReceiver alarmReceiver = new MyReceiver();
                    alarmReceiver.setRepeatingAlarm(getApplicationContext(), MyReceiver.TYPE_REPEATING,subuh,"waktu sholat Subuh");
                    alarmReceiver.setRepeatingAlarm(getApplicationContext(), MyReceiver.TYPE_REPEATING,dzuhur,"waktu sholat Dzuhur");
                    alarmReceiver.setRepeatingAlarm(getApplicationContext(), MyReceiver.TYPE_REPEATING,ashar,"waktu sholat ashar");
                    alarmReceiver.setRepeatingAlarm(getApplicationContext(), MyReceiver.TYPE_REPEATING,maghrib,"waktu sholat magrib");
                    alarmReceiver.setRepeatingAlarm(getApplicationContext(), MyReceiver.TYPE_REPEATING,isya,"waktu sholat isya");

                    try {
                        Date d1 = new SimpleDateFormat("HH:mm").parse(subuh);
                        Calendar calSubuh = Calendar.getInstance();
                        calSubuh.setTime(d1);
                        Date d2 = new SimpleDateFormat("HH:mm").parse(dzuhur);
                        Calendar calDzuhur = Calendar.getInstance();
                        calDzuhur.setTime(d2);
                        Date d3 = new SimpleDateFormat("HH:mm").parse(ashar);
                        Calendar calAshar = Calendar.getInstance();
                        calAshar.setTime(d3);
                        Date d4 = new SimpleDateFormat("HH:mm").parse(maghrib);
                        Calendar calMaghrib = Calendar.getInstance();
                        calMaghrib.setTime(d4);
                        Date d5 = new SimpleDateFormat("HH:mm").parse(isya);
                        Calendar calIsya = Calendar.getInstance();
                        calIsya.setTime(d5);

                        Date x = Calendar.getInstance().getTime();
                        if(x.before(calSubuh.getTime())){
                            nextSholat.setText("Subuh");
                            Log.d("TES6", "err1");
                        } else if(x.before(calDzuhur.getTime())){
                            nextSholat.setText("Dzuhur");
                            Log.d("TES6", "err2");
                        } else if(x.before(calAshar.getTime())){
                            nextSholat.setText("Ashar");
                            Log.d("TES6", "err3");
                        } else if(x.before(calMaghrib.getTime())){
                            nextSholat.setText("Maghrib");
                            Log.d("TES6", "err4");
                        }else if(x.before(calIsya.getTime())){
                            nextSholat.setText("Isya");
                            Log.d("TES6", "err5");
                        } else {
                            Log.d("TES6", "err "+x+" "+calMaghrib.getTime());
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.d("TES7", "err6");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
//                JSONObject firstEvent = timeline.get(0);
//                String tweetText = firstEvent.getString("text");
//
//                // Do something with the response
//                System.out.println(tweetText);
                Log.d("TES2", timeline.toString());
            }
        });
    }
}
