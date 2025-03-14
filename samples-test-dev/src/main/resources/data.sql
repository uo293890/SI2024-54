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
INSERT INTO Edition (event_id, edition_title, edition_inidate, edition_enddate, edition_status, sponsorship_fee) 
VALUES 
(1, 'ImpulsoTIC Week 2023', '2023-11-10', '2023-11-15', 'Completed', 2000),
(1, 'ImpulsoTIC Week 2024', '2024-11-11', '2024-11-15', 'Ongoing', 2500),
(1, 'ImpulsoTIC Week 2025', '2025-11-10', '2025-11-14', 'Planned', 3000),
(2, 'Hour of Code 2023', '2023-12-01', '2023-12-07', 'Completed', 1500),
(2, 'Hour of Code 2024', '2024-12-01', '2024-12-07', 'Ongoing', 1800),
(2, 'Hour of Code 2025', '2025-12-08', '2025-12-19', 'Planned', 2000);

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
INSERT INTO GBMember (gbmember_name)
VALUES
('Dean'),
('Alexander'),
('Laura Johnson'),
('Michael Smith'),
('Sophia Williams'),
('Daniel Martinez');

-- Insert sample agreements with different amounts and statuses
INSERT INTO Agreement (edition_id, sponsor_id, gbmember_id, contact_name, contact_number, contact_email, agreement_date, agreement_amount, agreement_status, early_invoice_request) 
VALUES 
(1, 1, 1, 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com', '2023-10-01', 2500, 'Paid', NULL),
(2, 1, 2, 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com', '2024-03-01', 3000, 'Paid', NULL),
(3, 2, 3, 'James Brown', '+34123456789', 'james.brown@ccii.com', '2024-05-15', 5000, 'Agreed', '2024-06-01');

-- Insert sample invoices
INSERT INTO Invoice (agreement_id, invoice_date, invoice_number, invoice_vat, recipient_name, recipient_tax_id, recipient_address, sent_date) 
VALUES 
(1, '2023-11-16', '000000001', 21.0, 'Caja Rural de Asturias', 'A12345678', 'Av. Principal, 123, Oviedo', NULL),
(2, '2024-11-16', '000000002', 21.0, 'Caja Rural de Asturias', 'A12345678', 'Av. Principal, 123, Oviedo', NULL),
(3, '2024-07-01', '000000003', 21.0, 'AI Research Group', 'B98765432', 'Calle Innovación, 45, Madrid', NULL);

-- Insert sample payments
INSERT INTO Payment (invoice_id, payment_date, payment_amount, payment_type)
VALUES
(1, '2023-11-20', 2500, 'Standard'),
(2, '2024-11-20', 3000, 'Standard'),
(3, '2024-07-10', 5000, 'Standard');

-- Insert sample other income/expenses
INSERT INTO Otherie (edition_id, otherie_amount, otherie_description, otherie_status) 
VALUES 
(1, 5000, 'Marketing Campaign', 'Paid'),
(1, 2000, 'Venue Rental', 'Paid'),
(2, 6000, 'Communication Services', 'Paid'),
(3, 1500, 'Equipment Rental', 'Pending');

-- Insert sample financial movements
INSERT INTO Movement (otherie_id, invoice_id, movement_date, movement_concept, movement_amount, payment_status, movement_notes) 
VALUES
(1, NULL, '2023-11-16', 'Marketing Expenses', 5000, 'Paid', 'Marketing campaign expenses for promotion'),
(2, NULL, '2023-11-16', 'Venue Rental Payment', 2000, 'Paid', 'Payment for renting the event space'),
(3, NULL, '2024-06-25', 'Equipment Rental Advance', 1500, 'Estimated', 'Advance payment for equipment'),
(4, 3, '2024-07-02', 'TechCorp Sponsorship Payment', 4000, 'Paid', 'Sponsorship received from TechCorp');
