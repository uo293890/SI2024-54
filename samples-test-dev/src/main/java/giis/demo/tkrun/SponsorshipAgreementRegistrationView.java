package giis.demo.tkrun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class SponsorshipAgreementRegistrationView extends JFrame {
    private JComboBox<String> eventComboBox;
    private JComboBox<String> sponsorComboBox;
    private JComboBox<String> gbMemberComboBox;
    private JTextField agreementDateField;
    private JTextField sponsorshipLevelField;
    private JTextField agreedAmountField;
    private JButton registerButton;

    private SponsorshipAgreementRegistrationController controller;

    public SponsorshipAgreementRegistrationView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sponsorship Agreement Registration");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2));

        // Event Selection
        add(new JLabel("Event:"));
        eventComboBox = new JComboBox<>();
        add(eventComboBox);

        // Sponsor Selection
        add(new JLabel("Sponsor:"));
        sponsorComboBox = new JComboBox<>();
        add(sponsorComboBox);

        // Governing Board Member Selection
        add(new JLabel("Governing Board Member:"));
        gbMemberComboBox = new JComboBox<>();
        add(gbMemberComboBox);

        // Agreement Date
        add(new JLabel("Agreement Date (YYYY-MM-DD):"));
        agreementDateField = new JTextField();
        add(agreementDateField);

        // Sponsorship Level
        add(new JLabel("Sponsorship Level:"));
        sponsorshipLevelField = new JTextField();
        add(sponsorshipLevelField);

        // Agreed Amount
        add(new JLabel("Agreed Amount:"));
        agreedAmountField = new JTextField();
        add(agreedAmountField);

        // Register Button
        registerButton = new JButton("Register Agreement");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerAgreement();
            }
        });
        add(registerButton);

        // Initialize Controller
        controller = new SponsorshipAgreementRegistrationController(
            new SponsorshipAgreementRegistrationModel(), this);

        // Load Data
        loadEvents();
        loadSponsors();
        loadGBMembers();
    }

    private void loadEvents() {
        List<EventDTO> events = controller.getAllEvents();
        for (EventDTO event : events) {
            eventComboBox.addItem(event.getTitle());
        }
    }

    private void loadSponsors() {
        List<SponsorDTO> sponsors = controller.getAllSponsors();
        for (SponsorDTO sponsor : sponsors) {
            sponsorComboBox.addItem(sponsor.getSponsorName());
        }
    }

    private void loadGBMembers() {
        List<GBMemberDTO> members = controller.getAllGBMembers();
        for (GBMemberDTO member : members) {
            gbMemberComboBox.addItem(member.getName());
        }
    }

    private void registerAgreement() {
        SponsorshipAgreementRegistrationDTO dto = new SponsorshipAgreementRegistrationDTO();
        dto.setEventId(eventComboBox.getSelectedIndex() + 1); // Assuming IDs start at 1
        dto.setSponsorId(sponsorComboBox.getSelectedIndex() + 1);
        dto.setGbMemberId(gbMemberComboBox.getSelectedIndex() + 1);
        dto.setAgreementDate(LocalDate.parse(agreementDateField.getText()));
        dto.setSponsorshipLevel(sponsorshipLevelField.getText());
        dto.setAgreedAmount(Double.parseDouble(agreedAmountField.getText()));
        dto.setSponsorshipStatus("Active");

        controller.registerAgreement(dto);
        JOptionPane.showMessageDialog(this, "Sponsorship Agreement Registered Successfully!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SponsorshipAgreementRegistrationView view = new SponsorshipAgreementRegistrationView();
            view.setVisible(true);
        });
    }
}