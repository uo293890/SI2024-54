-- Insertar sponsors
INSERT INTO Sponsor (name) 
VALUES 
    ('Sponsor A'), 
    ('Sponsor B');

-- Insertar acuerdos
INSERT INTO Agreement (sponsor_id, kind, date) 
VALUES 
    (1, 'Gold', '2024-01-01'), 
    (2, 'Silver', '2024-02-01');

-- Insertar eventos
INSERT INTO Event (agreement_id, title, price, amount) 
VALUES 
    (1, 'Event 1', 100.0, 10), 
    (2, 'Event 2', 200.0, 20);

-- Insertar fechas
INSERT INTO Date (event_id, initial, final) 
VALUES 
    (1, '2024-03-01', '2024-03-10'), 
    (2, '2024-04-01', '2024-04-10');

-- Insertar facturas
INSERT INTO Invoice (agreement_id, amount, vat_percentage, profit_or_nonprofit) 
VALUES 
    (1, 1000.0, 21.0, 'NonProfit'), 
    (2, 2000.0, 21.0, 'Profit');

-- Insertar pagos
INSERT INTO Payment (invoice_id, date, amount) 
VALUES 
    (1, '2024-03-01', 1000.0), 
    (2, '2024-04-01', 2000.0);
