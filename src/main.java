import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Dashboard dash = new Dashboard();
            
            // 1. Always start on the primary monitor for reliability
            dash.setLocationRelativeTo(null); 
            
            // 2. Make it visible so they can see the "Switch Monitor" button
            dash.setVisible(true);
        });
    }
}