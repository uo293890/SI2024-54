package giis.demo.tkrun;

import giis.demo.util.SwingUtil; // Assuming SwingUtil is used elsewhere or will be
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class FinancialReportController {
    private FinancialReportModel model;
    private FinancialReportView view;
    private List<FinancialReportDTO> currentData; // Holds the current report data

    private LocalDate workingDate; // The date the application considers "today"

    // Using DateTimeFormatter for consistent date string handling (yyyy-MM-dd)
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * Constructs a FinancialReportController.
     *
     * @param model The FinancialReportModel for data access and business logic.
     * @param view The FinancialReportView for user interface interactions.
     * @param workingDate The date considered "today" for application logic (e.g., LocalDate.now()).
     */
    public FinancialReportController(FinancialReportModel model, FinancialReportView view, LocalDate workingDate) {
        // Use Objects.requireNonNull for basic null checks on dependencies
        this.model = Objects.requireNonNull(model, "FinancialReportModel cannot be null");
        this.view = Objects.requireNonNull(view, "FinancialReportView cannot be null");
        this.workingDate = Objects.requireNonNull(workingDate, "workingDate cannot be null");

        initializeController(); // Set up event listeners and default state
    }

    /**
     * Initializes action listeners and sets the initial default date range.
     */
    private void initializeController() {
        // Set the default date range in the view using the workingDate's year
        setDefaultYearFilter();

        // Add action listener to the consult button to trigger data refresh
        view.getConsultButton().addActionListener(e -> refreshReportData());

        // Perform the initial data load and display based on the default filters
        refreshReportData();
    }

    /**
     * Sets the default start and end date filter fields in the view
     * to the beginning and end of the year corresponding to the working date.
     */
    private void setDefaultYearFilter() {
        int currentYear = workingDate.getYear(); // Use the year from the workingDate
        view.setStartDate(currentYear + "-01-01"); // Set start date to Jan 1st of the year
        view.setEndDate(currentYear + "-12-31"); // Set end date to Dec 31st of the year
        // Note: refreshReportData is called separately after this in initializeController
    }

    /**
     * Retrieves report data from the model based on the dates and status selected in the view,
     * and updates the view's table and total summaries.
     * Handles potential exceptions during data retrieval or view update.
     */
    private void refreshReportData() {
        String startDateStr = view.getStartDate(); // Get start date string from view
        String endDateStr = view.getEndDate();    // Get end date string from view
        String status = view.getStatus();        // Get status from view

        // Basic validation for date format before calling the model (optional, model validates too)
        try {
            // Allow empty dates for filtering (if model supports it, which the provided one does not,
            // but it throws IllegalArgumentException which we catch).
            // If dates are mandatory, this check could enforce that.
             if (!startDateStr.trim().isEmpty()) LocalDate.parse(startDateStr.trim(), dateFormatter);
             if (!endDateStr.trim().isEmpty()) LocalDate.parse(endDateStr.trim(), dateFormatter);
        } catch (DateTimeParseException e) {
            view.showError("Invalid date format in filter fields. Please use YYYY-MM-dd.");
            // Clear view data on date parse error
            view.updateActivitiesTable(java.util.Collections.emptyList());
            view.updateTotals(0, 0, 0, 0, 0); // Clear totals
            return; // Stop processing if dates are invalid
        }


        try {
            // Retrieve financial report data from the model using the filter criteria
            // The model is responsible for handling null/empty inputs or specific business logic for filters
            currentData = model.getFinancialReport(startDateStr, endDateStr, status);

            // Update the activities table in the view with the retrieved data
            view.updateActivitiesTable(currentData);

            // Calculate and update the total summary labels in the view
            updateTotals();

        } catch (SQLException e) {
            // Catch and report specific database errors
            view.showError("Database error refreshing financial report: " + e.getMessage());
            e.printStackTrace(); // Print stack trace to console for debugging
            // Clear view data on database error for clarity
            view.updateActivitiesTable(java.util.Collections.emptyList());
            view.updateTotals(0, 0, 0, 0, 0);

        } catch (IllegalArgumentException e) {
             // Catch and report errors from model validation (e.g., empty dates if not handled by model)
             view.showError("Filter validation error: " + e.getMessage());
             e.printStackTrace(); // Print stack trace
             view.updateActivitiesTable(java.util.Collections.emptyList());
             view.updateTotals(0, 0, 0, 0, 0);

        } catch (Exception e) {
            // Catch any other unexpected exceptions during the process
            view.showError("Error refreshing financial report: " + e.getMessage());
            e.printStackTrace(); // Print stack trace to console for debugging
            // Clear view data on unexpected error
            view.updateActivitiesTable(java.util.Collections.emptyList());
            view.updateTotals(0, 0, 0, 0, 0);
        }
    }

    /**
     * Calculates the total estimated and actual income, expenses, and balances
     * from the current report data (stored in currentData) and updates the
     * total summary labels in the view.
     * This method correctly uses the combined income/expense values provided by the DTOs.
     */
    private void updateTotals() {
        double totalEstimatedIncome = 0;
        double totalEstimatedExpenses = 0;
        double totalActualIncome = 0;
        double totalActualExpenses = 0;

        // Ensure currentData list is not null before attempting to iterate
        if (currentData != null) {
            for (FinancialReportDTO dto : currentData) {
                // Sum the *combined* values from each DTO
                // The DTO's estimatedIncome includes Estimated Sponsorship + Estimated Other Income
                totalEstimatedIncome += dto.getEstimatedIncome();
                // The DTO's estimatedExpenses includes Estimated Expenses (from IncomesExpenses)
                totalEstimatedExpenses += dto.getEstimatedExpenses();
                // The DTO's actualIncome includes Actual Sponsorship + Actual Other Income
                totalActualIncome += dto.getActualIncome();
                // The DTO's actualExpenses includes Actual Expenses (from IncomesExpenses)
                totalActualExpenses += dto.getActualExpenses();
            }
        }

        // Calculate overall balances
        double totalEstimatedBalance = totalEstimatedIncome - totalEstimatedExpenses;
        double totalActualBalance = totalActualIncome - totalActualExpenses;

        // Update the total summary labels in the view with the calculated values
        view.updateTotals(
            totalEstimatedIncome,
            totalEstimatedExpenses,
            totalActualIncome,
            totalActualExpenses,
            totalActualBalance
        );
    }

    // The FinancialReportModel and FinancialReportView classes are assumed to be
    // correctly implemented with the methods called by this controller.

    // Example of how this controller might be instantiated in your main application:
    /*
    public static void main(String[] args) {
        // Assuming Database is initialized and passed or managed within the model
        FinancialReportModel model = new FinancialReportModel();
        FinancialReportView view = new FinancialReportView();
        // Provide the desired working date, e.g., today's date
        LocalDate today = LocalDate.now();
        // Instantiate the controller, injecting dependencies and the workingDate
        FinancialReportController controller = new FinancialReportController(model, view, today);
        // Make the view visible to the user
        view.setVisible(true);
    }
    */
}