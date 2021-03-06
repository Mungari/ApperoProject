package com.example.jonny.googlemapsdonebyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       LinearLayout btnPerc = findViewById(R.id.mapMarker);
        btnPerc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PercListActivity.class);
                startActivity(i);
            }
        });
        LinearLayout btnQr = findViewById(R.id.btnCode);
        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QrReaderActivity.class);
                startActivity(i);
            }
        });
    }
}
