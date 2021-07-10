package com.example.a18_arid_3033_qno4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText mystate;
        EditText positive;
        EditText total;

    }
    public void displaydata(View view){
        EditText state_name= (EditText) findViewById(R.id.state);
        String Name = state_name.getText().toString();
        String strURL="https://www.latlong.net/category/states-236-14.html"+Name;
        if (Name.length()==0) {
            Toast.makeText(this, "No Country Specified", Toast.LENGTH_SHORT).show();
        }
        else {
            new wsAsyncTask().execute(strURL);
        }
    }
    private class wsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strURL) {
            return requestWebService(strURL[0]);
        }
        @SuppressLint("WrongViewCast")
        @Override
        protected void onPostExecute(String result) {
            mystate = (EditText) findViewById(R.id.st);
            positivity = (EditText) findViewById(R.id.positive);
            total =(EditText) findViewById(R.id.total);

            try {
                JSONArray rootArray=new JSONArray(result);
                JSONObject rootObject=rootArray.getJSONObject(0);
                mystate.setText(rootObject.optString("state"));
                positivity.setText(rootObject.optString("positivity"));
                total.setText(rootObject.optString("total"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static String requestWebService(String serviceUrl) {
        HttpURLConnection urlConnection = null;
        try {
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
// get JSON data
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
// converting InputStream into String
            Scanner scanner = new Scanner(in);
            String strJSON = scanner.useDelimiter("\\A").next();
            scanner.close();
            return strJSON;
        } catch (MalformedURLException e) {
            e.printStackTrace(); // URL is invalid
        } catch (SocketTimeoutException e) {
            e.printStackTrace(); // data retrieval or connection timed out
        } catch (IOException e) {
            e.printStackTrace(); // could not read response body
// (could not create input stream)
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
    }
}