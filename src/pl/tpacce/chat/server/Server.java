package pl.tpacce.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Szymon on 2015-06-19.
 */

public class Server {

    private static Server instance;
    private int port = 9999;
    private ServerSocket socket;
    private Set<User> users = new HashSet<User>();

    public Server(int port) {
        instance = this;
        this.port = port;
        System.out.println("Uruchamianie serwera...");
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Rozpoczynanie nas³uchu na porcie " + port);
        Thread connectionThread = new Thread(new ConnectionRunnable(socket));
        connectionThread.setDaemon(false);
        connectionThread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (!actionConsole(line)) actionAdmin(line);
        }
    }

    private boolean actionConsole(String msg) {
        if (msg.equals("list:online")) {
            String message = "Online: ";
            for (User user : users) message = message + user.getNickName() + " ,";
            if (message.equals("Online: ")) message = message + "no online";
            if (message.endsWith(" ,")) message = message.substring(0, message.length() - 2);
            System.out.println(message);
            return true;
        }
        String[] strings = msg.split(":");
        if (strings[0].equals("isonline")) {
            for (User user : users) {
                if (user.getNickName().startsWith(strings[1])) {
                    System.out.println(strings[1] + " is online");
                    return true;
                }
            }
            System.out.println(strings[1] + " is not online");
            return true;
        }
        if (strings[0].equals("isadmin")) {
            for (User user : users) {
                if (user.getNickName().startsWith(strings[1])) {
                    System.out.println(strings[1] + " is" + (user.getAdmin() ? "" : " not") + " admin");
                    return true;
                }
            }
            System.out.println(strings[1] + " is not admin");
            return true;
        }
        return false;
    }

    private boolean actionUser(User u, String msg) {
        if (msg.equals("list:online")) {
            String message = "Online: ";
            for (User user : users) message = message + user.getNickName() + " ,";
            if (message.equals("Online: ")) message = message + "no online";
            if (message.endsWith(" ,")) message = message.substring(0, message.length() - 2);
            u.send(message);
            return true;
        }
        String[] strings = msg.split(":");
        if (strings[0].equals("isonline")) {
            for (User user : users) {
                if (user.getNickName().startsWith(strings[1])) {
                    u.send(strings[1] + " is online");
                    return true;
                }
            }
            u.send(strings[1] + " is not online");
            return true;
        }
        if (strings[0].equals("isadmin")) {
            for (User user : users) {
                if (user.getNickName().startsWith(strings[1])) {
                    u.send(strings[1] + " is" + (user.getAdmin() ? "" : " not") + " admin");
                    return true;
                }
            }
            u.send(strings[1] + " is not admin");
            return true;
        }
        return false;
    }

    private boolean actionAdmin(String line) {
        if (!line.contains(":")) return false;
        String[] strings = line.split(":");
        if (strings[0].equals("kick")) {
            for (User user : users) {
                if (user.getNickName().startsWith(strings[1])) {
                    kickClient(user);
                    return true;
                }
            }
        }
        if (strings[0].equals("admin")) {
            if (strings.length == 2) {
                for (User user : users) {
                    if (user.getNickName().startsWith(strings[1])) {
                        setAdmin(user);
                        return true;
                    }
                }
            }
            if (strings.length == 3) for (User user : users) {
                if (user.getNickName().startsWith(strings[1])) {
                    setAdmin(user, Boolean.getBoolean(strings[2]));
                    return true;
                }
            }
        }
        if (strings[0].equals("broadcast") || strings[0].equals("bc")) {
            sendToAllClients("[Broadcast] > " + strings[1]);
            return true;
        }
        return false;
    }

    public static Server getInstance() {
        return instance;
    }

    public void addClient(final Socket s, String nickName) {
        System.out.println(s.getInetAddress().getHostAddress() + " " + nickName + " connected!");
        final User u = new User(s, nickName);
        users.add(u);
        u.send("Witamy na chacie");
        u.send("Miłych rozmów ;)");
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    final BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        if (actionUser(u, msg)) {}
                        else if (u.getAdmin() && actionAdmin(msg)) {}
                        else Server.getInstance().sendToAllClients(u.getNickName() + ": " + msg);
                    }
                } catch (SocketException e) {
                    Server.getInstance().removeClient(u);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void kickClient(User u) {
        sendToAllClients(u.getNickName() + " kicked!");
        users.remove(u);
    }

    public void setAdmin(User u) {
        sendToAllClients(u.getNickName() + " has" + (!u.getAdmin() ? "" : "n't") + " admin");
        u.setAdmin(!u.getAdmin());
    }

    public void setAdmin(User u, boolean b) {
        sendToAllClients(u.getNickName() + " has" + (b ? "" : "n't") + " admin");
        u.setAdmin(b);
    }

    private void sendToAllClients(String msg) {
        System.out.println(msg);
        for (User u : users) {
            u.send(msg);
        }
    }

    public void removeClient(User u) {
        sendToAllClients(u.getNickName() + " disconnected!");
        users.remove(u);
    }
}