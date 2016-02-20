package emillozev.sofiatraffic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartScreen.this, MapsActivity.class);
                StartScreen.this.startActivity(intent);
                StartScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
