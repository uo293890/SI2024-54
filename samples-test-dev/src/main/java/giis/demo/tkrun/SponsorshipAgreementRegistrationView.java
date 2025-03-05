package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class SponsorshipAgreementRegistrationView extends JFrame {
    private SponsorshipAgreementRegistrationController controller;
    private JTextField agreementDateField;
    private JTextField registeredAmountField;
    private JTextField governingBoardMemberField;
    private JTextField sponsorNameField;
    private JTextField sponsorEmailField;
    private JTextField sponsorPhoneField;

    public SponsorshipAgreementRegistrationView() {
        this.controller = new SponsorshipAgreementRegistrationController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sponsorship Agreement Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 2));

        // Agreement Date
        add(new JLabel("Agreement Date (YYYY-MM-DD):"));
        agreementDateField = new JTextField();
        add(agreementDateField);

        // Registered Amount
        add(new JLabel("Registered Amount:"));
        registeredAmountField = new JTextField();
        add(registeredAmountField);

        // Governing Board Member
        add(new JLabel("Governing Board Member:"));
        governingBoardMemberField = new JTextField();
        add(governingBoardMemberField);

        // Sponsor Contact Information
        add(new JLabel("Sponsor Name:"));
        sponsorNameField = new JTextField();
        add(sponsorNameField);

        add(new JLabel("Sponsor Email:"));
        sponsorEmailField = new JTextField();
        add(sponsorEmailField);

        add(new JLabel("Sponsor Phone:"));
        sponsorPhoneField = new JTextField();
        add(sponsorPhoneField);

        // Register Button
        JButton registerButton = new JButton("Register Agreement");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerAgreement();
            }
        });
        add(registerButton);

        setVisible(true);
    }

    private void registerAgreement() {
        SponsorshipAgreementRegistrationDTO agreementDTO = new SponsorshipAgreementRegistrationDTO();

        try {
            agreementDTO.setAgreementDate(LocalDate.parse(agreementDateField.getText()));
            agreementDTO.setAgreedAmount(Double.parseDouble(registeredAmountField.getText()));
            agreementDTO.setGoverningBoardMemberName(governingBoardMemberField.getText());
            agreementDTO.setSponsorContactName(sponsorNameField.getText());
            agreementDTO.setSponsorContactEmail(sponsorEmailField.getText());
            // Phone is not part of the DTO, but you can add it if needed

            boolean success = controller.registerAgreement(agreementDTO);

            if (success) {
                JOptionPane.showMessageDialog(this, "Agreement Registered Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed. Please check the agreed amount.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check the fields.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SponsorshipAgreementRegistrationView();
            }
        });
    }
}
