INSERT INTO Event (event_title) 
VALUES
('ImpulsoTIC Week'),
('Hour of Code');

INSERT INTO Edition (event_id, edition_title, edition_inidate, edition_enddate, edition_status) 
VALUES 
(1, 'ImpulsoTIC Week 2024', '2024-11-11', '2024-11-15', 'Closed'),
(1, 'ImpulsoTIC Week 2025', '2025-11-10', '2025-11-14', 'Planned'),
(2, 'Hour of Code 2025', '2025-12-08', '2025-12-19', 'Planned');

INSERT INTO Sponsor (sponsor_name) 
VALUES 
('Caja Rural de Asturias'), 
('CCII');

INSERT INTO Agreement (edition_id, sponsor_id, negotiator, contact_worker, contact_number, contact_email, agreement_date, agreement_amount, agreement_status) 
VALUES 
(1, 1, 'John Doe', 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com', '2024-10-01', 3000, 'Paid'),
(3, 1, 'John Doe', 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com', '2025-03-01', 750, 'Agreed');

INSERT INTO Invoice (agreement_id, invoice_date, invoice_number, invoice_vat) 
VALUES (1, '2024-11-16', '000000001', 21.0);

INSERT INTO Otherie (edition_id, otherie_amount, otherie_description, otherie_status) 
VALUES 
(1, 6000, 'Communication services', 'Paid'),
(1, 1000, 'CCII Grant', 'Paid');

INSERT INTO Movement (otherie_id, invoice_id, movement_date, movement_concept, movement_amount) 
VALUES
(1, NULL, '2024-11-16', 'Payment for communication services', 6000),
(2, NULL, '2024-11-16', 'Grant received from CCII', 1000);
