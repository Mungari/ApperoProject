package com.example.jonny.googlemapsdonebyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

public class webActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        WebView web = findViewById(R.id.web);
        ImageButton btnMap = findViewById(R.id.btnMap);
        ImageButton btnHome = findViewById(R.id.btnHome);

        String Valore = getIntent().getStringExtra("Informazioni");


        // elementi contiene il nome del percorso e il link passato dall'Activity di GMaps
        String[] elementi = Valore.split(",");
        final String percorso = elementi[0];
        String Link = elementi[1];
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(Link);
        web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Percorso = "Percorso";
                Intent goToApp = new Intent(webActivity.this, MapsActivity.class);
                goToApp.putExtra("Percorso", percorso); // il  valore deve essere una stringa
                startActivity(goToApp);
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(webActivity.this,MainActivity.class);
                startActivity(i);
            }
        });


    }


}


