package emillozev.sofiatraffic.Messaging;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient extends Thread {

    private String textToSend;
    private static final String HOST = "192.168.0.103";
    private static final int PORT = 4444;

    public EchoClient(String text){
        textToSend = text;
    }

    @Override
    public void run() {
        try (Socket echoSocket = new Socket(HOST, PORT);
             OutputStream outStream = echoSocket.getOutputStream();
             BufferedReader inReader = new BufferedReader(
                     new InputStreamReader(echoSocket.getInputStream()))
        ) {

            outStream.write(textToSend.getBytes());
            outStream.flush();

            // do something...
            Log.i("ECHO","echo: " + new String(inReader.readLine()));
            echoSocket.close();
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