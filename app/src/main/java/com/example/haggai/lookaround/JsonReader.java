package com.example.haggai.lookaround;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonReader extends AsyncTask<String, String, List<PointOfInterest>> {

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//    @Override
//    protected void onPostExecute(String result) {
//        super.onPostExecute(result);
//    }

    protected List<PointOfInterest> doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
            buffer.append(line+"\n");
            Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }

            JSONObject responseObj = new JSONObject(buffer.toString());
            String status = responseObj.getString("status") ;
            if(!status.equals("OK")){
                String errorMsg = responseObj.getString("error_message");
                throw new Exception(errorMsg);
            }

            JSONArray resultsArr = responseObj.getJSONArray("results");
            List<PointOfInterest> fetchedList = new ArrayList<PointOfInterest>();
            for(int i=0 ;i<resultsArr.length() ; ++i){
                PointOfInterest poi = new PointOfInterest() ;
                poi.parseFromJson(resultsArr.getJSONObject(i));
                fetchedList.add(poi);
            }
            return fetchedList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}