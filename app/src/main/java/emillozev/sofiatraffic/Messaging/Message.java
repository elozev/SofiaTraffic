package emillozev.sofiatraffic.Messaging;

import android.os.Bundle;
import android.os.Handler;
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
    private String textFieldText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button connectButton = (Button) findViewById(R.id.connectButton);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        final EditText textField = (EditText) findViewById(R.id.messageToSend);
        final EchoClient[] echoClient = new EchoClient[1];


        if (connectButton != null) {
            connectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isConnected) {
//                        textToSend = textField.getText().toString();
//                        Log.i("TEXTFIELD", textToSend);
                        echoClient[0] = new EchoClient();
                        echoClient[0].start();
//                        textField.setText("");
                        connectButton.setText("DISCONNECT");
                        isConnected = true;
                    } else {
                        //echoClient[0].interrupt();
                        //echoClient[0].stop();
                        echoClient[0].setTextToSend(null);
                        connectButton.setText("CONNECT");
                        isConnected = false;
                    }
                }
            });
        }

        if (sendButton != null) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnected && textField.getText() != null) {
                        echoClient[0].setTextToSend(textField.getText().toString());
                        textField.setText("");
                    } else if (!isConnected) {
                        Toast.makeText(Message.this, "Please connect first", Toast.LENGTH_SHORT).show();
                    } else if (textField.getText() == null || textField.getText().equals("")) {
                        Toast.makeText(Message.this, "Please fill in message", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

//        final Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//
//            @Override
//            public void run() {
//                try{
//                    String [] splitMessage;
//                    splitMessage = EchoClient.receivedMessage.split(" ");
//                    textFieldText += splitMessage[1];
//                    textFieldText += ": ";
//                    textFieldText += splitMessage[2];
//                    textField.setText(textFieldText);
//                }
//                catch (Exception e) {
//                    // TODO: handle exception
//                }
//                finally{
//                    //also call the same runnable to call it at regular interval
//                    handler.postDelayed(this, 1000);
//                }
//            }
//        };
//        handler.postDelayed(runnable, 1000);
//
//    }
    }
}
