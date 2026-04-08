import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SidebarMenu extends JPanel {
    private JPanel menuContainer;
    private Map<String, JPanel> subPanels = new LinkedHashMap<>();
    private JButton currentlySelectedSubItem = null;

    public SidebarMenu(Consumer<String> onGuideSelected) {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30)); // Slightly lighter than your main background

        menuContainer = new JPanel();
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
        menuContainer.setBackground(new Color(30, 30, 30));

        // --- DEFINE YOUR 7 TABS AND SUB-SECTIONS HERE ---
        // LinkedHashMap keeps them in the exact order you write them
        LinkedHashMap<String, String[]> menuStructure = new LinkedHashMap<>();
        menuStructure.put("Act 1", new String[]{"Clearfell", "Grelwood", "Red Vale", "Grim Tangle", "Cemetery of the Eternal", "Tomb of the Consort", "Mausoleum of the Praetor", "Hunting Grounds", "Freythorn", "Ogham Farmlands", "Ogham Village", "The Manor Ramparts", "Ogham Manor"});
        menuStructure.put("Act 2", new String[]{"Vastiri Outskirts", "Mawdun Quarry", "Mawdun Mine", "Traitors Passage", "Halani Gates", "Valley of the Titans", "The Titan Grotto", "Keth", "The Lost City", "Buried Shrines", "Mastodon Badlands", "The Bone Pits", "Deshar", "Path of Mourning", "The Spires of Desahr", "The Dreadnaught", "The Dreadnought Vanguard"});
        menuStructure.put("Act 3", new String[]{"Sandsweapt March", "Jungle Ruins", "The Venom Crypts", "Infested Barrens", "The Azak Bog", "Chimeral Wetlands", "Jiquani's Machinarium", "Jiquani's Sanctum", "The Matlan Waterways", "The Drowned City", "The Molten Vault", "Apex of Filth", "Tempel of kopec", "Utzaal", "Aggorat", "The Black Chambers"});
        menuStructure.put("Act 4", new String[]{"Isle of Kin", "Volcanic Warrens", "Kedge Bay", "Journey's End", "Shrike island", "Whakapanu Island", "Singing Caverns", "Abandoned Prison", "Solitary Confinement", "Eye of Hinakora", "Trial of the Ancestors", "Halls of the Dead", "Arastas", "The Excavation", "Ngakanu", "Heart of the Tribe", "Plunderes Point"});
        menuStructure.put("Interlude 2", new String[]{"The Khari Crossing", "Pools of Khatal", "Sel Khari Sanctuary", "The Galani Gates", "Qimah", "Qimah Reservoir"});
        menuStructure.put("Interlude 3", new String[]{"Ashen Forrest", "Kriah Village", "Glacial Tarn", "Howing Caves", "Kriar Peaks", "Etched Ravine", "The Cuachic Vault"});
        menuStructure.put("Inderlude 1", new String[]{"Scorched Farmlands", "The Blackwood", "Stones of Serle", "Holten", "Wolvenhold", "Holten Estate"});

        // Build the UI based on the structure above
        for (Map.Entry<String, String[]> entry : menuStructure.entrySet()) {
            addCategory(entry.getKey(), entry.getValue(), onGuideSelected);
        }

        // Put it in a scroll pane in case your lists get very long
        JScrollPane scrollPane = new JScrollPane(menuContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Forces the sidebar to take up exactly 1/4 of the parent window
    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();
        int width = (parent != null && parent.getWidth() > 0) ? parent.getWidth() / 4 : 300;
        return new Dimension(width, super.getPreferredSize().height);
    }

    private void addCategory(String title, String[] subItems, Consumer<String> onGuideSelected) {
        // --- 1. The Header Button ---
        JButton headerBtn = new JButton(title);
        headerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        headerBtn.setBackground(new Color(45, 45, 45));
        headerBtn.setForeground(Color.WHITE);
        headerBtn.setFocusPainted(false);
        headerBtn.setFont(new Font("Arial", Font.BOLD, 16));
        headerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- 2. The Sub-Items Panel ---
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
        subPanel.setBackground(new Color(25, 25, 25));
        subPanel.setVisible(false); // Hidden by default

        for (String item : subItems) {
            JButton subBtn = new JButton("  " + item);
            subBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            subBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            subBtn.setBackground(new Color(25, 25, 25));
            subBtn.setForeground(new Color(200, 200, 200)); // Dimmer text for sub-items
            subBtn.setFocusPainted(false);
            subBtn.setBorderPainted(false);
            subBtn.setHorizontalAlignment(SwingConstants.LEFT);
            subBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            subBtn.addActionListener(e -> {
                // Clear previous highlight
                if (currentlySelectedSubItem != null) {
                    currentlySelectedSubItem.setBackground(new Color(25, 25, 25));
                    currentlySelectedSubItem.setForeground(new Color(200, 200, 200));
                }
                
                // Highlight the new selection
                subBtn.setBackground(new Color(70, 130, 180)); // Steel Blue highlight
                subBtn.setForeground(Color.WHITE);
                currentlySelectedSubItem = subBtn;

                // Send the text to your main display
                onGuideSelected.accept(item); 
            });
            subPanel.add(subBtn);
        }

        // --- 3. Accordion Logic ---
        headerBtn.addActionListener(e -> {
            boolean isCurrentlyVisible = subPanel.isVisible();
            
            // Close all open panels
            for (JPanel p : subPanels.values()) {
                p.setVisible(false);
            }
            
            // Toggle the clicked one
            subPanel.setVisible(!isCurrentlyVisible);
            
            menuContainer.revalidate();
            menuContainer.repaint();
        });

        subPanels.put(title, subPanel);
        menuContainer.add(headerBtn);
        menuContainer.add(subPanel);
    }
}