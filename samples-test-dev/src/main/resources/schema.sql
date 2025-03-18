DROP TABLE IF EXISTS Movement;
DROP TABLE IF EXISTS Otherie;
DROP TABLE IF EXISTS Payment;
DROP TABLE IF EXISTS Invoice;
DROP TABLE IF EXISTS Agreement;
DROP TABLE IF EXISTS Sponsor;
DROP TABLE IF EXISTS Edition;
DROP TABLE IF EXISTS Event;
DROP TABLE IF EXISTS GbMember;

CREATE TABLE IF NOT EXISTS Type (
    type_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    type_name    TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Event (
    event_id         	INTEGER PRIMARY KEY AUTOINCREMENT,
    type_id           	INTEGER NOT NULL,
    event_name     		TEXT NOT NULL,
    event_inidate   	DATE,
    event_enddate    	DATE,
    event_location		TEXT,
    event_status     	TEXT NOT NULL CHECK(agreement_status IN ('Planned', 'Closed')) DEFAULT 'Planned',
    FOREIGN KEY (type_id) REFERENCES Type(type_id)
);

CREATE TABLE IF NOT EXISTS LevelOfSponsorship (
	level_id		INTEGER PRIMARY KEY AUTOINCREMENT,
	event_id		INTEGER NOT NULL,
	level_name		TEXT NOT NULL,
	level_minamount INTEGER NOT NULL,
	advantages		TEXT NOT NULL,
	FOREIGN KEY (event_id) REFERENCES Event(event_id)
);

CREATE TABLE IF NOT EXISTS Agreement (
    agreement_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    level_id           INTEGER NOT NULL,
    sponsor_id         INTEGER NOT NULL,
    gbmember_id        INTEGER NOT NULL,
    spcontact_id		   INTEGER NOT NULL,
    agreement_date     DATE NOT NULL,
    agreement_amount   DOUBLE NOT NULL CHECK (agreement_amount >= 0),
    agreement_status   TEXT NOT NULL CHECK(agreement_status IN ('Agreed', 'Paid', 'Closed')) DEFAULT 'Agreed',
    FOREIGN KEY (level_id) REFERENCES Level(level_id),
    FOREIGN KEY (sponsor_id) REFERENCES Sponsor(sponsor_id),
    FOREIGN KEY (gbmember_id) REFERENCES GbMember(gbmember_id),
    FOREIGN KEY (spcontact_id) REFERENCES SpContact(spcontact_id)
);

CREATE IF NOT EXISTS SpContact (
	spcontact_id	   INTEGER PRIMARY KEY AUTOINCREMENT,
	spcontact_name       TEXT NOT NULL,
    spcontact_number     TEXT NOT NULL CHECK(contact_number LIKE '+%'),
    spcontact_email      TEXT NOT NULL CHECK(contact_email LIKE '%@%'),
);

CREATE TABLE IF NOT EXISTS Sponsor (
    sponsor_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsor_name       TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS GbMember (
    gbmember_id        INTEGER PRIMARY KEY AUTOINCREMENT,
    gbmember_name      TEXT NOT NULL,
    gbmember_email	   TEXT NOT NULL,
    gbmember_position  TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Invoice (
    invoice_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    agreement_id       INTEGER NOT NULL,
    invoice_date       DATE NOT NULL,
    invoice_number     TEXT NOT NULL CHECK (invoice_number GLOB '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    invoice_vat        DOUBLE NOT NULL,
    FOREIGN KEY (agreement_id) REFERENCES Agreement(agreement_id)
);

CREATE TABLE IF NOT EXITS IncomesExpenses (
	incexp_id		INTEGER PRIMARY KEY AUTOINCREMENT,
	event_id		INTEGER NOT NULL,
	incexp_concept	TEXT NOT NULL,
	incexp_amount	INTEGET NOT NULL,
	incexp_status	TEXT NOT NULL CHECK(agreement_status IN ('Estimated', 'Paid')) DEFAULT 'Estimated',
	FOREIGN KEY (event_id) REFERENCES Event(event_id)
);

CREATE TABLE IF NOT EXISTS Movement (
    movement_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    incexp_id           INTEGER,
    invoice_id          INTEGER,
    movement_date       DATE NOT NULL,
    movement_concept    TEXT NOT NULL,
    movement_amount     DOUBLE NOT NULL CHECK (movement_amount >= 0),
    FOREIGN KEY (incexp_id) REFERENCES IncomesExpenses(incexp_id),
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id)
);