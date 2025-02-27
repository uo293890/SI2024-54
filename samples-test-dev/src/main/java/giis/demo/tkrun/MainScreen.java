package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.table.TableModel;

import java.awt.*;
import java.util.List;
import java.util.Map;

import giis.demo.jdbc.activities.DatabaseConnection;
import giis.demo.util.SwingUtil;

public class MainScreen extends JFrame {

    private JTable table;

    public MainScreen() {
        super("Sports Competition Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton billingButton = new JButton("Generar y Enviar Facturas");
        billingButton.addActionListener(e -> {
            BillingScreen billingScreen = new BillingScreen();
            billingScreen.setVisible(true);
        });

        JButton reportButton = new JButton("Consultar Informe de Ingresos y Gastos");
        reportButton.addActionListener(e -> {
            ReportScreen reportScreen = new ReportScreen();
            reportScreen.setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(billingButton);
        buttonPanel.add(reportButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        String query = "SELECT * FROM Activity";
        List<Map<String, Object>> data = DatabaseConnection.executeQuery(query);
        String[] columnNames = data.isEmpty() ? new String[0] : data.get(0).keySet().toArray(new String[0]);
        TableModel model = SwingUtil.getTableModelFromPojos(data, columnNames);
        table.setModel(model);
        SwingUtil.autoAdjustColumns(table);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainScreen mainScreen = new MainScreen();
            mainScreen.setVisible(true);
        });
    }
}