package giis.demo.jdbc.Invoices;

public class Invoice {
    private int id;
    private int agreementId;
    private double amount;
    private double vatPercentage;
    private String profitOrNonprofit;

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(int agreementId) {
        this.agreementId = agreementId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(double vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public String getProfitOrNonprofit() {
        return profitOrNonprofit;
    }

    public void setProfitOrNonprofit(String profitOrNonprofit) {
        this.profitOrNonprofit = profitOrNonprofit;
    }

    @Override
    public String toString() {
        return "Invoice{" +
               "id=" + id +
               ", agreementId=" + agreementId +
               ", amount=" + amount +
               ", vatPercentage=" + vatPercentage +
               ", profitOrNonprofit='" + profitOrNonprofit + '\'' +
               '}';
    }
}
