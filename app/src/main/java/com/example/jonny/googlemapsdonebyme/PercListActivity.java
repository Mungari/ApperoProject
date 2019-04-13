package com.example.jonny.googlemapsdonebyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class PercListActivity extends AppCompatActivity {

    ListView list;
    String valueToPass = "Percorso";

    String[] Nome = {
            "Prova 1", "Prova 2",
            "Prova 3", "Prova 4",
            "Prova 5",
    };

    String[] Descrizione = {
            "Descrizione 1", "Descrizione 2",
            "Descrizione 3", "Descrizione 4",
            "Descrizione 5",
    };

    Integer[] Immagini = {
            R.drawable.download, R.drawable.corso_mille7,
            R.drawable.foto, R.drawable.download,
            R.drawable.foto,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perclist);

        final String percorso = new String();
        Modello_Standard adapter = new Modello_Standard(this, Nome, Descrizione, Immagini);
        list = findViewById(R.id.list);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    Intent goToApp = new Intent(PercListActivity.this, MapsActivity.class);
                    goToApp.putExtra(valueToPass, "P1");
                    startActivity(goToApp);
                    //code specific to first list item
                    Toast.makeText(getApplicationContext(), "Place Your First Option Code", Toast.LENGTH_SHORT).show();
                } else if (position == 1) {
                    Intent goToApp = new Intent(PercListActivity.this, MapsActivity.class);
                    goToApp.putExtra(valueToPass, "P2");
                    startActivity(goToApp);
                    //code specific to 2nd list item
                    Toast.makeText(getApplicationContext(), "Place Your Second Option Code", Toast.LENGTH_SHORT).show();
                } else if (position == 2) {

                    Toast.makeText(getApplicationContext(), "Place Your Third Option Code", Toast.LENGTH_SHORT).show();
                } else if (position == 3) {

                    Toast.makeText(getApplicationContext(), "Place Your Forth Option Code", Toast.LENGTH_SHORT).show();
                } else if (position == 4) {

                    Toast.makeText(getApplicationContext(), "Place Your Fifth Option Code", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


}