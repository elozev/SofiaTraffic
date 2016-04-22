package emillozev.sofiatraffic.UI.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.MainActivity;

public class NavigationFragment extends Fragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View navigation_fragment = inflater.inflate(R.layout.navigation_fragment, container, false);


        return navigation_fragment;

    }
}
