package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import giis.demo.jdbc.activities.ReportFilter;
import giis.demo.jdbc.activities.Reportservice;
import giis.demo.jdbc.activities.DatabaseConnection;
import giis.demo.util.Util;

public class ReportScreen extends JDialog {

    private final Reportservice reportService;

    public ReportScreen() {
        super((Frame) null, "Consultar Informe de Ingresos y Gastos", true);
        this.reportService = new Reportservice();

        setSize(600, 400);
        setLocationRelativeTo(null);

        JLabel startDateLabel = new JLabel("Fecha de Inicio:");
        JTextField startDateField = new JTextField(10);
        JLabel endDateLabel = new JLabel("Fecha de Fin:");
        JTextField endDateField = new JTextField(10);
        JLabel statusLabel = new JLabel("Estado:");
        JTextField statusField = new JTextField(10);

        JButton generateReportButton = new JButton("Generar Informe");

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date startDate = Util.isoStringToDate(startDateField.getText());
                Date endDate = Util.isoStringToDate(endDateField.getText());
                String status = statusField.getText();

                ReportFilter filter = new ReportFilter(startDate, endDate, status);
                List<Activity> activities = getActivities(startDate, endDate, status);
                ActivityReport report = reportService.generateReport(activities, filter);
                JOptionPane.showMessageDialog(ReportScreen.this,
                        report,
                        "Informe de Ingresos y Gastos",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.add(startDateLabel);
        panel.add(startDateField);
        panel.add(endDateLabel);
        panel.add(endDateField);
        panel.add(statusLabel);
        panel.add(statusField);
        panel.add(generateReportButton);

        add(panel);
    }

    private List<Activity> getActivities(Date startDate, Date endDate, String status) {
        String query = "SELECT * FROM Activity WHERE start_date >= ? AND end_date <= ? AND status = ?";
        return DatabaseConnection.executeQuery(query, startDate, endDate, status)
                .stream()
                .map(row -> new Activity(
                        (int) row.get("id"),
                        (String) row.get("name"),
                        (Date) row.get("start_date"),
                        (Date) row.get("end_date"),
                        (String) row.get("status")
                ))
                .collect(Collectors.toList());
    }
}