import javax.swing.*;
import java.awt.*;

public class InformationView extends JPanel {
    private JLabel displayLabel;
    private JButton gearButton;
    private JButton menuButton;
    
    // Our new Sidebar
    private SidebarMenu sidebarMenu;

    public InformationView(Runnable returnAction) {
        setBackground(new Color(23, 23, 23));
        setLayout(new BorderLayout());

        // --- 1. Top Bar Navigation ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        // Give the top bar a set height so it doesn't feel cramped
        topBar.setPreferredSize(new Dimension(0, 50)); 

        // Setup Buttons (Keep your existing icon/scaling code here...)
        ImageIcon menuIcon = new ImageIcon("data/assets/menu.png");
        ImageIcon gearIcon = new ImageIcon("data/assets/settings-gear.png");
        Image menuImg = menuIcon.getImage().getScaledInstance(43, 30, Image.SCALE_SMOOTH);
        Image gearImg = gearIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        
        menuButton = new JButton(new ImageIcon(menuImg));
        styleButton(menuButton);
        menuButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        topBar.add(menuButton, BorderLayout.WEST);

        gearButton = new JButton(new ImageIcon(gearImg));
        styleButton(gearButton);
        gearButton.addActionListener(e -> returnAction.run());
        gearButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        topBar.add(gearButton, BorderLayout.EAST);

        // --- NEW: Add the label to the MIDDLE of the top bar ---
        displayLabel = new JLabel("The PoE2 Campaign Companion", SwingConstants.CENTER);
        displayLabel.setFont(new Font("Arial", Font.BOLD, 22)); // Smaller to fit the line
        displayLabel.setForeground(new Color(134, 134,134));
        topBar.add(displayLabel, BorderLayout.CENTER);

        // Add the completed topBar to the NORTH
        add(topBar, BorderLayout.NORTH);

        // --- 2. The Sidebar ---
        sidebarMenu = new SidebarMenu(this::updateContent);
        sidebarMenu.setVisible(false);
        add(sidebarMenu, BorderLayout.WEST);

        // --- 3. The Main Content Area (Now empty/transparent) ---
        // This is where your PNG images will eventually be drawn
        JPanel contentArea = new JPanel();
        contentArea.setOpaque(false);
        add(contentArea, BorderLayout.CENTER);

        menuButton.addActionListener(e -> {
            sidebarMenu.setVisible(!sidebarMenu.isVisible());
            revalidate();
            repaint();
        });
    }

    private void styleButton(JButton btn) {
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void updateContent(String text) {
        SwingUtilities.invokeLater(() -> displayLabel.setText(text));
    }

    public void handleStep(boolean forward) {
        if (sidebarMenu != null) {
            sidebarMenu.cycleSelection(forward);
        }
    }
}