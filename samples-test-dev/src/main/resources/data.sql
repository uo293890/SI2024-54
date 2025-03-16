-- Insert sample events
INSERT INTO Event (event_title) 
VALUES
('ImpulsoTIC Week'),
('Hour of Code'),
('Tech Innovation Summit'),
('Cyber Security Forum'),
('AI & Data Science Conference'),
('Blockchain Expo'),
('Cloud Computing Summit'),
('Big Data Conference'),
('Startup Expo'),
('IoT World Congress'),
('Women in Tech Forum');


-- Insert sample editions with different statuses
INSERT INTO Edition (event_id, edition_title, edition_inidate, edition_enddate, edition_status, sponsorship_fee) 
VALUES 
(1, 'ImpulsoTIC Week 2023', '2023-11-10', '2023-11-15', 'Completed', 2000),
(1, 'ImpulsoTIC Week 2024', '2024-11-11', '2024-11-15', 'Ongoing', 2500),
(1, 'ImpulsoTIC Week 2025', '2025-11-10', '2025-11-14', 'Planned', 3000),
(2, 'Hour of Code 2023', '2023-12-01', '2023-12-07', 'Completed', 1500),
(2, 'Hour of Code 2024', '2024-12-01', '2024-12-07', 'Ongoing', 1800),
(2, 'Hour of Code 2025', '2025-12-08', '2025-12-19', 'Planned', 2000),
(3, 'Tech Innovation Summit 2023', '2023-09-10', '2023-09-12', 'Completed', 3000),
(3, 'Tech Innovation Summit 2024', '2024-09-15', '2024-09-17', 'Ongoing', 3500),
(3, 'Tech Innovation Summit 2025', '2025-09-20', '2025-09-22', 'Planned', 4000),
(4, 'Cyber Security Forum 2023', '2023-10-05', '2023-10-07', 'Completed', 2500),
(4, 'Cyber Security Forum 2024', '2024-10-06', '2024-10-08', 'Ongoing', 2800),
(4, 'Cyber Security Forum 2025', '2025-10-09', '2025-10-11', 'Planned', 3200);

-- Insert sample sponsors
INSERT INTO Sponsor (sponsor_name) 
VALUES 
('Caja Rural de Asturias'), 
('CCII'),
('TechCorp'),
('CyberSecure Inc'),
('AI Research Group'),
('Blockchain Global'),
('GlobalTech Solutions'), 
('SecureNet Corp'),
('CloudWare'),
('Data Analytics Inc'),
('Startup Accelerator');

-- Insert sample GB members
INSERT INTO GBMember (gbmember_name)
VALUES
('Dean'),
('Alexander'),
('Laura Johnson'),
('Michael Smith'),
('Sophia Williams'),
('Daniel Martinez'),
('Carlos Fernandez'),
('Emma Thompson'),
('John Doe'),
('Alice White'),
('Robert Wilson');

