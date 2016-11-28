package in.quastio.arjun.taskapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by arjun on 26-11-2016.
 */
public class AccountDetails extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountetails);
        Button logout = (Button) findViewById(R.id.logout);
        Button getWeather = (Button) findViewById(R.id.getdata);

        Bundle myBundle = getIntent().getExtras();

        String name = myBundle.get("name").toString();
        String email =myBundle.get("email").toString();
        String gender=myBundle.get("gender").toString();


        TextView profilename = (TextView) findViewById(R.id.profilename);
        TextView proEmail=(TextView)findViewById(R.id.email);
        TextView proGender=(TextView)findViewById(R.id.gender);
        proEmail.setText(email);
        proGender.setText(gender);

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
                    Intent weather=new Intent(AccountDetails.this,WeatherActivity.class);
                    startActivity(weather);
                }

            }
        });


    }




        public void logout() {
            LoginManager.getInstance().logOut();
            Intent logout = new Intent(AccountDetails.this, MainActivity.class);
            startActivity(logout);
            finish();
        }

}