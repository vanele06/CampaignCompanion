import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class InputHandler {
    // Maps KeyCode (Integer) -> Action (Enum)
    private Map<Integer, Action> bindings = new HashMap<>();

    public InputHandler() {
        // Set default bindings
        bindings.put(KeyEvent.VK_K, Action.NEXT);
        bindings.put(KeyEvent.VK_L, Action.NEXT_PAGE);
        bindings.put(KeyEvent.VK_O, Action.PREVIOUS);
    }

    public void rebind(Action action, int newKeyCode) {
        bindings.values().removeIf(a -> a == action);
        bindings.put(newKeyCode, action);
    }

    public Action getAction(int keyCode) {
        return bindings.get(keyCode);
    }

    public Map<Integer, Action> getBindings() {
        return bindings;
    }
}