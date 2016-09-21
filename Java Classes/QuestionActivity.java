package uk.ac.ucl.cege.cegeg077.ucesvka.londonquiz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class QuestionActivity extends AppCompatActivity implements  View.OnClickListener
{
    private TextView tv;
    private TextView tvName;
    private TextView tvQuestion;
    private Button b;
    private String correctAnswer = "";
    private String answer;
    private String n;
    private String q;
    private String pa1, pa2, pa3, pa4;
    private String tf = "FALSE";
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //get the extras from the intent and assign them to the appropriate global variables of this class
        Bundle extras = getIntent().getExtras();
        n = extras.getString("name");
        q = extras.getString("question");
        pa1 = extras.getString("possible_answer_1");
        pa2 = extras.getString("possible_answer_2");
        pa3 = extras.getString("possible_answer_3");
        pa4 = extras.getString("possible_answer_4");
        String ca = extras.getString("correct_answer");

        //find the layout's properties
        tvName = (TextView) findViewById(R.id.textViewName);
        tvQuestion = (TextView) findViewById(R.id.textViewQuestion);
        RadioButton r1 = (RadioButton) findViewById(R.id.rbFirstOption);
        RadioButton r2 = (RadioButton) findViewById(R.id.rbSecondOption);
        RadioButton r3 = (RadioButton) findViewById(R.id.rbThirdOption);
        RadioButton r4 = (RadioButton) findViewById(R.id.rbFourthOption);
        b = (Button) findViewById(R.id.buttonSubmitAnswer);

        //set the text of the layout's properties
        tvName.setText("This is: " + n);
        tvQuestion.setText(q);
        r1.setText(pa1);
        r2.setText(pa2);
        r3.setText(pa3);
        r4.setText(pa4);
        correctAnswer = ca;

        //set the click listener for the radio buttons and the submit button
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
        b.setOnClickListener(this);

        //set the text view at the end of the layout for debugging reasons
        tv = (TextView) findViewById(R.id.textViewWebResponse);
    }

    //Responds to click events of the submit button and the radio button
    //If the submit button is clicked it calls for the submitDataPost method
    //It also checks which radio button was clicked, compares the answer to the correct one,
    //  and stops the switch-case statement
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.buttonSubmitAnswer:
                submitDataPost();
                //break;
            case R.id.rbFirstOption:
                answer = pa1;
                if (answer.equals(correctAnswer))
                {
                    Log.i("First option reached: ", answer);
                    tf = "TRUE";
                    score = score +10;
                    Log.i("Correct answer: ", correctAnswer);
                }
                break;
            case R.id.rbSecondOption:
                answer = pa2;
                if (answer.equals(correctAnswer))
                {
                    Log.i("Second option reached: ", answer);
                    tf = "TRUE";
                    score = score +10;
                    Log.i("Correct answer: ", correctAnswer);
               }
                break;
            case R.id.rbThirdOption:
                answer = pa3;
                if (answer.equals(correctAnswer))
                {
                    Log.i("Third option reached: ", answer);
                    tf = "TRUE";
                    score = score +10;
                    Log.i("Correct answer: ", correctAnswer);
                }
                break;
            case R.id.rbFourthOption:
                answer = pa4;
                if (answer.equals(correctAnswer))
                {
                    Log.i("Fourth option reached: ", answer);
                    tf = "TRUE";
                    score = score +10;
                    Log.i("Correct answer: ", correctAnswer);
                }
                break;
        }
    }

    private void submitDataPost()
    {
        // create an asynchronous operation that will take these values and send them to the server
        SendHttpRequestTask sfd = new SendHttpRequestTask();
        try
        {
            // get the mobile id
            // found on http://developer.android.com/reference/android/telephony/TelephonyManager.html
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String theMobileID = telephonyManager.getDeviceId();

            //assign the parameters inside the urlParameters string variable
            String urlParameters = "n=" + URLEncoder.encode(n, "UTF-8") +
                    "&q=" + URLEncoder.encode(q, "UTF-8") +
                    "&answer=" + URLEncoder.encode(answer, "UTF-8") +
                    "&tf=" + URLEncoder.encode(tf, "UTF-8") +
                    "&mID=" + URLEncoder.encode(theMobileID, "UTF-8");

            //the following code is for debugging reasons
            Log.i("answer", answer + "     ");
            Log.i("mobile id: ", theMobileID + "     ");
            Log.i("The correct answer is: ", correctAnswer + "     ");
            Log.i("result", urlParameters);

            sfd.execute(urlParameters);
        }
        catch (UnsupportedEncodingException e)
        {

        }
    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {

        }

        //this method attempts to connect to the Web Server via an HTTP Request
        @Override
        protected String doInBackground(String... params)
        {
            URL url;
            String urlParams = params[0];
            //String targetURL="http://developer.cege.ucl.ac.uk:30522/teaching/user5/process_form.php";
            String targetURL="http://developer.cege.ucl.ac.uk:30522/teaching/user5/appProcessAnswer.php";

            HttpURLConnection connection = null;
            try
            {
                //Create connection
                url = new URL(targetURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParams.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(urlParams);
                wr.flush();
                wr.close();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null)
                {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                connection.disconnect();
                return response.toString();

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
            finally
            {
                if (connection != null)
                {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String response)
        {
            //Make a toast to let the user know the correct answer
            Toast.makeText(getApplicationContext(), tf +
                            "!   The correct answer is " + correctAnswer,
                            Toast.LENGTH_LONG).show();

            //tv.setText(response); // for debugging reasons
            finish(); // Close the question activity
        }
    }
}
