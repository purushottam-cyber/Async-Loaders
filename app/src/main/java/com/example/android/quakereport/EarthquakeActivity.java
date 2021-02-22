/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementing the LoaderCallbacks in our activity is a little more complex.
 * First we need to say that EarthquakeActivity implements the LoaderCallbacks interface,
 * along with a generic parameter specifying what the loader will return (in this case an Earthquake).
 */

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {


    /**
     * First we need to specify an ID for our loader. This is only really relevant if we were using multiple loaders in the same activity.
     * We can choose any integer number, so we choose the number 1.
     */

    private  static  final int EARTHQUAKE_LOADER_ID = 1;
    private TextView mEmptyStateTextView;



    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private  static final String USGS_REQUEST_URL =   "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    private EarthQuackAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);



        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView)findViewById(R.id.empty_view);
        // remember we haven't set the string resource here yet because we dont want is to display no earthquack at starting but we will do
        // that afeter we had performed networking requests
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EarthQuackAdapter(
                this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        /**
         * Finally, to retrieve an earthquake, we need to get the loader manager and tell the loader manager to initialize the loader with the specified ID,
         * the second argument allows us to pass a bundle of additional information, which we'll skip.
         * The third argument is what object should receive the LoaderCallbacks (and therefore, the data when the load is complete!) -
         * which will be this activity.
         * This code goes inside the onCreate() method of the EarthquakeActivity, so that the loader can be initialized as soon as the app opens.
         */

        // now check for connnectivity

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();



        if(isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            /**
             *  Pass in this activity for the LoaderCallbacks parameter (3rd parameter) (which is valid
             *  because this activity implements the LoaderCallbacks interface).
             */
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else
        {
            mEmptyStateTextView.setText("No Internet Connection");
        }
    }

    /**
     * Then we need to override the three methods specified in the LoaderCallbacks interface. We need onCreateLoader(),
     * for when the LoaderManager has determined that the loader with our specified ID isn't running, so we should create a new one.
     * @param i  The id of our Loader
     * @return new Loader object if one with the specified ID don't exist
     */

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new EarthquakeLoader(this,USGS_REQUEST_URL);
    }

    /**
     * We need onLoadFinished(), where we'll do exactly what we did in onPostExecute(),
     * and use the earthquake data to update our UI - by updating the dataset in the adapter.
     * @param loader
     * @param earthquakes
     */

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);

        }
        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_eathquake);

    }

    /**
     * And we need onLoaderReset(), we're we're being informed that the data from our loader is no longer valid.
     * This isn't actually a case that's going to come up with our simple loader, but the correct thing to do is
     * to remove all the earthquake data from our UI by clearing out the adapter’s data set.
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();

    }

    // we have used The List here To get Flexibility as we had read here :-
    // https://classroom.udacity.com/courses/ud843/lessons/0fdf2184-5ea3-4751-afc6-9287274982b0/concepts/b9d67793-a3d2-43f4-91cc-83a7cb273238

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of earthquakes in the response.

     */

}
