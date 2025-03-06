-- Eventos
-- Datos para la tabla Event
INSERT INTO Event (event_title) VALUES  -- Correcto
    ('Tech Summit 2024'),
    ('Global Marketing Forum');

-- Datos para la tabla Edition
INSERT INTO Edition (event_id, edition_title, edition_inidate, edition_enddate) VALUES 
    (1, 'Edición Primavera', '2024-03-01', '2024-03-05'),
    (2, 'Edición Otoño', '2024-10-10', '2024-10-15');

-- Patrocinadores
INSERT OR IGNORE INTO Sponsor (sponsor_name) VALUES 
    ('TechCorp'),
    ('EduSupport');

-- Acuerdos
INSERT OR IGNORE INTO Agreement (edition_id, sponsor_id, negotiator, contact_worker, 
                                 contact_number, contact_email, agreement_date, 
                                 agreement_amount) 
VALUES 
    (1, 1, 'Luis García', 'Juan Pérez', '+5551234', 'jperez@techcorp.com', '2025-04-01', 1000.00),
    (1, 2, 'María López', 'Ana Gómez', '+5555678', 'agomez@edusupport.com', '2025-04-05', 1500.00);

-- Facturas (con invoice_number válido)
INSERT OR IGNORE INTO Invoice (agreement_id, invoice_date, invoice_number, invoice_vat) 
VALUES 
    (1, '2025-04-10', '123456789', 160.00),
    (2, '2025-05-05', '987654321', 240.00);

-- Otros Ingresos/Egresos (Otherie)
INSERT OR IGNORE INTO Otherie (edition_id, otherie_amount, otherie_description, otherie_status) VALUES
    (1, 1000.00, 'Ingreso Estimado', 'Estimated'),
    (1, 500.00, 'Gasto Pagado', 'Paid'),
    (1, 2000.00, 'Ingreso Pagado', 'Paid'),
    (1, 300.00, 'Gasto Estimado', 'Estimated');

-- Movimientos Financieros
INSERT OR IGNORE INTO Movement (otherie_id, invoice_id, movement_date, movement_concept, movement_amount) VALUES 
    (1, NULL, '2025-04-01', 'Ingreso Estimado', 1000.00),
    (2, NULL, '2025-04-05', 'Gasto Pagado', 500.00),
    (3, NULL, '2025-04-10', 'Ingreso Pagado', 2000.00),
    (4, NULL, NULL, 'Gasto Estimado', 300.00);

-- CONSULTAS PARA VERIFICAR LOS DATOS INSERTADOS
SELECT * FROM Event;
SELECT * FROM Edition;
SELECT * FROM Sponsor;
SELECT * FROM Agreement;
SELECT * FROM Invoice;
SELECT * FROM Otherie;
SELECT * FROM Movement;