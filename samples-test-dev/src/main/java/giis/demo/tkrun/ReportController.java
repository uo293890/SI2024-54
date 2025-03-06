package giis.demo.tkrun;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;

public class ReportController {
    private ReportModel model;
    private ReportView view;

    public ReportController(ReportModel model, ReportView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        view.getGenerateButton().addActionListener(e -> generateReport());
    }

    private void generateReport() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date startUtil = sdf.parse(view.getStartDate());
            java.util.Date endUtil = sdf.parse(view.getEndDate());

            java.sql.Date start = new java.sql.Date(startUtil.getTime());
            java.sql.Date end = new java.sql.Date(endUtil.getTime());

            List<ReportDTO> report = model.getFinancialReport(start, end, view.getStatus());
            view.updateTable(report);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(view, "Invalid date format (Use dd/MM/yyyy)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}