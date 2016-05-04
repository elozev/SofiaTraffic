package emillozev.sofiatraffic.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import emillozev.sofiatraffic.DirectionsAndNavigation.DrawRoute;
import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.MainActivity;

public class NavigationFragment extends Fragment{

    private PlaceAutocompleteFragment autocompleteFragment = new PlaceAutocompleteFragment();
    private PlaceAutocompleteFragment autocompleteFragment2 = new PlaceAutocompleteFragment();
    private Button mGetDirectionsButton;
    private Button mStartNavigation;

    public static LatLng origin;
    public static LatLng dest;
    public static PolylineOptions polylineOptions;

    private RadioGroup radioGroup;
    private RadioButton radioB;

    private DrawRoute mRoute = new DrawRoute();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View navigation_fragment = inflater.inflate(R.layout.navigation_fragment, container, false);
        final MainActivity mainActivity = (MainActivity) getActivity();

        autocompleteFragment = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                origin = place.getLatLng();
                Toast.makeText(mainActivity, origin.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {
                Log.i("AUTO COMPLETE", "An error occurred: " + status);
            }
        });

        autocompleteFragment2 = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                dest = place.getLatLng();
                Toast.makeText(mainActivity, dest.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {
                Log.i("AUTO COMPLETE", "An error occurred: " + status);
            }
        });


        mGetDirectionsButton = (Button) navigation_fragment.findViewById(R.id.getDirectionsButton);

        mGetDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(origin == null && dest == null){
                    Toast.makeText(mainActivity, "Please fill up both for directions", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mainActivity, "Button clicked", Toast.LENGTH_LONG).show();
                    mRoute.clearAll();
                    mRoute.addMarkerToList(origin);
                    mRoute.addMarkerToList(dest);

                    String url = mRoute.getDirectionsUrl(origin, dest);

                    mRoute.downloadTask(url);
                    polylineOptions = mRoute.getLinesOptions();

                    FragmentManager fm = getFragmentManager();
                    ImportFragment importFragment = new ImportFragment();
                    fm.beginTransaction().replace(R.id.main_fragment_for_replacement, importFragment).commit();
                }
            }
        });

        mStartNavigation = (Button) navigation_fragment.findViewById(R.id.startNavigationButton);
        radioGroup = (RadioGroup) navigation_fragment.findViewById(R.id.rg_navigation_method);

        mStartNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dest != null && origin == null) {
                    String modeForNavigation;
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId == -1) {
                        modeForNavigation = "d";
                    } else {
                        Log.i("SELECTED ID", "" + selectedId);
                        radioB = (RadioButton) navigation_fragment.findViewById(selectedId);
                        Toast.makeText(mainActivity, radioB.getText().toString(), Toast.LENGTH_SHORT).show();
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

                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + dest.latitude + "," + dest.longitude + "&mode=" + modeForNavigation);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(mainActivity, "Fill in only \"To:\"! The start point is your location! ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return navigation_fragment;

    }


}