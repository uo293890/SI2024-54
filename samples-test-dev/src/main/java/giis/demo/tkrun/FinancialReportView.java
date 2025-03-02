package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;

public class FinancialReportView {
    private JFrame frame;
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    private JComboBox<String> statusComboBox;
    private JButton btnGenerateReport;
    private JTextArea txtReport;

    public FinancialReportView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Consult Financial Report");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(0, 2, 10, 10));

        // Start Date
        frame.add(new JLabel("Start Date:"));
        txtStartDate = new JTextField();
        frame.add(txtStartDate);

        // End Date
        frame.add(new JLabel("End Date:"));
        txtEndDate = new JTextField();
        frame.add(txtEndDate);

        // Status Dropdown
        frame.add(new JLabel("Activity Status:"));
        statusComboBox = new JComboBox<>(new String[]{"Open", "Closed"});
        frame.add(statusComboBox);

        // Generate Report Button
        btnGenerateReport = new JButton("Generate Report");
        frame.add(btnGenerateReport);

        // Report Area
        txtReport = new JTextArea();
        txtReport.setEditable(false);
        frame.add(new JScrollPane(txtReport));

        frame.setVisible(true);
    }

    public JButton getBtnGenerateReport() {
        return btnGenerateReport;
    }

    public String getStartDate() {
        return txtStartDate.getText();
    }

    public String getEndDate() {
        return txtEndDate.getText();
    }

    public String getStatus() {
        return (String) statusComboBox.getSelectedItem();
    }

    public void displayReport(FinancialReportDTO report) {
        StringBuilder reportText = new StringBuilder();
        reportText.append("Date       | Activity     | Status  | Income  | Expenses\n");
        for (ActivityDTO activity : report.getActivities()) {
            reportText.append(activity.getDate()).append(" | ")
                      .append(activity.getName()).append(" | ")
                      .append(activity.getStatus()).append(" | ")
                      .append(activity.getIncome()).append(" | ")
                      .append(activity.getExpenses()).append("\n");
        }
        reportText.append("Totals:                  | ")
                  .append(report.getTotalIncome()).append(" | ")
                  .append(report.getTotalExpenses());
        txtReport.setText(reportText.toString());
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    public JFrame getFrame() {
        return frame;
    }
}