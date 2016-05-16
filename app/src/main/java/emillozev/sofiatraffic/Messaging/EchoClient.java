package emillozev.sofiatraffic.Messaging;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient extends Thread {

    private static final String HOST = "192.168.0.104";
    private static final int PORT = 4444;
    public static String receivedMessage;
    private String client = "LOGIN ";
    private String message = "";

    public EchoClient(String client, String message){
        this.client += client;
        this.message = message;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter outStream = null;
        try {
            outStream = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader inReader = null;
        try {
            inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("SERVER","Send to server: " + client);
        outStream.println(client);

        String fromServer;
        try {
            if((fromServer = inReader.readLine()) != null){
                Log.i("Server", "Received from server: " + fromServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        outStream.println("BRDCAST " + message);
        String fromServer2;
        try {
            if((fromServer2 = inReader.readLine()) != null){
                Log.i("Server", "Received from server2: " + fromServer2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        outStream.println("GET");
        String fromServer3;
        try {
            if((fromServer3 = inReader.readLine()) != null){
                Log.i("Server", "Received from server3: " + fromServer3);
                receivedMessage = fromServer3;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("Server", "END OF CONNECTION");

        outStream.close();
        try {
            inReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}