package emillozev.sofiatraffic.Messaging;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.MainActivity;

public class Message extends AppCompatActivity {

    private String textToSend = "Hello";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button connectButton = (Button) findViewById(R.id.connectButton);
        final EditText textField = (EditText) findViewById(R.id.messageToSend);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textField.getText().toString().equals("")) {
                    textToSend = textField.getText().toString();
                    Log.i("TEXTFIELD", textToSend);
                    EchoClient echoClient = new EchoClient(textToSend);
                    echoClient.start();
//                    try {
//                        echoClient.join();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    textField.setText("");
                }else{
                    Toast.makeText(Message.this, "You cannot send empty message", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
