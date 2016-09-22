package pl.tpacce.chat.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Szymon on 2015-06-20.
 */
public class User {

    private Socket s;
    private String nickName;
    private boolean admin;

    private PrintWriter writer;
    public User(Socket s, String nickName) {
        try {
            writer = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.s = s;
        this.nickName = nickName;
    }

    public Socket getS() {
        return s;
    }

    public String getNickName() {
        return nickName;
    }

    public boolean getAdmin() { return this.admin; }

    public void setAdmin(boolean admin) { this.admin = admin; }

    public void send(String msg) {
        writer.println(msg);
    }
}
