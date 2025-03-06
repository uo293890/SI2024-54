package giis.demo.tkrun;

public class ActivityDTO {
	private String name;
    private String startDate;
    private String endDate;
    private String status;
    private double estimatedIncome;
    private double actualIncome;
    private double estimatedExpenses;
    private double actualExpenses;

    // Constructor
    public ActivityDTO(String name, String startDate, String endDate, String status, 
                       double estimatedIncome, double actualIncome, 
                       double estimatedExpenses, double actualExpenses) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.estimatedIncome = estimatedIncome;
        this.actualIncome = actualIncome;
        this.estimatedExpenses = estimatedExpenses;
        this.actualExpenses = actualExpenses;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public double getEstimatedIncome() {
        return estimatedIncome;
    }

    public double getActualIncome() {
        return actualIncome;
    }

    public double getEstimatedExpenses() {
        return estimatedExpenses;
    }

    public double getActualExpenses() {
        return actualExpenses;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEstimatedIncome(double estimatedIncome) {
        this.estimatedIncome = estimatedIncome;
    }

    public void setActualIncome(double actualIncome) {
        this.actualIncome = actualIncome;
    }

    public void setEstimatedExpenses(double estimatedExpenses) {
        this.estimatedExpenses = estimatedExpenses;
    }

    public void setActualExpenses(double actualExpenses) {
        this.actualExpenses = actualExpenses;
    }
}