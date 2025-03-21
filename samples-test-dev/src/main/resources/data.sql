-- Insert event types
INSERT INTO Type (type_name)
VALUES
('Week Event'),
('Educational'),
('Conference'),
('Forum'),
('Expo'),
('Summit');

-- Insert sample events (usando el type_id correcto según nombre)
INSERT INTO Event (type_id, event_name, event_inidate, event_enddate, event_location, event_status)
VALUES
(1, 'ImpulsoTIC Week', '2023-11-10', '2023-11-15', 'Oviedo', 'Closed'),
(2, 'Hour of Code', '2023-12-01', '2023-12-07', 'Gijón', 'Closed'),
(6, 'Tech Innovation Summit', '2023-09-10', '2023-09-12', 'Oviedo', 'Closed'),
(4, 'Cyber Security Forum', '2023-10-05', '2023-10-07', 'Avilés', 'Closed'),
(3, 'AI & Data Science Conference', '2024-05-01', '2024-05-05', 'Madrid', 'Planned'),
(5, 'Blockchain Expo', '2024-06-01', '2024-06-03', 'Barcelona', 'Planned');

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

-- Insert sponsor contacts
INSERT INTO SpContact (sponsor_id, spcontact_name, spcontact_number, spcontact_email)
VALUES
(1, 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com'),
(2, 'James Brown', '+34123456789', 'james.brown@ccii.com'),
(7, 'Alice White', '+34981234567', 'alice.white@globaltech.com'),
(8, 'Michael Scott', '+34965432109', 'michael.scott@securenet.com'),
(3, 'Robert Wilson', '+34123412345', 'robert.wilson@techcorp.com');

-- Insert GB members
INSERT INTO GbMember (gbmember_name, gbmember_email, gbmember_position)
VALUES
('Dean', 'dean@example.com', 'President'),
('Alexander', 'alexander@example.com', 'Treasurer'),
('Laura Johnson', 'laura.johnson@example.com', 'Board Member'),
('Michael Smith', 'michael.smith@example.com', 'Board Member'),
('Sophia Williams', 'sophia.williams@example.com', 'Board Member'),
('Daniel Martinez', 'daniel.martinez@example.com', 'Board Member');

-- Insert level of sponsorships
INSERT INTO LevelOfSponsorship (event_id, level_name, level_minamount, advantages)
VALUES
(1, 'Gold', 2000, 'Full visibility, booth, keynote slot'),
(1, 'Silver', 1500, 'Visibility, booth'),
(2, 'Gold', 2500, 'Top visibility and full package'),
(2, 'Bronze', 1000, 'Visibility only'),
(3, 'Platinum', 3000, 'All access and premium visibility'),
(4, 'Gold', 2500, 'Full access');

-- Insert agreements
INSERT INTO Agreement (level_id, gbmember_id, spcontact_id, agreement_date, agreement_amount, agreement_status)
VALUES
(1, 1, 1, '2023-10-01', 2500, 'Paid'),
(1, 2, 2, '2023-10-05', 3000, 'Paid'),
(1, 3, 3, '2023-10-10', 4000, 'Agreed'),
(1, 4, 4, '2023-09-20', 5000, 'Agreed'),
(3, 2, 1, '2024-03-01', 3000, 'Paid'),
(3, 3, 3, '2024-03-15', 2500, 'Agreed'),
(3, 4, 4, '2024-04-05', 4000, 'Agreed'),
(3, 5, 5, '2024-02-15', 5000, 'Agreed');

-- Insert invoices
INSERT INTO Invoice (agreement_id, invoice_date, invoice_number, invoice_vat)
VALUES
(1, '2023-11-16', '000000001', 21.0),
(2, '2024-11-16', '000000002', 21.0),
(3, '2024-07-01', '000000003', 21.0),
(4, '2023-09-15', '000000004', 21.0),
(5, '2024-02-25', '000000005', 21.0),
(6, '2025-04-15', '000000006', 21.0);

-- Insert income and expenses
INSERT INTO IncomesExpenses (event_id, incexp_concept, incexp_amount, incexp_status)
VALUES
(1, 'Marketing Campaign', 5000, 'Paid'),
(1, 'Venue Rental', 2000, 'Paid'),
(2, 'Communication Services', 6000, 'Paid'),
(3, 'Equipment Rental', 1500, 'Estimated'),
(4, 'Security Equipment Rental', 4000, 'Paid'),
(4, 'Cybersecurity Awareness Campaign', 1800, 'Paid'),
(5, 'Data Protection Services', 2500, 'Paid'),
(6, 'Cloud Hosting Setup', 3000, 'Estimated');

-- Insert movements
INSERT INTO Movement (incexp_id, invoice_id, movement_date, movement_concept, movement_amount)
VALUES
(1, NULL, '2023-11-16', 'Marketing Expenses', 5000),
(2, NULL, '2023-11-16', 'Venue Rental Payment', 2000),
(3, NULL, '2024-06-25', 'Equipment Rental Advance', 1500),
(NULL, 3, '2024-07-02', 'TechCorp Sponsorship Payment', 4000),
(5, NULL, '2023-09-15', 'Security Equipment Rental', 4000),
(6, NULL, '2023-09-15', 'Cybersecurity Awareness Campaign', 1800),
(7, NULL, '2024-02-20', 'Data Protection Services', 2500),
(8, 6, '2025-04-16', 'Cloud Hosting Setup Payment', 3000);
