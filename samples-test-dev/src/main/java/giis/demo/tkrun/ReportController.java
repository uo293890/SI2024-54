package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportController {
    private ReportModel model;
    private ReportView view;
    private SimpleDateFormat dateFormat;

    public ReportController(ReportModel model, ReportView view) {
        this.model = model;
        this.view = view;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        initView();
        initController();
    }

    private void initView() {
        view.setVisible(true);
    }

    private void initController() {
        view.getGenerateButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
    }

    private void generateReport() {
        try {
            Date startUtil = dateFormat.parse(view.getStartDate());
            Date endUtil = dateFormat.parse(view.getEndDate());
            System.out.println("Fechas parseadas: " + startUtil + " - " + endUtil);
            
            java.sql.Date start = new java.sql.Date(startUtil.getTime());
            java.sql.Date end = new java.sql.Date(endUtil.getTime());
            
            java.util.List<ReportDTO> report = model.getFinancialReport(start, end, view.getStatus());
            System.out.println("Tamaño del reporte: " + report.size());
            view.updateTable(report);
        } catch (ParseException ex) {
            view.showError("Formato de fecha inválido (Utiliza dd/MM/yyyy)");
            ex.printStackTrace();
        }
    }
}
