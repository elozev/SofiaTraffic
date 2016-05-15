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

import java.io.IOException;

import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.MainActivity;
import emillozev.sofiatraffic.UI.RoadWorkActivity;

public class RoadworksFragment extends Fragment{


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View roadworks = inflater.inflate(R.layout.list_road_works, container, false);

        final TextView roadworks_text = (TextView) roadworks.findViewById(R.id.road_works_text);


        Button download = (Button) roadworks.findViewById(R.id.download);
        final MainActivity mainActivity = (MainActivity) getActivity();

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String[] str = parsingTheSite();
//                String newStr = "";
//
//                for(String string : str){
//                    newStr += string + "\n";
//                }
//                roadworks_text.setText(newStr);
//                Toast.makeText(mainActivity, "CLICKED", Toast.LENGTH_SHORT).show();

                MainActivity mainActivity = (MainActivity) getActivity();
                Intent intent = new Intent(mainActivity, RoadWorkActivity.class);
                startActivity(intent);


            }

        });


        return roadworks;

    }


}
