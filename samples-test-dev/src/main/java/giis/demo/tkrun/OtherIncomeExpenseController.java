package giis.demo.tkrun;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import giis.demo.util.SwingUtil; // Para mostrar errores fácilmente

import java.awt.Color;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

/**
 * Controller for the "Manage Other Income/Expense" screen.
 * Handles creating new estimated entries and registering movements against existing ones.
 * Manages interaction between View, Model, and DTOs, including filters and validation.
 */
public class OtherIncomeExpenseController {

    private OtherIncomeExpenseModel model; // Handles data operations
    private OtherIncomeExpenseView view;    // Manages the GUI

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Date format for display/parsing
    private LocalDate workingDate; // The application's current date

    // Small tolerance for double comparisons to avoid floating point issues
    private static final double DOUBLE_TOLERANCE = 0.001;

    // Map to store Event Name -> Event ID for quick lookup when creating/filtering entries
    private Map<String, Integer> eventNameToIdMap = new HashMap<>();
    // Map to store Event ID -> Event Name for quick lookup (useful if we get ID first)
    // private Map<Integer, String> eventIdToNameMap = new HashMap<>(); // Not currently needed based on flow

    /**
     * Sets up the controller with its model, view, and the working date.
     */
    public OtherIncomeExpenseController(OtherIncomeExpenseModel model, OtherIncomeExpenseView view, LocalDate workingDate) {
        this.model = Objects.requireNonNull(model, "Model cannot be null");
        this.view = Objects.requireNonNull(view, "View cannot be null");
        this.workingDate = Objects.requireNonNull(workingDate, "Working date cannot be null");

        initController(); // Set up listeners and load initial data
    }

