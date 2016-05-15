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

    public String[] textFromSite;

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

    public String[] parsingTheSite() {
        //Log.i("DOWNLOADING",textFromSite);


        Thread downloadThread = new Thread(new Runnable() {

            //Log.i("DOWNLOADING",textFromSite);

            public void run() {
                org.jsoup.nodes.Document doc = null;
                try {
                    doc = Jsoup.connect("http://www.api.bg/index.php/bg/promeni/").timeout(10*1000).get();
                } catch (IOException e) {
                    return;
                }

                Elements newsHeadlines = doc.select("div.news-item>p");

                textFromSite = newsHeadlines.text().toString().split("\\.[^ ]");

                String text = newsHeadlines.text().toString();
                Log.i("YOLO2", text + "\n ------------------ \n");


                for(String string: textFromSite) {
                    Log.i("YOLO", string + "\n ------------------ \n");
                }
            }
        });
        downloadThread.start();
        try {
            downloadThread.join();
        } catch (InterruptedException e) {
        }

        return textFromSite;
    }

}
