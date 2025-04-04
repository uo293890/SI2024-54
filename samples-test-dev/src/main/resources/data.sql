INSERT INTO Type (type_name)
VALUES
 ('Week Event'),
 ('Educational');

-- Eventos (todos válidos ahora)
INSERT INTO Event (type_id, event_name, event_inidate, event_enddate, event_location, event_status)
VALUES
 (1, 'ImpulsoTIC Week 2024', '2024-11-11', '2024-11-15', 'Oviedo', 'Closed'),
 (1, 'ImpulsoTIC Week 2025', '2025-11-10', '2025-11-14', 'Oviedo', 'Planned'),
 (2, 'Hour of Code 2025', '2025-12-08', '2025-12-19', 'Gijon', 'Planned');

-- Sponsors
INSERT INTO Sponsor (sponsor_name)
VALUES
 ('Caja Rural de Asturias'),
 ('CCII'),
 ('TechCorp'),
 ('CyberSecure Inc');

-- Contactos (solo usando sponsor_id del 1 al 13)
INSERT INTO SpContact (sponsor_id, spcontact_name, spcontact_number, spcontact_email)
VALUES
 (1, 'Maria Perez', '+34987654321', 'maria.perez@cajarural.com'),
 (1, 'James Brown', '+34123456789', 'james.brown@cajarural.com'),
 (2, 'Alice White', '+34981234567', 'alice.white@ccii.com'),
 (3, 'Robert Wilson', '+34123412345', 'robert.wilson@techcorp.com'),
 (4, 'Michael Scott', '+34965432109', 'michael.scott@cybersecure.com');

-- Miembros del Consejo
INSERT INTO GbMember (gbmember_name, gbmember_email, gbmember_position)
VALUES
 ('Dean', 'dean@example.com', 'President'),
 ('Alexander', 'alexander@example.com', 'Treasurer'),
 ('Laura Johnson', 'laura.johnson@example.com', 'Board Member'),
 ('Michael Smith', 'michael.smith@example.com', 'Board Member'),
 ('Sophia Williams', 'sophia.williams@example.com', 'Board Member'),
 ('Daniel Martinez', 'daniel.martinez@example.com', 'Board Member'),
 ('Marina Díaz', 'marina.diaz@example.com', 'Board Member'),
 ('Carlos Ruiz', 'carlos.ruiz@example.com', 'Board Member');

-- Niveles de patrocinio (evento_id válido de 1 a 7)
INSERT INTO LevelOfSponsorship (event_id, level_name, level_minamount)
VALUES
 (1, 'Standart', 0),
 (2, 'Silver', 2000),
 (2, 'Gold', 4000),
 (3, 'Standart', 0);

-- Acuerdos válidos
INSERT INTO Agreement (level_id, gbmember_id, spcontact_id, agreement_date, agreement_amount, agreement_status)
VALUES
 (1, 1, 1, '2025-11-01', 3000, 'Paid'),
 (4, 2, 2, '2025-11-01', 750, 'Agreed');

-- Facturas
INSERT INTO Invoice (agreement_id, invoice_date, invoice_number, invoice_vat)
VALUES
 (1, '2025-11-02', '000000001', 21.0);

-- Ingresos y gastos
INSERT INTO IncomesExpenses (event_id, incexp_concept, incexp_amount, incexp_status)
VALUES
 (1, 'Communication Services', -6000, 'Paid'),
 (1, 'CCII Grant', 1000, 'Paid');

-- Movimientos
INSERT INTO Movement (incexp_id, invoice_id, movement_date, movement_concept, movement_amount)
VALUES
 (1, NULL, '2025-11-04', 'Communication Services', -6000),
 (2, NULL, '2025-11-04', 'CCII Grant', 1000);