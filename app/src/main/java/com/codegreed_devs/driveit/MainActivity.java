package com.codegreed_devs.driveit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import de.psdev.licensesdialog.LicensesDialog;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,LocationListener{
    GoogleMap mMap;
    Marker melbourne;
    LatLng latLng;
    Toolbar toolbar;
    CircleImageView user_dp;
    String display_name,display_email;
    Uri profile_url;
    TextView user_name,user_email;
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding views
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //getting the map view
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapV);
        mapFragment.getMapAsync(this);


        //Get user info
        display_name= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        display_email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        profile_url= FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navView=navigationView.getHeaderView(0);
        user_name= navView.findViewById(R.id.user_name);
        user_email= navView.findViewById(R.id.user_email);
        user_dp= navView.findViewById(R.id.user_dp);

        //setting views
        user_name.setText(display_name);
        user_email.setText(display_email);
        if (profile_url != null) {
            Picasso
                    .with(getBaseContext())
                    .load(profile_url)
                    .transform(new CropCircleTransformation())
                    .resize(128, 128)
                    .centerCrop()
                    .placeholder(R.drawable.avatar)
                    .into(user_dp);

        }else {
            Picasso
                    .with(getBaseContext())
                    .load(R.drawable.avatar)
                    .transform(new CropCircleTransformation())
                    .resize(128, 128)
                    .centerCrop()
                    .into(user_dp);

            //Else It will display the default dp
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_top_cars) {
            Intent top_cars=new Intent(MainActivity.this,TopCars.class);
            startActivity(top_cars);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_manage) {
            Intent my_account=new Intent(MainActivity.this,MyAccount.class);
            startActivity(my_account);

        } else if (id == R.id.nav_share) {
                terms_conditions();
        } else if (id == R.id.nav_send) {
            SignOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void terms_conditions(){
        new LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .build()
                .show();
    }
    public void SignOut(){
        FirebaseAuth.getInstance().signOut();
        // Return to sign in
        Intent intent=new Intent(MainActivity.this,Login.class);
        startActivity(intent);
        finish();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng jkuat = new LatLng(-1.089040, 37.010450);
        LatLngBounds jkuatBounds = new LatLngBounds(
                new LatLng(-1.092914, 37.019999), new LatLng(-1.081586, 37.009871));

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

//        mMap.addMarker(new MarkerOptions().position(jkuat).title("Marker Jkuat"));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_LOCATION_REQUEST_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);

        //start of getting my current location
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
//            melbourne.remove();
            onLocationChanged(location);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)      // Sets the center of the map to Mountain View
                    .zoom(12)                   // Sets the zoom
                    .bearing(45)                // Sets the orientation of the camera to east
                    .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
    }
    @Override
    public void onLocationChanged(Location location) {
        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        latLng = new LatLng(latitude, longitude);
        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        if (melbourne != null) {
            melbourne.remove();
        }

        // Zoom in the Google Map
        melbourne = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Your Current Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
//        mMap.
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Intent refresh=getIntent();
        finish();
        overridePendingTransition(0,0);
        startActivity(refresh);
        overridePendingTransition(0,0);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
