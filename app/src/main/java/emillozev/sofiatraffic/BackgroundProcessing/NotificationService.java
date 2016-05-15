package emillozev.sofiatraffic.BackgroundProcessing;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.MainActivity;

public class NotificationService extends Service implements LocationListener{

    private final static String TAG = "ShowNotification";
    private List<LatLng> markerForTraffic;
    private String[] textFromSite;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        GeoCoderIsShit geoCoderIsShit = new GeoCoderIsShit();
        geoCoderIsShit.execute("ape");



        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotification() {
        android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(this);
        builder.setAutoCancel(true)
                .setContentTitle("Sofia Traffic Notification")
                .setContentText(("You are getting nearby a jammed area!"))
                .setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(8, notification);

        Log.i(TAG, "Notification created");
    }

    @Override
    public void onDestroy() {
        // STOP YOUR TASKS
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        for (LatLng latLng : markerForTraffic) {
            if (distanceBetweenLatLng(latLng.latitude, latLng.longitude, location.getLatitude(), location.getLongitude()) < 1) {
                sendNotification();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class GeoCoderIsShit extends AsyncTask<String, Integer, List<LatLng>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<LatLng> doInBackground(String... params) {
            String[] textSite = parsingTheSite();

            if (textSite != null) {
                List<LatLng> toBeCopied = new ArrayList<>();

                for (String a : textSite) {

                    Geocoder geocoder = new Geocoder(NotificationService.this);
                    List<Address> addresses = null;

                    try {
                        addresses = geocoder.getFromLocationName(a, 1);
                    } catch (IOException e) {
                        Log.i("ERROR GEOCODER", "Error");
                    }

                    if (addresses != null && !addresses.isEmpty()) {
                        toBeCopied.add(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()));
                        Log.i("SERVICE", "ADDING TO LIST");
                    }
                }
                markerForTraffic = new ArrayList<>(toBeCopied);
            } else {
                //Toast.makeText(MainActivity.this, "Please enable data and restart the app!", Toast.LENGTH_LONG).show();
                Log.i("ASYNCTASK", "NO INTERNET");
            }
            return markerForTraffic;
        }
    }

    public String[] parsingTheSite() {

        Thread downloadThread = new Thread(new Runnable() {

            public void run() {
                org.jsoup.nodes.Document doc = null;

                try {
                    doc = Jsoup.connect("http://tix.bg/bg/Sofia/").get();
                } catch (IOException e) {
                    return;
                }

                Elements newsHeadlines = doc.select(".dhtzelement");
                textFromSite = newsHeadlines.text().toString().split("\\b(?:към|от)\\b");

            }
        });
        downloadThread.start();
        try {
            downloadThread.join();
        } catch (InterruptedException e) {

        }

        return textFromSite;
    }


    public double distanceBetweenLatLng(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371; //kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);

        return dist;
    }

}
