package emillozev.sofiatraffic.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.MainActivity;

public class SettingsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View settings_fragment = inflater.inflate(R.layout.settings_fragment, container, false);

        CheckBox notificationsCheckBox = (CheckBox)
                settings_fragment.findViewById(R.id.notificationsCheckBox);

        CheckBox speedometerCheckBox = (CheckBox)
                settings_fragment.findViewById(R.id.speedometerCheckBox);

        boolean checkedNotifications = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean("notificationsCheckBox", false);
        notificationsCheckBox.setChecked(checkedNotifications);

        notificationsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    MainActivity.mShowNotifications = true;
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("notificationsCheckBox", true).commit();
                } else {
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("notificationsCheckBox", false).apply();
                    MainActivity.mShowNotifications = false;
                }
            }
        });

        boolean checkedSpeedometer = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean("speedometerCheckBox", false);
        speedometerCheckBox.setChecked(checkedSpeedometer);

        speedometerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    MainActivity.mShowSpeedometer = true;
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("speedometerCheckBox", true).commit();
                }else {
                    MainActivity.mShowSpeedometer = false;
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("speedometerCheckBox", false).apply();
                }
            }
        });

        return settings_fragment;

    }


}
