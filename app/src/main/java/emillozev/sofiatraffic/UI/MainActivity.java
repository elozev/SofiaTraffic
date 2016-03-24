package emillozev.sofiatraffic.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.DirectionsAndNavigation.DrawRoute;
import emillozev.sofiatraffic.UI.Fragments.ImportFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static final int REQUEST_PLACE_PICKER = 23;
    public SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private final int REQUEST_CODE_PERMISSION = 0;
    private ArrayList<LatLng> markerPoints;
    public DrawRoute mRoute;
    private LatLng addressOrigin;
    private LatLng addressDest;
    private Button getDirectionsButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();


        if (!mMapFragment.isAdded())
            fm.beginTransaction().add(R.id.content_frame, mMapFragment).commit();
        else
            fm.beginTransaction().show(mMapFragment).commit();


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("PLACE TAG", "Place: " + place.getName());

                String placeDetailsStr = place.getName() + "\n"
                        + place.getId() + "\n"
                        + place.getLatLng().toString() + "\n"
                        + place.getAddress() + "\n"
                        + place.getAttributions();

                mMap.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .title((String) place.getAddress())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));

                addressOrigin = place.getLatLng();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("PLACE TAG", "An error occurred: " + status);
            }
        });


        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("PLACE TAG", "Place: " + place.getName());

                String placeDetailsStr = place.getName() + "\n"
                        + place.getId() + "\n"
                        + place.getLatLng().toString() + "\n"
                        + place.getAddress() + "\n"
                        + place.getAttributions();


                mMap.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .title((String) place.getAddress())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                addressDest = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("PLACE TAG", "An error occurred: " + status);
            }
        });


        getDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRoute.clearAll();
                mRoute.addMarkerToList(addressOrigin);
                mRoute.addMarkerToList(addressDest);
                // Getting URL to the Google Directions API
                String url = mRoute.getDirectionsUrl(addressOrigin, addressDest);
                Log.i("DIRECTIONS", "SOMETHING");
                mRoute.downloadTask(url);
                mMap.addPolyline(mRoute.getLinesOptions());
            }
        });


        mMapFragment.getMapAsync(this);
        mMap = mMapFragment.getMap();
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
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        if (mMapFragment.isAdded())
            sFm.beginTransaction().hide(mMapFragment).commit();

        if (id == R.id.nav_camera) {
            fm.beginTransaction().replace(R.id.content_frame, new ImportFragment()).commit();
        } else if (id == R.id.nav_gallery) {

            if (!mMapFragment.isAdded())
                sFm.beginTransaction().add(R.id.map, mMapFragment).commit();
            else
                sFm.beginTransaction().show(mMapFragment).commit();

        } else if (id == R.id.search_places) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            if (mRoute.getMarkerPointsSize() >= 2) {
                LatLng origin = mRoute.getMarkerPoints().get(0);
                LatLng dest = mRoute.getMarkerPoints().get(1);

                // Getting URL to the Google Directions API
                String url = mRoute.getDirectionsUrl(origin, dest);

                mRoute.downloadTask(url);
                mMap.addPolyline(mRoute.getLinesOptions());
            }

        } else if (id == R.id.search_places) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //DrawRoute mRoute = new DrawRoute(mMap);
        mMap = googleMap;
        markerPoints = new ArrayList<LatLng>();


        mMap.setTrafficEnabled(true);
        // Add a marker in Sofia and move the camera
        LatLng sofia = new LatLng(42.697626, 23.322284);
        float zoomLevel = (float) 11.00; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sofia, zoomLevel));

        checkIfLocationTurned();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showMessageOKCancel("You need to allow access to Location Services",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                            REQUEST_CODE_PERMISSION);
                                }
                            });
                    return;
                }
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_PERMISSION);
                return;
            }
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already 10 locations with 8 waypoints and 1 start location and 1 end location.
                // Upto 8 waypoints are allowed in a query for non-business users
                if (markerPoints.size() >= 10) {
                    return;
                }

                // Adding new item to the ArrayList
                //markerPoints.add(point);
                mRoute.addMarkerToList(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED and
                 * for the rest of markers, the color is AZURE
                 */
                if (mRoute.getMarkerPointsSize() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (mRoute.getMarkerPointsSize() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                } else {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                mMap.clear();
                markerPoints.clear();
                mRoute.clearAll();
            }
        });


    }

    private void checkIfLocationTurned(){
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
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

}
