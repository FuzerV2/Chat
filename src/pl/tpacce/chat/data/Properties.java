package pl.tpacce.chat.data;

import java.io.*;

/**
 * Created by Szymon on 2015-06-23.
 */
public class Properties {

    public java.util.Properties prop = new java.util.Properties();
    private File folder = new File(System.getenv("APPDATA") + File.separator + ".Chat");
    private File file = new File(System.getenv("APPDATA") + File.separator + ".Chat" + File.separator + "config.properties");
    private FileInputStream input = null;
    private FileOutputStream output = null;

    public void load() {
        if (!file.exists()) {
            try {
                folder.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            input = new FileInputStream(file);
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            output = new FileOutputStream(file);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
