package com.example.etudiant.meteo;


import com.example.etudiant.meteo.model.Location;
import com.example.etudiant.meteo.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Seb on 03/02/15.
 */
public class JSONWeatherParser {

    public static Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();

        // On cree l'objet JSONObject a partir des donnees
        JSONObject jObj = new JSONObject(data);

        // Infos du lieu (localisation)
        Location loc = new Location();

        JSONObject coordObj = getObject("coord", jObj);
        loc.setLatitude(getFloat("lat", coordObj));
        loc.setLongitude(getFloat("lon", coordObj));

        JSONObject sysObj = getObject("sys", jObj);
        loc.setCountry(getString("country", sysObj));
        loc.setSunrise(getInt("sunrise", sysObj));
        loc.setSunset(getInt("sunset", sysObj));
        loc.setCity(getString("name", jObj));
        weather.location = loc;

        // On prends l'array Weather
        JSONArray jArr = jObj.getJSONArray("weather");

        // Infos Generales
        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
        weather.currentCondition.setDescr(getString("description", JSONWeather));
        weather.currentCondition.setCondition(getString("main", JSONWeather));
        weather.currentCondition.setIcon(getString("icon", JSONWeather));

        // Infos Meteo
        JSONObject mainObj = getObject("main", jObj);
        weather.currentCondition.setHumidity(getInt("humidity", mainObj));
        weather.currentCondition.setPressure(getInt("pressure", mainObj));
        weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
        weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
        weather.temperature.setTemp(getFloat("temp", mainObj));

        // Vent
        JSONObject wObj = getObject("wind", jObj);
        weather.wind.setSpeed(getFloat("speed", wObj));
        weather.wind.setDeg(getFloat("deg", wObj));

        // Nuages
        JSONObject cObj = getObject("clouds", jObj);
        weather.clouds.setPerc(getInt("all", cObj));

        // Jour
        Calendar today = Calendar.getInstance();
        weather.currentCondition.setDay(getDay(today.DAY_OF_WEEK) + " " + today.DAY_OF_MONTH + " " + getMonth(today.MONTH));

        return weather;
    }


    public static List<Weather> getForecast(String data) throws JSONException {
        List<Weather> weatherList = new ArrayList<Weather>();
        Weather weather;

        // On cree l'objet JSONObject a partir des donnees
        JSONObject jObj = new JSONObject(data);

        // Extraction des previsions
        JSONArray jForecastArr = jObj.getJSONArray("list");
        int nbPrevisions = jObj.getInt("cnt");
        Calendar today = Calendar.getInstance();
        for(int i=0; i<nbPrevisions ; i++){

            // Nouvel objet Weather pour chaque journee
            JSONObject JSONForecast = jForecastArr.getJSONObject(i);
            weather = new Weather();

            // Infos Generales
            JSONArray jWeatherArr = JSONForecast.getJSONArray("weather");
            JSONObject JSONWeather = jWeatherArr.getJSONObject(0);
            weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
            weather.currentCondition.setDescr(getString("description", JSONWeather));
            weather.currentCondition.setCondition(getString("main", JSONWeather));
            weather.currentCondition.setIcon(getString("icon", JSONWeather));

            // Infos Meteo
            JSONObject tempObj = getObject("temp", JSONForecast);
            weather.currentCondition.setHumidity(getInt("humidity", JSONForecast));
            weather.currentCondition.setPressure(getInt("pressure", JSONForecast));
            weather.temperature.setMaxTemp(getFloat("max", tempObj));
            weather.temperature.setMinTemp(getFloat("min", tempObj));
            weather.temperature.setTemp(getFloat("day", tempObj));

            // Vent
            weather.wind.setSpeed(getFloat("speed", JSONForecast));
            weather.wind.setDeg(getFloat("deg", JSONForecast));

            // Nuages
            weather.clouds.setPerc(getInt("clouds", JSONForecast));

            // Jour
            today.add(Calendar.DATE,1);
            weather.currentCondition.setDay(getDay(today.DAY_OF_WEEK) + " " + today.DAY_OF_MONTH + " " + getMonth(today.MONTH));

            //Ajout de l'objet Weather a la liste
            weatherList.add(weather);
        }

        return weatherList;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    private static String getDay(int day){
        switch (day){
            case Calendar.MONDAY :      return ("Lundi");
            case Calendar.TUESDAY :     return ("Mardi");
            case Calendar.WEDNESDAY :   return ("Mercredi");
            case Calendar.THURSDAY :    return ("Jeudi");
            case Calendar.FRIDAY :      return ("Vendredi");
            case Calendar.SATURDAY :    return ("Samedi");
            case Calendar.SUNDAY :      return ("Dimanche");
            default :                   return("=getDayFail=");
        }
    }

    private static String getMonth(int month){
        switch (month){
            case Calendar.JANUARY :     return("Janvier");
            case Calendar.FEBRUARY :    return("Fevrier");
            case Calendar.MARCH :       return("Mars");
            case Calendar.APRIL :       return("Avril");
            case Calendar.MAY :         return("Mai");
            case Calendar.JUNE :        return("Juin");
            case Calendar.JULY :        return("Juillet");
            case Calendar.AUGUST :      return("Aout");
            case Calendar.SEPTEMBER :   return("Septembre");
            case Calendar.OCTOBER :     return("Octobre");
            case Calendar.NOVEMBER :    return("Novembre");
            case Calendar.DECEMBER :    return("Decembre");
            default :                   return("=getMonthFail=");

        }
    }
}
