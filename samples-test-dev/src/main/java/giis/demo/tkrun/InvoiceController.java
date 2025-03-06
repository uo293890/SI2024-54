package giis.demo.tkrun;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

public class InvoiceController {
    private InvoiceModel model;
    private InvoiceView view;
    private SimpleDateFormat dateFormat;

    public InvoiceController(InvoiceModel model, InvoiceView view) {
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
        view.getGenerateButton().addActionListener(e -> generateInvoice());
        view.getSendButton().addActionListener(e -> sendInvoice());
    }

    private void generateInvoice() {
        try {
            InvoiceDTO newInvoice = new InvoiceDTO();
            newInvoice.setInvoiceNumber(model.generateInvoiceNumber());
            newInvoice.setInvoiceDate(new java.util.Date());
            
            view.setInvoiceNumber(newInvoice.getInvoiceNumber());
            view.setInvoiceDate(dateFormat.format(newInvoice.getInvoiceDate()));
            
        } catch (Exception ex) {
            showError("Error generating invoice: " + ex.getMessage());
        }
    }

    private void sendInvoice() {
        try {
            InvoiceDTO invoice = new InvoiceDTO();
            invoice.setInvoiceNumber(view.getInvoiceNumber());
            invoice.setInvoiceDate(dateFormat.parse(view.getInvoiceDate()));
            invoice.setRecipientName(view.getRecipientName());
            invoice.setTaxId(view.getTaxId());
            invoice.setAddress(view.getAddress());

            if (validateData(invoice)) {
                model.saveInvoice(invoice);
                showMessage("Invoice saved successfully");
            }
        } catch (Exception ex) {
            showError("Error sending invoice: " + ex.getMessage());
        }
    }

    private boolean validateData(InvoiceDTO invoice) {
        // Validate that the recipient's name is not empty
        if (invoice.getRecipientName() == null || invoice.getRecipientName().trim().isEmpty()) {
            showError("Recipient name is required");
            return false;
        }

        // Validate the Tax ID (NIF)
        if (invoice.getTaxId() == null || !validateTaxId(invoice.getTaxId())) {
            showError("Invalid Tax ID. Required format: 8 digits + 1 letter (individuals) or 1 letter + 7 digits + 1 letter (companies/foreigners)");
            return false;
        }

        return true;
    }

    private boolean validateTaxId(String taxId) {
        // Regular expression to validate the Tax ID format
        String regex = "^([A-HJ-NP-SUVW]\\d{7}[A-Z]|\\d{8}[A-Z])$";
        if (!taxId.matches(regex)) {
            return false; // Incorrect format
        }

        // Extract the control letter
        char controlLetter = taxId.charAt(taxId.length() - 1);
        String numbers = taxId.substring(0, taxId.length() - 1);

        // Calculate the expected control letter
        char calculatedLetter = calculateControlLetter(numbers);

        // Compare the provided control letter with the calculated one
        return controlLetter == calculatedLetter;
    }

    private char calculateControlLetter(String numbers) {
        // Control letter table for Tax ID
        String letters = "TRWAGMYFPDXBNJZSQVHLCKE";

        // Convert the numbers to an integer value
        int number = Integer.parseInt(numbers);

        // Calculate the position of the letter in the table
        int index = number % 23;
        return letters.charAt(index);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(view, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}