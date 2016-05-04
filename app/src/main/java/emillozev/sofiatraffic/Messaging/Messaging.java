package emillozev.sofiatraffic.Messaging;

import android.app.Application;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.parse.Parse;

import emillozev.sofiatraffic.R;

public class Messaging extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Parse.initialize(this, "sofiatraffic1234", "123456789qwertyuiop");
    }


}
