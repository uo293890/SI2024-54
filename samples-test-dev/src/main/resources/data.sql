-- Insert sample data (only if the tables are empty)
INSERT OR IGNORE INTO Event (title, event_date, location, status, description, created_date)
VALUES ('Annual Tech Conference', '2025-09-15', 'Conference Hall A', 'Planned', 'A full-day tech conference.', '2025-03-06');

INSERT OR IGNORE INTO GBMember (name, role, email, phone)
VALUES ('Alice Johnson', 'Dean', 'alice@university.edu', '+1234567890');

INSERT OR IGNORE INTO Sponsor (sponsor_name, tax_id, billing_address, contact_name, contact_email, contact_phone)
VALUES ('TechCorp', 'B12345678', '123 Tech Street, City', 'John Doe', 'johndoe@techcorp.com', '+123456789');

INSERT OR IGNORE INTO Sponsorship (event_id, sponsor_id, gb_member_id, agreement_date, sponsorship_level, agreed_amount, sponsorship_status)
VALUES (1, 1, 1, '2025-03-05', 'Gold', 5000.00, 'Active');

INSERT OR IGNORE INTO Invoice (sponsorship_id, agreement_id, invoice_date, invoice_number, recipient_name, recipient_tax_id, recipient_address, base_amount, vat, invoice_amount, sent_date)
VALUES (1, 1, '2025-03-06', 'INV-2025-001', 'TechCorp', 'B12345678', '123 Tech Street, City', 5000.00, 21.00, 6050.00, '2025-03-06');

INSERT OR IGNORE INTO Transactions (event_id, invoice_id, transaction_type, status, record_date, payment_date, concept, details, amount)
VALUES (1, 1, 'Income', 'Paid', '2025-03-06', '2025-03-07', 'Sponsorship Payment', 'Full payment received for Gold sponsorship', 5000.00);