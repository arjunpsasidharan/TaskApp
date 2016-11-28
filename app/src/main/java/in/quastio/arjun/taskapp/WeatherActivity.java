package in.quastio.arjun.taskapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WeatherActivity extends AppCompatActivity {
    String TAG = AccountDetails.class.getSimpleName();
    ListView myList;
    private static String url = "http://api.openweathermap.org/data/2.5/forecast?";
    private static String apiKey = "8ce7125dd3aab32f637ae3ca388ffa3e";
    ArrayList<HashMap<String, String>> weatherList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        myList=(ListView)findViewById(R.id.myList);
        GetLocation gps = new GetLocation(WeatherActivity.this);
        Double latitude = gps.getLatitude();
        Double longitude = gps.getLongitude();
       weatherList=   new ArrayList<>();

        myList= (ListView)findViewById(R.id.myList);

        String newUrl = url + "lat=" + latitude + "&lon=" + longitude +"&units=metric"+ "&APPID=" + apiKey;

        Log.e("newurl", newUrl);
        new GetData().execute(newUrl);
    }



    private class GetData extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(WeatherActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("connecting.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            Httphandler http = new Httphandler();
            String json = http.GetSteam(params[0]);
            Log.e(TAG, "response from url" + json);
            if (json != null) {
                Log.e(TAG, "nothingwron");
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray list = jsonObject.getJSONArray("list");
                    for (int j = 0; j < list.length(); j++) {
                        Log.e("array", list.get(j).toString());
                    }

                    for (int i = 0; i < list.length(); i++) {

                        JSONObject c = list.getJSONObject(i);
                        String date = c.getString("dt_txt");
                        JSONObject main = c.getJSONObject("main");
                        int temp = main.getInt("temp");
                        Log.e("date", date);
                        Log.e("temp", String.valueOf(temp));

                        JSONArray weather = c.getJSONArray("weather");
                        String description = weather.getJSONObject(0).getString("description");

                        Log.e("description", description);


                        HashMap<String, String> post = new HashMap<>();
                        post.put("date", date);
                        post.put("temp", String.valueOf(temp)+"°C");
                        post.put("description", description);


                        weatherList.add(post);
                        Log.e("weatherlist size", String.valueOf(weatherList.size()));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } else {
                Log.e(TAG, "something wrong");


            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            ListView myList=(ListView)findViewById(R.id.myList);
            ListAdapter myAdapter = new SimpleAdapter(WeatherActivity.this, weatherList, R.layout.listrow, new String[]{"date", "temp", "description"}, new int[]{R.id.date, R.id.temp, R.id.desc});
            myList.setAdapter(myAdapter);
        }


    }

}
