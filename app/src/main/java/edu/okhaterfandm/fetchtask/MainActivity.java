package edu.okhaterfandm.fetchtask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Send url to Async
        new AsyncJsonFetch().execute("https://fetch-hiring.s3.amazonaws.com/hiring.json");
    }

    private class AsyncJsonFetch extends AsyncTask<String, Integer, JSONArray> {
        //Fetch JSON data from url
        @Override
        protected JSONArray doInBackground(String ... urlS) {
            JSONArray jsonArr = new JSONArray();
            try {
                URL url = new URL(urlS[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");

                }
                String data = sb.toString();
                jsonArr = new JSONArray(data);
                return jsonArr;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return jsonArr;
        }
        @Override
        protected void onPostExecute(JSONArray arr){
            //Turn the fetched JSON array into a hash map
            HashMap<String, ArrayList<String>> dataMap = ExpandListData.returnData(arr);
            //Get our ExpandableListView
            ExpandableListView expandableListView = findViewById(R.id.expandableListView);
            ArrayList<String> listIDs = new ArrayList<>(dataMap.keySet());
            //Sort the listIDs
            Collections.sort(listIDs, new ExpandListData.CustomComparator());
            CustomExpandableListAdapter listAdapter = new CustomExpandableListAdapter(MainActivity.this, listIDs, dataMap);
            expandableListView.setAdapter(listAdapter);
        }
    }
}
