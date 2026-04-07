import javax.swing.*;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Dashboard extends JFrame {
    private InputHandler inputHandler = new InputHandler();
    private JLabel infoLabel = new JLabel("Page 1: Welcome", SwingConstants.CENTER);

    private int currentScreenIndex = 0;

    public Dashboard() {
        setTitle("Secondary Monitor Tool");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true); // Useful for second monitor tools
        
        setLayout(new BorderLayout());
        add(infoLabel, BorderLayout.CENTER);

        JButton rebindBtn = new JButton("Configure Binds");
        rebindBtn.addActionListener(e -> openRebindDialog(Action.NEXT)); 
        add(rebindBtn, BorderLayout.SOUTH);

        JButton switchMonitorBtn = new JButton("Next Monitor");
        switchMonitorBtn.addActionListener(e -> cycleScreen());
        add(switchMonitorBtn);

        // Add the listener to the window itself
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Iterate through your Enum to see if the pressed key matches a saved binding
                Action triggeredAction = null;
                for (Action a : Action.values()) {
                    if (inputHandler.getBinding(a) == e.getKeyCode()) {
                        triggeredAction = a;
                        break;
                    }
                }

                if (triggeredAction != null) {
                    processAction(triggeredAction);
                }
                if (triggeredAction != null) {
                    processAction(triggeredAction);
                }
            }
        });

        // Close all JNativeHook procces uppon closing Window
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    // This is the "Kill Switch"
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });

        setFocusable(true);
        setVisible(true);
    }

    private void processAction(Action action) {
        // All "use case" logic is centralized here
        switch (action) {
            case NEXT -> infoLabel.setText("Showing Next Info...");
            case PREVIOUS -> infoLabel.setText("Showing Previous Info...");
            case NEXT_PAGE -> infoLabel.setText("Switching Page...");
            default -> throw new IllegalArgumentException("Unexpected value: " + action);
        }
    }

    private void openRebindDialog(Action action) {
        new RebindDialog(this, action, inputHandler).setVisible(true);
    }

    public void cycleScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
    
        // Increment index and wrap around using modulo
        currentScreenIndex = (currentScreenIndex + 1) % screens.length;
    
        // Get the bounds of the newly selected screen
        Rectangle bounds = screens[currentScreenIndex].getDefaultConfiguration().getBounds();

        // Move the window to the top-left of that screen
        this.setLocation(bounds.x + 50, bounds.y + 50);
    }
}