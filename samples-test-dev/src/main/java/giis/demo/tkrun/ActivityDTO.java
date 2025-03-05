package giis.demo.tkrun;

public class ActivityDTO {
    private String name;
    private String startDate;
    private String endDate;
    private String status;
    private double income;
    private double expenses;
    private double balance;

    public ActivityDTO(String name, String startDate, String endDate, String status, 
                       double income, double expenses, double balance) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.income = income;
        this.expenses = expenses;
        this.balance = balance;
    }

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

    public double getIncome() {
        return income;
    }

    public double getExpenses() {
        return expenses;
    }

    public double getBalance() {
        return balance;
    }
}