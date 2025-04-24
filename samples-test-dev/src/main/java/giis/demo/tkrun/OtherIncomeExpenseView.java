package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Map;
import java.util.List;

/**
 * View for managing Other Income / Expense entries and registering movements.
 * Contains a section to create new estimated entries and a section to select
 * existing entries and register movements against them.
 */
public class OtherIncomeExpenseView extends JDialog {
    private static final long serialVersionUID = 1L;

    // --- Components for Create New Entry Section ---
    private JComboBox<String> cmbEventForCreation; // For selecting the event when creating
    private JTextField txtNewEntryConcept;
    private JTextField txtNewEntryEstimatedAmount;
    private JButton btnCreateEntry;

    // --- Components for Existing Entries and Movement Registration Section ---
    private JTable tableIncomesExpenses;
    private DefaultTableModel tableIncomesExpensesModel; // Controller sets this
    private JComboBox<String> cmbFilterStatus; // Filter by status for the main table
    private JComboBox<String> cmbFilterEvent; // Filter by event for the main table

    private JTable tableMovementHistory;
    private DefaultTableModel tableMovementHistoryModel; // Controller sets this

    // Fields for new movement registration
    private JTextField txtMovementDate;
    private JTextField txtMovementAmount;
    private JTextField txtMovementConcept; // Optional

    // Labels for messages and summary for the SELECTED entry
    private JLabel lblValidation; // Shows validation status of current movement input
    private JLabel lblRemainingAmount; // Shows remaining amount after current movement input (calculated by controller)
    private JLabel lblSummary; // Shows total paid vs estimated for selected entry (calculated by controller)

    // Buttons for actions
    private JButton btnRegisterMovement; // Renamed for clarity
    private JButton btnClose; // Changed name to Close for a dialog

    // Internal state for the selected entry amounts (managed and set by Controller)
    public double currentEstimatedAmount = 0.0; // Raw estimated amount of selected entry (can be negative)
    public double currentTotalPaid = 0.0;        // Raw total paid for selected entry (raw sum, can be negative)


    public OtherIncomeExpenseView(Frame parent) {
        super(parent, "Manage Other Income / Expense", true); // Modal dialog

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(15, 15));
        getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding
        setResizable(true); // Allow the window to be resized and potentially maximized

