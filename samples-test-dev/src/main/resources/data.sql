-- Types (si aún no están)
INSERT INTO Type (type_id, type_name) VALUES
(10, 'Hackathon');

-- Event
INSERT INTO Event (event_id, type_id, event_name, event_inidate, event_enddate, event_location, event_status) VALUES
(10, 10, 'Asturias Tech Hackathon', '2025-05-25', '2025-05-27', 'Oviedo', 'Planned');

-- Sponsors
INSERT INTO Sponsor (sponsor_id, sponsor_name) VALUES
(20, 'TechNova'),
(21, 'FutureMind');

-- Contacts
INSERT INTO SpContact (spcontact_id, sponsor_id, spcontact_name, spcontact_number, spcontact_email) VALUES
(20, 20, 'Lucía Torres', '+34611112222', 'lucia.torres@technova.com'),
(21, 21, 'Jorge Rivera', '+34622223333', 'jorge.rivera@futuremind.ai');

-- GB Members
INSERT INTO GbMember (gbmember_id, gbmember_name, gbmember_email, gbmember_position) VALUES
(10, 'Marina Díaz', 'marina.diaz@example.com', 'Board Member'),
(11, 'Carlos Ruiz', 'carlos.ruiz@example.com', 'Board Member');

-- Sponsorship levels
INSERT INTO LevelOfSponsorship (level_id, event_id, level_name, level_minamount, advantages) VALUES
(20, 10, 'Gold', 4000, 'Stand + branding'),
(21, 10, 'Silver', 2500, 'Stand only');

-- Agreements visibles
INSERT INTO Agreement (agreement_id, level_id, gbmember_id, spcontact_id, agreement_date, agreement_amount, agreement_status) VALUES
(20, 20, 10, 20, '2025-03-20', 4200, 'Agreed'),
(21, 21, 11, 21, '2025-03-21', 2600, 'Agreed');
