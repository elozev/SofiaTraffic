package emillozev.sofiatraffic.UI.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.MainActivity;

public class NavigationFragment extends Fragment{

    public Button mGetDirectionsButton;
    public  PlaceAutocompleteFragment autocompleteFragment = new PlaceAutocompleteFragment();
    public  PlaceAutocompleteFragment autocompleteFragment2 = new PlaceAutocompleteFragment();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View navigation_fragment = inflater.inflate(R.layout.navigation_fragment, container, false);


        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng originLat = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                Log.i("AUTO COMPLETE", "An error occurred: " + status);
            }
        });

        autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng dest = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                Log.i("AUTO COMPLETE", "An error occurred: " + status);
            }
        });

        final MainActivity mainActivity = (MainActivity) getActivity();

        mGetDirectionsButton = (Button) navigation_fragment.findViewById(R.id.getDirectionsButton);

        mGetDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mainActivity, "Button clicked", Toast.LENGTH_LONG).show();
            }
        });



        return navigation_fragment;

    }
}
