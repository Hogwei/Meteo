package com.example.etudiant.meteo.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.etudiant.meteo.MainActivity;
import com.example.etudiant.meteo.R;

import java.util.List;

/**
 * Created by etudiant on 04/02/2015.
 */
public class ImageAdapter extends BaseAdapter{
    private Context context;
    private final List<Weather> mobileValues;

    public ImageAdapter(Context context, List<Weather> mobileValues) {
        this.context = context;
        this.mobileValues = mobileValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View gridView;
        Weather weather = mobileValues.get(position);

        gridView = new View(context);

        // get layout from mobile.xml
        gridView = inflater.inflate(R.layout.grid_item, parent, false);

        // set value into textview
        TextView textView = (TextView) gridView
                .findViewById(R.id.joursuivant);
        textView.setText("J+"+(position+1));

        // set image based on selected text
        ImageView imageView = (ImageView) gridView
                .findViewById(R.id.imageJ1);

        imageView.setImageResource(context.getResources().getIdentifier("r"+String.valueOf(weather.currentCondition.getWeatherId()), "drawable", "com.example.etudiant.meteo"));

        // set value into textview
        textView = (TextView) gridView
                .findViewById(R.id.descriptionj1);
        textView.setText(weather.currentCondition.getDescr());

        // set value into textview
        textView = (TextView) gridView
                .findViewById(R.id.pressionj1);
        textView.setText("" + weather.currentCondition.getPressure()+ " hPa");

        // set value into textview
        textView = (TextView) gridView
                .findViewById(R.id.temperaturej1);
        textView.setText(""+Math.round((weather.temperature.getTemp() - 273.15)) + "°C");

        // set value into textview
        textView = (TextView) gridView
                .findViewById(R.id.windDeg);
        textView.setText("" + MainActivity.getWindDirection(weather.wind.getDeg()));

        // set value into textview
        textView = (TextView) gridView
                .findViewById(R.id.ventj1);
        textView.setText(""+(int)weather.wind.getSpeed()*3.6 + " km/h");

        return gridView;
    }

    @Override
    public int getCount() {
        return mobileValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mobileValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}