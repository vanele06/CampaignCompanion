import javax.swing.*;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;

public class Dashboard extends JFrame {
    private InputHandler inputHandler;
    
    // The "Switcher" that swaps between Setup and Information views
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);
    
    private JPanel setupPanel;
    private InformationView infoView;

    private JLabel infoLabel = new JLabel("Page 1: Welcome", SwingConstants.CENTER);
    private int currentScreenIndex = 0;
    private GraphicsDevice currentDevice;

    public Dashboard(InputHandler handler) {
        this.inputHandler = handler;
        
        setTitle("Campaign Companion");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); 

        // --- 1. Setup the "Setup Panel" (Original Buttons) ---
        setupPanel = new JPanel(new BorderLayout());
        infoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        setupPanel.add(infoLabel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(3, 1));
        
        JButton switchMonitorBtn = new JButton("Move to Next Monitor");
        switchMonitorBtn.addActionListener(e -> cycleScreen());

        JButton chooseScreenBtn = new JButton("Choose This Screen");
        chooseScreenBtn.addActionListener(e -> maximizeToCurrentScreen());

        JButton settingsBtn = new JButton("Open Rebind Menu");
        settingsBtn.addActionListener(e -> openRebindDialog(Action.NEXT));

        controlPanel.add(switchMonitorBtn);
        controlPanel.add(chooseScreenBtn); 
        controlPanel.add(settingsBtn);
        setupPanel.add(controlPanel, BorderLayout.SOUTH);

        // --- 2. Setup the "Information View" (The new Slate) ---
        // We pass 'this::returnToDashboard' so the gear button can trigger the reset
        infoView = new InformationView(this::returnToDashboard);

        // --- 3. Add both to the CardLayout Container ---
        mainContainer.add(setupPanel, "SETUP");
        mainContainer.add(infoView, "INFO");
        add(mainContainer);

        // --- 4. Listeners ---
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public void processAction(Action action) {
        SwingUtilities.invokeLater(() -> {
            String content = switch (action) {
                case NEXT -> "NEXT CONTENT";
                case PREVIOUS -> "PREVIOUS CONTENT";
                case NEXT_PAGE -> "PAGE SWAP";
            };
            // Tell the big slate to update its text
            infoView.updateContent(content);
        });
    }

    public void maximizeToCurrentScreen() {
        if (currentDevice == null) {
            currentDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        }
        
        Rectangle bounds = currentDevice.getDefaultConfiguration().getBounds();
        
        this.dispose();
        this.setLocation(bounds.x, bounds.y);
        this.setUndecorated(false); 
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Switch the UI to show the Information Slate
        cardLayout.show(mainContainer, "INFO");
        
        this.setVisible(true);
    }

    public void returnToDashboard() {
        // Return window to normal size and center it
        this.setExtendedState(JFrame.NORMAL);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        
        // Switch back to the setup buttons
        cardLayout.show(mainContainer, "SETUP");
    }

    private void openRebindDialog(Action action) {
        new RebindDialog(this, action, inputHandler).setVisible(true);
    }

    public void cycleScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        currentScreenIndex = (currentScreenIndex + 1) % screens.length;
        currentDevice = screens[currentScreenIndex];
        
        Rectangle bounds = currentDevice.getDefaultConfiguration().getBounds();
        this.setLocation(bounds.x + (bounds.width / 2) - 200, bounds.y + (bounds.height / 2) - 100);
    }
}