-- Datos para la tabla Event
INSERT INTO Event (title) VALUES 
('Tech Conference 2025'),
('Health Summit 2025');

-- Datos para la tabla Edition
INSERT INTO Edition (event_id, title, date, place, planned_completed) VALUES 
(1, 'Tech Conference Spring Edition', '2025-03-15', 'New York', TRUE),
(1, 'Tech Conference Fall Edition', '2025-09-10', 'San Francisco', FALSE),
(2, 'Health Summit Annual Meeting', '2025-06-20', 'Los Angeles', TRUE);

-- Datos para la tabla EstimatedIncomeExpenses
INSERT INTO EstimatedIncomeExpenses (edition_id, amount, paid_cancelled, description) VALUES 
(1, 5000.00, TRUE, 'Venue booking for Spring Edition'),
(2, 3000.00, FALSE, 'Marketing expenses'),
(3, 7000.00, TRUE, 'Catering services');

-- Datos para la tabla Sponsor
INSERT INTO Sponsor (name, contact_name, contact_number, contact_email) VALUES 
('TechCorp', 'John Doe', '555-1234', 'john.doe@techcorp.com'),
('HealthPlus', 'Jane Smith', '555-5678', 'jane.smith@healthplus.com');

-- Datos para la tabla Agreement
INSERT INTO Agreement (edition_id, sponsor_id, level_amount, date, negotiator, paid_cancelled) VALUES 
(1, 1, 10000.00, '2025-01-15', 'Alice Manager', TRUE),
(2, 2, 8000.00, '2025-05-10', 'Bob Negotiator', FALSE);

-- Datos para la tabla Invoice
INSERT INTO Invoice (agreement_id, date, vat) VALUES 
(1, '2025-02-01', 21.00),
(2, '2025-05-20', 18.00);

-- Datos para la tabla Movements
INSERT INTO Movements (invoice_id, date, amount, concept) VALUES 
(1, '2025-02-10', 5000.00, 'Initial payment'),
(1, '2025-03-01', 5000.00, 'Final settlement'),
(2, '2025-06-01', 4000.00, 'First installment');