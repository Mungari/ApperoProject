package com.example.jonny.googlemapsdonebyme;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Modello_Standard extends ArrayAdapter {


    private final Activity context;
    private final String[] nome;
    private final String[] descrizione;
    private final Integer[] immagini;

    public Modello_Standard(Activity context, String[] Nome, String[] Descrizione, Integer[] Immagini) {
        super(context, R.layout.row_layout, Nome);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.nome = Nome;
        this.descrizione = Descrizione;
        this.immagini = Immagini;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.row_layout, null, true);

        TextView Nome = rowView.findViewById(R.id.nome);
        ImageView Immagine = rowView.findViewById(R.id.icona);
        TextView Descrizione = rowView.findViewById(R.id.descrizione);

        Nome.setText(nome[position]);
        Immagine.setImageResource(immagini[position]);
        Descrizione.setText(descrizione[position]);

        return rowView;


    }
}
