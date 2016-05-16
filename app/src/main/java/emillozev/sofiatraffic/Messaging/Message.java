package emillozev.sofiatraffic.Messaging;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import emillozev.sofiatraffic.R;

public class Message extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button sendButton = (Button) findViewById(R.id.sendButton);
        final Button refreshButton = (Button) findViewById(R.id.refreshButton);
        final EditText textField = (EditText) findViewById(R.id.messageToSend);
        final TextView messageBox = (TextView) findViewById(R.id.messageBox);

        messageBox.setMovementMethod(new ScrollingMovementMethod());

        if (sendButton != null) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!textField.getText().toString().equals("")) {
                        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                        StringBuilder sb = new StringBuilder();
                        Random random = new Random();
                        for (int i = 0; i < 20; i++) {
                            char c = chars[random.nextInt(chars.length)];
                            sb.append(c);
                        }
                        String output = sb.toString();

                        EchoClient echoClient = new EchoClient(output, textField.getText().toString());
                        echoClient.start();
                        textField.setText("");

                        refreshButton.performClick();
                    }else{
                        Toast.makeText(Message.this, "Please fill the message", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (refreshButton != null) {
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                    StringBuilder sb = new StringBuilder();
                    Random random = new Random();
                    for (int i = 0; i < 20; i++) {
                        char c = chars[random.nextInt(chars.length)];
                        sb.append(c);
                    }
                    String output = sb.toString();

                    EchoClient echoClient = new EchoClient(output,"");
                    echoClient.start();

                    String replaceString = "";

                    if(EchoClient.receivedMessage != null)
                        replaceString = EchoClient.receivedMessage.replaceAll("#", "\n");

                    messageBox.setText(replaceString);
                }
            });
        }

    }
}
