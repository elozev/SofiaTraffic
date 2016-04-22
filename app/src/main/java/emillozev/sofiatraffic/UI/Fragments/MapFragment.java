package emillozev.sofiatraffic.UI.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.MainActivity;

public class MapFragment extends Fragment {

    public Button mSearchButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View map_view = inflater.inflate(R.layout.map_fragment, container, false);

        mSearchButton = (Button) map_view.findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mSearchButton.setText("Back");

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NavigationFragment navigationFragment = new NavigationFragment();

                fragmentTransaction.add(R.id.main_fragment_for_replacement, navigationFragment);
                fragmentTransaction.show(navigationFragment).commit();

            }
        });

        return map_view;



    }
}
