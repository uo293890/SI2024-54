package giis.demo.tkrun;

import java.util.List;

public class RegisterSponsorDTO {
    private int sponsorId;
    private String sponsorName;
    private List<SponsorContactDTO> contacts;

    // Getters and Setters
    public int getSponsorId() { return sponsorId; }
    public void setSponsorId(int sponsorId) { this.sponsorId = sponsorId; }
    
    public String getSponsorName() { return sponsorName; }
    public void setSponsorName(String sponsorName) { this.sponsorName = sponsorName; }
    
    public List<SponsorContactDTO> getContacts() { return contacts; }
    public void setContacts(List<SponsorContactDTO> contacts) { 
        this.contacts = contacts; 
    }

    // Nested DTO for sponsor contacts
    public static class SponsorContactDTO {
        private String contactName;
        private String phoneNumber;
        private String email;

        // Getters and Setters
        public String getContactName() { return contactName; }
        public void setContactName(String contactName) { this.contactName = contactName; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}