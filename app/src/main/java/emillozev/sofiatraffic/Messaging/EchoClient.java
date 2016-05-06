package emillozev.sofiatraffic.Messaging;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
        try (Socket echoSocket = new Socket(HOST, PORT);
             PrintWriter outStream = new PrintWriter(echoSocket.getOutputStream(),true);
             BufferedReader inReader = new BufferedReader(
                     new InputStreamReader(echoSocket.getInputStream()))
        ) {

            while(true) {
                if(send) {
                    outStream.println(textToSend);

                    String inputReader = new String(inReader.readLine());

                    Log.d("INPUTREADER", inputReader);
                    // do something...
                    Log.d("ECHO", "echo: " + new String(inReader.readLine()));
                    //echoSocket.close();
                    send = false;
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
//        EchoClient client = new EchoClient(textToSend);
//        client.start();
    }

}