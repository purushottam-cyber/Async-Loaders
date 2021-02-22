package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthQuackAdapter extends ArrayAdapter<Earthquake> {


    public EarthQuackAdapter(Activity context, ArrayList<Earthquake> earthquackList) {
        super(context, 0, earthquackList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currView = convertView;
        if (currView == null) {
            currView = LayoutInflater.from(getContext()).inflate(R.layout.earthquack_list_item, parent, false);
        }

        Earthquake currentEarthquack = getItem(position);

        TextView magnitude = (TextView) currView.findViewById(R.id.magnitudeView);


        magnitude.setText(formatMagnitude(currentEarthquack.getMagnitude()));

        TextView palce = (TextView) currView.findViewById(R.id.placeview);
        palce.setText(currentEarthquack.getPlace());

        TextView dateView = (TextView) currView.findViewById(R.id.dateView);
        TextView timeView = (TextView) currView.findViewById(R.id.timeView);

        Date dateObject = new Date(currentEarthquack.getTimeInMillisecond());

        String formattedDate = FormatDate(dateObject);
        String formattedTime = FormatTime(dateObject);

        dateView.setText(formattedDate);
        timeView.setText(formattedTime);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquack.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return currView;
    }


    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }

        //The int returned by ContextCompat.getColor() is actually the color you want (an hexadecimal (ffc00 type) color as an integer),
        //in most case you will be asked for that color. The R.color.xxx int is actually just a ID referencing you
        //hexadecimal/integer color from your resources
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId); // clor resource to color int
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private String FormatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String FormatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
