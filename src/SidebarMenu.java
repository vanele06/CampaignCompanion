import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;

public class SidebarMenu extends JPanel {
    private JPanel menuContainer;
    private Map<String, JPanel> subPanels = new LinkedHashMap<>();
    private JButton currentlySelectedSubItem = null;
    private List<JButton> allSubButtons = new ArrayList<>();
    
    // 1. ADDED: Class-level field to store the consumer
    private Consumer<String> onGuideSelected;

    public SidebarMenu(Consumer<String> onGuideSelected) {
        // 2. FIXED: Assign the incoming parameter to the class field
        this.onGuideSelected = onGuideSelected;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));

        menuContainer = new JPanel();
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
        menuContainer.setBackground(new Color(30, 30, 30));

        // --- DEFINE YOUR 7 TABS AND SUB-SECTIONS HERE ---
        LinkedHashMap<String, String[]> menuStructure = new LinkedHashMap<>();
        menuStructure.put("Act 1", new String[]{"Clearfell", "Grelwood", "Red Vale", "Grim Tangle", "Cemetery of the Eternal", "Tomb of the Consort", "Mausoleum of the Praetor", "Hunting Grounds", "Freythorn", "Ogham Farmlands", "Ogham Village", "The Manor Ramparts", "Ogham Manor"});
        menuStructure.put("Act 2", new String[]{"Vastiri Outskirts", "Mawdun Quarry", "Mawdun Mine", "Traitors Passage", "Halani Gates", "Valley of the Titans", "The Titan Grotto", "Keth", "The Lost City", "Buried Shrines", "Mastodon Badlands", "The Bone Pits", "Deshar", "Path of Mourning", "The Spires of Desahr", "The Dreadnaught", "The Dreadnought Vanguard"});
        menuStructure.put("Act 3", new String[]{"Sandsweapt March", "Jungle Ruins", "The Venom Crypts", "Infested Barrens", "The Azak Bog", "Chimeral Wetlands", "Jiquani's Machinarium", "Jiquani's Sanctum", "The Matlan Waterways", "The Drowned City", "The Molten Vault", "Apex of Filth", "Tempel of kopec", "Utzaal", "Aggorat", "The Black Chambers"});
        menuStructure.put("Act 4", new String[]{"Isle of Kin", "Volcanic Warrens", "Kedge Bay", "Journey's End", "Shrike island", "Whakapanu Island", "Singing Caverns", "Abandoned Prison", "Solitary Confinement", "Eye of Hinakora", "Trial of the Ancestors", "Halls of the Dead", "Arastas", "The Excavation", "Ngakanu", "Heart of the Tribe", "Plunderes Point"});
        menuStructure.put("Interlude 2", new String[]{"The Khari Crossing", "Pools of Khatal", "Sel Khari Sanctuary", "The Galani Gates", "Qimah", "Qimah Reservoir"});
        menuStructure.put("Interlude 3", new String[]{"Ashen Forrest", "Kriah Village", "Glacial Tarn", "Howing Caves", "Kriar Peaks", "Etched Ravine", "The Cuachic Vault"});
        menuStructure.put("Inderlude 1", new String[]{"Scorched Farmlands", "The Blackwood", "Stones of Serle", "Holten", "Wolvenhold", "Holten Estate"});

        // 3. FIXED: Removed redundant parameter passing
        for (Map.Entry<String, String[]> entry : menuStructure.entrySet()) {
            addCategory(entry.getKey(), entry.getValue());
        }

        JScrollPane scrollPane = new JScrollPane(menuContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();
        int width = (parent != null && parent.getWidth() > 0) ? parent.getWidth() / 4 : 300;
        return new Dimension(width, super.getPreferredSize().height);
    }

    // 4. FIXED: Removed Consumer parameter, uses class field instead
    private void addCategory(String title, String[] subItems) {
        JButton headerBtn = new JButton(title);
        headerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        headerBtn.setBackground(new Color(45, 45, 45));
        headerBtn.setForeground(Color.WHITE);
        headerBtn.setFocusPainted(false);
        headerBtn.setFont(new Font("Arial", Font.BOLD, 16));
        headerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
        subPanel.setBackground(new Color(25, 25, 25));
        subPanel.setVisible(false);

        for (String item : subItems) {
            JButton subBtn = new JButton("  " + item);
            subBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            subBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            subBtn.setBackground(new Color(25, 25, 25));
            subBtn.setForeground(new Color(200, 200, 200));
            subBtn.setFocusPainted(false);
            subBtn.setBorderPainted(false);
            subBtn.setHorizontalAlignment(SwingConstants.LEFT);
            subBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            subBtn.addActionListener(e -> {
                if (currentlySelectedSubItem != null) {
                    currentlySelectedSubItem.setBackground(new Color(25, 25, 25));
                    currentlySelectedSubItem.setForeground(new Color(200, 200, 200));
                }
                
                subBtn.setBackground(new Color(70, 130, 180));
                subBtn.setForeground(Color.WHITE);
                currentlySelectedSubItem = subBtn;

                // FIXED: Now uses the stored class-level consumer
                if (onGuideSelected != null) {
                    onGuideSelected.accept(item); 
                }
            });
            allSubButtons.add(subBtn);
            subPanel.add(subBtn);
        }

        headerBtn.addActionListener(e -> {
            boolean isCurrentlyVisible = subPanel.isVisible();
            for (JPanel p : subPanels.values()) {
                p.setVisible(false);
            }
            subPanel.setVisible(!isCurrentlyVisible);
            menuContainer.revalidate();
            menuContainer.repaint();
        });

        subPanels.put(title, subPanel);
        menuContainer.add(headerBtn);
        menuContainer.add(subPanel);
    }
    
    public void cycleSelection(boolean forward) {
        if (allSubButtons.isEmpty()) return;

        int currentIndex = allSubButtons.indexOf(currentlySelectedSubItem);
        int nextIndex;

        if (forward && currentIndex == allSubButtons.size() - 1) {
            showMainScreen();
            return;
        } else if (!forward && currentIndex == 0) {
            showMainScreen();
            return;
        } else if (currentlySelectedSubItem == null) {
            nextIndex = forward ? 0 : allSubButtons.size() - 1;
        } else {
            nextIndex = forward ? (currentIndex + 1) : (currentIndex - 1);
        }

        JButton target = allSubButtons.get(nextIndex);
        currentlySelectedSubItem = target; 
        
        JPanel targetParentPanel = (JPanel) target.getParent();

        if (!targetParentPanel.isVisible()) {
            for (JPanel p : subPanels.values()) {
                p.setVisible(false);
            }
            targetParentPanel.setVisible(true);
            menuContainer.revalidate();
            menuContainer.repaint();
        }

        target.doClick(); 
        target.scrollRectToVisible(new Rectangle(0, 0, target.getWidth(), target.getHeight()));
    }

    private void showMainScreen() {
        currentlySelectedSubItem = null; 
        
        // 5. FIXED: Correct name to match the class field
        if (onGuideSelected != null) {
            onGuideSelected.accept("Main Screen");
        }
        
        for (JPanel p : subPanels.values()) {
            p.setVisible(false);
        }
        menuContainer.revalidate();
        menuContainer.repaint();
    }
}