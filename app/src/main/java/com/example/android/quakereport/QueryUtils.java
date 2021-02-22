package com.example.android.quakereport;

import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Sample JSON response for a USGS query
     */
    private static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /*
    * Add in the fetchEarthquakeData() helper method that ties all the steps together - creating a URL,
    * sending the request, processing the response. Since this is the only “public” QueryUtils method
    * that the EarthquakeAsyncTask needs to interact with, make all other helper methods in QueryUtils “private”.
    **/

    public static List<Earthquake> fetchEarthquakeData(String requestURL)
    {
        URL url = createURL(requestURL);

        String JsonResponse = null;

        try {
            JsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem in making HTTP Request ",e);
        }
        List<Earthquake> earthquakes = extractFeatureFromJson(JsonResponse);

        return earthquakes;
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     * and Remember This is the Unly public class here so we can call it outside the class.
     */
    public static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {

        if(TextUtils.isEmpty(earthquakeJSON))
            return null;

        // Create an empty ArrayList that we can start adding earthquakes to
        /*
        * And Remember the List class is actually an Interface and The ArraList , LinkList etc are Derived from This so whenever it required
        * a linkList instead of an ArrayList we can directly change from here    List<Earthquake> earthquakes = new LinkList<>();
        * And Thus our code will become more flexible
        **/
        List<Earthquake> earthquakes = new ArrayList<Earthquake>();


        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.


            JSONObject baseJasonResponse = new JSONObject(earthquakeJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).

            JSONArray earhQuackArray = baseJasonResponse.getJSONArray("features");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < earhQuackArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currEarthQuack = earhQuackArray.getJSONObject(i);

                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake.
                JSONObject properties = currEarthQuack.getJSONObject("properties");

                // Extract the value for the key called "mag"
                double magnitude = properties.getDouble("mag");

                // Extract the value for the key called "place"
                String location = properties.getString("place");

                // Extract the value for the key called "time"
                long time = properties.getLong("time");

                // Extract the value for the key called "url"
                String url = properties.getString("url");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                Earthquake earthquake = new Earthquake(magnitude, location, time, url);

                // Add the new {@link Earthquake} to the list of earthquakes.
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    /**
     * Returns new URL object from the given string URL.
     */

    private static URL createURL(String stringURL)
    {
        URL url = null;

        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */

    private static String makeHttpRequest(URL url) throws IOException{
        if(url == null)
            return "";

        String JasonResponse = "";

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.

            if(urlConnection.getResponseCode()==200)
            {
                inputStream = urlConnection.getInputStream();
                JasonResponse = readFromStream(inputStream);
            }else {
                Log.e("QueryUtils"," Problem in retriving Jason Reasponse "  + (urlConnection.getResponseCode()));
            }


        } catch (IOException e) {
            Log.e("QUTIL ","Problem in making connection " , e);
        }finally {

            if(urlConnection!=null)
                urlConnection.disconnect();

            // Closing the input stream could throw an IOException, which is why
            // the makeHttpRequest(URL url) method signature specifies than an IOException
            // could be thrown.
            if(inputStream!=null)
                inputStream.close();
        }

        return JasonResponse;

    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */

    private static String readFromStream(InputStream inputStream) throws IOException{

        StringBuilder result = new StringBuilder();

        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line!=null)
            {
                result.append(line);
                line = reader.readLine();
            }
        }

        return result.toString();
    }

}