package com.example.etudiant.meteo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Seb on 03/02/15.
 */

public class WeatherHttpClient {


    private static String CURRENT_URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    private static String NBPREVISONS = "&cnt=5";
    private static String LANGUE = "&lang=fr";

    final static int GET_CURRENT = 0;
    final static int GET_FORECAST = 1;

    public String getWeatherData(String location, int param) {
        String url = new String();

        switch (param){
            case GET_CURRENT :
                url = (CURRENT_URL + "q=" + location + LANGUE);break;
            case GET_FORECAST :
                url = (FORECAST_URL + "q=" + location + NBPREVISONS + LANGUE);
                break;
            default :
                System.out.println("==========LOCATION ERROR=========");
                break;

        }
        return(getWeatherData(url));
    }


    public String getWeatherData(double lat, double lon, int param) {
        String url = new String();

        switch (param){
            case GET_CURRENT :
                url = (CURRENT_URL + "lat=" + lat + "&lon=" + lon + LANGUE);break;
            case GET_FORECAST :
                url = (FORECAST_URL + "lat=" + lat + "&lon=" + lon + NBPREVISONS + LANGUE);
                break;
            default :
                System.out.println("==========LOCATION ERROR=========");
                break;

        }
        return(getWeatherData(url));
    }


    public String getWeatherData(String url) {
        HttpURLConnection con = null ;
        InputStream is = null;



        try {
            con = (HttpURLConnection) ( new URL(url).openConnection());
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }
}
