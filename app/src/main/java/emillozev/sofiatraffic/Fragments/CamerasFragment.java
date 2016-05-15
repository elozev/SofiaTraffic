package emillozev.sofiatraffic.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;

import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.CamerasWebView;
import emillozev.sofiatraffic.UI.MainActivity;


public class CamerasFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View cameras_fragment = inflater.inflate(R.layout.cameras_fragment, container, false);

        final Button showCameras = (Button) cameras_fragment.findViewById(R.id.showCameras);

        showCameras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                Intent intent = new Intent(mainActivity, CamerasWebView.class);
                startActivity(intent);
            }
        });
        return cameras_fragment;
    }

}
