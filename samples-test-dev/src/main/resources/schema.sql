DROP TABLE IF EXISTS Movement;
DROP TABLE IF EXISTS IncomesExpenses;
DROP TABLE IF EXISTS Invoice;
DROP TABLE IF EXISTS GbMember;
DROP TABLE IF EXISTS Agreement;
DROP TABLE IF EXISTS LevelOfSponsorship;
DROP TABLE IF EXISTS SpContact;
DROP TABLE IF EXISTS Sponsor;
DROP TABLE IF EXISTS Event;
DROP TABLE IF EXISTS Type;

CREATE TABLE IF NOT EXISTS Type (
    type_id INTEGER PRIMARY KEY AUTOINCREMENT,
    type_name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Event (
    event_id INTEGER PRIMARY KEY AUTOINCREMENT,
    type_id INTEGER,
    event_name TEXT NOT NULL,
    event_inidate DATE,
    event_enddate DATE,
    event_location TEXT,
    event_status TEXT NOT NULL CHECK(event_status IN ('Planned', 'Closed')) DEFAULT 'Planned',
    FOREIGN KEY (type_id) REFERENCES Type(type_id)
);

CREATE TABLE IF NOT EXISTS Sponsor (
    sponsor_id INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsor_name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS SpContact (
    spcontact_id INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsor_id INTEGER NOT NULL,
    spcontact_name TEXT NOT NULL,
    spcontact_number TEXT NOT NULL CHECK(spcontact_number LIKE '+%'),
    spcontact_email TEXT NOT NULL CHECK(spcontact_email LIKE '%@%'),
    FOREIGN KEY (sponsor_id) REFERENCES Sponsor(sponsor_id)
);

CREATE TABLE IF NOT EXISTS LevelOfSponsorship (
    level_id INTEGER PRIMARY KEY AUTOINCREMENT,
    event_id INTEGER NOT NULL,
    level_name TEXT NOT NULL,
    level_minamount INTEGER NOT NULL,
    FOREIGN KEY (event_id) REFERENCES Event(event_id)
);

CREATE TABLE IF NOT EXISTS GbMember (
    gbmember_id INTEGER PRIMARY KEY AUTOINCREMENT,
    gbmember_name TEXT NOT NULL,
    gbmember_email TEXT NOT NULL,
    gbmember_position TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Agreement (
    agreement_id INTEGER PRIMARY KEY AUTOINCREMENT,
    level_id INTEGER NOT NULL,
    gbmember_id INTEGER NOT NULL,
    spcontact_id INTEGER NOT NULL,
    agreement_date DATE NOT NULL,
    agreement_amount DOUBLE NOT NULL CHECK (agreement_amount >= 0),
    agreement_status TEXT NOT NULL CHECK(agreement_status IN ('Agreed', 'Paid', 'Closed')) DEFAULT 'Agreed',
    FOREIGN KEY (level_id) REFERENCES LevelOfSponsorship(level_id),
    FOREIGN KEY (gbmember_id) REFERENCES GbMember(gbmember_id),
    FOREIGN KEY (spcontact_id) REFERENCES SpContact(spcontact_id)
);

CREATE TABLE IF NOT EXISTS Invoice (
    invoice_id INTEGER PRIMARY KEY AUTOINCREMENT,
    agreement_id INTEGER,
    invoice_date DATE,
    invoice_number TEXT,
    invoice_vat DOUBLE,
    FOREIGN KEY (agreement_id) REFERENCES Agreement(agreement_id)
);

CREATE TABLE IF NOT EXISTS IncomesExpenses (
    incexp_id INTEGER PRIMARY KEY AUTOINCREMENT,
    event_id INTEGER,
    incexp_concept TEXT,
    incexp_amount DOUBLE,
    incexp_status TEXT CHECK(incexp_status IN ('Estimated', 'Paid')) DEFAULT 'Estimated',
    FOREIGN KEY (event_id) REFERENCES Event(event_id)
);

CREATE TABLE IF NOT EXISTS Movement (
    movement_id INTEGER PRIMARY KEY AUTOINCREMENT,
    incexp_id INTEGER,
    invoice_id INTEGER,
    movement_date DATE ,
    movement_concept TEXT ,
    movement_amount DOUBLE ,
    FOREIGN KEY (incexp_id) REFERENCES IncomesExpenses(incexp_id),
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id)
);