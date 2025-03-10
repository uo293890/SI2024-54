package giis.demo.tkrun;

import java.util.Date;

public class ReportDTO {
    private String editionTitle;       // Nombre de la actividad (edición)
    private Date editionStartDate;       // Fecha de inicio de la edición
    private Date editionEndDate;         // Fecha de fin de la edición
    private String editionStatus;        // Estado de la edición
    private double totalEstimatedAgreement;      // Ingresos estimados provenientes de acuerdos
    private double totalEstimatedOtherIncome;      // Otros ingresos estimados (de Otherie con monto positivo)
    private double totalEstimatedOtherExpenses;    // Otros gastos estimados (de Otherie con monto negativo)
    private double totalPaidIncome;                // Ingresos pagados (desde facturas asociadas a acuerdos)
    private double totalPaidExpenses;              // Gastos pagados (desde movimientos asociados a Otherie)

    // Getters y setters
    public String getEditionTitle() { return editionTitle; }
    public void setEditionTitle(String editionTitle) { this.editionTitle = editionTitle; }

    public Date getEditionStartDate() { return editionStartDate; }
    public void setEditionStartDate(Date editionStartDate) { this.editionStartDate = editionStartDate; }

    public Date getEditionEndDate() { return editionEndDate; }
    public void setEditionEndDate(Date editionEndDate) { this.editionEndDate = editionEndDate; }

    public String getEditionStatus() { return editionStatus; }
    public void setEditionStatus(String editionStatus) { this.editionStatus = editionStatus; }

    public double getTotalEstimatedAgreement() { return totalEstimatedAgreement; }
    public void setTotalEstimatedAgreement(double totalEstimatedAgreement) { this.totalEstimatedAgreement = totalEstimatedAgreement; }

    public double getTotalEstimatedOtherIncome() { return totalEstimatedOtherIncome; }
    public void setTotalEstimatedOtherIncome(double totalEstimatedOtherIncome) { this.totalEstimatedOtherIncome = totalEstimatedOtherIncome; }

    public double getTotalEstimatedOtherExpenses() { return totalEstimatedOtherExpenses; }
    public void setTotalEstimatedOtherExpenses(double totalEstimatedOtherExpenses) { this.totalEstimatedOtherExpenses = totalEstimatedOtherExpenses; }

    public double getTotalPaidIncome() { return totalPaidIncome; }
    public void setTotalPaidIncome(double totalPaidIncome) { this.totalPaidIncome = totalPaidIncome; }

    public double getTotalPaidExpenses() { return totalPaidExpenses; }
    public void setTotalPaidExpenses(double totalPaidExpenses) { this.totalPaidExpenses = totalPaidExpenses; }

    // Campos calculados para el reporte
    public double getEstimatedIncome() { 
        // Suma de ingresos estimados: acuerdos y otros ingresos
        return totalEstimatedAgreement + totalEstimatedOtherIncome; 
    }
    public double getEstimatedExpenses() { 
        return totalEstimatedOtherExpenses; 
    }
    public double getEstimatedBalance() { 
        return getEstimatedIncome() - getEstimatedExpenses(); 
    }
    public double getPaidBalance() { 
        return totalPaidIncome - totalPaidExpenses; 
    }
}
