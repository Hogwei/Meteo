package com.example.etudiant.meteo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.etudiant.meteo.model.Weather;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    double latitude, longitude;
    private TextView tvCoordonnes;

    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView hum;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    private ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        String city = "Angers,FR";

       cityText = (TextView) findViewById(R.id.tvCity);
        condDescr = (TextView) findViewById(R.id.description);
      //  temp = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.humidite);
        press = (TextView) findViewById(R.id.pression);
        windSpeed = (TextView) findViewById(R.id.vent);
        //windDeg = (TextView) findViewById(R.id.windDeg);
        imgView = (ImageView) findViewById(R.id.image);

        JSONWeatherTask task = new JSONWeatherTask();
        if(isNetworkAvailable())    task.execute(new String[]{city});

        tvCoordonnes = (TextView) findViewById(R.id.tvCity);


        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = 0.0;
        latitude = 0.0;
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            //    tvCoordonnes.setText(latitude + " / " + longitude);
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
        //tvCoordonnes.setText(latitude + " / " + longitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  getWeather();
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

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
               // weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }




        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            /*if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);
            }*/

           cityText.setText(" "+weather.location.getCity() + " (" + weather.location.getCountry()+")");
           condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
//            temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
            hum.setText("Humidité :" + weather.currentCondition.getHumidity() + "%");
            press.setText("Pression :" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("Vent :" + (int)weather.wind.getSpeed()*3.6 + " km/h");
  //          windDeg.setText("" + weather.wind.getDeg() + "°");

            switch(weather.currentCondition.getWeatherId()){
                case 800:imgView.setImageResource(R.drawable.clearsky);break;               //sunny
                case 801:imgView.setImageResource(R.drawable.fewclouds);break;              //m-cloudy
                case 802:imgView.setImageResource(R.drawable.scatteredclouds);break;        //partly-cloudy
                case 803:imgView.setImageResource(R.drawable.brokenclouds);break;           //fair
                case 804:imgView.setImageResource(R.drawable.overcastclouds);break;         //smoke
                default : imgView.setImageResource(R.drawable.na);break;
            }


        }







    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


