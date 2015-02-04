package com.example.etudiant.meteo;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.etudiant.meteo.model.ImageAdapter;
import com.example.etudiant.meteo.model.Weather;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    double latitude, longitude;

    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView hum;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    private ImageView imgView;
    private GridView gridView;
    private TextView jouractuel;
    private TextView joursuivants;
    public int mode;

    final static int DEBUG_MODE = 0;
    final static int GPS_MODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Choix du mode d'acquisition des données météo
        mode = GPS_MODE;
        String debug_city = "Angers,FR";

        // Binding
        setContentView(R.layout.activity_main);
        jouractuel = (TextView) findViewById(R.id.jouractuel);
        joursuivants = (TextView) findViewById(R.id.joursuivant);
        cityText = (TextView) findViewById(R.id.tvCity);
        condDescr = (TextView) findViewById(R.id.description);
        temp = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.humidite);
        press = (TextView) findViewById(R.id.pression);
        windSpeed = (TextView) findViewById(R.id.vent);
        windDeg = (TextView) findViewById(R.id.windDeg);
        imgView = (ImageView) findViewById(R.id.image);
        gridView = (GridView) findViewById(R.id.gridview);

        // Traitement : GPS
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = 0.0;
        latitude = 0.0;
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                if(mode==GPS_MODE & isNetworkAvailable()){
                    JSONWeatherTask task = new JSONWeatherTask();
                    task.execute(new String[]{"" + latitude, "" + longitude});
                }

            }

            public void onProviderDisabled(String arg0) {
                // TODO Auto-generated method stub

            }

            public void onProviderEnabled(String arg0) {
                // TODO Auto-generated method stub

            }

            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
                // TODO Auto-generated method stub

            }
        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        // Traitement : Meteo
        JSONWeatherTask task = new JSONWeatherTask();
        switch (mode){
            case DEBUG_MODE :
                if(isNetworkAvailable())    task.execute(new String[]{debug_city});
                break;
            case GPS_MODE :
                if(isNetworkAvailable())    task.execute(new String[]{"" + latitude, "" + longitude});
                break;
            default :
                task.execute(new String[]{debug_city});
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, List<Weather>> {

        @Override
        protected List<Weather> doInBackground(String... params) {
            List<Weather> previsions = new ArrayList<>();
            String data;

            try {
                switch (mode){
                    case DEBUG_MODE :
                        //Meteo du jour
                        data = ( (new WeatherHttpClient()).getWeatherData(params[0], WeatherHttpClient.GET_CURRENT));
                        previsions.add(JSONWeatherParser.getWeather(data));

                        //Previsions a J+5
                        data = ( (new WeatherHttpClient()).getWeatherData(params[0], WeatherHttpClient.GET_FORECAST));
                        previsions.addAll(JSONWeatherParser.getForecast(data));

                        break;

                    case GPS_MODE :
                        //Meteo du jour
                        data = ( (new WeatherHttpClient()).getWeatherData(Double.parseDouble(params[0]), Double.parseDouble(params[1]), WeatherHttpClient.GET_CURRENT));
                        previsions.add(JSONWeatherParser.getWeather(data));

                        //Previsions a J+5
                        data = ( (new WeatherHttpClient()).getWeatherData(Double.parseDouble(params[0]), Double.parseDouble(params[1]), WeatherHttpClient.GET_FORECAST));
                        previsions.addAll(JSONWeatherParser.getForecast(data));

                        break;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return previsions;

        }




        @Override
        protected void onPostExecute(List<Weather> previsions) {
            super.onPostExecute(previsions);

            // Meteo du jour
            jouractuel.setText(""+previsions.get(0).currentCondition.getDay());
            cityText.setText(" " + previsions.get(0).location.getCity() + " (" + previsions.get(0).location.getCountry()+")");
            condDescr.setText(previsions.get(0).currentCondition.getDescr());
            temp.setText("Température :" + Math.round((previsions.get(0).temperature.getTemp() - 273.15)) + "°C");
            hum.setText("Humidité :" + previsions.get(0).currentCondition.getHumidity() + "%");
            press.setText("Pression :" + previsions.get(0).currentCondition.getPressure() + " hPa");
            windSpeed.setText("Vent :" + (int)previsions.get(0).wind.getSpeed()*3.6 + " km/h");
            windDeg.setText("Direction : " + getWindDirection(previsions.get(0).wind.getDeg()));
            imgView.setImageResource(getResources().getIdentifier("r" + String.valueOf(previsions.get(0).currentCondition.getWeatherId()), "drawable", "com.example.etudiant.meteo"));

            // Previsions à J+5
            previsions.remove(0);
            gridView.setAdapter(new ImageAdapter(MainActivity.this,previsions));
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getWindDirection(float windDeg){
        if(windDeg>=0 && windDeg<22)        return("Nord");
        if(windDeg>=22 && windDeg<67)       return("Nord-Est");
        if(windDeg>=67 && windDeg<112)      return("Est");
        if(windDeg>=112 && windDeg<157)     return("Sud-Est");
        if(windDeg>=157 && windDeg<202)     return("Sud");
        if(windDeg>=202 && windDeg<247)     return("Sud-Ouest");
        if(windDeg>=247 && windDeg<292)     return("Ouest");
        if(windDeg>=292 && windDeg<337)     return("Nord-Ouest");
        if(windDeg>=337 && windDeg<360)     return("Nord");

        return("failWindDirection");
    }
}