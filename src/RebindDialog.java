import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import javax.swing.*;
import java.awt.BorderLayout;

public class RebindDialog extends JDialog implements NativeKeyListener {
    private Action action;
    private InputHandler handler;

    public RebindDialog(JFrame parent, Action action, InputHandler handler) {
        super(parent, "Binding: " + action, true);
        this.action = action;
        this.handler = handler;

        setLayout(new BorderLayout());
        add(new JLabel("Press any key to bind to " + action, SwingConstants.CENTER), BorderLayout.CENTER);
        
        setSize(300, 150);
        setLocationRelativeTo(parent);

        // Register this dialog to hear global events while it is open
        GlobalScreen.addNativeKeyListener(this);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int code = e.getKeyCode();
        String keyName = NativeKeyEvent.getKeyText(code);
        
        handler.rebind(action, code); // Saves to file
        
        GlobalScreen.removeNativeKeyListener(this);
        
        SwingUtilities.invokeLater(() -> {
            // Refresh the labels on the parent dashboard
            if (getParent() instanceof Dashboard) {
                ((Dashboard) getParent()).refreshButtonLabels();
            }
            
            JOptionPane.showMessageDialog(this, action + " bound to " + keyName);
            dispose();
        });
    }

    // Required empty methods for the interface
    @Override public void nativeKeyReleased(NativeKeyEvent e) {}
    @Override public void nativeKeyTyped(NativeKeyEvent e) {}
}