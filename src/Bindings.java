import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class Bindings {
    public static String getHumanReadable(Action action, InputHandler handler) {
        int code = handler.getBinding(action);
        if (code != -1) {
            return NativeKeyEvent.getKeyText(code);
        }
        return "Not Bound";
    }
}