INSERT INTO Event (event_title)
VALUES
('Informatics Olympics'),
('ImpulsoTIC Week'),
('Hour of Code');


INSERT INTO Edition (event_id, edition_title, edition_inidate, edition_enddate, edition_location)
VALUES
(1, 'Apr25', '2025-01-01', '2025-01-02', 'Asturias'),
(1, 'Jul25', NULL, NULL, 'Madrid'),
(2, 'May25', NULL, NULL, 'Asturias'),
(2, 'Aug25', NULL, NULL, 'Madrid'),
(3, 'Jun25', NULL, NULL, 'Asturias'),
(3, 'Sep25', NULL, NULL, 'Madrid');


INSERT INTO Sponsor (sponsor_name)
VALUES
('Uniovi'),
('HP'),
('Linux');


INSERT INTO Agreement (edition_id, sponsor_id, negotiator, contact_worker, contact_number, contact_email, agreement_date, agreement_amount)
VALUES 
(1, 1, 'Rosa Garcia', 'Fanjul', '+34 661661661', 'jgfanjul@uniovi.es', '2025-01-01', 3000),
(2, 2, 'Rosa Garcia', 'Jose Antonio', '+34 662662662', 'jantonio@gmail.com', '2025-01-01', 3000),
(3, 3, 'Rosa Garcia', 'Manuel Garcia', '+34 663663663', 'mgarcia@hotmail.com', '2025-01-01', 3000),
(4, 1, 'Rosa Garcia', 'Fanjul', '+34 661661661', 'jgfanjul@uniovi.es', '2025-01-01', 3000),
(5, 2, 'Rosa Garcia', 'Jose Antonio', '+34 662662662', 'jantonio@gmail.com', '2025-01-01', 3000),
(6, 3, 'Rosa Garcia', 'Manuel Garcia', '+34 663663663', 'mgarcia@hotmail.com', '2025-01-01', 3000);



INSERT INTO Invoice (agreement_id, invoice_date, invoice_number, invoice_vat)
VALUES
(1, '2025-03-03', '123456789', 0),
(2, '2025-03-03', '012345678', 0),
(3, '2025-03-03', '001234567', 0),
(4, '2025-03-03', '000123456', 0),
(5, '2025-03-03', '000012345', 0),
(6, '2025-03-03', '000001234', 0);


INSERT INTO Otherie (edition_id, otherie_amount, otherie_description)
VALUES
(1, -1500, 'Room rent fee at Gijon TownHall'), 
(2, -2500, 'Room rent fee at Madrid TownHall'), 
(3, -1500, 'Award'), 
(4, -2500, 'Award'), 
(5, -1500, 'Room rent fee at Gijon TownHall'),
(6, -2500, 'Room rent fee at Madrid TownHall');


INSERT INTO Movement (otherie_id, invoice_id, movement_date, movement_concept, movement_amount)
VALUES
(1, NULL, '2025-03-02', 'Fee payment for Gijon townhall room', -1500),
(3, NULL, '2025-03-02', 'TownHall award', 6000),
(NULL, 1, '2025-03-04', 'Sponsorship payment for invoice 123456789', 3000),
(NULL, 2, '2025-03-04', 'Sponsorship payment for invoice 012345678', 3000);
