package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FinancialReportView extends JFrame {
    private JTable tableActivities;
    private DefaultTableModel modelActivities;
    private JLabel lblTotalEstIncome, lblTotalEstExpenses, lblEstBalance;
    private JLabel lblTotalIncome, lblTotalExpenses, lblActualBalance;
    private JButton btnConsult;
    private JTextField txtStartDate, txtEndDate;
    private JComboBox<String> cmbStatus;

    public FinancialReportView() {
        try {
            setTitle("Financial Report - Income & Expenses");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(1300, 800);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(20, 20));
            initComponents();
        } catch (Exception e) {
            showError("Failed to initialize the view: " + e.getMessage());
        }
    }

    private void initComponents() {
        try {
            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            filterPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 3),
                    "Filters", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24)));

            filterPanel.add(createLargeLabel("Start Date:"));
            txtStartDate = createTextField();
            filterPanel.add(txtStartDate);

            filterPanel.add(createLargeLabel("End Date:"));
            txtEndDate = createTextField();
            filterPanel.add(txtEndDate);

            filterPanel.add(createLargeLabel("Status:"));
            cmbStatus = new JComboBox<>(new String[]{"All", "Planned", "Closed"});
            cmbStatus.setFont(new Font("Arial", Font.PLAIN, 18));
            filterPanel.add(cmbStatus);

            btnConsult = new JButton("Consult");
            btnConsult.setFont(new Font("Arial", Font.BOLD, 18));
            btnConsult.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        fireConsultAction();
                    } catch (Exception ex) {
                        showError("Error during consultation: " + ex.getMessage());
                    }
                }
            });
            filterPanel.add(btnConsult);

            add(filterPanel, BorderLayout.NORTH);

            modelActivities = new DefaultTableModel(
                new Object[]{"Activity Name", "Status", "Start Date", "End Date", "Estimated Income",
                             "Estimated Expenses", "Estimated Balance", "Actual Income", "Actual Expenses", "Actual Balance"}, 0);
            tableActivities = new JTable(modelActivities);
            tableActivities.setFont(new Font("Arial", Font.PLAIN, 16));
            tableActivities.setRowHeight(28);
            formatTable();
            JScrollPane scrollPane = new JScrollPane(tableActivities);
            scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2),
                    "Activities", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 20)));
            add(scrollPane, BorderLayout.CENTER);

            JPanel totalsPanel = new JPanel(new GridLayout(1, 2, 30, 30));
            totalsPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLUE, 3),
                    "Financial Summary", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24), Color.BLUE));
            totalsPanel.setBackground(new Color(245, 250, 255));

            totalsPanel.add(createSummarySection("Estimated",
                    lblTotalEstIncome = createStyledLabel(),
                    lblTotalEstExpenses = createStyledLabel(),
                    lblEstBalance = createStyledLabel()));

            totalsPanel.add(createSummarySection("Actual",
                    lblTotalIncome = createStyledLabel(),
                    lblTotalExpenses = createStyledLabel(),
                    lblActualBalance = createStyledLabel()));

            add(totalsPanel, BorderLayout.SOUTH);
        } catch (Exception e) {
            showError("Error initializing components: " + e.getMessage());
        }
    }

    private JPanel createSummarySection(String title, JLabel income, JLabel expenses, JLabel balance) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 10));
        try {
            panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                    title, TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 22)));
            panel.setBackground(new Color(230, 240, 255));

            panel.add(createSummaryLabel(title + " Income:"));
            panel.add(income);
            panel.add(createSummaryLabel(title + " Expenses:"));
            panel.add(expenses);
            panel.add(createSummaryLabel(title + " Balance:"));
            panel.add(balance);
        } catch (Exception e) {
            showError("Error creating summary section: " + e.getMessage());
        }
        return panel;
    }

    private void formatTable() {
        try {
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            for (int i = 4; i <= 9; i++) {
                tableActivities.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            }

            DefaultTableCellRenderer balanceRenderer = new DefaultTableCellRenderer() {
                @Override
                public void setValue(Object value) {
                    if (value instanceof Number) {
                        double val = ((Number) value).doubleValue();
                        setForeground(val < 0 ? Color.RED : Color.BLACK);
                        setFont(new Font("Arial", Font.BOLD, 18));
                    }
                    super.setValue(value);
                }
            };
            tableActivities.getColumnModel().getColumn(6).setCellRenderer(balanceRenderer);
            tableActivities.getColumnModel().getColumn(9).setCellRenderer(balanceRenderer);
        } catch (Exception e) {
            showError("Error formatting table: " + e.getMessage());
        }
    }

    private JLabel createStyledLabel() {
        JLabel label = new JLabel("0.00", SwingConstants.RIGHT);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        return label;
    }

    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        return label;
    }

    private JLabel createLargeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(12);
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        return field;
    }

    public JButton getConsultButton() { return btnConsult; }
    public String getStartDate() { return txtStartDate.getText(); }
    public String getEndDate() { return txtEndDate.getText(); }
    public String getStatus() { return (String) cmbStatus.getSelectedItem(); }
    public void setStartDate(String date) { txtStartDate.setText(date); }
    public void setEndDate(String date) { txtEndDate.setText(date); }

    public void updateActivitiesTable(List<FinancialReportDTO> data) {
        try {
            modelActivities.setRowCount(0);
            for (FinancialReportDTO dto : data) {
                modelActivities.addRow(new Object[]{
                    dto.getActivityName(),
                    dto.getStatus(),
                    dto.getStartDate(),
                    dto.getEndDate(),
                    dto.getEstimatedIncome(),
                    dto.getEstimatedExpenses(),
                    dto.getEstimatedBalance(),
                    dto.getActualIncome(),
                    dto.getActualExpenses(),
                    dto.getActualBalance()
                });
            }
        } catch (Exception e) {
            showError("Error updating activities table: " + e.getMessage());
        }
    }

    public void updateTotals(double estIncome, double estExpenses, double actualIncome, double actualExpenses, double actualBalance) {
        try {
            lblTotalEstIncome.setText(String.format("%.2f", estIncome));
            lblTotalEstExpenses.setText(String.format("%.2f", estExpenses));
            lblEstBalance.setText(String.format("%.2f", estIncome - estExpenses));
            lblTotalIncome.setText(String.format("%.2f", actualIncome));
            lblTotalExpenses.setText(String.format("%.2f", actualExpenses));
            lblActualBalance.setText(String.format("%.2f", actualBalance));
        } catch (Exception e) {
            showError("Error updating totals: " + e.getMessage());
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Placeholder method for event delegation
    private void fireConsultAction() {
        // This method can be overridden or delegated externally
    }
}