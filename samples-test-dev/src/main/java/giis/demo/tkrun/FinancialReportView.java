package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        setTitle("Generate Income and Expense Report");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout(20, 20));
        initComponents();
    }

    private void initComponents() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        filterPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 3),
                "Filters", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24)));

        filterPanel.add(createLargeLabel("Start Date:"));
        txtStartDate = new JTextField(15);
        txtStartDate.setFont(new Font("Arial", Font.PLAIN, 20));
        filterPanel.add(txtStartDate);

        filterPanel.add(createLargeLabel("End Date:"));
        txtEndDate = new JTextField(15);
        txtEndDate.setFont(new Font("Arial", Font.PLAIN, 20));
        filterPanel.add(txtEndDate);

        filterPanel.add(createLargeLabel("Status:"));
        cmbStatus = new JComboBox<>(new String[]{"All", "Planned", "Closed"});
        cmbStatus.setFont(new Font("Arial", Font.PLAIN, 20));
        filterPanel.add(cmbStatus);

        btnConsult = new JButton("Consult");
        btnConsult.setFont(new Font("Arial", Font.BOLD, 20));
        filterPanel.add(btnConsult);

        add(filterPanel, BorderLayout.NORTH);

        modelActivities = new DefaultTableModel(
            new Object[]{"Activity Name", "Status", "Start Date", "End Date", "Estimated Income", "Estimated Expenses", "Estimated Balance", "Actual Income", "Actual Expenses", "Actual Balance"}, 0
        );
        tableActivities = new JTable(modelActivities);
        tableActivities.setFont(new Font("Arial", Font.PLAIN, 18));
        tableActivities.setRowHeight(30);
        formatTable();
        JScrollPane scrollPane = new JScrollPane(tableActivities);
        add(scrollPane, BorderLayout.CENTER);

        JPanel totalsContainer = new JPanel(new GridLayout(1, 2, 40, 40));
        totalsContainer.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLUE, 4),
                "Financial Summary", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 30), Color.BLUE));
        totalsContainer.setBackground(new Color(220, 230, 255));

        // Estimated
        JPanel estimatedPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        estimatedPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3),
                "Estimated", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 26), Color.DARK_GRAY));
        estimatedPanel.setBackground(new Color(200, 220, 250));

        lblTotalEstIncome = createStyledLabel();
        lblTotalEstExpenses = createStyledLabel();
        lblEstBalance = createStyledLabel();

        estimatedPanel.add(createSummaryLabel("Estimated Income:"));
        estimatedPanel.add(lblTotalEstIncome);
        estimatedPanel.add(createSummaryLabel("Estimated Expenses:"));
        estimatedPanel.add(lblTotalEstExpenses);
        estimatedPanel.add(createSummaryLabel("Estimated Balance:"));
        estimatedPanel.add(lblEstBalance);

        // Actual
        JPanel actualPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        actualPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3),
                "Actual", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 26), Color.DARK_GRAY));
        actualPanel.setBackground(new Color(200, 220, 250));

        lblTotalIncome = createStyledLabel();
        lblTotalExpenses = createStyledLabel();
        lblActualBalance = createStyledLabel();

        actualPanel.add(createSummaryLabel("Actual Income:"));
        actualPanel.add(lblTotalIncome);
        actualPanel.add(createSummaryLabel("Actual Expenses:"));
        actualPanel.add(lblTotalExpenses);
        actualPanel.add(createSummaryLabel("Actual Balance:"));
        actualPanel.add(lblActualBalance);

        totalsContainer.add(estimatedPanel);
        totalsContainer.add(actualPanel);

        add(totalsContainer, BorderLayout.SOUTH);
    }

    private void formatTable() {
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
                    setFont(new Font("Arial", Font.BOLD, 22));
                } else {
                    setForeground(Color.BLACK);
                }
                super.setValue(value);
            }
        };

        // Estimated Balance
        tableActivities.getColumnModel().getColumn(6).setCellRenderer(balanceRenderer);
        // Actual Balance
        tableActivities.getColumnModel().getColumn(9).setCellRenderer(balanceRenderer);
    }

    private JLabel createStyledLabel() {
        JLabel label = new JLabel("0.0", SwingConstants.RIGHT);
        label.setFont(new Font("Arial", Font.BOLD, 26));
        return label;
    }

    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        return label;
    }

    private JLabel createLargeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        return label;
    }

    public JButton getConsultButton() {
        return btnConsult;
    }

    public String getStartDate() {
        return txtStartDate.getText();
    }

    public String getEndDate() {
        return txtEndDate.getText();
    }

    public String getStatus() {
        return (String) cmbStatus.getSelectedItem();
    }

    public void setStartDate(String date) {
        txtStartDate.setText(date);
    }

    public void setEndDate(String date) {
        txtEndDate.setText(date);
    }

    public void updateActivitiesTable(List<FinancialReportDTO> data) {
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
    }

    public void updateTotals(double estIncome, double estExpenses, double actualIncome, double actualExpenses, double actualBalance) {
        lblTotalEstIncome.setText(String.format("%.2f", estIncome));
        lblTotalEstExpenses.setText(String.format("%.2f", estExpenses));
        lblEstBalance.setText(String.format("%.2f", estIncome - estExpenses));
        lblTotalIncome.setText(String.format("%.2f", actualIncome));
        lblTotalExpenses.setText(String.format("%.2f", actualExpenses));
        lblActualBalance.setText(String.format("%.2f", actualBalance));
    }
}
