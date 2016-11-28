package in.quastio.arjun.taskapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arjun on 26-11-2016.
 */
public class AccountDetails extends Activity {
    String TAG = AccountDetails.class.getSimpleName();
    ListView myList;
    private String url = "http://api.openweathermap.org/data/2.5/forecast?";
    private String apiKey = "8ce7125dd3aab32f637ae3ca388ffa3e";
    ArrayList<HashMap<String, String>> weatherList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountetails);
        Button logout = (Button) findViewById(R.id.logout);
        Button getWeather = (Button) findViewById(R.id.getdata);

        Bundle myBundle = getIntent().getExtras();

        String name = myBundle.get("name").toString();


        TextView profilename = (TextView) findViewById(R.id.profilename);

        profilename.setText(name);
        ProfilePictureView propic = (ProfilePictureView) findViewById(R.id.profilepic);
        propic.setProfileId(myBundle.get("id").toString());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                Log.e("button","clicked");
            }
        });
        getWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager) AccountDetails.this.getSystemService(Context.LOCATION_SERVICE);
                boolean gps_on = false;
                boolean network_on = false;
                try {
                    gps_on = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

                } catch (Exception e) {
                }
                try {
                    network_on = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception e) {
                }
                if (!gps_on && !network_on) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AccountDetails.this);
                    builder.setMessage("Location Service is Off");
                    builder.setTitle("");
                    builder.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent loc = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            AccountDetails.this.startActivity(loc);
                        }
                    });


                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                } else {
                    GetLocation gps = new GetLocation(AccountDetails.this);
                    Double latitude = gps.getLatitude();
                    Double longitude = gps.getLongitude();

                    String newUrl = url + "lat=" + latitude + "&lon=" + longitude + "&APPID=" + apiKey;

                    Log.e("newurl", newUrl);
                    new GetData().execute(newUrl);
                }

            }
        });


    }

    private class GetData extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AccountDetails.this);
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
Log.e(TAG,"nothingwron");
                try {
              JSONObject jsonObject = new JSONObject(json);
                    JSONArray list =jsonObject.getJSONArray("list");

                for(int i=0;i<list.length();i++){

                        JSONObject c= list.getJSONObject(i);
                        String date = c.getString("dt_text");
                    JSONObject main= c.getJSONObject("main");
                    int temp =main.getInt("temp");
                    JSONArray weather=main.getJSONArray("weather");
                    String description = weather.getJSONObject(0).getString("description");



                        HashMap<String, String > post=new HashMap<>();
                        post.put("date",date);
                        post.put("temp",String.valueOf(temp));
                    post.put("description",description);


                        weatherList.add(post);





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


             myList=(ListView)findViewById(R.id.myList);
            ListAdapter myAdapter=new SimpleAdapter(AccountDetails.this,weatherList,R.layout.listrow,new  String[]{"date","temp","description"},new int[]{R.id.date,R.id.temp,R.id.desc});
          myList.setAdapter(myAdapter);
       }

        }


        public void logout() {
            LoginManager.getInstance().logOut();
            Intent logout = new Intent(AccountDetails.this, MainActivity.class);
            startActivity(logout);
            finish();
        }

}