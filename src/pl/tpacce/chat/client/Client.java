package pl.tpacce.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Szymon on 2015-06-19.
 */

public class Client  {

    private static Socket socket;

    public Client(String host, int port, String nick) throws Exception{
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        socket = new Socket(host, port);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer.println("nick:" + nick);
        Thread readThread = new Thread(new Runnable(){
            public void run() {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        readThread.setDaemon(true);
        readThread.start();
        while (true){
            String s = console.readLine();
            writer.println(s);
        }
    }
}