package com.example.etudiant.meteo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    double latitude, longitude;

    private TextView tvCity;
    private TextView tvTemperature0, tvTemperature1, tvTemperature2, tvTemperature3, tvTemperature4, tvTemperature5;
    private TextView tvPrecipation0, tvPrecipation1, tvPrecipation2, tvPrecipation3, tvPrecipation4, tvPrecipation5;
    private TextView tvHumidity0, tvHumidity1, tvHumidity2, tvHumidity3, tvHumidity4, tvHumidity5;
    private TextView tvDay0, tvDay1, tvDay2, tvDay3, tvDay4, tvDay5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCity = (TextView) findViewById(R.id.tvCity);


        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = 0.0;
        latitude = 0.0;
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                tvCity.setText("latitude : " + latitude + "/ longitude : " + longitude);
                System.out.println("in");
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
        tvCity.setText("latitude : " + latitude + "/ longitude : " + longitude);
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

    public void getWeather(){


    }
}
