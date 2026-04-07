import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import java.io.*;
import java.util.Properties;

// The 'implements' keyword here is what fixes your '@Override' error
public class InputHandler implements NativeKeyListener {
    private Properties userBinds = new Properties();
    private Dashboard gui;
    private final String USER_PATH = "data/user_binds.properties";
    private final String DEFAULT_PATH = "data/default_binds.properties";
    
    public InputHandler() {
        loadBinds();
    }

    public void setGui(Dashboard gui) {
        this.gui = gui;
    }

    private void loadBinds() {
        File userFile = new File(USER_PATH);
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

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        // DIAGNOSTIC 1: See every key the library hears
        System.out.println("Library detected Raw Code: " + e.getKeyCode());

        Action triggeredAction = null;
        for (Action a : Action.values()) {
            int boundCode = getBinding(a);
            
            // DIAGNOSTIC 2: Compare what was pressed vs what is saved
            if (boundCode == e.getKeyCode()) {
                triggeredAction = a;
                break;
            }
        }

        if (triggeredAction != null) {
            System.out.println("Match found! Triggering: " + triggeredAction);
            if (gui != null) {
                gui.processAction(triggeredAction);
            }
        }
    }

    // These MUST be present (even if empty) to satisfy the NativeKeyListener interface
    @Override public void nativeKeyReleased(NativeKeyEvent e) {}
    @Override public void nativeKeyTyped(NativeKeyEvent e) {}
}