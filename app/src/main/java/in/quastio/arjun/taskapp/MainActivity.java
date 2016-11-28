package in.quastio.arjun.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };


profileTracker=new ProfileTracker(){
@Override
protected void onCurrentProfileChanged(Profile oldProfile, final Profile currentProfile) {

    passTonext(currentProfile);
}};
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        LoginButton loginButton=(LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_friends"));
        FacebookCallback<LoginResult>callback=new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                AccessToken accessToken = loginResult.getAccessToken();
                Profile   profile = Profile.getCurrentProfile();

                    passTonext(profile);



            }

@Override
public void onCancel(){
        Toast.makeText(getBaseContext(),"Login Cancelled",Toast.LENGTH_SHORT).show();

        }

@Override
public void onError(FacebookException error){
        Toast.makeText(getBaseContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

        }
        };

        loginButton.registerCallback(callbackManager,callback);
        }

@Override
protected void onResume(){
        super.onResume();
      Profile  profile=Profile.getCurrentProfile();
passTonext(profile);

}


@Override
protected void onPause(){
        super.onPause();
        }

@Override
protected void onStop(){
        super.onStop();

        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
        }

@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        }

private void passTonext(Profile currentProfile){

    if(currentProfile!=null  ){
        Intent myIntent=new Intent(MainActivity.this,AccountDetails.class);
        myIntent.putExtra("name",currentProfile.getName()).toString();
        myIntent.putExtra("id",currentProfile.getId()).toString();

        startActivity(myIntent);
        finish();


        }
    else {

        Log.e("intent passing","something wrong");
    }

        }

}
