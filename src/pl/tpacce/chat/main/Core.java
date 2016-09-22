package pl.tpacce.chat.main;

import pl.tpacce.chat.client.Client;
import pl.tpacce.chat.data.Config;
import pl.tpacce.chat.data.Properties;
import pl.tpacce.chat.server.Server;

import java.util.Scanner;

/**
 * Created by Szymon on 2015-06-19.
 */
public class Core {

    public static Scanner scanner;
    public static Server server;
    public static Client client;
    public static Properties properties;
    public static Config config;
    private static boolean logged = false;

    public static void main(String[] args) {
        properties = new Properties();
        config = new Config();
        properties.load();
        config.load();
        config.save();
        properties.save();
        scanner = new Scanner(System.in);
        while (!logged) {
            System.out.println("Server/Client?");
            String serverclient = scanner.nextLine();
            System.out.println("Port?");
            String port = scanner.nextLine();
            if (port.equals("")) port = "2048";
            if (serverclient.equalsIgnoreCase("server")) {
                server = new Server(Integer.parseInt(port));
                logged = !logged;
            }
            else if (serverclient.equalsIgnoreCase("client")) {
                System.out.println("Host?");
                String host = scanner.nextLine();
                if (host.equals("")) host = "localhost";
                System.out.println("Nick? Jeżeli rejestracja wpisz \"Register\"");
                String nick = scanner.nextLine();
                String password = "";
                if (nick.equalsIgnoreCase("Register")) {
                    System.out.println("Nick?");
                    nick = scanner.nextLine();
                    config.setNick(nick);
                    System.out.println("Hasło?");
                    password = scanner.nextLine();
                    config.setPassword(password);
                    config.save();
                    properties.save();
                }
                else {
                    System.out.println("Hasło?");
                    password = scanner.nextLine();
                }
                if (!nick.equals(config.getNick())) System.out.println("Zły nick!");
                else if (!password.equals(config.getPassword())) System.out.println("Złe hasło!");
                else {
                    try {
                        client = new Client(host, Integer.parseInt(port), nick);
                        logged = !logged;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
