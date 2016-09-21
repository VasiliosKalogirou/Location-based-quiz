package uk.ac.ucl.cege.cegeg077.ucesvka.londonquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class LoginActivity extends AppCompatActivity
{
    //Called when the activity is first created.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); //super means it is a method of the superclass (in this case: AppCompatActivity
        setContentView(R.layout.activity_login); //sets the content view (User Interface) of the activity
    }

    //Called when the user clicks the Start Playing button
    public void startMapActivity(View view)
    {
        // This intent will call the MapActivity to start
        Intent intent = new Intent(this, MapActivity.class);
        LoginActivity.this.startActivity(intent);
    }
}
