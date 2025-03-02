package giis.demo.tkrun;

public class ActivityDTO {
    private String date;
    private String name;
    private String status;
    private double income;
    private double expenses;

    public ActivityDTO(String date, String name, String status, double income, double expenses) {
        this.date = date;
        this.name = name;
        this.status = status;
        this.income = income;
        this.expenses = expenses;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
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
}