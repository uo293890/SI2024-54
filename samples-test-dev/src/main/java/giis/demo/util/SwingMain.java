package giis.demo.util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Main");
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton btnInitDB = new JButton("Inicializar BD");
            btnInitDB.addActionListener(e -> {
                Database db = new Database();
                db.createDatabase(false);
                JOptionPane.showMessageDialog(frame, "Base de datos creada!");
            });

            JButton btnLoadData = new JButton("Cargar Datos");
            btnLoadData.addActionListener(e -> {
                Database db = new Database();
                db.loadDatabase();
                JOptionPane.showMessageDialog(frame, "Datos cargados!");
            });

            JPanel panel = new JPanel();
            panel.add(btnInitDB);
            panel.add(btnLoadData);
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}