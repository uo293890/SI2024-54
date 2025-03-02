package giis.demo.util;

import javax.swing.*;

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

    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}