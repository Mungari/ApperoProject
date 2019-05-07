package com.example.jonny.googlemapsdonebyme;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Marker myMarker;
    GoogleMap mMap;
    Marcatore MarkOrigin, MarkDest, Marker;
    List<Marcatore> Intermedi = new ArrayList<Marcatore>();
    String Percorso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageButton home = findViewById(R.id.btnHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, PercListActivity.class);
                startActivity(i);
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ottieniJson();
        //Initialize Google Play Services

        // Setting onclick event listener for the map


    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;

    }


    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                // Starts parsing data
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);


            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
            }
        }
    }


    public void Gmap() {
        String waypoints = "";
        mMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(MarkOrigin.lat), Float.parseFloat(MarkOrigin.lon))).title("origine"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(MarkDest.lat), Float.parseFloat(MarkDest.lon))).title("destinazione"));
        for (Marcatore elemento : Intermedi) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(elemento.lat), Float.parseFloat(elemento.lon))).title(String.valueOf(Intermedi.indexOf(elemento) + 1)));
            waypoints += elemento.lat + "," + elemento.lon + "|";
        }
        waypoints = waypoints.substring(0, waypoints.length() - 1);

        FetchUrl FetchUrl = new FetchUrl();
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + MarkOrigin.lat + "," + MarkOrigin.lon + "&destination=" + MarkDest.lat + "," + MarkDest.lon + "&waypoints=" + waypoints + "&mode=walking&key=AIzaSyAd5kzmwbUWadc2hLxAeEVEiKjdpkZBRHA";
        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
        //move map camera

        LatLng camera = new LatLng(45.651046, 9.596216);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(camera));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String Informazioni = "Informazioni";
                if (marker.getTitle().equals("origine")) {
                    Intent goToApp = new Intent(MapsActivity.this, webActivity.class);
                    goToApp.putExtra(Informazioni, Percorso + "," + MarkOrigin.link); // il  valore deve essere una stringa
                    startActivity(goToApp);
                } else if (marker.getTitle().equals("destinazione")) {
                    {
                        Intent goToApp = new Intent(MapsActivity.this, webActivity.class);
                        goToApp.putExtra(Informazioni, Percorso + "," + MarkDest.link); // il  valore deve essere una stringa
                        startActivity(goToApp);
                    }
                } else {
                    for (Marcatore elemento : Intermedi) {
                        if (marker.getTitle().equals(elemento.id)) {
                            Intent goToApp = new Intent(MapsActivity.this, webActivity.class);
                            goToApp.putExtra(Informazioni, Percorso + "," + elemento.link); // il  valore deve essere una stringa
                            startActivity(goToApp);
                        }
                    }
                }

                return true;
            }
        });
    }

    public void ottieniJson() {
        Percorso = getIntent().getStringExtra("Percorso");
        String lat = "";
        String lon = "";
        String link = "";
        String id = "";

        String json;
        try {
            InputStream is = getAssets().open("document");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray array = new JSONArray(json);


            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("idPercorso");
                if (name.equals(Percorso)) {
                    JSONArray origine = obj.getJSONArray("origine");
                    for (int j = 0; j < origine.length(); j++) {

                        JSONObject obj2 = origine.getJSONObject(j);
                        link = obj2.getString("link");
                        lat = obj2.getString("lat");
                        lon = obj2.getString("long");
                        id = "0";
                    }
                    MarkOrigin = new Marcatore(id, lat, lon, link);
                    JSONArray intermedi = obj.getJSONArray("intermedi");
                    for (int j = 0; j < intermedi.length(); j++) {
                        JSONObject obj2 = intermedi.getJSONObject(j);
                        link = obj2.getString("link");
                        lat = obj2.getString("lat");
                        lon = obj2.getString("long");
                        id = obj2.getString("idIntermedio");
                        Marker = new Marcatore(id, lat, lon, link);


                        Intermedi.add(Marker);
                    }

                    JSONArray destinazione = obj.getJSONArray("destinazione");
                    for (int j = 0; j < destinazione.length(); j++) {
                        JSONObject obj2 = destinazione.getJSONObject(j);
                        link = obj2.getString("link");
                        lat = obj2.getString("lat");
                        lon = obj2.getString("long");
                    }
                    MarkDest = new Marcatore(id, lat, lon, link);

                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }

        Gmap();
    }


}

