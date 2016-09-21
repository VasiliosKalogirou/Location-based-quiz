package uk.ac.ucl.cege.cegeg077.ucesvka.londonquiz;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Vasilis on 5/7/2016.
 */
public class CustomLocationListener implements LocationListener
{
    //Global variables of the class
    public MapActivity parentActivity;
    public ArrayList<GeoPoint> pointList;
    //Context allows access to application-specific resources and classes, as well as calls for application-level
    // operations such as launching activities, broadcasting and receiving intents, etc.
    // Instances of the the class android.content.Context provide the connection to the Android system
    // which executes the application - from stackOverflow
    //http://stackoverflow.com/questions/3572463/what-is-context-on-android
    private final Context context;
    private int reached = 999999;

    //This method is the constructor of the class
    public CustomLocationListener(Context context)
    {
        //it is necessary to get the context in order to call the question activity
        // as the listener is a non-activity class
        this.context=context;
    }

    // this method is called every time the user moves around - i.e. changes location
    // it calls the question activity if the user is inside a 10m radius of a point and the point
    //has not been reached before
    @Override
    public void onLocationChanged(Location location)
    {
        for (int i = 0; i < pointList.size(); i++)
        {
            GeoPoint gp = pointList.get(i); //get the i point from the list
            Location fixedLoc = new Location("one"); // create a new instance of location for the point

            //Retrieve all the properties of the each point (geopoint object inside the arraylist)
            Double lat = Double.valueOf(String.valueOf(gp.getLatitude()));
            Double lng = Double.valueOf(String.valueOf(gp.getLongitude()));
            String n = gp.getName();
            String q = gp.getQuestion();
            String pa1 = gp.getPossibleAnswer1();
            String pa2 = gp.getPossibleAnswer2();
            String pa3 = gp.getPossibleAnswer3();
            String pa4 = gp.getPossibleAnswer4();
            Boolean r = gp.getReached();
            String ca = gp.getCorrectAnswer();

            fixedLoc.setLatitude(lat);
            fixedLoc.setLongitude(lng);
            Log.i("location", lat + " " + location.getLatitude());
            Log.i("location", lng + " " + location.getLongitude());

            // we can make use of the Android distanceTo function to calculate the distances
            float distance = location.distanceTo(fixedLoc); // used to measure the distance from all the points of the geojson file
            Log.i("distance", Float.toString(distance)); //used for debugging reasons
            Log.i("Point", i +  "Reached?" + r); //used for debugging reasons

            //Call the question activity if the point has not been reached and the user is inside 10m of the point
            if ((distance < 10) && i != reached)
            {
                reached = i;
                Log.i("inside question: ", q);
                gp.setReached(Boolean.TRUE);

                //Create a new intent to call the question activity class
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //add the properties of the point as extras so that the question activity
                //receives the name, question, possible answers and correct answer for each location
                intent.putExtra("name", n);
                intent.putExtra("question", q);
                intent.putExtra("possible_answer_1", pa1);
                intent.putExtra("possible_answer_2", pa2);
                intent.putExtra("possible_answer_3", pa3);
                intent.putExtra("possible_answer_4", pa4);
                intent.putExtra("correct_answer", ca);
                context.startActivity(intent); //call the question activity
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Toast.makeText(context, "GPS signal lost! ", Toast.LENGTH_LONG).show();

    }
}
