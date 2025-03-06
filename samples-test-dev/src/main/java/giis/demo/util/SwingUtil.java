package giis.demo.util;

import javax.swing.JOptionPane;

public class SwingUtil {
    public static void exceptionWrapper(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}