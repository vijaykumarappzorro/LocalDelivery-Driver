package com.localdelivery.driver.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.localdelivery.driver.R;
import com.localdelivery.driver.controller.ModelManager;
import com.localdelivery.driver.controller.PendingRequestsManager;
import com.localdelivery.driver.model.Beans.PendingRequestsBeans;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.LDPreferences;
import com.localdelivery.driver.model.Operations;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.localdelivery.driver.R.id.map;

public class HomePageActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = HomePageActivity.class.getSimpleName();
    Toolbar toolbar;
    GoogleMap googleMap;

    private GoogleApiClient mGoogleApiClient;
    DrawerLayout drawerLayout;
    Activity activity;
    Circle mCircle;


    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initViews();
    }

    public void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        activity = this;

        ModelManager.getInstance().getPendingRequestsManager().getCompleteRequests(activity, Operations.getPendingRequests(activity,
                LDPreferences.readString(activity, "driver_id")));

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

        this.googleMap.setMyLocationEnabled(true);

    }



    public void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {
                    case R.id.settings:
                        Intent intent = new Intent(HomePageActivity.this,ProfileActivity.class);
                        startActivity(intent);
                        break;

                    case  R.id.requests:
                        Intent intent1 = new Intent(activity,CustomerRequestActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.trips:
                        startActivity(new Intent(activity, TripsActivity.class));
                        break;

                    case R.id.ratings:
                        startActivity(new Intent(activity, RatingScreenActivity.class));
                        break;

                    case R.id.earnings:
                        startActivity(new Intent(activity, EarningsActivity.class));
                        break;
                }

                return true;
            }
        });

        String name = LDPreferences.readString(activity, "name");
        String phone = LDPreferences.readString(activity, "mobile");
        String profile = LDPreferences.readString(activity, "profilePic");

        Log.e(TAG, "name--"+ name+ "\n phone--"+phone+"\n profile-- "+profile);

        View header = navigationView.getHeaderView(0);
        TextView driverName = (TextView) header.findViewById(R.id.driverName);
        TextView driverContact = (TextView)header.findViewById(R.id.driverContact);

        if (phone.equals("null")) {

            driverName.setText(name);
        } else {

            driverName.setText(LDPreferences.readString(activity, "name"));
            driverContact.setText(LDPreferences.readString(activity, "mobile"));
        }
        ImageView imageView = (ImageView) header.findViewById(R.id.nav_image);

        if (profile.equals("")) {
            Picasso.with(this)
                    .load(profile)
                    .into(imageView);
        }

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

        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Event event) {

        switch (event.getKey()) {
            case Constants.PENDING_REQUESTS:
                int size = PendingRequestsManager.allRequestsList.size();
                for (int i=0; i < size; i++) {
                    PendingRequestsBeans pendingRequestsBeans = PendingRequestsManager.allRequestsList.get(i);
                    String source_latitude = pendingRequestsBeans.getSource_lat();
                    String source_longitude = pendingRequestsBeans.getSource_lng();

                    String destination_latitude = pendingRequestsBeans.getDestination_lat();
                    String destination_longitude = pendingRequestsBeans.getDestination_lng();

                    Log.e(TAG, "source lat---"+source_latitude);

                    if (!source_latitude.isEmpty()) {
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(source_latitude),
                                Double.parseDouble(source_longitude))));
                    }
                }
        }
    }

    private void initCamera(Location location) {

        Log.e(TAG, "current location---"+location);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //   googleMap.setTrafficEnabled(true);
        try {

            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            if (!success) {
                Log.e("sorry try again", "Style parsing failed.");
            }
        }catch (Resources.NotFoundException e){

            e.printStackTrace();
        }


        googleMap.setMyLocationEnabled(true);

       /* View btnMyLocation = ((View) mapView.findViewById(1).getParent()).findViewById(2);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(80,80); // size of button in dp
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params.setMargins(0, 0, 20, 0);
        btnMyLocation.setLayoutParams(params);*/

        CameraPosition position = CameraPosition.builder()
                .target( new LatLng( location.getLatitude(),
                        location.getLongitude() ) )
                .zoom( 16f )
                .bearing( 0.0f )
                .tilt( 0.0f )
                .build();



        googleMap.setPadding(0, dpToPx(48), 0, 0);
        double lat = location.getLatitude();
        double lng= location.getLongitude();
        LatLng latLng = new LatLng(lat,lng);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        googleMap.moveCamera(CameraUpdateFactory
                .newCameraPosition(position));
        drawMarkerWithCircle(latLng);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
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
