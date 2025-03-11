package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DTO Class
public class ReportDTO {
    private int id;
    private String activityName;
    private String status;
    private Date startDate;
    private Date endDate;
    private double estimatedIncome;
    private double estimatedExpenses;
    private double actualIncome;
    private double actualExpenses;
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    
    public double getEstimatedIncome() { return estimatedIncome; }
    public void setEstimatedIncome(double estimatedIncome) { this.estimatedIncome = estimatedIncome; }
    
    public double getEstimatedExpenses() { return estimatedExpenses; }
    public void setEstimatedExpenses(double estimatedExpenses) { this.estimatedExpenses = estimatedExpenses; }
    
    public double getActualIncome() { return actualIncome; }
    public void setActualIncome(double actualIncome) { this.actualIncome = actualIncome; }
    
    public double getActualExpenses() { return actualExpenses; }
    public void setActualExpenses(double actualExpenses) { this.actualExpenses = actualExpenses; }
    
    public double getEstimatedBalance() { return estimatedIncome - estimatedExpenses; }
    public double getActualBalance() { return actualIncome - actualExpenses; }
}