-- Insert sample agreements with different amounts and statuses
INSERT INTO Agreement (edition_id, sponsor_id, gbmember_id, contact_name, contact_number, contact_email, agreement_date, agreement_amount, agreement_status, early_invoice_request) 
VALUES 
(1, 1, 1, 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com', '2023-10-01', 2500, 'Paid', NULL),
(1, 2, 2, 'James Brown', '+34123456789', 'james.brown@ccii.com', '2023-10-05', 3000, 'Paid', NULL),
(1, 3, 3, 'Alice White', '+34981234567', 'alice.white@globaltech.com', '2023-10-10', 4000, 'Agreed', '2023-11-01'),
(1, 4, 4, 'Michael Scott', '+34965432109', 'michael.scott@securenet.com', '2023-09-20', 5000, 'Agreed', NULL),
(2, 1, 2, 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com', '2024-03-01', 3000, 'Paid', NULL),
(2, 3, 3, 'Alice White', '+34981234567', 'alice.white@globaltech.com', '2024-03-15', 2500, 'Agreed', '2024-04-01'),
(2, 4, 4, 'Michael Scott', '+34965432109', 'michael.scott@securenet.com', '2024-04-05', 4000, 'Agreed', NULL),
(2, 5, 5, 'Robert Wilson', '+34123412345', 'robert.wilson@techcorp.com', '2024-02-15', 5000, 'Agreed', '2024-03-01'),
(3, 2, 3, 'James Brown', '+34123456789', 'james.brown@ccii.com', '2024-05-15', 5000, 'Agreed', '2024-06-01'),
(3, 3, 4, 'Alice White', '+34981234567', 'alice.white@globaltech.com', '2024-06-20', 6000, 'Agreed', NULL),
(3, 4, 5, 'Michael Scott', '+34965432109', 'michael.scott@securenet.com', '2024-07-10', 4500, 'Paid', '2024-07-20'),
(3, 5, 6, 'Robert Wilson', '+34123412345', 'robert.wilson@techcorp.com', '2024-08-01', 7000, 'Agreed', '2024-08-15'),
(4, 1, 1, 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com', '2023-11-01', 2000, 'Paid', NULL),
(4, 2, 2, 'James Brown', '+34123456789', 'james.brown@ccii.com', '2023-11-10', 2500, 'Agreed', '2023-12-01'),
(4, 3, 3, 'Alice White', '+34981234567', 'alice.white@globaltech.com', '2023-11-15', 3000, 'Agreed', NULL),
(4, 4, 4, 'Michael Scott', '+34965432109', 'michael.scott@securenet.com', '2023-11-20', 4000, 'Paid', '2023-12-10'),
(5, 1, 2, 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com', '2024-05-01', 2200, 'Paid', NULL),
(5, 2, 3, 'James Brown', '+34123456789', 'james.brown@ccii.com', '2024-05-15', 2600, 'Agreed', '2024-06-01'),
(5, 3, 4, 'Alice White', '+34981234567', 'alice.white@globaltech.com', '2024-06-10', 3200, 'Agreed', NULL),
(5, 4, 5, 'Michael Scott', '+34965432109', 'michael.scott@securenet.com', '2024-07-01', 4500, 'Paid', '2024-07-10');



-- Insert sample invoices
INSERT INTO Invoice (agreement_id, invoice_date, invoice_number, invoice_vat, recipient_name, recipient_tax_id, recipient_address, sent_date) 
VALUES 
(1, '2023-11-16', '000000001', 21.0, 'Caja Rural de Asturias', 'A12345678', 'Av. Principal, 123, Oviedo', NULL),
(2, '2024-11-16', '000000002', 21.0, 'Caja Rural de Asturias', 'A12345678', 'Av. Principal, 123, Oviedo', NULL),
(3, '2024-07-01', '000000003', 21.0, 'AI Research Group', 'B98765432', 'Calle Innovación, 45, Madrid', NULL),
(4, '2023-09-15', '000000004', 21.0, 'GlobalTech Solutions', 'C12345678', 'Calle Empresa, 56, Madrid', '2023-09-16'),
(5, '2024-02-25', '000000005', 21.0, 'SecureNet Corp', 'D98765432', 'Paseo Segura, 89, Barcelona', '2024-02-26'),
(6, '2025-04-15', '000000006', 21.0, 'CloudWare', 'E76543210', 'Avenida Nube, 12, Valencia', NULL);


-- Insert sample payments
INSERT INTO Payment (invoice_id, payment_date, payment_amount, payment_type)
VALUES
(1, '2023-11-20', 2500, 'Standard'),
(2, '2024-11-20', 3000, 'Standard'),
(3, '2024-07-10', 5000, 'Standard'),
(4, '2023-09-20', 4500, 'Standard'),
(5, '2024-02-28', 5000, 'Standard'),
(6, '2025-04-20', 6000, 'Standard');


-- Insert sample other income/expenses
INSERT INTO Otherie (edition_id, otherie_amount, otherie_description, otherie_status) 
VALUES 
(1, 5000, 'Marketing Campaign', 'Paid'),
(1, 2000, 'Venue Rental', 'Paid'),
(2, 6000, 'Communication Services', 'Paid'),
(3, 1500, 'Equipment Rental', 'Pending'),
(4, 4000, 'Security Equipment Rental', 'Paid'),
(4, 1800, 'Cybersecurity Awareness Campaign', 'Paid'),
(5, 2500, 'Data Protection Services', 'Paid'),
(6, 3000, 'Cloud Hosting Setup', 'Pending');


-- Insert sample financial movements
INSERT INTO Movement (otherie_id, invoice_id, movement_date, movement_concept, movement_amount, payment_status, movement_notes) 
VALUES
(1, NULL, '2023-11-16', 'Marketing Expenses', 5000, 'Paid', 'Marketing campaign expenses for promotion'),
(2, NULL, '2023-11-16', 'Venue Rental Payment', 2000, 'Paid', 'Payment for renting the event space'),
(3, NULL, '2024-06-25', 'Equipment Rental Advance', 1500, 'Estimated', 'Advance payment for equipment'),
(4, 3, '2024-07-02', 'TechCorp Sponsorship Payment', 4000, 'Paid', 'Sponsorship received from TechCorp'),
(5, NULL, '2023-09-15', 'Security Equipment Rental', 4000, 'Paid', 'Alquiler de equipos de seguridad'),
(6, NULL, '2023-09-15', 'Cybersecurity Awareness Campaign', 1800, 'Paid', 'Campaña de concienciación sobre ciberseguridad'),
(7, NULL, '2024-02-20', 'Data Protection Services', 2500, 'Estimated', 'Servicios de protección de datos contratados'),
(8, 6, '2025-04-16', 'Cloud Hosting Setup Payment', 3000, 'Paid', 'Pago del setup de hosting en la nube');