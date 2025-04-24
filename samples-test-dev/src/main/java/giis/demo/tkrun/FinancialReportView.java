package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent; // Keep if ActionEvent is used (though replaced by lambda)
import java.awt.event.ActionListener; // Keep if ActionListener is used (though replaced by lambda)
import java.util.List;
import java.util.Objects;


public class FinancialReportView extends JFrame {
    private JTable tableActivities;
    private DefaultTableModel modelActivities;
    // Labels for displaying totals (these will show combined values)
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
            setMinimumSize(new Dimension(1000, 600)); // Ensure minimum size is reasonable
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(20, 20));
            initComponents();
        } catch (Exception e) {
            showError("Failed to initialize the view: " + e.getMessage());
        }
    }

    private void initComponents() {
        try {
            // Filter Panel
            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
            filterPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 3),
                    "Filters", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24)));

            filterPanel.add(createLargeLabel("Start Date (yyyy-MM-dd):"));
            txtStartDate = createTextField();
            filterPanel.add(txtStartDate);

            filterPanel.add(createLargeLabel("End Date (yyyy-MM-dd):"));
            txtEndDate = createTextField();
            filterPanel.add(txtEndDate);

            filterPanel.add(createLargeLabel("Status:"));
            cmbStatus = new JComboBox<>(new String[]{"All", "Planned", "Open", "Closed"});
            cmbStatus.setFont(new Font("Arial", Font.PLAIN, 18));
            filterPanel.add(cmbStatus);

            btnConsult = new JButton("Consult Report");
            btnConsult.setFont(new Font("Arial", Font.BOLD, 18));
            // Action listener is added by the Controller
            filterPanel.add(btnConsult);

            add(filterPanel, BorderLayout.NORTH);

            // Table Panel
            // Table columns match DTO and SQL output order (Estimated/Actual include Other Income/Expenses)
            modelActivities = new DefaultTableModel(
                new Object[]{"Activity Name", "Status", "Start Date", "End Date",
                             "Estimated Income", "Estimated Expenses", "Estimated Balance",
                             "Actual Income", "Actual Expenses", "Actual Balance"}, 0);
            tableActivities = new JTable(modelActivities);
            tableActivities.setFont(new Font("Arial", Font.PLAIN, 16));
            tableActivities.setRowHeight(28);
            tableActivities.setDefaultEditor(Object.class, null); // Make table non-editable
            tableActivities.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
            formatTable(); // Apply custom formatting
            JScrollPane scrollPane = new JScrollPane(tableActivities);
            scrollPane.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2),
                    "Activities Report", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 20)));
            add(scrollPane, BorderLayout.CENTER);

            // Totals Panel
            JPanel totalsPanel = new JPanel(new GridLayout(1, 2, 30, 30));
            totalsPanel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.BLUE, 3),
                    "Financial Summary", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24), Color.BLUE));
            totalsPanel.setBackground(new Color(245, 250, 255));

            // Estimated Summary Section (includes Estimated Sponsorship + Other Income)
            totalsPanel.add(createSummarySection("Estimated",
                    lblTotalEstIncome = createStyledLabel(),
                    lblTotalEstExpenses = createStyledLabel(), // Includes Estimated Expenses
                    lblEstBalance = createStyledLabel()));

            // Actual Summary Section (includes Actual Sponsorship + Other Income)
            totalsPanel.add(createSummarySection("Actual",
                    lblTotalIncome = createStyledLabel(),
                    lblTotalExpenses = createStyledLabel(), // Includes Actual Expenses
                    lblActualBalance = createStyledLabel()));

            add(totalsPanel, BorderLayout.SOUTH);

        } catch (Exception e) {
            showError("Error initializing components: " + e.getMessage());
        }
    }

    private JPanel createSummarySection(String title, JLabel incomeLabel, JLabel expensesLabel, JLabel balanceLabel) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 10));
        try {
            panel.setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
                    title, TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 22)));
            panel.setBackground(new Color(230, 240, 255));

            panel.add(createSummaryLabel(title + " Income:"));
            panel.add(incomeLabel); // This label shows the combined income (Sponsorship + Other)
            panel.add(createSummaryLabel(title + " Expenses:"));
            panel.add(expensesLabel); // This label shows the total expenses
            panel.add(createSummaryLabel(title + " Balance:"));
            panel.add(balanceLabel);
        } catch (Exception e) {
            showError("Error creating summary section: " + e.getMessage());
        }
        return panel;
    }

    private void formatTable() {
        try {
            // Renderer for number columns (right aligned)
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

            // Apply right alignment to all numerical/amount columns
            // Columns: 4 (Est Inc), 5 (Est Exp), 6 (Est Bal), 7 (Act Inc), 8 (Act Exp), 9 (Act Bal)
            for (int i = 4; i <= 9; i++) {
                 if (i < tableActivities.getColumnModel().getColumnCount()) { // Basic safety check
                    tableActivities.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
                 }
            }

            // Custom renderer for Balance columns (color based on value)
            DefaultTableCellRenderer balanceRenderer = new DefaultTableCellRenderer() {
                @Override
                public void setValue(Object value) {
                    // Cast value to Number safely
                     double val = 0.0;
                     if (value instanceof Number) {
                        val = ((Number) value).doubleValue();
                     } else if (value != null) {
                         // Attempt to parse if not null and not number (e.g. String from model)
                         try {
                             val = Double.parseDouble(value.toString());
                         } catch (NumberFormatException e) {
                             // Log or handle parse error if necessary, keep val at 0.0
                         }
                     }


                    // Set text color: Red if negative balance, Black otherwise
                    setForeground(val < 0 ? Color.RED : Color.BLACK);
                    // Set font to bold for balances
                    setFont(new Font("Arial", Font.BOLD, 18));

                    super.setValue(value); // Set the actual value text
                    setHorizontalAlignment(SwingConstants.RIGHT); // Ensure alignment is right
                }
                 @Override // Ensure other aspects like background are handled
                 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                     Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                      // Restore default background color
                     if (!isSelected) {
                         c.setBackground(table.getBackground());
                     }
                     return c;
                 }
            };
            // Apply the balance renderer to the Estimated Balance (column 6) and Actual Balance (column 9) columns
            if (tableActivities.getColumnModel().getColumnCount() > 6) { // Safety checks
                tableActivities.getColumnModel().getColumn(6).setCellRenderer(balanceRenderer);
            }
            if (tableActivities.getColumnModel().getColumnCount() > 9) { // Safety checks
                 tableActivities.getColumnModel().getColumn(9).setCellRenderer(balanceRenderer);
            }

        } catch (Exception e) {
            showError("Error formatting table: " + e.getMessage());
        }
    }

    /** Helper to create styled labels for total summary values */
    private JLabel createStyledLabel() {
        JLabel label = new JLabel("0.00", SwingConstants.RIGHT);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        return label;
    }

    /** Helper to create labels for summary section titles */
    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        return label;
    }

    /** Helper to create large labels for filter panel */
    private JLabel createLargeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        return label;
    }

    /** Helper to create standard text fields for filters */
    private JTextField createTextField() {
        JTextField field = new JTextField(12);
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        return field;
    }

    // --- Getters for Controller ---
    public JButton getConsultButton() { return btnConsult; }
    public String getStartDate() { return txtStartDate.getText(); }
    public String getEndDate() { return txtEndDate.getText(); }
    public String getStatus() { return (String) cmbStatus.getSelectedItem(); }

    // --- Setters for Controller ---
    public void setStartDate(String date) { txtStartDate.setText(date); }
    public void setEndDate(String date) { txtEndDate.setText(date); }

    /**
     * Updates the main activities table with data from a list of DTOs.
     * @param data The list of FinancialReportDTOs to display.
     */
    public void updateActivitiesTable(List<FinancialReportDTO> data) {
        try {
            modelActivities.setRowCount(0); // Clear existing rows
            if (data != null) {
                for (FinancialReportDTO dto : data) {
                    modelActivities.addRow(new Object[]{
                                dto.getActivityName(),
                                dto.getStatus(),
                                dto.getStartDate(),
                                dto.getEndDate(),
                                // These are the combined totals from the DTO/Model
                                dto.getEstimatedIncome(),
                                dto.getEstimatedExpenses(),
                                dto.getEstimatedBalance(),
                                dto.getActualIncome(),
                                dto.getActualExpenses(),
                                dto.getActualBalance()
                    });
                }
            }
        } catch (Exception e) {
            showError("Error updating activities table: " + e.getMessage());
        }
    }

    /**
     * Updates the total summary labels with calculated values.
     * Assumes estimated/actual totals passed already include all relevant components.
     * @param estIncome Total estimated income (Sponsorship + Other).
     * @param estExpenses Total estimated expenses.
     * @param actualIncome Total actual income (Sponsorship + Other).
     * @param actualExpenses Total actual expenses.
     * @param actualBalance Total actual balance (calculated by controller).
     */
    public void updateTotals(double estIncome, double estExpenses, double actualIncome, double actualExpenses, double actualBalance) {
        try {
            // Format totals to 2 decimal places and set text
            lblTotalEstIncome.setText(String.format("%.2f", estIncome));
            lblTotalEstExpenses.setText(String.format("%.2f", estExpenses));
            // Estimated Balance is calculated in the controller, pass it here
            lblEstBalance.setText(String.format("%.2f", estIncome - estExpenses)); // Calculated here in view, pass total estimated income/expense

            lblTotalIncome.setText(String.format("%.2f", actualIncome));
            lblTotalExpenses.setText(String.format("%.2f", actualExpenses));
            // Actual Balance is calculated in the controller, pass it here
            lblActualBalance.setText(String.format("%.2f", actualBalance));

            // Apply color formatting to total balance labels based on their value
            lblEstBalance.setForeground((estIncome - estExpenses) < 0 ? Color.RED : Color.BLACK);
            lblActualBalance.setForeground(actualBalance < 0 ? Color.RED : Color.BLACK);

        } catch (Exception e) {
            showError("Error updating totals: " + e.getMessage());
        }
    }

    /**
     * Displays an error message dialog.
     * @param message The message to display.
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}