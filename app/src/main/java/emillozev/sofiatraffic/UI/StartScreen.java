package emillozev.sofiatraffic.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import emillozev.sofiatraffic.R;

public class StartScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartScreen.this, MainActivity.class);
                StartScreen.this.startActivity(intent);
                StartScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


}
