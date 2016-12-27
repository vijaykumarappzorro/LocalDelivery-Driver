package com.localdelivery.driver.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.localdelivery.driver.R;
import com.squareup.picasso.Picasso;

import static com.localdelivery.driver.R.id.map;

public class HomePageActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Toolbar toolbar;
    GoogleMap googleMap;
    SharedPreferences sharedPreferences;
    String name1, profile, mobile;
    private GoogleApiClient mGoogleApiClient;
    DrawerLayout drawerLayout;
    Activity activity;
    Circle mCircle;

    private Marker mMarker;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home Page");
        setSupportActionBar(toolbar);
        activity = this;
        sharedPreferences = getSharedPreferences("USERDEATIL", Context.MODE_PRIVATE);


        initNavigationDrawer();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googleMap.setMyLocationEnabled(true);

    }

    public void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.setting:
                        Intent intent = new Intent(HomePageActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break;
                    case  R.id.trip:
                        Intent intent1 = new Intent(activity,CustomerRequestActivity.class);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });
        String name = sharedPreferences.getString("name", "");
        String phone = sharedPreferences.getString("mobile", "");
        String profile = sharedPreferences.getString("profilepic", "");
        Log.e("profile pic the ",profile);
        Log.e("name of user",name);
        Log.e("phone number",phone);

        View header = navigationView.getHeaderView(0);
        TextView txtname = (TextView) header.findViewById(R.id.txtphonenumber);

        if (phone.equals(null)) {

            txtname.setText(name);
        } else {

            txtname.setText(sharedPreferences.getString("name", "") + "\n" + "\n" + sharedPreferences.getString("mobile", ""));
        }
        ImageView imageView = (ImageView) header.findViewById(R.id.nav_image);
        if (profile!="") {
            Picasso.with(this)
                    .load(profile)
                    .into(imageView);
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_home_page_);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        initCamera(mLastLocation);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }
    private void initCamera(Location location) {

        //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //   googleMap.setTrafficEnabled(true);
        try {


            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.e("sorry try agian", "Style parsing failed.");
            }
        }catch (Resources.NotFoundException e){

            e.printStackTrace();
        }


        googleMap.setMyLocationEnabled(true);


        CameraPosition position = CameraPosition.builder()
                .target( new LatLng( location.getLatitude(),
                        location.getLongitude() ) )
                .zoom( 16f )
                .bearing( 0.0f )
                .tilt( 0.0f )
                .build();
        double lat = location.getLatitude();
        double longi= location.getLongitude();
        LatLng latLng = new LatLng(lat,longi);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        googleMap.moveCamera(CameraUpdateFactory
                .newCameraPosition(position));
        drawMarkerWithCircle(latLng);


    }
    private void drawMarkerWithCircle(LatLng position){
        double radiusInMeters = 50.0;
        String skycolor ="#bfdff6";
        int strokeColor = Color.parseColor(skycolor);
       // int strokeColor = 0xffff0000; //red outline
        String skycolor1= "#a4d2f3";
        int shadeColor = Color.parseColor(skycolor1);
        //int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = googleMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);

    }


}
