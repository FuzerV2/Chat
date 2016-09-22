package pl.tpacce.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Szymon on 2015-06-20.
 */
public class ConnectionRunnable implements Runnable {

    private ServerSocket socket;

    public ConnectionRunnable(ServerSocket socket) {
        this.socket = socket;
    }

    public void run() {
        while(true) {
            Socket s = null;
            try {
                s = socket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String str;
                if((str = reader.readLine()) != null) {
                    if(str.contains("nick:")){
                        Server.getInstance().addClient(s, str.split(":")[1]);
                    }else{
                        s.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
