package emillozev.sofiatraffic.UI.BackgroundProcessing;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GeoCoderIsShit extends AsyncTask<String,Integer,List<LatLng>>{
    @Override
    protected List<LatLng> doInBackground(String... params) {
        return null;
    }

    @Override
    protected void onPostExecute(List<LatLng> list){

    }
}
