-- Insert sample events
INSERT INTO Event (event_title) 
VALUES
('ImpulsoTIC Week'),
('Hour of Code'),
('Tech Innovation Summit'),
('Cyber Security Forum'),
('AI & Data Science Conference'),
('Blockchain Expo');

-- Insert sample editions with different statuses
INSERT INTO Edition (event_id, edition_title, edition_inidate, edition_enddate, edition_status) 
VALUES 
(1, 'ImpulsoTIC Week 2023', '2023-11-10', '2023-11-15', 'Completed'),
(1, 'ImpulsoTIC Week 2024', '2024-11-11', '2024-11-15', 'Ongoing'),
(1, 'ImpulsoTIC Week 2025', '2025-11-10', '2025-11-14', 'Planned'),
(2, 'Hour of Code 2023', '2023-12-01', '2023-12-07', 'Completed'),
(2, 'Hour of Code 2024', '2024-12-01', '2024-12-07', 'Ongoing'),
(2, 'Hour of Code 2025', '2025-12-08', '2025-12-19', 'Planned'),
(3, 'Tech Innovation Summit 2023', '2023-06-15', '2023-06-17', 'Completed'),
(3, 'Tech Innovation Summit 2024', '2024-06-20', '2024-06-22', 'Ongoing'),
(3, 'Tech Innovation Summit 2025', '2025-06-18', '2025-06-20', 'Planned'),
(4, 'Cyber Security Forum 2023', '2023-09-10', '2023-09-12', 'Completed'),
(4, 'Cyber Security Forum 2024', '2024-09-10', '2024-09-12', 'Ongoing'),
(4, 'Cyber Security Forum 2025', '2025-09-08', '2025-09-10', 'Planned'),
(5, 'AI & Data Science Conference 2023', '2023-04-05', '2023-04-07', 'Completed'),
(5, 'AI & Data Science Conference 2024', '2024-04-10', '2024-04-12', 'Ongoing'),
(5, 'AI & Data Science Conference 2025', '2025-04-15', '2025-04-17', 'Planned');

-- Insert sample sponsors
INSERT INTO Sponsor (sponsor_name) 
VALUES 
('Caja Rural de Asturias'), 
('CCII'),
('TechCorp'),
('CyberSecure Inc'),
('AI Research Group'),
('Blockchain Global');

-- Insert sample GB members
INSERT INTO GBMember (gbmember_id, gbmember_name)
VALUES
(1, 'Dean'),
(2, 'Alexander'),
(3, 'Laura Johnson'),
(4, 'Michael Smith'),
(5, 'Sophia Williams'),
(6, 'Daniel Martinez');

-- Insert sample agreements with different amounts and statuses
INSERT INTO Agreement (edition_id, sponsor_id, gbmember_id, contact_name, contact_number, contact_email, agreement_date, agreement_amount, agreement_status, early_invoice_request) 
VALUES 
(1, 1, 1, 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com', '2023-10-01', 2500, 'Paid', NULL),
(2, 1, 2, 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com', '2024-03-01', 3000, 'Paid', NULL),
(3, 2, 3, 'James Brown', '+34123456789', 'james.brown@ccii.com', '2024-05-15', 5000, 'Agreed', '2024-06-01'),
(4, 3, 4, 'Emma Watson', '+34765432198', 'emma.watson@techcorp.com', '2024-08-12', 4000, 'Agreed', '2024-09-01'),
(5, 4, 5, 'John Doe', '+34987654329', 'john.doe@cybersecure.com', '2023-02-10', 3500, 'Paid', NULL),
(6, 5, 6, 'Sophia Williams', '+34981234567', 'sophia.williams@airesearch.com', '2025-02-15', 6000, 'Agreed', '2025-03-01');

-- Insert sample invoices
INSERT INTO Invoice (agreement_id, invoice_date, invoice_number, invoice_vat, recipient_name, recipient_tax_id, recipient_address, sent_date) 
VALUES 
(1, '2023-11-16', '000000001', 21.0, 'Caja Rural de Asturias', 'A12345678', 'Av. Principal, 123, Oviedo', NULL),
(2, '2024-11-16', '000000002', 21.0, 'Caja Rural de Asturias', 'A12345678', 'Av. Principal, 123, Oviedo', NULL),
(3, '2024-07-01', '000000003', 21.0, 'AI Research Group', 'B98765432', 'Calle Innovación, 45, Madrid', NULL),
(4, '2024-09-05', '000000004', 21.0, 'TechCorp', 'C45678901', 'Paseo Tecnológico, 78, Barcelona', NULL),
(5, '2023-05-02', '000000005', 21.0, 'CyberSecure Inc', 'D11223344', 'Cyber Street, 22, Valencia', NULL),
(6, '2025-04-10', '000000006', 21.0, 'Blockchain Global', 'E55667788', 'Crypto Avenue, 99, Sevilla', NULL);

-- Insert sample other income/expenses
INSERT INTO Otherie (edition_id, otherie_amount, otherie_description, otherie_status) 
VALUES 
(1, 5000, 'Marketing Campaign', 'Paid'),
(1, 2000, 'Venue Rental', 'Paid'),
(2, 6000, 'Communication Services', 'Paid'),
(3, 1500, 'Equipment Rental', 'Pending'),
(5, 2500, 'Security Measures', 'Agreed'),
(6, 3000, 'AI Research Sponsorship', 'Paid'),
(7, 4000, 'Blockchain Development', 'Agreed'),
(8, 4500, 'Cybersecurity Infrastructure', 'Paid');

-- Insert sample financial movements
INSERT INTO Movement (otherie_id, invoice_id, movement_date, movement_concept, movement_amount) 
VALUES
(1, NULL, '2023-11-16', 'Marketing Expenses', 5000),
(2, NULL, '2023-11-16', 'Venue Rental Payment', 2000),
(3, NULL, '2024-06-25', 'Equipment Rental Advance', 1500),
(4, 3, '2024-07-02', 'TechCorp Sponsorship Payment', 4000),
(5, NULL, '2024-09-08', 'Security Equipment Purchase', 2500),
(6, 5, '2023-05-05', 'CyberSecure Security Investment', 3500),
(7, NULL, '2025-04-15', 'Blockchain Fund Allocation', 4000),
(8, NULL, '2024-09-12', 'Cybersecurity Enhancement Payment', 4500);
