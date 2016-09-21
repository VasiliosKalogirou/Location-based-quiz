package uk.ac.ucl.cege.cegeg077.ucesvka.londonquiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonGeometry;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPoint;
import com.google.maps.android.geojson.GeoJsonPointStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener
{
    //Global variables of the class
    private GeoJsonLayer mLayer;
    private GoogleMap map;
    private final static String mLogTag = "GeoJsonDemo";
    private String mySnippet = "Approach the point on the map to unlock the question!";
    //private String mySnippet = "You haven't been to this location. Approach the point on the map to unlock the question!";
    //private String mySnippet2 = "You have been to this location!";
    private static final long minimum_Distance_Change_For_Update = 2; // in Meters
    private static final long minimum_Time_Between_Update = 1500; // in Milliseconds
    private LocationManager locationManager;
    ArrayList<GeoPoint> pointList = new ArrayList<GeoPoint>(); // create a list of geopoints
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Intent intent = getIntent(); //receive the intent from the login activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map); //set the UI - layout view of the activity

        //Get the device's Location (GPS) service
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Create a new instance of the CustomLocationListener class to track the user's movement
        CustomLocationListener customLL = new CustomLocationListener(getApplicationContext());
        customLL.parentActivity = this;

        // assign the map fragment to a variable so it can be manipulated
        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        // set up the code to call onMapReady once the map is drawn
        // Async here refers to Asynchronous -i.e. the system will only call the next method once
        // the map is drawn (normally a line of code runs immediately after the previous one, i.e. is synchronous)
        mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        customLL.pointList = pointList; // set up the location manager and listener
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minimum_Time_Between_Update,
                minimum_Distance_Change_For_Update, customLL);
    }

    @Override
    public void onMapReady(final GoogleMap newMap)
    {
        // centre the map at UCL at the start
        LatLngBounds UCL = new LatLngBounds(new LatLng(51.52226921971217, -0.1397538185119629),
                new LatLng(51.528744085048615, -0.1254415512084961));
        map = newMap; // Get the newMap local variable and assign it to the map global variable
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(UCL, 20)); //set the map to focus on UCL and set the zoom level to 20 - street level
        retrieveFileFromUrl(); // call the retrieveFileFromUrl method to download the geojson file
        map.setOnMarkerClickListener((OnMarkerClickListener) this); //call thiss method to respond to click events on the markers
    }

    private void retrieveFileFromUrl()
    {
        String mGeoJsonUrl = "http://developer.cege.ucl.ac.uk:30522/teaching/user5/appCreateGeoJSON.php";
        DownloadGeoJsonFile downloadGeoJsonFile = new DownloadGeoJsonFile();
        downloadGeoJsonFile.execute(mGeoJsonUrl);
    }

    @Override
    public boolean onMarkerClick(final Marker marker)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this); //create a new dialog
        dialog.setTitle(marker.getTitle()); //set the title of the dialog
        dialog.setMessage(marker.getSnippet()); //set the snippet of the dialog
/*
        String htmlString = "<h2>This is a Web String</h2>";
        WebView wv = new WebView(this);
        wv.loadData(htmlString, "text/html", "utf-8");
        wv.setBackgroundColor(Color.CYAN);
        wv.getSettings().setDefaultFontSize(10);
        wv.getSettings().setDefaultTextEncodingName("utf-8");

        dialog.setView(wv);
*/
        dialog.setNegativeButton(R.string.button_close, null);
        dialog.show(); // show the dialog on click event
        return true;
    }

    private void addColorsToMarkers()
    {
        for (GeoJsonFeature feature : mLayer.getFeatures())// Iterate over all the features stored in the layer
        {
            // Check if the point_name and question properties exist
            if (feature.hasProperty("point_name") && feature.hasProperty("question"))
            {
                GeoJsonGeometry coords = feature.getGeometry(); //used for debugging reasons
                Log.i("coords:  ", coords.toString()); //used for debugging reasons

                //Retrieve the coordinates of the point
                double lat = ((GeoJsonPoint) feature.getGeometry()).getCoordinates().latitude;
                double lng = ((GeoJsonPoint)feature.getGeometry()).getCoordinates().longitude;
                //Retrieve the properties of the point
                String n = feature.getProperty("point_name");
                String q = feature.getProperty("question");
                String pa1 = feature.getProperty("possible_answer_1");
                String pa2 = feature.getProperty("possible_answer_2");
                String pa3 = feature.getProperty("possible_answer_3");
                String pa4 = feature.getProperty("possible_answer_4");
                String ca = feature.getProperty("correct_answer");

                // Get the icon for the feature
                    BitmapDescriptor pointIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

                    // Create a new point style
                    GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();

                    // Set options for the point style
                    pointStyle.setIcon(pointIcon);
                    pointStyle.setTitle("Point Name: " + n);
                    pointStyle.setSnippet(mySnippet);

                    // Assign the point style to the feature
                    feature.setPointStyle(pointStyle);

                //Create the geopoints list to save the question, possible answers and coordinates of each
                //point to an array of points which stores all these information
                GeoPoint qPoint = new GeoPoint(lat, lng, n, q, pa1, pa2, pa3, pa4, ca);
                pointList.add(qPoint); //add the point to the list

            }
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Map Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://uk.ac.ucl.cege.cegeg077.ucesvka.londonquiz/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop()
    {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Map Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://uk.ac.ucl.cege.cegeg077.ucesvka.londonquiz/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class DownloadGeoJsonFile extends AsyncTask<String, Void, JSONObject>
    {
        protected JSONObject doInBackground(String... params)
        {
            try
            {
                // Open a stream from the URL
                InputStream stream = new URL(params[0]).openStream();

                String line;
                StringBuilder result = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                while ((line = reader.readLine()) != null)
                {
                    // Read and save each line of the stream
                    result.append(line);
                }

                // Close the stream
                reader.close();
                stream.close();

                // Convert result to JSONObject - it will later stored in the mLayer GeoJSON layer type variable
                return new JSONObject(result.toString());
            } catch (IOException e) {
                Log.e(mLogTag, "GeoJSON file could not be read");
            } catch (JSONException e) {
                Log.e(mLogTag, "GeoJSON file could not be converted to a JSONObject");
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject)
        {
            if (jsonObject != null) {
                mLayer = new GeoJsonLayer(map, jsonObject); // Create a new GeoJsonLayer, pass in downloaded GeoJSON file as JSONObject
                addColorsToMarkers(); // Call the addColorsToMarkers to create markers for the points
                mLayer.addLayerToMap(); //Add the layer onto the map
            }
        }
    }
}
