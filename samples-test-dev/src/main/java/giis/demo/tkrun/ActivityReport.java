package giis.demo.tkrun;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import giis.demo.jdbc.activities.DatabaseConnection;

public class ActivityReport {

    private List<Activity> activities;

    public ActivityReport(List<Activity> activities) {
        this.activities = activities;
    }

    public ActivityReport() {}

    public String generateReport(Date startDate, Date endDate, String statusFilter) {
        List<Activity> activities = getActivities(startDate, endDate, statusFilter);
        StringBuilder report = new StringBuilder();
        double totalIncome = 0.0;
        double totalExpense = 0.0;

        for (Activity activity : activities) {
            double activityIncome = getTotalIncome(activity);
            double activityExpense = getTotalExpense(activity);
            double balance = activityIncome - activityExpense;

            totalIncome += activityIncome;
            totalExpense += activityExpense;

            report.append("Actividad: ").append(activity.getName()).append("\n")
                  .append("Fechas: ").append(activity.getStartDate()).append(" - ")
                  .append(activity.getEndDate()).append("\n")
                  .append("Estado: ").append(activity.getStatus()).append("\n")
                  .append("Ingresos Totales: ").append(activityIncome).append("\n")
                  .append("Gastos Totales: ").append(activityExpense).append("\n")
                  .append("Balance: ").append(balance).append("\n\n");
        }

        report.append("Totales Generales:\n")
              .append("Ingresos Totales: ").append(totalIncome).append("\n")
              .append("Gastos Totales: ").append(totalExpense).append("\n")
              .append("Balance Total: ").append(totalIncome - totalExpense).append("\n");

        return report.toString();
    }

    private List<Activity> getActivities(Date startDate, Date endDate, String statusFilter) {
        String query = "SELECT * FROM Activity WHERE start_date >= ? AND end_date <= ? AND status = ?";
        return DatabaseConnection.executeQuery(query, startDate, endDate, statusFilter)
                .stream()
                .map(row -> new Activity(
                        (int) row.get("id"),
                        (String) row.get("name"),
                        (Date) row.get("start_date"),
                        (Date) row.get("end_date"),
                        (String) row.get("status")
                ))
                .collect(Collectors.toList());
    }

    private double getTotalIncome(Activity activity) {
        String query = "SELECT SUM(amount) AS total_income FROM Invoice WHERE activity_id = ? AND status = 'Paid'";
        return executeQueryAndSum(query, activity.getId(), "total_income");
    }

    private double getTotalExpense(Activity activity) {
        String query = "SELECT SUM(amount) AS total_expense FROM Payment WHERE activity_id = ? AND status = 'Paid'";
        return executeQueryAndSum(query, activity.getId(), "total_expense");
    }

    private double executeQueryAndSum(String query, int activityId, String columnLabel) {
        Map<String, Object> result = DatabaseConnection.executeQuerySingle(query, activityId);
        return result != null ? ((Number) result.getOrDefault(columnLabel, 0.0)).doubleValue() : 0.0;
    }

    public void calculateTotals() {
        double totalIncome = 0.0;
        double totalExpense = 0.0;

        for (Activity activity : activities) {
            totalIncome += getTotalIncome(activity);
            totalExpense += getTotalExpense(activity);
        }

        System.out.println("Total Ingresos: " + totalIncome);
        System.out.println("Total Gastos: " + totalExpense);
        System.out.println("Balance Total: " + (totalIncome - totalExpense));
    }
}
