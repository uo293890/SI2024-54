package giis.demo.tkrun;

import java.util.List;

public class RegisterPaymentsSponsorDTO {
	private double amount;
	private String Date;
	private String concept;
	private int invoice;

	public RegisterPaymentsSponsorDTO(int invoice, double amount, String Date, String concept) {
		this.amount=amount;
		this.Date=Date;
		this.concept=concept;
		this.invoice=invoice;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public int getInvoice() {
		return invoice;
	}

	public void setInvoice(int invoice) {
		this.invoice = invoice;
	}


}
