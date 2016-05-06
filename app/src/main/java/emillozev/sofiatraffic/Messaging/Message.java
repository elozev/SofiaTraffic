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
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button connectButton = (Button) findViewById(R.id.connectButton);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        final EditText textField = (EditText) findViewById(R.id.messageToSend);
        final EchoClient echoClient = new EchoClient();


        if(connectButton != null) {
            connectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isConnected) {
//                        textToSend = textField.getText().toString();
//                        Log.i("TEXTFIELD", textToSend);

                        echoClient.start();
//                        textField.setText("");
                        connectButton.setText("DISCONNECT");
                        isConnected = true;
                    }else{
                        try {
                            echoClient.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        connectButton.setText("CONNECT");
                    }
                }
            });
        }

        if(sendButton != null) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isConnected && textField.getText() != null){
                        echoClient.setTextToSend(textField.getText().toString());
                        textField.setText("");
                    }else if(!isConnected){
                        Toast.makeText(Message.this, "Please connect first", Toast.LENGTH_SHORT).show();
                    }else if(textField.getText() == null){
                        Toast.makeText(Message.this, "Please fill message", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

}
