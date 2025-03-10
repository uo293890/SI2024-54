package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportController {
    private ReportModel model;
    private ReportView view;
    private SimpleDateFormat dateFormat;

    public ReportController(ReportModel model, ReportView view) {
        this.model = model;
        this.view = view;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        initController();
        view.setVisible(true);
    }

    private void initController() {
        view.getConsultarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
    }

    private void generateReport() {
        try {
            Date startUtil = dateFormat.parse(view.getStartDate());
            Date endUtil = dateFormat.parse(view.getEndDate());

            // Convertimos a java.sql.Date para la consulta en la base de datos
            java.sql.Date start = new java.sql.Date(startUtil.getTime());
            java.sql.Date end = new java.sql.Date(endUtil.getTime());

            // Se obtiene el filtro de estado; si se selecciona "Todos", se ignora el filtro.
            String status = view.getStatus();
            List<ReportDTO> report = model.getFinancialReport(start, end, status);
            view.updateTable(report);
        } catch (ParseException ex) {
            view.showError("Formato de fecha inválido (Utiliza dd/MM/yyyy)");
        }
    }
}
