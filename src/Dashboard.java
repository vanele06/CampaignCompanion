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

    private JLabel infoLabel = new JLabel("Welcome to the Campaign Companion", SwingConstants.CENTER);
    private int currentScreenIndex = 0;
    private GraphicsDevice currentDevice;

    private JButton rebindNextBtn;
    private JButton rebindPrevBtn;
    private JButton rebindPageBtn;

    public Dashboard(InputHandler handler) {
        this.inputHandler = handler;
        
        setTitle("Campaign Companion");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); 

        // --- 1. Setup the "Setup Panel" ---
        setupPanel = new JPanel(new BorderLayout());
        infoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        setupPanel.add(infoLabel, BorderLayout.CENTER);

        // Increase grid rows to 5 to fit the new buttons
        JPanel controlPanel = new JPanel(new GridLayout(5, 1, 5, 5)); 

        JButton switchMonitorBtn = new JButton("Move to Next Monitor");
        switchMonitorBtn.addActionListener(e -> cycleScreen());

        JButton chooseScreenBtn = new JButton("Choose This Screen");
        chooseScreenBtn.addActionListener(e -> maximizeToCurrentScreen());

        // Create specific buttons for each rebindable action
        rebindNextBtn = new JButton("Rebind NEXT (" + Bindings.getHumanReadable(Action.NEXT, inputHandler) + ")");
        rebindNextBtn.addActionListener(e -> openRebindDialog(Action.NEXT));

        rebindPrevBtn = new JButton("Rebind PREVIOUS (" + Bindings.getHumanReadable(Action.PREVIOUS, inputHandler) + ")");
        rebindPrevBtn.addActionListener(e -> openRebindDialog(Action.PREVIOUS));

        rebindPageBtn = new JButton("Rebind PAGE SWAP (WIP) (" + Bindings.getHumanReadable(Action.NEXT_PAGE, inputHandler) + ")");
        rebindPageBtn.addActionListener(e -> openRebindDialog(Action.NEXT_PAGE));

        refreshButtonLabels();

        controlPanel.add(switchMonitorBtn);
        controlPanel.add(chooseScreenBtn); 
        controlPanel.add(rebindNextBtn);
        controlPanel.add(rebindPrevBtn);
        controlPanel.add(rebindPageBtn);

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
            switch (action) {
                case NEXT:
                    infoView.handleStep(true);
                    break;
                case PREVIOUS:
                    infoView.handleStep(false);
                    break;
                case NEXT_PAGE:
                    // Reserved for full section swaps
                    break;
            }
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

    public void refreshButtonLabels() {
        SwingUtilities.invokeLater(() -> {
            rebindNextBtn.setText("Rebind NEXT (" + Bindings.getHumanReadable(Action.NEXT, inputHandler) + ")");
            rebindPrevBtn.setText("Rebind PREVIOUS (" + Bindings.getHumanReadable(Action.PREVIOUS, inputHandler) + ")");
            rebindPageBtn.setText("Rebind PAGE SWAP (" + Bindings.getHumanReadable(Action.NEXT_PAGE, inputHandler) + ")");
        });
    }
}