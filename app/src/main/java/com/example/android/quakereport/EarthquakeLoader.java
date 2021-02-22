package com.example.android.quakereport;

/*
* To define the com.example.android.quakereport.EarthquakeLoader class, we extend AsyncTaskLoader and specify List as the generic parameter,
* which explains what type of data is expected to be loaded. In this case, the loader is loading a list of Earthquake objects.
*Then we take a String URL in the constructor, and in loadInBackground(), we'll do the exact same operations as in doInBackground back in EarthquakeAsyncTask.
* Important: Notice that we also override the onStartLoading() method to call forceLoad()
* which is a required step to actually trigger the loadInBackground() method to execute.
**/

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */

public class EarthquakeLoader extends AsyncTaskLoader {
    private String mUrl;
    private static  final String LOG_TAG = EarthquakeLoader.class.getName();


    /**
     * Constructs a new {@link EarthquakeLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */

    public EarthquakeLoader(Context context , String url) {
        super(context);
        this.mUrl = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public Object loadInBackground() {
        if(mUrl == null)
            return null;
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}
