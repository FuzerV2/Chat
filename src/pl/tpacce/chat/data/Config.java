package pl.tpacce.chat.data;

import pl.tpacce.chat.main.Core;

/**
 * Created by Szymon on 2015-06-23.
 */
public class Config {

    private String nick;
    private String password;

    public void load() {
        nick = Core.properties.prop.getProperty("nick", "");
        password = Core.properties.prop.getProperty("password", "");
    }

    public void save() {
        Core.properties.prop.setProperty("nick", nick);
        Core.properties.prop.setProperty("password", password);
        Core.properties.save();
    }

    public String getNick() { return this.nick; }

    public void setNick(String nick) { this.nick = nick; }

    public String getPassword() { return this.password; }

    public void setPassword(String password) { this.password = password; }
}
