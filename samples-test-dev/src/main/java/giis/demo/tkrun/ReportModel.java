package giis.demo.tkrun;

import giis.demo.util.Database;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;
import javax.swing.JLabel;
import java.awt.Color;

public class ReportModel {
    private Database db = new Database();

    public List<ReportDTO> getFinancialReport(String startDate, String endDate, String status) {
        List<ReportDTO> reports = new ArrayList<>();

        if (startDate == null || startDate.trim().isEmpty() || endDate == null || endDate.trim().isEmpty()) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            startDate = year + "-01-01";
            endDate = year + "-12-31";
        }

        boolean isAll = "All".equalsIgnoreCase(status);
        
        System.out.println("Querying with Start Date: " + startDate + ", End Date: " + endDate + ", Status: " + status);
        
        String sql = "SELECT " +
                     "  e.edition_id AS id, " +
                     "  e.edition_title AS name, " +
                     "  e.edition_status AS status, " +
                     "  e.edition_inidate AS start_date, " +
                     "  e.edition_enddate AS end_date, " +
                     "  COALESCE((SELECT SUM(a.agreement_amount) FROM Agreement a WHERE a.edition_id = e.edition_id), 0) AS estimated_income, " +
                     "  COALESCE((SELECT SUM(o.otherie_amount) FROM Otherie o WHERE o.edition_id = e.edition_id), 0) AS estimated_expenses, " +
                     "  COALESCE((SELECT SUM(i.invoice_vat) FROM Invoice i JOIN Agreement a ON i.agreement_id = a.agreement_id WHERE a.edition_id = e.edition_id), 0) AS actual_income, " +
                     "  COALESCE((SELECT SUM(m.movement_amount) FROM Movement m JOIN Otherie o ON m.otherie_id = o.otherie_id WHERE o.edition_id = e.edition_id), 0) AS actual_expenses " +
                     "FROM Edition e " +
                     "WHERE 1=1 ";
        
        if (isAll) {
            sql += "AND e.edition_status IN ('Completed', 'Planned', 'Ongoing') ";
        } else {
            sql += "AND e.edition_status = ? ";
        }
        
        sql += "ORDER BY e.edition_inidate";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (!isAll) {
                stmt.setString(1, status);
            }
            
            ResultSet rs = stmt.executeQuery();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            while (rs.next()) {
                ReportDTO report = new ReportDTO();
                report.setId(rs.getInt("id"));
                report.setActivityName(rs.getString("name"));
                report.setStatus(rs.getString("status"));

                String startDateStr = rs.getString("start_date");
                String endDateStr = rs.getString("end_date");

                try {
                    report.setStartDate(startDateStr != null ? new java.sql.Date(dateFormat.parse(startDateStr).getTime()) : null);
                    report.setEndDate(endDateStr != null ? new java.sql.Date(dateFormat.parse(endDateStr).getTime()) : null);
                } catch (ParseException e) {
                    e.printStackTrace();
                    report.setStartDate(null);
                    report.setEndDate(null);
                }

                report.setEstimatedIncome(rs.getDouble("estimated_income"));
                report.setEstimatedExpenses(rs.getDouble("estimated_expenses"));
                report.setActualIncome(rs.getDouble("actual_income"));
                report.setActualExpenses(rs.getDouble("actual_expenses"));
                reports.add(report);
                
                System.out.println("Edition Retrieved: " + report.getId() + " - " + report.getActivityName() + " - " + report.getStatus());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        System.out.println("Final Retrieved " + reports.size() + " records with status: " + status);
        
        return reports;
    }

    public static void applyColorToTable(JTable table) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof Number) {
                    double val = ((Number) value).doubleValue();
                    if (val < 0) {
                        setForeground(Color.RED);
                    } else {
                        setForeground(Color.BLACK);
                    }
                } else {
                    setForeground(Color.BLACK);
                }
                super.setValue(value);
            }
        };

        for (int i = 5; i <= 10; i++) { // Aplica color a columnas de valores numéricos
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    public static void applyColorToFinancialOverview(JLabel... labels) {
        for (JLabel label : labels) {
            try {
                double value = Double.parseDouble(label.getText().replace(',', '.'));
                if (value < 0) {
                    label.setForeground(Color.RED);
                } else {
                    label.setForeground(Color.BLACK);
                }
            } catch (NumberFormatException e) {
                label.setForeground(Color.BLACK);
            }
        }
    }
}