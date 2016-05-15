package emillozev.sofiatraffic.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import emillozev.sofiatraffic.Fragments.CamerasFragment;
import emillozev.sofiatraffic.R;

public class CamerasWebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameras_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebView mWebView=new WebView(CamerasWebView.this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url="+"http://kat.mvr.bg/NR/rdonlyres/FC9B850F-9001-4659-97F1-9A4E48FD71C7/0/askpd_tkontrol.pdf");
        setContentView(mWebView);
    }

}
