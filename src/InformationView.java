import javax.swing.*;
import java.awt.*;

public class InformationView extends JPanel {
    private JLabel displayLabel;
    private JButton gearButton;
    private JButton menuButton;

    public InformationView(Runnable returnAction) {
        // Deep dark theme - matches your background choice
        setBackground(new Color(23, 23, 23));
        setLayout(new BorderLayout());

        // --- 1. Top Bar Navigation ---
        // We use a BorderLayout here to put the menu on the WEST and gear on the EAST
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        // Fixed the path typo: "daza" -> "data"
        ImageIcon menuIcon = new ImageIcon("data/assets/menu.png");
        ImageIcon gearIcon = new ImageIcon("data/assets/settings-gear.png");

        // Scaling using your specific dimensions (43x30 and 30x30)
        Image menuImg = menuIcon.getImage().getScaledInstance(43, 30, Image.SCALE_SMOOTH);
        Image gearImg = gearIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        
        // Setup Menu Button (Aligned to the Left)
        menuButton = new JButton(new ImageIcon(menuImg));
        styleButton(menuButton);
        // Add a small margin to the left so it's not touching the edge
        menuButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        topBar.add(menuButton, BorderLayout.WEST);

        // Setup Gear Button (Aligned to the Right)
        gearButton = new JButton(new ImageIcon(gearImg));
        styleButton(gearButton);
        gearButton.addActionListener(e -> returnAction.run());
        // Add a small margin to the right
        gearButton.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 10));
        topBar.add(gearButton, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // --- 2. The Center Slate ---
        displayLabel = new JLabel("Your Guide Content Here", SwingConstants.CENTER);
        displayLabel.setFont(new Font("Arial", Font.BOLD, 48));
        
        // Set text color to white for visibility on the dark background
        displayLabel.setForeground(Color.WHITE); 
        
        add(displayLabel, BorderLayout.CENTER);
    }

    // Helper method to keep the code clean and consistent
    private void styleButton(JButton btn) {
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void updateContent(String text) {
        // SwingUtilities ensures the UI updates correctly from background threads
        SwingUtilities.invokeLater(() -> displayLabel.setText(text));
    }
}