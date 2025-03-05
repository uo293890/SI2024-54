-- Datos para la tabla Event
INSERT INTO Event (title) VALUES 
('Tech Conference 2025'),
('Health Summit 2025');

-- Datos para la tabla Edition
INSERT INTO Edition (event_id, title, date, end_date, status, place, planned_completed) VALUES 
(1, 'Tech Conference Spring Edition', '2025-03-15', '2025-03-20', 'Planned', 'New York', 1),
(1, 'Tech Conference Fall Edition', '2025-09-10', '2025-09-15', 'Planned', 'San Francisco', 0),
(2, 'Health Summit Annual Meeting', '2025-06-20', '2025-06-25', 'Completed', 'Los Angeles', 1);

-- Datos para la tabla EstimatedIncomeExpenses
INSERT INTO EstimatedIncomeExpenses (edition_id, amount, paid_cancelled, description) VALUES 
(1, 5000.00, 1, 'Venue booking for Spring Edition'),
(2, 3000.00, 0, 'Marketing expenses'),
(3, 7000.00, 1, 'Catering services');

-- Datos para la tabla Sponsor
INSERT INTO Sponsor (name, contact_name, contact_number, contact_email) VALUES 
('TechCorp', 'John Doe', '555-1234', 'john.doe@techcorp.com'),
('HealthPlus', 'Jane Smith', '555-5678', 'jane.smith@healthplus.com');

-- Datos para la tabla Agreement
INSERT INTO Agreement (edition_id, sponsor_id, level_amount, date, negotiator, paid_cancelled) VALUES 
(1, 1, 10000.00, '2025-01-15', 'Alice Manager', 1),
(2, 2, 8000.00, '2025-05-10', 'Bob Negotiator', 0);

-- Datos para la tabla Invoice
INSERT INTO Invoice (agreement_id, invoice_number, invoice_date, recipient_name, recipient_tax_id, recipient_address, base_amount, vat) 
VALUES 
(1, 'INV-001', '2025-02-01', 'TechCorp', 'A12345678', '123 Tech Street, New York', 10000.00, 21.0),
(2, 'INV-002', '2025-05-20', 'HealthPlus', 'B87654321', '456 Health Ave, San Francisco', 8000.00, 21.0);

-- Datos para la tabla Movements
INSERT INTO Movements (invoice_id, date, amount, concept) VALUES 
(1, '2025-02-10', 5000.00, 'Initial payment'),
(1, '2025-03-01', 5000.00, 'Final settlement'),
(2, '2025-06-01', 4000.00, 'First installment');