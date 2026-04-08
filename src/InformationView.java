import javax.swing.*;
import java.awt.*;

public class InformationView extends JPanel {
    private JLabel displayLabel;
    private JButton gearButton;
    private JButton menuButton;
    
    // Our new Sidebar
    private SidebarMenu sidebarMenu;

    public InformationView(Runnable returnAction) {
        // Deep dark theme - matches your background choice
        setBackground(new Color(23, 23, 23));
        setLayout(new BorderLayout());

        // --- 1. Top Bar Navigation ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        ImageIcon menuIcon = new ImageIcon("data/assets/menu.png");
        ImageIcon gearIcon = new ImageIcon("data/assets/settings-gear.png");

        Image menuImg = menuIcon.getImage().getScaledInstance(43, 30, Image.SCALE_SMOOTH);
        Image gearImg = gearIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        
        menuButton = new JButton(new ImageIcon(menuImg));
        styleButton(menuButton);
        menuButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        topBar.add(menuButton, BorderLayout.WEST);

        gearButton = new JButton(new ImageIcon(gearImg));
        styleButton(gearButton);
        gearButton.addActionListener(e -> returnAction.run());
        gearButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 10));
        topBar.add(gearButton, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // --- 2. The Center Slate ---
        displayLabel = new JLabel("Your Guide Content Here", SwingConstants.CENTER);
        displayLabel.setFont(new Font("Arial", Font.BOLD, 48));
        displayLabel.setForeground(Color.WHITE); 
        add(displayLabel, BorderLayout.CENTER);

        // --- 3. The New Sidebar Menu ---
        // We pass 'this::updateContent' so clicking a guide updates the main slate
        sidebarMenu = new SidebarMenu(this::updateContent);
        sidebarMenu.setVisible(false); // Hidden until menu button is clicked
        add(sidebarMenu, BorderLayout.WEST);

        // --- 4. Wire up Menu Button Toggle ---
        menuButton.addActionListener(e -> {
            sidebarMenu.setVisible(!sidebarMenu.isVisible());
            
            // Revalidate forces Swing to recalculate the layout sizes
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
}