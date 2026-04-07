import java.io.*;
import java.util.Properties;

public class InputHandler {
    private Properties userBinds = new Properties();
    private final String USER_PATH = "data/user_binds.properties";
    private final String DEFAULT_PATH = "data/default_binds.properties";

    public InputHandler() {
        loadBinds();
    }

    private void loadBinds() {
        File userFile = new File(USER_PATH);
        
        // If user file doesn't exist, load from default then save a copy
        if (!userFile.exists()) {
            System.out.println("User binds missing, loading defaults...");
            loadFromFile(DEFAULT_PATH);
            saveBinds(); 
        } else {
            loadFromFile(USER_PATH);
        }
    }

    private void loadFromFile(String path) {
        try (InputStream input = new FileInputStream(path)) {
            userBinds.load(input);
        } catch (IOException ex) {
            System.err.println("Could not find file: " + path);
        }
    }

    public void saveBinds() {
        try (OutputStream output = new FileOutputStream(USER_PATH)) {
            userBinds.store(output, "Campaign Companion - Key Bindings");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void rebind(Action action, int keyCode) {
        userBinds.setProperty(action.name(), String.valueOf(keyCode));
        saveBinds();
    }

    public int getBinding(Action action) {
        String code = userBinds.getProperty(action.name());
        try {
            return (code != null) ? Integer.parseInt(code) : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}