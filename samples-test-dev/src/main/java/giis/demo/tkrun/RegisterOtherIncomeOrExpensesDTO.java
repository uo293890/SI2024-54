package giis.demo.tkrun;

public class RegisterOtherIncomeOrExpensesDTO {
	private double amount;
	private String Date;
	private String description;
	private int id_event;
	private int id_edition;

	public RegisterOtherIncomeOrExpensesDTO(int id_event, int id_edition, double amount, String Date, String description) {
		this.amount=amount;
		this.Date=Date;
		this.description=description;
		this.id_event=id_event;
		this.id_edition=id_edition;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId_event() {
		return id_event;
	}

	public void setId_event(int id_event) {
		this.id_event = id_event;
	}

	public int getId_edition() {
		return id_edition;
	}

	public void setId_edition(int id_edition) {
		this.id_edition = id_edition;
	}

}
