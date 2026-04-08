import javax.swing.*;
import java.awt.*;

public class InformationView extends JPanel {
    private JLabel displayLabel;
    private JButton gearButton;
    private JButton menuButton;
    private JTextField copyField;
    private JButton copyButton;
    private JLabel hyperlink;
    private JPanel footerContainer; // New container for bottom-left items
    
    private SidebarMenu sidebarMenu;

    public InformationView(Runnable returnAction) {
        setBackground(new Color(23, 23, 23));
        setLayout(new BorderLayout());

        // --- 1. Top Bar Navigation ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setPreferredSize(new Dimension(0, 50)); 

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

        displayLabel = new JLabel("The PoE2 Campaign Companion", SwingConstants.CENTER);
        displayLabel.setFont(new Font("Arial", Font.BOLD, 22)); 
        displayLabel.setForeground(new Color(134, 134, 134));
        topBar.add(displayLabel, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);

        // --- 2. The Sidebar ---
        sidebarMenu = new SidebarMenu(this::updateContent);
        sidebarMenu.setVisible(false);
        add(sidebarMenu, BorderLayout.WEST);

        // --- 3. The Footer Area (Bottom Left) ---
        footerContainer = new JPanel();
        footerContainer.setLayout(new BoxLayout(footerContainer, BoxLayout.Y_AXIS));
        footerContainer.setOpaque(false);
        // Add padding (bottom and left) so it doesn't touch the monitor edges
        footerContainer.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 0));

        // 1. Hyperlink
        hyperlink = new JLabel("<html><a style='color: #6495ED;' href=''>Filter</a></html>");
        hyperlink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hyperlink.setAlignmentX(Component.LEFT_ALIGNMENT); // Align Left
        hyperlink.setFont(new Font("Arial", Font.PLAIN, 18));
        hyperlink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new java.net.URI("https://www.pathofexile.com/account/view-profile/GuyThatDies-7619/item-filters"));
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        // 2. Copy Area
        JPanel copyRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        copyRow.setOpaque(false);
        copyRow.setAlignmentX(Component.LEFT_ALIGNMENT); // Align Left

        copyField = new JTextField("\"!(uiv)\" \"cross|mov|[egdl] da.* to a\"", 20);
        copyField.setEditable(false);
        copyField.setBackground(new Color(43, 43, 43)); // Darker field
        copyField.setForeground(Color.WHITE);
        copyField.setCaretColor(Color.WHITE);

        copyButton = new JButton("Copy Regex");
        copyButton.addActionListener(e -> {
            java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(copyField.getText());
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            copyButton.setText("Copied!");
            new Timer(2000, evt -> copyButton.setText("Copy Regex")).start();
        });

        copyRow.add(copyField);
        copyRow.add(Box.createRigidArea(new Dimension(10, 0))); // Gap between field and button
        copyRow.add(copyButton);

        // Stack them in the footer
        footerContainer.add(hyperlink);
        footerContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Vertical gap
        footerContainer.add(copyRow);

        // Place the footer in a wrapper to force it to the SOUTH-WEST
        JPanel footerWrapper = new JPanel(new BorderLayout());
        footerWrapper.setOpaque(false);
        footerWrapper.add(footerContainer, BorderLayout.WEST);
        
        add(footerWrapper, BorderLayout.SOUTH);

        // --- Menu Logic ---
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
        SwingUtilities.invokeLater(() -> {
            displayLabel.setText(text);
            
            // Hide footer if we aren't on the Main Screen
            boolean isMainScreen = text.equals("Main Screen");
            footerContainer.setVisible(isMainScreen);
            
            revalidate();
            repaint();
        });
    }

    public void handleStep(boolean forward) {
        if (sidebarMenu != null) {
            // If the sidebar is currently at -1 (Main Screen)
            // 'forward' (Next) should go to index 0
            // 'back' (Previous) should go to the last index of the menu
            sidebarMenu.cycleSelection(forward);
        }
    }
}