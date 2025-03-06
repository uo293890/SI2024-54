-- data.sql
-- Sample data for Event table
INSERT INTO Event (title, event_date, location, status, description, created_date) VALUES
('Tech Conference', '2024-05-01', 'New York', 'Planned', 'Annual tech conference', '2023-10-01'),
('Cybersecurity Forum', '2025-06-01', 'San Francisco', 'Planned', 'Cybersecurity event', '2023-10-01');

-- Sample data for GBMember table
INSERT INTO GBMember (name, role, email, phone) VALUES
('Jane Smith', 'Dean', 'jane.smith@example.com', '123-456-7890'),
('Bob Brown', 'Treasurer', 'bob.brown@example.com', '987-654-3210');

-- Sample data for Sponsor table
INSERT INTO Sponsor (sponsor_name, tax_id, billing_address, contact_name, contact_email, contact_phone) VALUES
('Company A', '12345', '123 Main St, New York', 'John Doe', 'john.doe@companyA.com', '111-222-3333'),
('Company B', '67890', '456 Elm St, San Francisco', 'Alice Johnson', 'alice.johnson@companyB.com', '444-555-6666');

-- Sample data for Sponsorship table
INSERT INTO Sponsorship (event_id, sponsor_id, gb_member_id, agreement_date, sponsorship_level, agreed_amount, sponsorship_status) VALUES
(1, 1, 1, '2024-01-15', 'Gold', 5000.00, 'Active'),
(2, 2, 2, '2024-03-05', 'Silver', 7000.00, 'Active');

-- Sample data for Invoice table
INSERT INTO Invoice (sponsorship_id, invoice_date, invoice_number, recipient_name, recipient_tax_id, recipient_address, base_amount, vat, invoice_amount, sent_date) VALUES
(1, '2024-01-20', 'INV001', 'Company A', '12345', '123 Main St, New York', 5000.00, 1000.00, 6000.00, '2024-01-21'),
(2, '2024-03-10', 'INV002', 'Company B', '67890', '456 Elm St, San Francisco', 7000.00, 1400.00, 8400.00, '2024-03-11');

-- Sample data for Transactions table
INSERT INTO Transactions (event_id, invoice_id, transaction_type, status, record_date, payment_date, concept, details, amount) VALUES
(1, 1, 'Income', 'Paid', '2024-01-22', '2024-01-22', 'Sponsorship Payment', 'Payment from Company A', 6000.00),
(2, 2, 'Income', 'Paid', '2024-03-12', '2024-03-12', 'Sponsorship Payment', 'Payment from Company B', 8400.00);