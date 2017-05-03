package com.localdelivery.driver.views;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
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
import com.localdelivery.driver.controller.ModelManager;
import com.localdelivery.driver.controller.PendingRequestsManager;
import com.localdelivery.driver.model.Beans.PendingRequestsBeans;
import com.localdelivery.driver.model.Constants;
import com.localdelivery.driver.model.Date_Time;
import com.localdelivery.driver.model.Event;
import com.localdelivery.driver.model.LDPreferences;
import com.localdelivery.driver.model.Operations;
import com.localdelivery.driver.model.Utility;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.localdelivery.driver.R.id.map;

public class HomePageActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = HomePageActivity.class.getSimpleName();

    Toolbar toolbar;
    GoogleMap googleMap;
    TextView date, time;

    private GoogleApiClient mGoogleApiClient;
    DrawerLayout drawerLayout;

    Activity activity;
    Circle mCircle;
    ACProgressFlower dialog;
    Dialog bidform;
    LocationRequest locationrequest;
    private static final int FINE_LOCATION = 0;
    private static String[] PERMISSION_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION};

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


       /* ModelManager.getInstance().getPendingRequestsManager().getCompleteRequests(activity, Operations.getPendingRequests(activity,
                LDPreferences.readString(activity, "driver_id")));*/


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

        this.googleMap =googleMap;

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


        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                Log.e("on move idle", "mmap");
              /*  ModelManager.getInstance().getPendingRequestsManager().getRecentRequests(activity, Operations.getPendingRequests(activity,
                        LDPreferences.readString(activity, "driver_id")));*/




            }
        });

        // click on marker icon and get the detail of customer request and driver also replay the customer rquest on the map screen
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                String lat = String.valueOf(latLng.latitude);
                String lng = String.valueOf(latLng.longitude);

                Log.e("marker lat lng", lat + "\n" + lng);

                for (int i = 0; i < PendingRequestsManager.recentRequestsList.size(); i++) {
                    Log.e("for","loop");
                    PendingRequestsBeans pendingRequestsBeans = PendingRequestsManager.recentRequestsList.get(i);
                    String source_latitude = pendingRequestsBeans.getSource_lat();
                    String source_longitude = pendingRequestsBeans.getSource_lng();
                    final String requestid = pendingRequestsBeans.getRequest_id();
                    final String customerid = pendingRequestsBeans.getCustomer_id();
                //    if (lat.equals(lat) && lat.equals(lng)) {

                        bidform = Utility.createBidForm(activity);
                        bidform.show();

                        TextView edtpick = (TextView) bidform.findViewById(R.id.picked_location);
                        TextView edttitle = (TextView) bidform.findViewById(R.id.titlebar);
                        TextView edtcustomer_price = (TextView) bidform.findViewById(R.id.customerprice);
                        TextView txtdate = (TextView) bidform.findViewById(R.id.customerDatetime);
                        txtdate.setText(pendingRequestsBeans.getDate()+":"+pendingRequestsBeans.getTime());

                        TextView edtdrop = (TextView) bidform.findViewById(R.id.destination);
                        final EditText edtprice = (EditText) bidform.findViewById(R.id.price);
                        edttitle.setText("Bid Form");

                      //  edtcustomer_price.setText("Customer Price :" + pendingRequestsBeans.getPrice());
                        edtcustomer_price.setText(" Price: $110");
                        txtdate.setText("20-April-2017: 01:30 pm");


                        date = (TextView) bidform.findViewById(R.id.datepicker);
                        time = (TextView) bidform.findViewById(R.id.timepicker);
                        Button requestbutton = (Button) bidform.findViewById(R.id.btnsend);
                        Button dialogCloseButton = (Button) bidform.findViewById(R.id.btncancel);
                        edtpick.setText(pendingRequestsBeans.getPickup_location());
                        edtdrop.setText(pendingRequestsBeans.getDrop_location());
                        edtprice.setTextColor(Color.parseColor("#000000"));


                        date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Date_Time date_time = new Date_Time(activity);
                                date_time.dateDialog();

                            }
                        });

                        time.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Date_Time date_time = new Date_Time(activity);
                                date_time.timedialog();
                            }
                        });


                        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                bidform.dismiss();

                            }
                        });
                        requestbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                if (date.getText().toString().isEmpty()) {

                                    date.setError("Required");
                                } else if (time.getText().toString().isEmpty()) {
                                    time.setError("Required Time");

                                } else if (edtprice.getText().toString().isEmpty()) {

                                    edtprice.setError("Required price");

                                } else {

                                    // here sen your bid to the customer
                                    dialog = new ACProgressFlower.Builder(activity).build();
                                    dialog.show();
                                    ModelManager.getInstance().getRequestAcecptManager().RequestAcecptManager(activity, Operations.acceptRequestofCustomer(activity, requestid,
                                            customerid, LDPreferences.readString(activity, "driver_id"), edtprice.getText().toString().trim(),
                                            date.getText().toString().replaceAll(" ", ""), time.getText().toString().replaceAll(" ", "")));

                                }

                            }
                        });




                  //  }


                }

                return false;
            }
        });

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
                        Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.requests:
                        Intent intent1 = new Intent(activity, CustomerRequestActivity.class);
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
                    case R.id.mapview:
                        startActivity(new Intent(activity, RideStartMapView.class));
                        break;
                }

                return true;
            }
        });

        String name = LDPreferences.readString(activity, "driver_name");
        String phone = LDPreferences.readString(activity, "mobile");
        String profile = LDPreferences.readString(activity, "driver_image");

        Log.e(TAG, "name--" + name + "\n phone--" + phone + "\n profile-- " + profile);

        View header = navigationView.getHeaderView(0);

        TextView driverName = (TextView) header.findViewById(R.id.driverName);
        TextView driverContact = (TextView) header.findViewById(R.id.driverContact);

        if (phone.equals("null")) {

            driverName.setText(name);

        } else {

            driverName.setText(LDPreferences.readString(activity, "driver_name"));
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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.
            locationPermission();

        } else {
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

            mLastLocation =LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            initCamera(mLastLocation);

            }







    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_LOCATION) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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


            } else {


            }
                Log.e(TAG, "CAMERA permission was NOT granted.");


            }
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

    @Override
    protected void onResume() {
        super.onResume();

        ModelManager.getInstance().getPendingRequestsManager().getRecentRequests(activity,Operations.getPendingRequests(activity
                ,"6"));


    }

    @Subscribe
    public void onEvent(Event event) {

        switch (event.getKey()) {
            case Constants.PENDING_REQUESTS:

                int size = PendingRequestsManager.recentRequestsList.size();
                    Log.e("sizeee",String.valueOf(size));

                    for (int i = 0; i<size; i++) {
                        PendingRequestsBeans pendingRequestsBeans = PendingRequestsManager.recentRequestsList.get(i);
                        String source_latitude = pendingRequestsBeans.getSource_lat();
                        String source_longitude = pendingRequestsBeans.getSource_lng();

                        String destination_latitude = pendingRequestsBeans.getDestination_lat();
                        String destination_longitude = pendingRequestsBeans.getDestination_lng();

                        Log.e(TAG, "source lat---" + source_latitude);

                      /*  googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(source_latitude),
                                    Double.parseDouble(source_longitude))));*/

                        googleMap.addMarker(new MarkerOptions().position(new LatLng(30.709113,76.695700)));
                        /*Marker myLocMarker = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(source_latitude),
                                        Double.parseDouble(source_longitude)))
                                .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.mipmap.circle, pendingRequestsBeans.getPrice()))));*/
                    }
                break;
            case Constants.DATEEVENT:
               // date.setText(""+event.getValue());
                    date.setText("20-April-2017");
                date.setTextColor(Color.parseColor("#000000"));
                break;
            case Constants.TIMEEVENT:
                time.setText(""+event.getValue());
                time.setTextColor(Color.parseColor("#000000"));
                break;
            case Constants.REQUESTACEPT:
                dialog.dismiss();
                String completemessage= event.getValue();
                String split1 []=completemessage.split(",");
                int reponseid = Integer.parseInt(split1[split1.length-2]);
                String responsemessage = split1[split1.length-1];
                if(reponseid>0){

                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(responsemessage)
                            .setContentText("Please wait for some time until you dont any response from the driver")
                            .show();
                }
                else {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(responsemessage)
                            .setContentText("Your request is  not sent to driver due to bad inernet connection please try again")
                            .show();
                }


        }
    }

 private void initCamera(Location location) {

        Log.e(TAG, "current location---"+location);

     CameraPosition position = CameraPosition.builder()
             .target( new LatLng(location.getLatitude(),
                     location.getLongitude()) )
             .zoom( 14f )
             .bearing( 0.0f )
             .tilt( 0.0f )
             .build();


       // googleMap.setPadding(0, dpToPx(48), 0, 0);

       double lat = location.getLatitude();
       double lng= location.getLongitude();
       LatLng latLng = new LatLng(lat,lng);

     googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position));
     googleMap.addMarker(new MarkerOptions().position(latLng));


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

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(15);
        mCircle = googleMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);

    }

    private void locationPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOCATION);
        }


    }

}