    /**
     * Sets up listeners for GUI components and loads initial data.
     */
    private void initController() {
        // Populate filter combo boxes and the creation event combo box
        populateEventCombos();
        view.populateFilterStatusCombo(model.getAllRelevantStatusesForQuery());

        // Load entries into the main table with the default filters ("All Status", "All Activities")
        loadIncomesExpensesEntries(getSelectedStatusesFromFilter(), getSelectedEventIdFilter());

        // Set the default date in the movement date field to the working date
        view.setDefaultMovementDate(workingDate.format(dateFormatter));

        // Listener for selecting a row in the main entries table
        view.getTableIncomesExpenses().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Ensure this is the final event
                if (view.getTableIncomesExpenses().getSelectedRow() != -1) {
                    onEntrySelected(); // Handle entry selection
                } else {
                    // Handle case when selection is cleared
                    clearEntryDetailsView();
                }
            }
        });

        // Listeners for real-time input validation on movement fields
        DocumentListener movementValidationListener = new DocumentListener() {
            @Override public void changedUpdate(DocumentEvent e) { updateMovementValidation(); }
            @Override public void removeUpdate(DocumentEvent e) { updateMovementValidation(); }
            @Override public void insertUpdate(DocumentEvent e) { updateMovementValidation(); }
        };
        view.getTxtMovementAmount().getDocument().addDocumentListener(movementValidationListener);
        view.getTxtMovementDate().getDocument().addDocumentListener(movementValidationListener);
        view.getTxtMovementConcept().getDocument().addDocumentListener(movementValidationListener); // Listener for concept (optional)


        // Listener for the Register Movement button
        view.getBtnRegisterMovement().addActionListener(e -> registerMovement());

        // Listener for the Close button
        view.getBtnClose().addActionListener(e -> view.dispose());

        // Listeners for the filter combo boxes
        view.getCmbFilterStatus().addActionListener(e -> onFilterChanged());
        view.getCmbFilterEvent().addActionListener(e -> onFilterChanged());


        // Listener for the Create Entry button
        view.getBtnCreateEntry().addActionListener(e -> createIncomesExpenseEntry());

        // Initially disable the register movement button
        view.getBtnRegisterMovement().setEnabled(false);
        // Set initial validation message for the movement section
         view.setValidationMessage("Select an entry above", Color.BLACK);
    }

    /**
     * Populates the Event selection combo boxes (for creation and filtering).
     */
    private void populateEventCombos() {
        try {
            List<Map<String, Object>> events = model.getAllEvents();
            // Create maps for name <-> id lookup
             eventNameToIdMap = new HashMap<>();
             // eventIdToNameMap = new HashMap<>(); // If needed

             if (events != null) {
                 for (Map<String, Object> event : events) {
                     String eventName = (String) event.get("event_name");
                     Integer eventId = (Integer) event.get("event_id");
                      if (eventName != null && eventId != null) {
                          eventNameToIdMap.put(eventName, eventId);
                          // eventIdToNameMap.put(eventId, eventName); // If needed
                      }
                 }
             }

            view.populateEventComboForCreation(events); // Populate creation combo
            view.populateEventComboForFilter(events);   // Populate filter combo

        } catch (SQLException e) {
            view.showError("Database error loading events: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
             view.showError("Error loading events: " + e.getMessage());
             e.printStackTrace();
        }
    }

     /**
      * Returns the ID of the currently selected event in the creation combo box.
      * Returns -1 if no valid event is selected or map is not initialized.
      */
     private int getSelectedEventIdForCreation() {
         String selectedName = (String) view.getCmbEventForCreation().getSelectedItem(); // Corrected getter
          // Return -1 if the default prompt, null, or map is not ready
         if (selectedName == null || "Select an Event".equals(selectedName) || eventNameToIdMap == null) {
             return -1;
         }
         // Use the stored map to get the ID
         return eventNameToIdMap.getOrDefault(selectedName, -1);
     }


    /**
     * Handles changes in either filter combo box.
     * Reloads the main table with the currently selected filters applied.
     */
    private void onFilterChanged() {
         // Get selected filters from both combos
         List<String> statusesToLoad = getSelectedStatusesFromFilter();
         Integer eventIdFilter = getSelectedEventIdFilter();

         // Load the table with combined filters
         loadIncomesExpensesEntries(statusesToLoad, eventIdFilter);
         // Clear table selection, which also clears the form via listener
         view.getTableIncomesExpenses().clearSelection();
    }


    /**
     * Determines the list of statuses to load into the main table based on the selected status filter.
     */
    private List<String> getSelectedStatusesFromFilter() {
         String selectedFilter = (String) view.getCmbFilterStatus().getSelectedItem();
         List<String> statusesToLoad = new ArrayList<>();

         if ("All".equalsIgnoreCase(selectedFilter)) {
             // Load all relevant statuses
             statusesToLoad = model.getAllRelevantStatusesForQuery();
         } else if ("Paid".equalsIgnoreCase(selectedFilter)) {
             statusesToLoad.add("Paid");
         } else if ("Not Paid".equalsIgnoreCase(selectedFilter)) {
             // Load statuses that are typically considered "not paid"
             // These should match the possible states of IncomesExpenses that aren't 'Paid'
             statusesToLoad.add("Estimated");
             statusesToLoad.add("Open");
             statusesToLoad.add("Closed"); // Assuming Closed might not mean fully paid
             statusesToLoad.add("Partial Paid"); // Include Partial Paid as "Not Fully Paid"
         } else {
             // If a specific status is selected directly
             statusesToLoad.add(selectedFilter);
         }

         // Return distinct statuses
         return statusesToLoad.stream().distinct().collect(Collectors.toList());
    }

     /**
      * Returns the ID of the currently selected event in the filter combo box.
      * Returns null if "All Activities" or no valid event is selected.
      */
     private Integer getSelectedEventIdFilter() {
         String selectedName = (String) view.getCmbFilterEvent().getSelectedItem(); // Corrected getter
          // Return null if the default filter option or null is selected
         if (selectedName == null || "All Activities".equals(selectedName)) {
             return null; // Indicates no event filter
         }
         // Use the stored map to get the ID
         return eventNameToIdMap.get(selectedName); // Returns null if name not found (shouldn't happen if map and combo are in sync)
     }


    /**
     * Loads entries from the model into the main table, applying the selected filters.
     */
    private void loadIncomesExpensesEntries(List<String> allowedStatuses, Integer eventIdFilter) {
        try {
            // Get entries from the model, applying the filters
            List<OtherIncomeExpenseDTO> entries = model.getAllIncomesExpensesWithTotalPaid(allowedStatuses, eventIdFilter);

            // Create table model for the view (Includes ID column at index 0, which will be hidden)
            // Headers match DTO and columns in the applyIncexpTableFormatting method
            DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"ID", "Event", "Type", "Concept", "Estimated (€)", "Total Paid (€)", "Status", "Remaining (€)"}, 0
            ) {
                 @Override // Make the table cells non-editable
                 public boolean isCellEditable(int row, int column) { return false; }
                 // Optional: Override getColumnClass for better rendering/sorting
                 @Override
                 public Class<?> getColumnClass(int columnIndex) {
                     if (columnIndex == 0) return Integer.class; // ID (hidden)
                     // Assuming Event Name (col 1), Type (col 2), Concept (col 3) are Strings
                     if (columnIndex == 4 || columnIndex == 5 || columnIndex == 7) return Double.class; // Estimated, Paid, Remaining (using model indices)
                     // Status (col 6) is String
                     return Object.class; // Default for safety
                 }
            };

            // Populate table model with data from DTOs
            if (entries != null) {
                for (OtherIncomeExpenseDTO entry : entries) {
                    // Add data including the raw incexpId at model index 0
                    tableModel.addRow(new Object[]{
                        entry.getIncexpId(), // Model Column 0 (Hidden)
                        entry.getEventName(), // Model Column 1 (View Column 0)
                        entry.getType(), // Model Column 2 (View Column 1)
                        entry.getConcept(), // Model Column 3 (View Column 2)
                        entry.getEstimatedAmount(), // Raw Estimated Amount (Model Column 4, View Column 3)
                        entry.getTotalPaid(), // Raw Total Paid Amount (Model Column 5, View Column 4)
                        entry.getStatus(), // Model Column 6 (View Column 5)
                        entry.getRemainingAmountRaw() // Raw Remaining Amount (Model Column 7, View Column 6)
                         // NOTE: Column indices shift in the View compared to the Model due to hiding ID.
                         // The View's applyIncexpTableFormatting uses the *model* indices (0, 4, 5, 7) for formatting.
                    });
                }
            }

            // Set the table model in the view and apply formatting (which hides the ID)
            view.setTableModelIncomesExpenses(tableModel);

        } catch (SQLException e) {
            view.showError("Database error loading entries: " + e.getMessage());
            e.printStackTrace();
            view.setTableModelIncomesExpenses(new DefaultTableModel()); // Clear table on error
             clearEntryDetailsView(); // Clear associated detail view as well
        } catch (Exception e) {
            view.showError("Error loading entries: " + e.getMessage());
            e.printStackTrace();
            view.setTableModelIncomesExpenses(new DefaultTableModel()); // Clear table on error
             clearEntryDetailsView(); // Clear associated detail view as well
        }
    }


    /**
     * Handles selection of an entry row in the main table.
     * Loads history and updates the movement form area.
     */
    private void onEntrySelected() {
        int selectedRow = view.getTableIncomesExpenses().getSelectedRow();
        if (selectedRow == -1) {
            // This case is handled by the ListSelectionListener when selection is cleared
            return; // No selection means nothing to load/update
        }

        try {
            // Get entry ID from the hidden column (model index 0) of the *table model*
             // Use getModel().getValueAt(row, modelColumnIndex) for data regardless of view visibility
            // Ensure row index is valid before accessing model
             if (selectedRow >= view.getTableIncomesExpenses().getModel().getRowCount() || selectedRow < 0) {
                 System.err.println("Warning: Selected row index is out of bounds for the model.");
                 clearEntryDetailsView(); // Clear view if selected row is invalid
                 return;
             }

            Object idValue = view.getTableIncomesExpenses().getModel().getValueAt(selectedRow, 0); // ID is at model index 0
             if (!(idValue instanceof Number)) {
                 throw new RuntimeException("Unexpected data type for entry ID in table model at row " + selectedRow + ".");
             }
            int incexpId = ((Number) idValue).intValue();

            // Get estimated and total paid amounts directly from the model for accuracy
            double rawEstimatedAmount = model.getIncexpEstimatedAmount(incexpId);
            double totalPaidValue = model.getIncexpTotalPaid(incexpId);

            // Update view's internal state and summary labels with raw amounts
            view.setCurrentAmounts(rawEstimatedAmount, totalPaidValue);
            // updateSummaryLabels() is called by setCurrentAmounts

            // Load and display movement history for the selected entry
            loadMovementHistory(incexpId);

            // Clear the new movement input fields but keep default date
            view.clearMovementFields(); // Clears amount, concept, resets validation/remaining labels
            view.setDefaultMovementDate(workingDate.format(dateFormatter)); // Reset date field to working date

            // Trigger validation to update initial state of the form fields for this selection
            // This will calculate and display the initial remaining based on current amounts
            updateMovementValidation();


        } catch (SQLException e) {
            view.showError("Database error loading entry details: " + e.getMessage());
            e.printStackTrace();
            clearEntryDetailsView(); // Clear view on error
        } catch (Exception ex) {
            view.showError("Error selecting entry: " + ex.getMessage());
            ex.printStackTrace();
            clearEntryDetailsView(); // Clear view on error
        }
    }

    /**
     * Clears the detail view area (movement form and history).
     * Called when table selection is cleared or on error.
     */
    private void clearEntryDetailsView() {
        // Clear input fields, validation message, and disable register button
        view.clearMovementFields(); // Clears amount, concept, validation message, disables button
        // Reset default date field
        view.setDefaultMovementDate(workingDate.format(dateFormatter));
        // Clear history table
        view.setTableModelMovementHistory(new DefaultTableModel());
        // Reset summary display and internal state to 0
        view.setCurrentAmounts(0.0, 0.0); // Resets view.currentEstimatedAmount and view.currentTotalPaid to 0.0
        // updateSummaryLabels() is called by setCurrentAmounts
        // Reset validation message
        view.setValidationMessage("Select an entry above", Color.BLACK);
    }


    /**
     * Loads movement history for a specific entry into the history table.
     */
    private void loadMovementHistory(int incexpId) {
        try {
            // Get movement data from the model
            List<Object[]> movements = model.getMovementsByIncexpId(incexpId);

            // Create table model for history
            // Headers match columns in the applyMovementHistoryTableFormatting method
            DefaultTableModel historyTableModel = new DefaultTableModel(
                 new Object[]{"Date", "Amount (€)", "Concept"}, 0
            ) {
                 @Override // Make the table cells non-editable
                 public boolean isCellEditable(int row, int column) { return false; }
                 // Optional: Override getColumnClass for better rendering/sorting
                 @Override
                 public Class<?> getColumnClass(int columnIndex) { // Corrected method signature
                     if (columnIndex == 1) return Double.class; // Amount column
                     return String.class; // Date and Concept
                 }
            };

            // Populate history table
            if (movements != null) {
                for (Object[] movement : movements) {
                    if (movement != null && movement.length >= 3) {
                         // Add raw data to the model
                         historyTableModel.addRow(new Object[]{
                             movement[0], // Date string
                             movement[1], // Amount (Number/Double)
                             movement[2]  // Concept (String, can be null)
                         });
                    } else {
                         System.err.println("Warning: Skipping movement history row due to insufficient data.");
                    }
                }
            }

            // Set history table model in the view (formatting applied by view)
            view.setTableModelMovementHistory(historyTableModel);

        } catch (SQLException e) {
             view.showError("Database error loading movement history: " + e.getMessage());
             e.printStackTrace();
             view.setTableModelMovementHistory(new DefaultTableModel()); // Clear history table on error
        } catch (Exception e) {
             view.showError("Error loading movement history: " + e.getMessage());
             e.printStackTrace();
             view.setTableModelMovementHistory(new DefaultTableModel()); // Clear history table on error
        }
    }

    /**
     * Performs real-time validation on movement input fields and updates the UI.
     * Provides feedback on date format, amount format, required fields, and the effect of the movement on the total.
     * This method is triggered by DocumentListeners on the movement input fields.
     */
    private void updateMovementValidation() {
        // Validation only applies if an entry is selected in the main table
        int selectedRow = view.getTableIncomesExpenses().getSelectedRow();
        if (selectedRow == -1) {
            // This case is handled by the selection listener clearing the view and setting default message
            return; // Do nothing if no entry is selected
        }

        // Get amounts for the selected entry from view's state (set on selection)
        double estimatedAmount = view.currentEstimatedAmount; // Raw estimated (can be negative)
        double totalPaid = view.currentTotalPaid; // Raw total paid (can be negative)

        // Get input from fields
        String amountStr = view.getTxtMovementAmount().getText().trim();
        String dateStr = view.getTxtMovementDate().getText().trim();
        // String concept = view.getTxtMovementConcept().getText().trim(); // Concept is optional for movement validation

        // Determine entry type (Income or Expense) based on estimated amount
        String entryType = estimatedAmount >= 0 ? "Income" : "Expense";

        boolean inputsAreValidAndComplete = false; // Flag to enable the register button
        String validationMessage = "";
        Color validationColor = Color.GRAY; // Default color for incomplete/pending input


        // --- 1. Amount Validation ---
        double movementAmountInput = 0.0; // Amount as entered by the user (user types "100" or "-50")
        boolean isAmountFormatValid = false;
        if (amountStr.isEmpty()) {
            validationMessage = "Enter amount";
            validationColor = Color.BLACK; // Neutral color for pending input
        } else {
             try {
                 movementAmountInput = Double.parseDouble(amountStr);
                 if (Math.abs(movementAmountInput) < DOUBLE_TOLERANCE) { // Use tolerance for zero check
                     validationMessage = "Amount cannot be zero";
                     validationColor = Color.RED;
                 } else {
                      isAmountFormatValid = true; // Amount format is valid and non-zero
                 }
             } catch (NumberFormatException ex) {
                 validationMessage = "Invalid amount format (use numbers and . for decimal)";
                 validationColor = Color.RED;
             }
        }

        // --- 2. Date Validation ---
         boolean isDateFormatValid = false;
         String dateStatusMsg = "";
         LocalDate movementDate = null; // Variable to hold parsed date if valid

         if (dateStr.isEmpty()) {
             dateStatusMsg = "Date is required";
             view.getTxtMovementDate().setForeground(Color.RED); // Indicate empty visually
         } else {
              try {
                  movementDate = LocalDate.parse(dateStr, dateFormatter);
                  isDateFormatValid = true;
                  view.getTxtMovementDate().setForeground(Color.BLACK); // Reset color

                  // Check date relative to working date
                  if (movementDate.isAfter(workingDate)) {
                       dateStatusMsg = "WARNING: Date is in the Future (" + movementDate.format(dateFormatter) + " vs Working Date " + workingDate.format(dateFormatter) + ")";
                       // Color for date warning is applied later, might override other colors
                  } else if (movementDate.isBefore(workingDate)) {
                      dateStatusMsg = "Date is in the Past (" + movementDate.format(dateFormatter) + " vs Working Date " + workingDate.format(dateFormatter) + ")";
                      // Past dates are generally fine, no extra color unless combined with amount issue
                  } else { // isEqual(workingDate)
                      dateStatusMsg = "Date is Present (Working Date: " + workingDate.format(dateFormatter) + ")";
                  }

              } catch (DateTimeParseException e) {
                  dateStatusMsg = "Invalid date format (yyyy-MM-dd)";
                  view.getTxtMovementDate().setForeground(Color.RED); // Indicate invalid format visually
              }
         }

        // --- Determine if all *required* inputs for movement registration are valid ---
        // Required fields are Date (valid format) and Amount (valid format, non-zero)
         inputsAreValidAndComplete = isAmountFormatValid && isDateFormatValid; // Concept is optional


        // --- Update Messages and Calculated Remaining based on Input ---
        // Only proceed with calculating potential amounts and detailed messages if required inputs are valid
        if (inputsAreValidAndComplete) {
             // Calculate the amount to be *added* to the total paid sum (raw value stored in DB)
             // This depends on the user input sign.
             // Let's store the raw user input amount directly in the Movement table.
             // The interpretation of this amount relative to the Entry type happens when calculating totalPaid.
             double amountToStoreRaw = movementAmountInput; // Store the raw user input amount


             double potentialNewTotalPaidRaw = totalPaid + amountToStoreRaw; // Raw sum of movements
             double potentialRemainingRaw = estimatedAmount - potentialNewTotalPaidRaw; // Estimated raw - New Total Paid raw


             // Build the payment status message based on the *effect* of the movement
             String paymentEffectMsg;
             if (Math.abs(movementAmountInput) < DOUBLE_TOLERANCE) {
                  paymentEffectMsg = "Amount is zero."; // Should be caught by zero check
             } else if (movementAmountInput > 0) { // User entered a positive amount
                 paymentEffectMsg = "Adding as Receipt"; // User adds positive -> increases total paid (positive or less negative)
             } else { // movementAmountInput < 0 // User entered a negative amount
                 paymentEffectMsg = "Adding as Payment Out / Refund Given"; // User adds negative -> decreases total paid (less positive or more negative)
             }


             // Build the combined validation message (Payment Effect | Outcome | Date Status)
             String outcomeMsg;
             double tolerance = DOUBLE_TOLERANCE; // Use tolerance for outcome checks
             if (entryType.equals("Income")) { // Income: estimated >= 0
                  if (Math.abs(potentialRemainingRaw) < tolerance) outcomeMsg = "Exact Receipt: Entry will be Paid";
                  else if (potentialRemainingRaw > tolerance) outcomeMsg = "Partial Receipt: " + String.format("%.2f € remaining", potentialRemainingRaw);
                  else outcomeMsg = "Overreceived: Exceeds estimated by " + String.format("%.2f €", Math.abs(potentialRemainingRaw)); // remaining < -tolerance
             } else { // Expense: estimated < 0
                  // Remaining needed for Expense is estimatedAbs - potentialNewTotalPaidAbs
                  double estimatedAbs = Math.abs(estimatedAmount);
                  double potentialNewTotalPaidAbs = Math.abs(potentialNewTotalPaidRaw); // Absolute value of sum of movements
                  double remainingNeededDisplay = estimatedAbs - potentialNewTotalPaidAbs; // How much ABSOLUTE amount is left to pay

                  if (Math.abs(remainingNeededDisplay) < tolerance) outcomeMsg = "Exact Payment: Entry will be Paid";
                  else if (remainingNeededDisplay > tolerance) outcomeMsg = "Partial Payment: " + String.format("%.2f € remaining", remainingNeededDisplay); // remainingNeededDisplay > tolerance
                  else outcomeMsg = "Overpaid: Exceeds estimated by " + String.format("%.2f €", Math.abs(remainingNeededDisplay)); // remainingNeededDisplay < -tolerance (means paid out more than estimated absolute)
             }

             // Combine messages: Payment Effect | Outcome | Date Status
             validationMessage = paymentEffectMsg + " | " + outcomeMsg;
              if (!dateStatusMsg.isEmpty() && !dateStatusMsg.equals("Date is required")) { // Append date status if it exists and isn't just a required field msg
                 validationMessage += " | " + dateStatusMsg;
              }


             // Update validation color based on potential state and date warning
              Color paymentColor = Color.BLACK; // Default color based on payment outcome

             // Determine color based on the potential *remaining* state after this movement
             double toleranceColor = DOUBLE_TOLERANCE; // Use same tolerance
             if (entryType.equals("Income")) {
                  // Income: Green if fully received, Red if overreceived, Black if remaining needed
                  if (Math.abs(potentialRemainingRaw) < toleranceColor) paymentColor = new Color(0, 128, 0); // Exact
                  else if (potentialRemainingRaw < -toleranceColor) paymentColor = Color.RED; // Overreceived
                  else paymentColor = Color.BLACK; // Partial remaining (still needed)

             } else { // Expense
                  // Expense: Green if fully paid, Red if overpaid, Black if remaining needed
                  if (Math.abs(potentialRemainingRaw) < toleranceColor) paymentColor = new Color(0, 128, 0); // Exact
                  else if (potentialRemainingRaw > toleranceColor) paymentColor = Color.RED; // Overpaid
                  else paymentColor = Color.BLACK; // Partial paid (still needed)
             }

             // Combine payment color and date warning color (Date warning overrides general validity color if present)
             validationColor = dateStatusMsg.startsWith("WARNING:") ? Color.ORANGE : paymentColor;
             if (dateStatusMsg.startsWith("Invalid date") || dateStatusMsg.equals("Date is required")) validationColor = Color.RED; // Invalid format or required is always Red


             // Update displayed remaining amount based on potential state (raw calculation)
             view.getLblRemainingAmount().setText(String.format("%.2f €", potentialRemainingRaw));
             // Set remaining label color based purely on payment effect color
             view.getLblRemainingAmount().setForeground(paymentColor);


         } else { // Inputs are invalid or incomplete (amount or date format/zero/empty)
             // Validation message and color are set by their specific checks above.
             // Ensure the Remaining label shows the current remaining amount.
             double currentRemaining = estimatedAmount - totalPaid;
             view.getLblRemainingAmount().setText(String.format("%.2f €", currentRemaining));
             // Set its color based on the *current* remaining state, mirroring the renderer logic
              Color currentRemainingColor = Color.BLACK;
              double currentRemainingRaw = estimatedAmount - totalPaid; // Use view's internal state
              double toleranceColor = DOUBLE_TOLERANCE;
              if (entryType.equals("Income")) {
                   if (Math.abs(currentRemainingRaw) < toleranceColor) currentRemainingColor = new Color(0, 128, 0);
                   else if (currentRemainingRaw < -toleranceColor) currentRemainingColor = Color.RED;
                   else currentRemainingColor = Color.BLACK;
              } else { // Expense
                   if (Math.abs(currentRemainingRaw) < toleranceColor) currentRemainingColor = new Color(0, 128, 0);
                   else if (currentRemainingRaw > toleranceColor) currentRemainingColor = Color.RED;
                   else currentRemainingColor = Color.BLACK;
              }
             view.getLblRemainingAmount().setForeground(currentRemainingColor);

             // If date format is invalid or date is empty, ensure Validation color is RED, overriding others
             if (dateStatusMsg.startsWith("Invalid date") || dateStatusMsg.equals("Date is required")) {
                 validationColor = Color.RED;
             }
              // If amount is invalid, validationColor is already RED.
         }


         // Set final validation message and color
         // If validation message is empty (e.g., only date warning), show default "Valid Input"?
         if (validationMessage.isEmpty() && inputsAreValidAndComplete) {
             validationMessage = "Valid Input";
             validationColor = Color.BLACK;
         } else if (validationMessage.isEmpty()) {
              // Should not be empty if inputs are invalid, but safety
              validationMessage = "Incomplete Input"; // Fallback if no specific message was set
              validationColor = Color.BLACK;
         }

         view.setValidationMessage(validationMessage, validationColor);
         // Enable register button only if ALL required inputs are valid and complete
         view.getBtnRegisterMovement().setEnabled(inputsAreValidAndComplete);
     }


    /**
     * Registers a new movement (payment or receipt) for the selected entry.
     * Called when the Register Movement button is clicked.
     */
    private void registerMovement() {
        // Use SwingUtil to wrap exceptions for clear error messages in dialogs
        SwingUtil.exceptionWrapper(() -> {
            // Catch SQLExceptions specifically within the lambda for better reporting
            try {
                // --- 1. Get Data and Validate (final check) ---
                int selectedRow = view.getTableIncomesExpenses().getSelectedRow();
                if (selectedRow == -1) {
                    // Should not happen if button is disabled correctly
                    throw new RuntimeException("Please select an Income/Expense entry.");
                }

                // Get incexpId from the hidden column (model index 0)
                 // Use getModel().getValueAt(row, modelColumnIndex)
                 // Ensure row index is valid before accessing model
                if (selectedRow >= view.getTableIncomesExpenses().getModel().getRowCount() || selectedRow < 0) {
                    System.err.println("Warning: Selected row index is out of bounds for the model during registration.");
                    // This indicates a state issue where a row is selected but invalid
                    throw new RuntimeException("Internal error: Invalid entry selected.");
                }

                Object idValue = view.getTableIncomesExpenses().getModel().getValueAt(selectedRow, 0); // ID is at model index 0
                 if (!(idValue instanceof Number)) {
                     throw new RuntimeException("Internal error: Could not retrieve entry ID from table model.");
                 }
                int incexpId = ((Number) idValue).intValue();

                // Get input from fields
                String dateStr = view.getTxtMovementDate().getText().trim();
                String amountStr = view.getTxtMovementAmount().getText().trim();
                String concept = view.getTxtMovementConcept().getText().trim(); // Concept can be empty/null


                // Final validation before saving (should largely match updateValidation, but crucial for safety)
                 if (amountStr.isEmpty() || dateStr.isEmpty()) {
                      // Re-run validation to set messages and throw if still invalid
                     updateMovementValidation();
                     throw new RuntimeException("Please fill all required fields (Amount, Date).");
                 }

                LocalDate movementDate;
                try {
                    movementDate = LocalDate.parse(dateStr, dateFormatter);
                } catch (DateTimeParseException ex) {
                    throw new RuntimeException("Invalid date format. Please useYYYY-MM-DD."); // Corrected message format hint
                }
                // Allow dates in the future relative to workingDate, but warn user BEFORE registering
                if (movementDate.isAfter(workingDate)) {
                    int confirm = JOptionPane.showConfirmDialog(view,
                            "The movement date (" + movementDate.format(dateFormatter) + ") is in the future relative to the working date (" + workingDate.format(dateFormatter) + "). Do you want to proceed?",
                            "Future Date Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return; // User chose not to proceed
                    }
                }


                double movementAmountInput;
                try {
                    movementAmountInput = Double.parseDouble(amountStr);
                    if (Math.abs(movementAmountInput) < DOUBLE_TOLERANCE) throw new NumberFormatException(); // Zero amount not allowed
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Invalid payment amount (cannot be zero).");
                }


                // --- 2. Determine Amount to Store in DB (Handle signs) ---
                // The amount stored in the Movement table should be positive for money received and negative for money paid out.
                // This depends ONLY on the user's input sign, NOT the entry type (Income vs Expense).
                double actualMovementAmountToStore = movementAmountInput; // Store the raw user input amount


                // --- 3. Determine Concept to Store (Use user concept or generate default) ---
                 String conceptToStore = concept.isEmpty() ? null : concept; // Start with user's concept or null if empty

                 if (conceptToStore == null) { // If user did not provide a concept
                     // Generate a default concept based on the sign of the amount being stored
                     if (actualMovementAmountToStore > 0) {
                         conceptToStore = "Receipt"; // Money coming in
                     } else {
                         conceptToStore = "Payment"; // Money going out
                     }
                 }


                // --- 4. Insert Movement Record ---
                // Pass null if concept is empty string, assuming DB column allows NULL
                 model.insertMovement(incexpId, dateStr, conceptToStore, actualMovementAmountToStore); // Use the determined conceptToStore


                // --- 5. Recalculate Total Paid and Determine New Status ---
                // Fetch the new total paid amount from the database after the insert
                double newTotalPaid = model.getIncexpTotalPaid(incexpId);
                // Fetch the estimated amount again to be safe
                double originalEstimatedAmount = model.getIncexpEstimatedAmount(incexpId);

                // Fetch the current status from the database BEFORE reloading the table
                // This is crucial to avoid overwriting manual 'Open'/'Closed' statuses unintentionally
                String statusInDbBeforeUpdate = model.getIncexpStatus(incexpId);


                // Determine the NEW status based *purely* on the relationship between newTotalPaid and originalEstimatedAmount
                String statusBasedOnAmounts;
                 if (originalEstimatedAmount >= 0) { // Income entry (Estimated >= 0)
                     if (Math.abs(newTotalPaid - originalEstimatedAmount) < DOUBLE_TOLERANCE) {
                         statusBasedOnAmounts = "Paid"; // Exactly paid/received
                     } else if (newTotalPaid > DOUBLE_TOLERANCE) { // Any positive balance means some paid, but not exactly estimated
                         statusBasedOnAmounts = "Partial Paid";
                     } else { // newTotalPaid <= DOUBLE_TOLERANCE (0 or negative net)
                         statusBasedOnAmounts = "Estimated"; // No net amount received (or negative net)
                     }
                 } else { // Expense entry (Estimated < 0)
                     // 'Paid' means total paid *out* reaches or exceeds the estimated *absolute* expense amount.
                     // This happens when newTotalPaid <= originalEstimatedAmount (e.g., estimated -100, paid -100 or -110)
                     if (Math.abs(newTotalPaid - originalEstimatedAmount) < DOUBLE_TOLERANCE) {
                          statusBasedOnAmounts = "Paid"; // Exactly paid
                      } else if (newTotalPaid < -DOUBLE_TOLERANCE) { // Any negative balance means some paid out, but not all
                           statusBasedOnAmounts = "Partial Paid"; // Partial if negative but not fully paid out (e.g. est -100, paid -50)
                      } else { // newTotalPaid >= -DOUBLE_TOLERANCE (0 or positive net)
                          statusBasedOnAmounts = "Estimated"; // No net amount paid out (or positive net = error/over-refunded)
                      }
                 }


                 // --- 6. Decide Final Status to Save and Update DB ---
                 // Based on the database constraint, we can ONLY save 'Estimated' or 'Paid'.
                 // If the calculated status is 'Partial Paid', we must map it to either 'Estimated' or 'Paid'.
                 // Mapping 'Partial Paid' to 'Estimated' is the most appropriate choice given the constraint.
                 String finalStatusToSetInDb;

                 if ("Paid".equalsIgnoreCase(statusBasedOnAmounts)) {
                     finalStatusToSetInDb = "Paid"; // Save 'Paid' if fully paid
                 } else {
                     finalStatusToSetInDb = "Estimated"; // Save 'Estimated' for Partial Paid or Estimated based on amounts
                 }

                 // Get the current status from the database BEFORE updating to avoid overwriting manual states like Open/Closed
                 // Removed duplicate declaration:
                 // String statusInDbBeforeUpdate = model.getIncexpStatus(incexpId);


                 // Only update the database status if the determined final status (based on amounts and DB constraint)
                 // is different from the status currently in the database.
                 // We still respect manual 'Open'/'Closed' statuses unless the entry becomes fully 'Paid'.
                 boolean shouldUpdateDbStatus = false;
                 if ("Paid".equalsIgnoreCase(finalStatusToSetInDb)) {
                      // If the new status is 'Paid', always update the DB status to 'Paid'
                      shouldUpdateDbStatus = !"Paid".equalsIgnoreCase(statusInDbBeforeUpdate);
                 } else { // finalStatusToSetInDb is "Estimated" (because Partial Paid is mapped to Estimated for DB)
                      // If the new status is 'Estimated' (meaning not fully paid),
                      // only update the DB status if the current status was 'Partial Paid' or 'Paid'
                      // (i.e., reverting a payment-driven status).
                      if ("Partial Paid".equalsIgnoreCase(statusInDbBeforeUpdate) || "Paid".equalsIgnoreCase(statusInDbBeforeUpdate)) {
                           shouldUpdateDbStatus = true;
                      }
                      // If the current status is Estimated, Open, or Closed, it stays that way if the new status is Estimated
                 }


                 if (shouldUpdateDbStatus) {
                     model.updateIncexpStatus(incexpId, finalStatusToSetInDb);
                 } else {
                     // Optional: Log or inform if DB status was not updated due to manual state
                     if (!finalStatusToSetInDb.equalsIgnoreCase(statusInDbBeforeUpdate)) {
                          System.out.println("Keeping database status '" + statusInDbBeforeUpdate + "' for incexpId " + incexpId + " as calculated status '" + statusBasedOnAmounts + "' maps to '" + finalStatusToSetInDb + "' but manual status takes precedence unless fully Paid.");
                     }
                 }


                // --- 7. Refresh View and Reset Form ---
                // Reload table with the currently selected filter to show updated status and total paid
                loadIncomesExpensesEntries(getSelectedStatusesFromFilter(), getSelectedEventIdFilter());

                loadMovementHistory(incexpId); // Refresh history table for the selected entry
                clearMovementForm(); // Reset amount and concept fields, validation label

                reselectEntryInTable(incexpId); // Find and reselect the entry in the table to update details view via listener

                // Show success message
                JOptionPane.showMessageDialog(view, "Movement registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                 // Wrap SQLException in RuntimeException for SwingUtil
                 throw new RuntimeException("Database error during movement registration: " + e.getMessage(), e);
            } catch (RuntimeException e) {
                 // Rethrow other RuntimeExceptions
                 throw e;
            } catch (Exception ex) {
                 // Wrap any other unexpected exceptions
                 throw new RuntimeException("Unexpected error during movement registration: " + ex.getMessage(), ex);
            }
        });
    }


    /**
     * Clears input fields and resets the default date and validation message.
     * Resets the state of the movement form section.
     */
    private void clearMovementForm() {
        view.clearMovementFields(); // Clears amount, concept, validation message text, disables button
        // Set default date to working date
        view.setDefaultMovementDate(workingDate.format(dateFormatter));
        // Validation update is triggered by DocumentListeners after clearing the fields
    }

     /**
      * Finds and re-selects an entry in the main table by its ID after reloading.
      * This triggers the selection listener to refresh the details view.
      */
     private void reselectEntryInTable(int incexpId) {
         JTable table = view.getTableIncomesExpenses();
         DefaultTableModel modelTable = (DefaultTableModel) table.getModel();
         int rowToSelect = -1; // Variable local para encontrar el índice en el modelo

         // Find the row index by ID (ID is at model column 0, hidden)
         for (int i = 0; i < modelTable.getRowCount(); i++) {
             // Ensure column 0 exists and value is a Number before accessing/casting
             if (modelTable.getColumnCount() > 0 && modelTable.getValueAt(i, 0) instanceof Number) {
                 Object idValue = modelTable.getValueAt(i, 0); // Get ID from model column 0
                 if (((Number) idValue).intValue() == incexpId) {
                     rowToSelect = i; // Found the index in the model
                     break; // Exit loop once found
                 }
             } else {
                  // Log a warning if data structure is unexpected during reselection search
                  System.err.println("Warning: Skipping row in reselectEntryInTable due to unexpected ID column data.");
             }
         }

         // Create a final variable to use inside the lambda
         final int finalModelRowToSelect = rowToSelect;


         // Select the row if found, otherwise clear selection (entry might be filtered out or deleted)
         // The check is now on the final variable
         if (finalModelRowToSelect != -1) {
             // Use invokeLater to ensure selection happens after table model updates are complete
             SwingUtilities.invokeLater(() -> {
                 // Convert the model index to a view index (important if table is sorted/filtered)
                 // Use the final variable here
                 int viewRowToSelect = table.convertRowIndexToView(finalModelRowToSelect);
                 if (viewRowToSelect != -1) {
                      // Select the row using the view index
                      table.setRowSelectionInterval(viewRowToSelect, viewRowToSelect);
                      // The selection listener for tableIncomesExpenses should automatically trigger onEntrySelected()
                      // which will update the details view for the newly selected row.
                 } else {
                      // Entry is in the model but not currently visible in the view (e.g., filtered out)
                      view.showError("Selected entry is now filtered out based on current view filters.");
                      table.clearSelection(); // Clear selection and details view
                 }
             });

         } else {
             // The previously selected entry ID was not found in the table model after reload.
             // This could happen if the entry was deleted, or if the filter somehow excluded it
             // even though the filter selection should prevent this if implemented correctly.
              // If the previous selection is no longer in the table at all, clear it and the details view.
              boolean wasEntrySelectedBeforeReload = (view.currentEstimatedAmount != 0.0 || view.currentTotalPaid != 0.0 || !view.getLblSummary().getText().isEmpty()); // Simple check if details were loaded

              if (wasEntrySelectedBeforeReload) {
                 System.err.println("Warning: Previously selected entry (ID " + incexpId + ") not found in table model after reload.");
                 view.showError("Previously selected entry not found or deleted.");
                 table.clearSelection(); // Clear selection and details view
              }
              // If nothing was selected before, do nothing.
         }
     }

    /**
     * Handles the action when the "Create Entry" button is clicked.
     * Validates input and inserts a new IncomesExpenses entry.
     */
    private void createIncomesExpenseEntry() {
         SwingUtil.exceptionWrapper(() -> {
             // 1. Get Input from Creation Fields
             int eventId = getSelectedEventIdForCreation(); // Gets ID from view's combo box logic
             String concept = view.getTxtNewEntryConcept().getText().trim();
             String amountStr = view.getTxtNewEntryEstimatedAmount().getText().trim();

             // 2. Validate Input for New Entry Creation
             if (eventId == -1) {
                 throw new RuntimeException("Please select an Event for the new entry.");
             }
             if (concept.isEmpty()) {
                 throw new RuntimeException("Concept is required for the new entry.");
             }
             double estimatedAmount;
             try {
                 estimatedAmount = Double.parseDouble(amountStr);
                 // Basic validation: Allow any number including zero for estimated amount.
                 // Sign determines Income (>=0) or Expense (<0).
             } catch (NumberFormatException ex) {
                 throw new RuntimeException("Invalid estimated amount format (use numbers and . for decimal).");
             }

             // 3. Insert New Entry via Model
             try {
                 // Insert the new entry with initial status 'Estimated'
                 model.insertIncomesExpensesEntry(eventId, concept, estimatedAmount);
             } catch (SQLException e) {
                  // Wrap SQLException for SwingUtil
                 throw new RuntimeException("Database error creating new entry: " + e.getMessage(), e);
             }


             // 4. Refresh Table and Clear Form
             // Reload the main table to include the newly created entry
             // Pass the *current* filters so the new entry appears if it matches
             loadIncomesExpensesEntries(getSelectedStatusesFromFilter(), getSelectedEventIdFilter());
             view.clearNewEntryFields(); // Clear the creation form fields

             // Optional: Show success message
             JOptionPane.showMessageDialog(view, "New entry created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

         });
    }
}
