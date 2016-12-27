package com.localdelivery.driver.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;
import com.localdelivery.driver.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*this is first screen of the app here userlogin status are checked  if user are not login then go for
login screen otherwise go to home screen to the directly -------------------Created by  Vijay Kumar*/

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks{

    private Handler handler;
    private Runnable runnable;
    AVLoadingIndicatorView avi;
    SharedPreferences sharedPreferences;
    private GoogleApiClient googleApiClient;
    String loginstatus="";

    final static int REQUEST_LOCATION = 199;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("device tiken ",""+token);
            try {
            PackageInfo info = getPackageManager().getPackageInfo("com.localdelivery.driver", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

 /* -----------this sharedPrefernce get the user login status the user already login in the device or not -------------------*/

        sharedPreferences = getSharedPreferences("LOGINDETAIL", Context.MODE_PRIVATE);
       loginstatus= sharedPreferences.getString("status","");

        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            enableLoc();

        else
            handleSleep();

    }

    private void handleSleep() {
        handler = new Handler();


        runnable = new Runnable() {
            @Override
            public void run() {
                if (loginstatus.equals("logged")){

                    Intent intent = new Intent(SplashActivity.this,HomePageActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {

                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        int SPLASH_TIME_OUT = 3000;
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.e("Location error ", "" + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        (Activity) SplashActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        finish();
                        break;
                    case Activity.RESULT_OK:
                        handleSleep();
                        break;
                    default: {
                        break;
                    }
                }
                break;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        finish();
    }
}
