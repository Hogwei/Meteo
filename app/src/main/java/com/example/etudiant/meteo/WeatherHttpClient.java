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


    private static String CURRENT_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    private static String NBPREVISONS = "&cnt=5";
    private static String LANGUE = "&lang=fr";

    final static int GET_CURRENT = 0;
    final static int GET_FORECAST = 1;


    public String getWeatherData(String location, int param) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            switch (param){
                case GET_CURRENT :
                    con = (HttpURLConnection) ( new URL(CURRENT_URL + location + LANGUE)).openConnection();
                    break;
                case GET_FORECAST :
                    con = (HttpURLConnection) ( new URL(FORECAST_URL + location + NBPREVISONS + LANGUE)).openConnection();
                    break;
                default :
                    System.out.println("==========ERROR=========");
                    break;

            }
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
