import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RebindDialog extends JDialog {
    public RebindDialog(JFrame parent, Action action, InputHandler handler) {
        super(parent, "Binding: " + action, true); // true = modal
        
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Press any key to bind to " + action, SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
        
        setSize(250, 150);
        setLocationRelativeTo(parent);

        // This listener captures the key and saves it
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                String keyName = KeyEvent.getKeyText(code);
                
                handler.rebind(action, code); // Update your logic
                
                JOptionPane.showMessageDialog(null, action + " bound to " + keyName);
                dispose(); // Close the popup
            }
        });
    }
}