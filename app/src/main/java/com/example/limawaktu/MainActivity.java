package com.example.limawaktu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // JAM SET 8 NE MUKLAS!!!
    // CATATAN BESOK
    //BUAT WIDGET, COUNTDOWN DIGANTI TANGGAL
    //BUAT NOTIFIKASI
    //BUAT FITUR GEOLOCATION, KALAU BISA


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnJadwal = findViewById(R.id.jadwalSholat);
        btnJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JadwalActivity.class);
                startActivity(intent);
            }
        });
    }
}
