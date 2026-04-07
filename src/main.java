import java.awt.*;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

public class Main {
    public static void main(String[] args) {
        try {
            // Start the Global Hook first
            GlobalScreen.registerNativeHook();

            InputHandler handler = new InputHandler();
            GlobalScreen.addNativeKeyListener(handler);
        
            EventQueue.invokeLater(() -> {
                Dashboard gui = new Dashboard(handler);
                handler.setGui(gui); 
                gui.setVisible(true);
            });

        } catch (NativeHookException ex) {
            System.err.println("Problem registering the native hook.");
            ex.printStackTrace();
            System.exit(1);
        }
    }
}