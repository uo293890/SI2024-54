package giis.demo.jdbc.activities;


import java.util.Date;

public class ReportFilter {

    private Date startDate;
    private Date endDate;
    private String status;

    public ReportFilter(Date startDate, Date endDate, String status) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
}
