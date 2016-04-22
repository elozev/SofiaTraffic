package emillozev.sofiatraffic.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.DirectionsAndNavigation.DrawRoute;
import emillozev.sofiatraffic.UI.Fragments.ImportFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {

    private static final int REQUEST_PLACE_PICKER = 23;
    public SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private final int REQUEST_CODE_PERMISSION = 0;
    private ArrayList<LatLng> markerPoints;
    public DrawRoute mRoute;
    private LatLng addressOrigin;
    private LatLng addressDest;
    private Button getDirectionsButton;
    private boolean isGetDirectionsClicked = false;
    private PolylineOptions addToMapPolyline = new PolylineOptions();
    private String[] textFromSite = null;
    private List<LatLng> markerForTraffic = new ArrayList<>();
    private LocationManager mLocationManager;
    private Button mSearchButton;
    private boolean isSearchButtonOnMap = true;
    private Button mSpeedButton;
    private Button mClearRouteButton;
    private Button mStartNavigationButton;
    public boolean isCopyReady = false;
    private static RadioGroup radioGroup;
    private static RadioButton radioB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        onClickListenerButton();

        mSpeedButton = (Button) findViewById(R.id.speedometerButton);
        mClearRouteButton = (Button) findViewById(R.id.clear_route);
        mSearchButton = (Button) findViewById(R.id.searchButton);


        if (!isGetDirectionsClicked) {
            mClearRouteButton.getBackground().setAlpha(100);
            mClearRouteButton.setText("");
        }

        mMapFragment = SupportMapFragment.newInstance();
        getDirectionsButton = (Button) findViewById(R.id.getDirectionsButton);
        mRoute = new DrawRoute();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final FragmentManager fm = getFragmentManager();
        final android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
        //MapFragment mapFragment = new MapFragment();

        fm.beginTransaction().replace(R.id.main_fragment_for_replacement, new MapFragment()).commit();
        if (!mMapFragment.isAdded())
            sFm.beginTransaction().add(R.id.main_fragment_for_replacement, mMapFragment).commit();
        else
            sFm.beginTransaction().show(mMapFragment).commit();



        mMapFragment.getMapAsync(this);
        mMap = mMapFragment.getMap();

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSearchButtonOnMap == true) {

                    isSearchButtonOnMap = false;
                    mSpeedButton.setText("");

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ImportFragment importFragment = new ImportFragment();

                    if (mMapFragment.isAdded()) {
//                        fm.beginTransaction().hide(mMapFragment).commit();
                    }

                    fragmentTransaction.add(R.id.content_frame, importFragment);
                    fragmentTransaction.show(importFragment).commit();

                    PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                            getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

                    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                        @Override
                        public void onPlaceSelected(Place place) {
                            addressOrigin = place.getLatLng();
                        }

                        @Override
                        public void onError(Status status) {
                            // TODO: Handle the error.
                            Log.i("AUTO COMPLETE", "An error occurred: " + status);
                        }
                    });

                    PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment)
                            getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

                    autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                        @Override
                        public void onPlaceSelected(Place place) {
                            addressDest = place.getLatLng();
                        }

                        @Override
                        public void onError(Status status) {
                            // TODO: Handle the error.
                            Log.i("AUTO COMPLETE", "An error occurred: " + status);
                        }
                    });

                    if (isGetDirectionsClicked == true && addToMapPolyline != null) {
                        mMap.addPolyline(addToMapPolyline);
                        isGetDirectionsClicked = false;
                        addToMapPolyline = null;
                    }


                } else {
                    mSearchButton.setText("Search");
                    isSearchButtonOnMap = true;
//                    //mSpeedButton.setText("-.-km/h");
//
//                    if (!mMapFragment.isAdded())
//                        fm.beginTransaction().add(R.id.map, mMapFragment).commit();
//                    else
//                        fm.beginTransaction().show(mMapFragment).commit();
//
//                    if (isGetDirectionsClicked == true && addToMapPolyline != null) {
//                        mMap.addPolyline(addToMapPolyline);
//                        isGetDirectionsClicked = false;
//                        addToMapPolyline = null;
//                    }

                }

            }

        });


        getDirectionsButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                if (addressOrigin == null && addressDest == null) {
                    Toast.makeText(MainActivity.this, "Please fill up both!", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < 2; i++) {
                        MarkerOptions options = new MarkerOptions();
                        if (i == 0) {
                            options.position(addressOrigin);
                        } else {
                            options.position(addressDest);
                        }

                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


                        // Add new marker to the Google Map Android API V2

                        mMap.addMarker(options);
                    }


                    mRoute.clearAll();
                    mRoute.addMarkerToList(addressOrigin);
                    mRoute.addMarkerToList(addressDest);
                    // Getting URL to the Google Directions API
                    String url = mRoute.getDirectionsUrl(addressOrigin, addressDest);
                    Log.i("DIRECTIONS", "SOMETHING");
                    mRoute.downloadTask(url);
                    mMap.addPolyline(mRoute.getLinesOptions());

                    addToMapPolyline = mRoute.getLinesOptions();
                    isGetDirectionsClicked = true;
//
//                    if (!mMapFragment.isAdded())
//                        fm.beginTransaction().add(R.id.map, mMapFragment).commit();
//                    else
//                        fm.beginTransaction().show(mMapFragment).commit();

                    mClearRouteButton.setText("Clear Route");
                    mClearRouteButton.getBackground().setAlpha(64);

                }
                mMap.addPolyline(mRoute.getLinesOptions());
                mSearchButton.setText("Search");
                isSearchButtonOnMap = true;
            }
        });


        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showMessageOKCancel("You need to allow access to Location Services",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                REQUEST_CODE_PERMISSION);
                                    }
                                });
                        return;
                    }
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_PERMISSION);
                    return;
                }
            }
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this); //the ints are getting how often get location info


        mClearRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                markerPoints.clear();
                mRoute.clearAll();

                for (LatLng latLng : markerForTraffic) {
                    mMap.addMarker(new MarkerOptions().position(latLng));
                }

                mClearRouteButton.setText("");
                mClearRouteButton.getBackground().setAlpha(0);
            }
        });

        GeoCoderIsShit geoCoderIsShit = new GeoCoderIsShit();
        geoCoderIsShit.execute("ape");
    }

    public void onClickListenerButton() {
        radioGroup = (RadioGroup) findViewById(R.id.rg_navigation_method);
        mStartNavigationButton = (Button) findViewById(R.id.startNavigationButton);

        PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                addressDest = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("AUTO COMPLETE", "An error occurred: " + status);
            }
        });

        mStartNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressDest != null && addressOrigin == null) {
                    String modeForNavigation;
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == -1) {
                        modeForNavigation = "d";
                    } else {
                        Log.i("SELECTED ID", "" + selectedId);
                        radioB = (RadioButton) findViewById(selectedId);
                        Toast.makeText(MainActivity.this, radioB.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (radioB.getText().toString() == "Car") {
                            modeForNavigation = "d";
                        } else if (radioB.getText().toString() == "Walking") {
                            modeForNavigation = "w";
                        } else if (radioB.getText().toString() == "Bicycle") {
                            modeForNavigation = "b";
                        } else {
                            modeForNavigation = "d";
                        }
                    }

                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + addressDest.latitude + "," + addressDest.longitude + "&mode=" + modeForNavigation);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Fill in only \"To:\"! The start point is your location! ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public String[] parsingTheSite() {


        Thread downloadThread = new Thread(new Runnable() {

            public void run() {
                org.jsoup.nodes.Document doc = null;

                try {
                    doc = Jsoup.connect("http://tix.bg/bg/Sofia/").get();
                } catch (IOException e) {
                    return;
                }


                Elements newsHeadlines = doc.select(".dhtzelement");
                textFromSite = newsHeadlines.text().toString().split("\\b(?:към|от)\\b");

            }
        });
        downloadThread.start();
        try {
            downloadThread.join();
        } catch (InterruptedException e) {

        }

        return textFromSite;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        final int[] id = {item.getItemId()};
        final FragmentManager fm = getFragmentManager();
        final android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ImportFragment importFragment = new ImportFragment();


        //  if (mMapFragment.isAdded())
        //    sFm.beginTransaction().hide(mMapFragment).commit();

        if (id[0] == R.id.map_menu) {
            fm.beginTransaction().replace(R.id.main_fragment_for_replacement, new ImportFragment()).commit();
            if (!mMapFragment.isAdded())
                sFm.beginTransaction().add(R.id.main_fragment_for_replacement, mMapFragment).commit();
            else
                sFm.beginTransaction().show(mMapFragment).commit();


            if (isGetDirectionsClicked == true && addToMapPolyline != null) {
                mMap.addPolyline(addToMapPolyline);
                isGetDirectionsClicked = false;
                addToMapPolyline = null;
            }
            mSearchButton.setText("Search");
        } else if (id[0] == R.id.list_traffic_zones) {

            if (mMapFragment.isAdded()) {
                sFm.beginTransaction().hide(mMapFragment).commit();
            }

            fragmentTransaction.add(R.id.fragment_import, importFragment);
            fragmentTransaction.show(importFragment).commit();


        } else if (id[0] == R.id.search_places) {
            if (mMapFragment.isAdded()) {
                sFm.beginTransaction().hide(mMapFragment).commit();
            }

            fragmentTransaction.add(R.id.content_frame, importFragment);
            fragmentTransaction.show(importFragment).commit();

            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    addressOrigin = place.getLatLng();
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i("AUTO COMPLETE", "An error occurred: " + status);
                }
            });

            PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

            autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    addressDest = place.getLatLng();
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i("AUTO COMPLETE", "An error occurred: " + status);
                }
            });


        } else if (id[0] == R.id.nav_tools) {

        } else if (id[0] == R.id.nav_share) {

        } else if (id[0] == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap = googleMap;
        markerPoints = new ArrayList<LatLng>();

        mMap.setTrafficEnabled(true);
        // Add a marker in Sofia and move the camera
        LatLng sofia = new LatLng(42.697626, 23.322284);
        float zoomLevel = (float) 11.00; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sofia, zoomLevel));
        mMap.addMarker(new MarkerOptions().position(sofia));

        checkIfLocationTurned();
        isInternetConnected();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            this.mMap.setMyLocationEnabled(true);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showMessageOKCancel("You need to allow access to Location Services",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            REQUEST_CODE_PERMISSION);
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_PERMISSION);
                return;
            }
        }

        if (isGetDirectionsClicked == true && addToMapPolyline != null) {
            mMap.addPolyline(addToMapPolyline);
            isGetDirectionsClicked = false;
            addToMapPolyline = null;
            Log.i("CHECKIN", "In the if");
        }

    }


    private void checkIfLocationTurned() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(this.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MainActivity.this.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(MainActivity.this.getString(R.string.cancel_button), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
            dialog.show();
        }
    }


    public void isInternetConnected() {
        ConnectivityManager connectivityMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // Check if wifi or mobile network is available or not. If any of them is
        // available or connected then it will return true, otherwise false;

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!(mobile.isConnected()) && !(wifi.isConnected())) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Please turn on Mobile data or Wi-Fi!");
            dialog.setNeutralButton(this.getResources().getString(R.string.mobile_data_settings),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent();
                            myIntent.setComponent(new ComponentName(
                                    "com.android.settings",
                                    "com.android.settings.Settings$DataUsageSummaryActivity"));

                            MainActivity.this.startActivity(myIntent);
                            //get gps
                        }
                    });
            dialog.setPositiveButton(this.getResources().getString(R.string.wifi_settings),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);

                            MainActivity.this.startActivity(myIntent);
                            //get gps
                        }
                    });
            dialog.setNegativeButton(MainActivity.this.getString(R.string.cancel_button),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
            dialog.show();

        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Toast.makeText(MainActivity.this, "Location Permission Enabled!", Toast.LENGTH_LONG).show();
                    mMap.setMyLocationEnabled(true);
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "In order this application to work, Location Services must be enabled!", Toast.LENGTH_LONG).show();
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public double distanceBetweenLatLng(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371; //kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);

        return dist;
    }

    private void sendNotifications() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("Sofia Traffic Notification");
        builder.setContentText(("You are getting nearby a jammed area!"));
        builder.setSmallIcon(R.drawable.common_ic_googleplayservices);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(8, notification);
    }

    @Override
    public void onLocationChanged(Location location) {

        if (isGetDirectionsClicked == true && addToMapPolyline != null) {
            mMap.addPolyline(addToMapPolyline);
            isGetDirectionsClicked = false;
            addToMapPolyline = null;
        }

        if (isSearchButtonOnMap == true) {
            //isSearchButtonOnMap = false;
            mSpeedButton.setText("");
        } else {
            float currentSpeed = location.getSpeed();
            mSpeedButton.setText((double) Math.round(currentSpeed * (1000 / 60) * 100) / 100 + " km/h");
        }

        for (LatLng latLng : markerForTraffic) {
            if (distanceBetweenLatLng(latLng.latitude, latLng.longitude, location.getLatitude(), location.getLongitude()) < 1) {
                sendNotifications();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "status");
    }

    public class GeoCoderIsShit extends AsyncTask<String, Integer, List<LatLng>> {
        @Override
        protected List<LatLng> doInBackground(String... params) {
            String[] textSite = parsingTheSite();

            if (textSite != null) {
                List<LatLng> toBeCopied = new ArrayList<>();

                for (String a : textSite) {

                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    List<Address> addresses = null;

                    try {
                        addresses = geocoder.getFromLocationName(a, 1);
                    } catch (IOException e) {
                    }

                    if (addresses != null && !addresses.isEmpty()) {
                        toBeCopied.add(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()));
                        Log.i("MARKERS", "ADDING TO LIST");
                    }

                }
                markerForTraffic = new ArrayList<>(toBeCopied);
                isCopyReady = true;

            } else {
                Toast.makeText(MainActivity.this, "Please enable data and restart the app!", Toast.LENGTH_LONG).show();
            }
            return markerForTraffic;
        }

        @Override
        protected void onPostExecute(List<LatLng> list) {
            for (LatLng latLng : list) {
                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        }
    }


}