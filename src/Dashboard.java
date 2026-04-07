import javax.swing.*;
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
                Action action = inputHandler.getAction(e.getKeyCode());
                if (action != null) {
                    processAction(action);
                }
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