        initComponents(); // Build GUI components
    }

    /**
     * Initializes and arranges GUI components.
     */
    private void initComponents() {
        // --- Top Panel: Create New Entry Section ---
        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBorder(new TitledBorder("Create New Income/Expense Entry"));
        GridBagConstraints gbcCreate = new GridBagConstraints();
        gbcCreate.insets = new Insets(5, 5, 5, 5);
        gbcCreate.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        JLabel lblEventCreation = new JLabel("Event:"); // Clarified label name
        cmbEventForCreation = new JComboBox<>(); // Controller will populate this
        JLabel lblConceptCreation = new JLabel("Concept:"); // Clarified label name
        txtNewEntryConcept = new JTextField(20);
        JLabel lblAmountCreation = new JLabel("Estimated Amount (€):"); // Clarified label name
        txtNewEntryEstimatedAmount = new JTextField(10); // Smaller for amount
        btnCreateEntry = new JButton("Create Entry");

        // Add components to the create panel
        gbcCreate.gridx = 0; gbcCreate.gridy = row; gbcCreate.anchor = GridBagConstraints.WEST; gbcCreate.weightx = 0; createPanel.add(lblEventCreation, gbcCreate);
        gbcCreate.gridx = 1; gbcCreate.gridy = row++; gbcCreate.weightx = 1; createPanel.add(cmbEventForCreation, gbcCreate);

        gbcCreate.gridx = 0; gbcCreate.gridy = row; gbcCreate.anchor = GridBagConstraints.WEST; gbcCreate.weightx = 0; createPanel.add(lblConceptCreation, gbcCreate);
        gbcCreate.gridx = 1; gbcCreate.gridy = row++; gbcCreate.weightx = 1; createPanel.add(txtNewEntryConcept, gbcCreate);

        gbcCreate.gridx = 0; gbcCreate.gridy = row; gbcCreate.anchor = GridBagConstraints.WEST; gbcCreate.weightx = 0; createPanel.add(lblAmountCreation, gbcCreate);
        gbcCreate.gridx = 1; gbcCreate.gridy = row++; gbcCreate.weightx = 1; createPanel.add(txtNewEntryEstimatedAmount, gbcCreate);

        gbcCreate.gridx = 0; gbcCreate.gridy = row++; gbcCreate.gridwidth = 2; gbcCreate.fill = GridBagConstraints.NONE; gbcCreate.anchor = GridBagConstraints.EAST; createPanel.add(btnCreateEntry, gbcCreate);


        // --- Panel for Filters and Table ---
        JPanel listPanel = new JPanel(new BorderLayout(10, 10));

        // Filter Panel at the top of the list panel - Now includes Event filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblFilterStatus = new JLabel("Status:"); // Clarified label name
        filterPanel.add(lblFilterStatus);
        cmbFilterStatus = new JComboBox<>(); // Controller will populate this
        filterPanel.add(cmbFilterStatus);

        JLabel lblFilterEvent = new JLabel("Event:"); // New label for Event filter
        filterPanel.add(lblFilterEvent);
        cmbFilterEvent = new JComboBox<>(); // New combo for Event filter
        filterPanel.add(cmbFilterEvent);


        listPanel.add(filterPanel, BorderLayout.NORTH);

        // Main Entries Table in the center of the list panel
        // tableIncomesExpensesModel is set by the controller
        tableIncomesExpenses = new JTable();
        tableIncomesExpenses.setFont(new Font("Arial", Font.PLAIN, 14));
        tableIncomesExpenses.setRowHeight(20);
        tableIncomesExpenses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableIncomesExpenses.setDefaultEditor(Object.class, null); // Non-editable
        tableIncomesExpenses.getTableHeader().setReorderingAllowed(false); // Prevent reordering

        JScrollPane scrollIncexp = new JScrollPane(tableIncomesExpenses);
        scrollIncexp.setBorder(new TitledBorder("Select Income/Expense Entry to Register Movement"));
        listPanel.add(scrollIncexp, BorderLayout.CENTER);


        // --- Right Panel: Movement Registration Form and History ---
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10)); // This panel holds the form AND the history
        // Increased preferred width to make the right section larger
        rightPanel.setPreferredSize(new Dimension(550, 0)); // Increased width


        // Movement History Table (Top part of right panel)
        JPanel historyPanel = new JPanel(new BorderLayout());
        // tableMovementHistoryModel is set by the controller
        tableMovementHistory = new JTable();
        tableMovementHistory.setFont(new Font("Arial", Font.PLAIN, 14));
        tableMovementHistory.setRowHeight(20);
        tableMovementHistory.setDefaultEditor(Object.class, null); // Non-editable
        tableMovementHistory.getTableHeader().setReorderingAllowed(false); // Prevent reordering
        tableMovementHistory.setEnabled(false); // History table is just for display


        JScrollPane scrollHistory = new JScrollPane(tableMovementHistory);
        scrollHistory.setBorder(new TitledBorder("Movement History for Selected Entry"));
        historyPanel.add(scrollHistory, BorderLayout.CENTER);
        // Give history a preferred size to hint at its proportion, but BorderLayout.CENTER will make it fill remaining space
        historyPanel.setPreferredSize(new Dimension(500, 250));


        // New Movement Registration Form (Bottom part of right panel)
        JPanel movementRegistrationPanel = new JPanel(new BorderLayout(10, 10));
        movementRegistrationPanel.setBorder(new TitledBorder("Register New Movement (Payment/Receipt)"));
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(5, 5, 5, 5);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.weightx = 1.0; // Give horizontal space to fields

        row = 0; // Reset row counter for this panel

        txtMovementDate = new JTextField();
        txtMovementAmount = new JTextField();
        txtMovementConcept = new JTextField();

        lblValidation = new JLabel("Select an entry above"); // Initial message
        lblValidation.setForeground(Color.BLACK);
        lblRemainingAmount = new JLabel();
        lblSummary = new JLabel();

        // Add labels and fields for movement registration
        addLabelField(formPanel, gbcForm, row++, "Movement Date (yyyy-MM-dd):", txtMovementDate);
        addLabelField(formPanel, gbcForm, row++, "Amount (€):", txtMovementAmount);
        addLabelField(formPanel, gbcForm, row++, "Concept (Optional):", txtMovementConcept);

        // Add labels for status, remaining, summary - these are updated by the controller
        gbcForm.gridx = 0; gbcForm.gridy = row++; gbcForm.gridwidth = 2; formPanel.add(new JSeparator(), gbcForm);
        addLabel(formPanel, gbcForm, row++, "Status:");
        addLabelField(formPanel, gbcForm, row++, "", lblValidation); // Validation label placeholder

        gbcForm.gridx = 0; gbcForm.gridy = row++; gbcForm.gridwidth = 2; formPanel.add(new JSeparator(), gbcForm);
        addLabel(formPanel, gbcForm, row++, "Remaining:");
        addLabelField(formPanel, gbcForm, row++, "", lblRemainingAmount); // Remaining label placeholder

        gbcForm.gridx = 0; gbcForm.gridy = row++; gbcForm.gridwidth = 2; formPanel.add(new JSeparator(), gbcForm);
        addLabel(formPanel, gbcForm, row++, "Summary:");
        addLabelField(formPanel, gbcForm, row++, "", lblSummary); // Summary label placeholder
        gbcForm.gridx = 0; gbcForm.gridy = row++; gbcForm.gridwidth = 2; formPanel.add(new JSeparator(), gbcForm);


        // --- Button Panel for Movement Registration ---
        btnRegisterMovement = new JButton("Register Movement");
        btnRegisterMovement.setEnabled(false); // Disabled initially
        btnClose = new JButton("Close");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.add(btnRegisterMovement);
        buttonPanel.add(btnClose);

        // Combine form and button panels
        JPanel movementFormAndButtons = new JPanel(new BorderLayout());
        movementFormAndButtons.add(formPanel, BorderLayout.CENTER);
        movementFormAndButtons.add(buttonPanel, BorderLayout.SOUTH);

        // --- Combine history table and movement form panels in the right panel using BorderLayout ---
        rightPanel.add(historyPanel, BorderLayout.CENTER); // History takes the center (expands)
        rightPanel.add(movementFormAndButtons, BorderLayout.SOUTH); // Form goes at the bottom


        // --- Add Panels to Main Frame Layout ---
        add(createPanel, BorderLayout.NORTH); // Create panel at the top
        add(listPanel, BorderLayout.CENTER); // List/Filter table in the center
        add(rightPanel, BorderLayout.EAST); // Registration form and history on the right

        // Set initial dummy values for summary labels until an entry is selected
        lblRemainingAmount.setText("");
        lblSummary.setText("");

        // Initially disable the movement registration form panel visually
        // Note: Actual component enablement is handled by the controller
        // setPanelEnabled(movementRegistrationPanel, false); // Helper to disable all components
    }

    /** Helper method to enable/disable a panel and its components */
    // private void setPanelEnabled(JPanel panel, boolean isEnabled) {
    //     panel.setEnabled(isEnabled);
    //     for (Component comp : panel.getComponents()) {
    //         comp.setEnabled(isEnabled);
    //         if (comp instanceof JPanel) {
    //             setPanelEnabled((JPanel) comp, isEnabled); // Recursively disable sub-panels
    //         }
    //     }
    // }


    /**
     * Helper to add a regular label to a GridBagLayout panel.
     */
    private void addLabel(JPanel panel, GridBagConstraints gbc, int y, String labelText) {
        gbc.gridx = 0; // Label column
        gbc.gridy = y; // Current row
        gbc.weightx = 0.0; // Label takes minimum width
        gbc.anchor = GridBagConstraints.WEST; // Align label to the west
        gbc.fill = GridBagConstraints.NONE; // Label doesn't fill
        gbc.gridwidth = 1; // Label takes 1 column

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label, gbc);
    }


    /**
     * Helper to add label and component to a GridBagLayout panel.
     * Used for input fields and labels whose content is set by controller.
     */
    private void addLabelField(JPanel panel, GridBagConstraints gbc, int y, String labelText, JComponent fieldComponent) {
        if (!labelText.isEmpty()) {
             // Add the label if text is provided
             gbc.gridx = 0; // Label column
             gbc.gridy = y; // Current row
             gbc.weightx = 0.0; // Label takes minimum width
             gbc.anchor = GridBagConstraints.WEST; // Align label to the west
             gbc.fill = GridBagConstraints.NONE; // Label doesn't fill
             gbc.gridwidth = 1; // Label takes 1 column

             JLabel label = new JLabel(labelText);
             label.setFont(new Font("Arial", Font.PLAIN, 14));
             panel.add(label, gbc);
        }


        gbc.gridx = labelText.isEmpty() ? 0 : 1; // Component starts in column 0 if no label, else column 1
        gbc.gridy = y; // Same row as label
        gbc.weightx = 1.0; // Component takes remaining width
        gbc.anchor = GridBagConstraints.EAST; // Align component to the east
        gbc.fill = GridBagConstraints.HORIZONTAL; // Component fills horizontally
        gbc.gridwidth = labelText.isEmpty() ? 2 : 1; // Component spans 2 columns if no label, else 1


        // Apply font to the field/component if it's not a JLabel used for dynamic text
        if (!(fieldComponent instanceof JLabel)) {
            fieldComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        } else {
             // Set font for the dynamic labels
             fieldComponent.setFont(new Font("Arial", Font.BOLD, 14)); // Make dynamic labels bold
        }

        panel.add(fieldComponent, gbc);
    }


    // --- Getters for Components (Used by Controller) ---
    // Getters for Create New Entry Section
    public JComboBox<String> getCmbEventForCreation() { return cmbEventForCreation; }
    public JTextField getTxtNewEntryConcept() { return txtNewEntryConcept; }
    public JTextField getTxtNewEntryEstimatedAmount() { return txtNewEntryEstimatedAmount; }
    public JButton getBtnCreateEntry() { return btnCreateEntry; }

    // Getters for Existing Entries and Movement Registration Section
    public JTable getTableIncomesExpenses() { return tableIncomesExpenses; }
    public JComboBox<String> getCmbFilterStatus() { return cmbFilterStatus; }
    public JComboBox<String> getCmbFilterEvent() { return cmbFilterEvent; } // New getter for Event filter combo
    public JTable getTableMovementHistory() { return tableMovementHistory; }

    // Getters for Movement Registration Fields
    public JTextField getTxtMovementDate() { return txtMovementDate; }
    public JTextField getTxtMovementAmount() { return txtMovementAmount; }
    public JTextField getTxtMovementConcept() { return txtMovementConcept; }

    // Getters for Status/Summary Labels
    public JLabel getLblValidation() { return lblValidation; }
    public JLabel getLblRemainingAmount() { return lblRemainingAmount; }
    public JLabel getLblSummary() { return lblSummary; }

    // Getters for Buttons
    public JButton getBtnRegisterMovement() { return btnRegisterMovement; }
    public JButton getBtnClose() { return btnClose; }


    // --- Setters for Table Models (Used by Controller) ---
    /**
     * Sets the table model for the main entries list and applies formatting.
     */
    public void setTableModelIncomesExpenses(DefaultTableModel model) {
        tableIncomesExpensesModel = model;
        tableIncomesExpenses.setModel(tableIncomesExpensesModel);
        applyIncexpTableFormatting(tableIncomesExpenses);
    }

    /**
     * Sets the table model for the movement history and applies formatting.
     */
    public void setTableModelMovementHistory(DefaultTableModel model) {
        tableMovementHistoryModel = model;
        tableMovementHistory.setModel(tableMovementHistoryModel);
        applyMovementHistoryTableFormatting(tableMovementHistory);
    }

    /**
     * Populates the Event selection combo box for creating new entries.
     * Note: Map storage/lookup moved to Controller for better MVC separation.
     */
    public void populateEventComboForCreation(List<Map<String, Object>> events) {
        cmbEventForCreation.removeAllItems(); // Clear existing items
        cmbEventForCreation.addItem("Select an Event"); // Default prompt
        if (events != null) {
            for (Map<String, Object> event : events) {
                String eventName = (String) event.get("event_name");
                 if (eventName != null) {
                     cmbEventForCreation.addItem(eventName);
                 }
            }
        }
    }

     /**
      * Populates the Event selection combo box for filtering entries.
      */
     public void populateEventComboForFilter(List<Map<String, Object>> events) {
         cmbFilterEvent.removeAllItems(); // Clear existing items
         cmbFilterEvent.addItem("All Activities"); // Default filter option
         if (events != null) {
            for (Map<String, Object> event : events) {
                String eventName = (String) event.get("event_name");
                 if (eventName != null) {
                     cmbFilterEvent.addItem(eventName);
                 }
            }
         }
     }


     /**
      * Populates the status filter combo box with relevant options.
      */
     public void populateFilterStatusCombo(List<String> statuses) {
         cmbFilterStatus.removeAllItems();
         cmbFilterStatus.addItem("All"); // Always include 'All' filter
          if (statuses != null) {
              for (String status : statuses) {
                  // Add statuses from the model, excluding 'All' if it was in the model list
                   if (!"All".equalsIgnoreCase(status)) {
                       cmbFilterStatus.addItem(status);
                   }
              }
          }
          cmbFilterStatus.addItem("Not Paid"); // Always include 'Not Paid' filter
     }


    /**
     * Applies formatting to the main entries table (hide ID, alignment, color).
     * Uses a custom renderer for the "Remaining" column.
     */
    private void applyIncexpTableFormatting(JTable table) {
         TableColumnModel columnModel = table.getColumnModel();
         // Ensure there are columns before trying to format
         if (columnModel.getColumnCount() > 0) {
             // Hide the ID column (column 0 in the model)
             // Ensure column 0 exists
             if (0 < columnModel.getColumnCount()) {
                 TableColumn idColumn = columnModel.getColumn(0);
                 idColumn.setMinWidth(0);
                 idColumn.setMaxWidth(0);
                 idColumn.setPreferredWidth(0);
                 idColumn.setResizable(false);
             }


             // Apply right alignment to currency columns
             DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
             rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
             // Columns are (Model Index): 0=ID(hidden), 1=Event Name, 2=Type, 3=Concept, 4=Estimated (Raw), 5=Total Paid (Raw), 6=Status, 7=Remaining (Raw)
             if (4 < columnModel.getColumnCount()) columnModel.getColumn(4).setCellRenderer(rightRenderer); // Estimated (Model Col 4)
             if (5 < columnModel.getColumnCount()) columnModel.getColumn(5).setCellRenderer(rightRenderer); // Total Paid (Model Col 5)
             // Remaining column gets a custom renderer if it exists

             // Custom renderer for Remaining column color and format
             // This renderer uses the *value* in the cell (the raw remaining amount) and the estimated amount from the same row
             DefaultTableCellRenderer remainingBalanceRenderer = new DefaultTableCellRenderer() {
                 @Override
                 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                     Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                     // Reset default background color if not selected
                     if (!isSelected) {
                          c.setBackground(table.getBackground());
                     }

                     // Ensure value is a Number
                     double remainingRaw = 0.0;
                     if (value instanceof Number) {
                         remainingRaw = ((Number) value).doubleValue();
                     }

                     // Get estimated amount from the same row (model column 4) using the provided 'row' parameter
                     double estimatedRaw = 0.0;
                     // Ensure column 4 exists and row is valid before accessing
                     if (table.getModel().getColumnCount() > 4 && row < table.getModel().getRowCount() && row >= 0) {
                         Object estimatedObj = table.getModel().getValueAt(row, 4); // Get Estimated from model col 4
                         if (estimatedObj instanceof Number) {
                              estimatedRaw = ((Number) estimatedObj).doubleValue();
                         }
                     }

                     // Now determine color and text based on estimatedRaw and remainingRaw
                     Color color = Color.BLACK; // Default color
                     String textToDisplay = String.format("%.2f €", remainingRaw); // Default display format (show raw value)

                     boolean isIncome = estimatedRaw >= 0;
                     double tolerance = 0.001; // Use a small tolerance for floating point comparisons

                     if (isIncome) { // Income: estimated >= 0
                         if (Math.abs(remainingRaw) < tolerance) { // remainingRaw == 0 (Estimated - TotalPaid == 0)
                             color = new Color(0, 128, 0); // Green for fully received
                             textToDisplay = "Fully Received";
                         } else if (remainingRaw > tolerance) { // remainingRaw > 0 (Estimated > TotalPaid)
                             color = Color.BLACK; // Black for remaining to receive
                             textToDisplay = String.format("%.2f € remaining", remainingRaw);
                         } else { // remainingRaw < -tolerance (Estimated < TotalPaid - overreceived)
                             color = Color.RED; // Red for overreceived
                             textToDisplay = String.format("Overreceived: %.2f €", Math.abs(remainingRaw));
                         }
                     } else { // Expense: estimated < 0
                          // remainingRaw = estimated - totalPaid. Since estimated is negative, and totalPaid should also be negative (payment out),
                          // remaining > 0 means overpaid (e.g., estimated -100, paid -120 -> remaining +20)
                          // remaining == 0 means fully paid (e.g., estimated -100, paid -100 -> remaining 0)
                          // remaining < 0 means remaining needed (e.g., estimated -100, paid -50 -> remaining -50)
                         if (Math.abs(remainingRaw) < tolerance) { // remainingRaw == 0
                             color = new Color(0, 128, 0); // Green for fully paid
                             textToDisplay = "Fully Paid";
                         } else if (remainingRaw > tolerance) { // remainingRaw > 0 (Overpaid)
                             color = Color.RED; // Red for overpaid
                             textToDisplay = String.format("Overpaid: %.2f €", remainingRaw);
                         } else { // remainingRaw < -tolerance (Remaining needed)
                              color = Color.BLACK; // Black for remaining to pay
                             textToDisplay = String.format("%.2f € remaining", Math.abs(remainingRaw)); // Show positive absolute value for 'remaining'
                         }
                     }

                     setForeground(color);
                     setText(textToDisplay); // Set the formatted text
                     setHorizontalAlignment(SwingConstants.RIGHT); // Ensure alignment is right

                     return c;
                 }
            };
            // Apply to "Remaining (€)" column if it exists (index 7 in model)
            if (7 < columnModel.getColumnCount()) columnModel.getColumn(7).setCellRenderer(remainingBalanceRenderer);
         }
    }


    /**
     * Applies formatting to the movement history table (right alignment for amount).
     */
     private void applyMovementHistoryTableFormatting(JTable table) {
          TableColumnModel columnModel = table.getColumnModel();
          // Ensure there are columns before trying to format
          if (columnModel.getColumnCount() > 1) {
              DefaultTableCellRenderer historyAmountRenderer = new DefaultTableCellRenderer();
              historyAmountRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
              columnModel.getColumn(1).setCellRenderer(historyAmountRenderer); // Amount column (index 1)
          }
     }


    // --- Update Methods (Used by Controller) ---

    /**
     * Sets the current estimated and total paid amounts for the selected entry
     * and updates summary labels. These values are stored internally by the view
     * for use in calculations and displays.
     * @param estimated The raw estimated amount of the selected entry.
     * @param totalPaid The raw total paid amount for the selected entry.
     */
    public void setCurrentAmounts(double estimated, double totalPaid) {
        this.currentEstimatedAmount = estimated; // Store raw estimated amount
        this.currentTotalPaid = totalPaid;        // Store total paid sum
        updateSummaryLabels(); // Update labels based on new state
    }

    /**
     * Updates the summary labels (Total Paid/Received vs Estimated)
     * based on the view's internal current amounts.
     */
    public void updateSummaryLabels() {
        String entryType = currentEstimatedAmount >= 0 ? "Income" : "Expense";

        // Update the Summary label
        // Show absolute values for display clarity
        lblSummary.setText(String.format("Total Paid/Received for this %s: %.2f € / Estimated: %.2f €",
                                         entryType, Math.abs(currentTotalPaid), Math.abs(currentEstimatedAmount)));

        // Clear the Remaining label text here, updateValidation will fill it when input changes.
        lblRemainingAmount.setText(""); // Clear until validation fills it
        lblRemainingAmount.setForeground(Color.BLACK); // Reset color
    }

    /**
     * Sets the default date in the movement date field.
     * @param dateStr The date string (yyyy-MM-dd) to set.
     */
    public void setDefaultMovementDate(String dateStr) {
        txtMovementDate.setText(dateStr);
    }

    /**
     * Updates the validation message label text and color.
     * @param message The message to display.
     * @param color The color for the message text.
     */
    public void setValidationMessage(String message, Color color) {
        lblValidation.setForeground(color);
        lblValidation.setText(message);
    }

    /**
     * Clears the movement input fields (Amount, Concept).
     * Does NOT clear the Date field or summary/validation labels.
     */
    public void clearMovementFields() {
        txtMovementAmount.setText("");
        txtMovementConcept.setText("");
        // Validation message and remaining/summary labels are updated
        // by the validation listener and selection logic.
        lblValidation.setText(""); // Clear validation message on field clear
        lblRemainingAmount.setText(""); // Clear remaining label on field clear
        lblRemainingAmount.setForeground(Color.BLACK); // Reset remaining label color
    }

     /**
      * Clears the create new entry fields.
      */
     public void clearNewEntryFields() {
         // cmbEventForCreation.setSelectedIndex(0); // Resets to "Select an Event" - done in controller
         txtNewEntryConcept.setText("");
         txtNewEntryEstimatedAmount.setText("");
     }

    /**
     * Displays an error message dialog.
     * @param message The message to display.
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Opens the dialog window.
     */
    public void open() {
        this.setVisible(true);
        // Optional: Request maximization after setting visible. Behavior depends on OS/LAF for JDialog.
        // setExtendedState(MAXIMIZED_BOTH); // This constant is typically from JFrame
    }
}
