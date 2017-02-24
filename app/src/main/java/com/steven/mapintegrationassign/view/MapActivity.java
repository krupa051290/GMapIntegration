package com.steven.mapintegrationassign.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.steven.mapintegrationassign.R;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    //Google map
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    Toast toast;
    private float ZOOM = 18.0f;
    SupportMapFragment mapFragment;

    //Navigation drawer
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView mNavigationView;

    private Toolbar toolbar;

    private int caseNumber;
    String[] zonesNew;
    public static java.util.Timer timer=null;

    FragmentManager fragmentManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_activity);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Find a drawer view
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        //Tie DrawerLayout events to ActionBArToggle
        mDrawerLayout.addDrawerListener(drawerToggle);

        //find or drawer view
        mNavigationView = (NavigationView) findViewById(R.id.left_drawer);
        setupDrawerContent(mNavigationView);

        //Display car no and driver no
        View headerLayout = mNavigationView.getHeaderView(0);
        TextView carNo = (TextView) headerLayout.findViewById(R.id.username);
        carNo.setText("KSHAH");

        fragmentManager = getSupportFragmentManager();
    }


    private void setupDrawerContent(NavigationView mNavigationView)
    {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                }
        );
    }

    public void selectDrawerItem(MenuItem menuItem)
    {
        Fragment fragment = null;
        Class fragmentClass ;

        try {
            switch (menuItem.getItemId()) {
                case R.id.nav_first_fragment:


                    break;

                case R.id.nav_second_fragment:
                    Intent intent = new Intent(MapActivity.this,AnimationActivity.class);
                    startActivity(intent);
                    break;
                default:
                    caseNumber = 0;
                    mGoogleApiClient.reconnect();
                    break;
            }


            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            setTitle(menuItem.getTitle());
            //Insert the fragment by replacing any existing fragment
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            // Close the navigation drawer
            mDrawerLayout.closeDrawers();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void selectDrawerItemRequestomplete()
    {
        Fragment fragment = null;
        Class fragmentClass;
        Bundle bundle = new Bundle();

        try {
            switch (caseNumber)
            {
                case 1:
                    fragmentClass = HomeFragment.class;
                    break;
                default:
                    fragmentClass = SupportMapFragment.class;
            }

            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(bundle);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(ZOOM);

        LatLng mylocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(mylocation).flat(true).title("Marker in my current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_simplegreencartopview)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, ZOOM));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mGoogleApiClient.isConnected())
            startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = SupportMapFragment.newInstance();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.flContent, mapFragment);
        fragmentTransaction.commit();

        //Accquire GoogleMap object
        mapFragment.getMapAsync(this);
        Log.d("MApsActivity", lastLocation.getLatitude() + ":::" + lastLocation.getLongitude());

        createLocationRequest();


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        int i = 0;
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1);
        locationRequest.setFastestInterval(5);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void updateMap()
    {
        if(mMap != null)
        {
            mMap.clear();
            LatLng mylocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(mylocation).flat(true).title("Marker in my current location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_simplegreencartopview)));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(mylocation)
                    .bearing(lastLocation.getBearing())
                    .zoom(ZOOM)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, ZOOM));
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    protected void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;
        toast = Toast.makeText(this, lastLocation.getLatitude()+":::"+lastLocation.getLongitude(), Toast.LENGTH_SHORT);
        toast.show();
        updateMap();

    }

    private ActionBarDrawerToggle setupDrawerToggle() {

        return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

}
