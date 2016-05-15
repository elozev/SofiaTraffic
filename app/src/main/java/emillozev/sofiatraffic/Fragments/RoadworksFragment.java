package emillozev.sofiatraffic.Fragments;

import android.app.Fragment;
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

public class RoadworksFragment extends Fragment{

    public String textFromSite = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View roadworks = inflater.inflate(R.layout.list_road_works, container, false);

        final TextView roadworks_text = (TextView) roadworks.findViewById(R.id.road_works_text);


        Button download = (Button) roadworks.findViewById(R.id.download);
        final MainActivity mainActivity = (MainActivity) getActivity();

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String str = parsingTheSite();
//                roadworks_text.setText(str);
                Toast.makeText(mainActivity, "CLICKED", Toast.LENGTH_SHORT).show();
            }
        });


        return roadworks;

    }
//
//    public String parsingTheSite() {
//        //Log.i("DOWNLOADING",textFromSite);
//
//
//        Thread downloadThread = new Thread(new Runnable() {
//
//            //Log.i("DOWNLOADING",textFromSite);
//
//            public void run() {
//                org.jsoup.nodes.Document doc = null;
//                Log.i("DOWNLOADING",textFromSite);
//                try {
//                    doc = Jsoup.connect("http://www.api.bg/index.php/bg/promeni").get();
//                } catch (IOException e) {
//                    return;
//                }
//
//                Elements newsHeadlines = doc.select(".news-title");
//                textFromSite = newsHeadlines.text().toString();
//                Log.i("DOWNLOADING",textFromSite);
//            }
//        });
//        downloadThread.start();
//        try {
//            downloadThread.join();
//        } catch (InterruptedException e) {
//            Log.i("DOWNLOADING", "error downloading");
//        }
//
//        return textFromSite;
//    }

}
