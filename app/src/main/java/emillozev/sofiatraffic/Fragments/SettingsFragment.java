package emillozev.sofiatraffic.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import emillozev.sofiatraffic.R;

public class SettingsFragment extends Fragment {

    public static boolean isNotificationsClicked;
    public static boolean isSpeedometerClicked;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View settings_fragment = inflater.inflate(R.layout.settings_fragment, container, false);
        CheckBox notificationsCheckBox = (CheckBox) settings_fragment.findViewById(R.id.notificationsCheckBox);
        CheckBox speedometerCheckBox = (CheckBox) settings_fragment.findViewById(R.id.speedometerCheckBox);

        notificationsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    isNotificationsClicked = true;
                else
                    isNotificationsClicked = false;
            }
        });

        speedometerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    isSpeedometerClicked = true;
                else
                    isSpeedometerClicked = false;
            }
        });

        return settings_fragment;

    }


}
