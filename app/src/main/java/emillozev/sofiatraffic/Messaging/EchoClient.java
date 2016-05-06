package emillozev.sofiatraffic.Messaging;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient extends Thread {

    private String textToSend;
    private static final String HOST = "192.168.0.103";
    private static final int PORT = 4444;
    private boolean send = false;

    public EchoClient(){}

    public void setTextToSend(String text){
        textToSend = text;
        send = true;
    }
    @Override
    public void run() {
        Socket echoSocket = null;
        try {
            echoSocket = new Socket(HOST, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter outStream = null;
        try {
            outStream = new PrintWriter(echoSocket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader inReader = null;
        try {
            inReader = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }


        while(true) {
                if(send) {
                    outStream.println(textToSend);
                    String inputReader = null;
                    try {
                        inputReader = new String(inReader.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.d("INPUTREADER", inputReader);
                    // do something...
                    try {
                        Log.d("ECHO", "echo: " + new String(inReader.readLine()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //echoSocket.close();
                    send = false;
                }
        }
            //echoSocket.close();
//        try {
//            //inReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
      //  outStream.close();

    }

    public static void main(String[] args) {
        EchoClient client = new EchoClient();
        client.start();
    }

}