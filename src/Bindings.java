import java.util.Map;
import java.awt.event.KeyEvent;

public class Bindings {
    // This class purely holds the data for the overview
    public static String getHumanReadable(Action action, InputHandler handler) {
        for (Map.Entry<Integer, Action> entry : handler.getBindings().entrySet()) {
            if (entry.getValue() == action) {
                return KeyEvent.getKeyText(entry.getKey());
            }
        }
        return "Not Bound";
    }
}