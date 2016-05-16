package emillozev.sofiatraffic.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;

import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.MainActivity;
import emillozev.sofiatraffic.UI.RoadWorkActivity;

public class RoadworksFragment extends Fragment{


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View roadworks = inflater.inflate(R.layout.list_road_works, container, false);

        Button download = (Button) roadworks.findViewById(R.id.download);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                Intent intent = new Intent(mainActivity, RoadWorkActivity.class);
                startActivity(intent);
            }

        });;


        return roadworks;

    }


}
