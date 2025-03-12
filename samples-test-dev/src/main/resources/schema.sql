DROP TABLE Movement;
DROP TABLE Otherie;
DROP TABLE Invoice;
DROP TABLE Agreement;
DROP TABLE Sponsor;
DROP TABLE Edition;
DROP TABLE Event;
DROP TABLE GBMember;

CREATE TABLE IF NOT EXISTS Event (
    event_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    event_title    TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Edition (
    edition_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    event_id           INTEGER NOT NULL,
    edition_title      TEXT NOT NULL,
    edition_inidate    DATE,
    edition_enddate    DATE,
    edition_location   TEXT,
    edition_status     TEXT DEFAULT 'Planned',
    FOREIGN KEY (event_id) REFERENCES Event(event_id)
);

CREATE TABLE IF NOT EXISTS Agreement (
    agreement_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    edition_id         INTEGER NOT NULL,
    sponsor_id         INTEGER NOT NULL,
    gbmember_id        TEXT NOT NULL,
    contact_name     TEXT NOT NULL,
    contact_number     TEXT NOT NULL CHECK(contact_number LIKE '+%'),
    contact_email      TEXT NOT NULL CHECK(contact_email LIKE '%@%'),
    agreement_date     DATE NOT NULL,
    agreement_amount   DOUBLE NOT NULL,
    agreement_status   TEXT DEFAULT 'Estimated',
    FOREIGN KEY (edition_id) REFERENCES Edition(edition_id),
    FOREIGN KEY (sponsor_id) REFERENCES Sponsor(sponsor_id),
    FOREIGN KEY (gbmember_id) REFERENCES GBMember(gbmember_id)
);

CREATE TABLE IF NOT EXISTS Sponsor (
    sponsor_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsor_name       TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Invoice (
    invoice_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    agreement_id       INTEGER NOT NULL,
    invoice_date       DATE NOT NULL,
    invoice_number     TEXT NOT NULL CHECK (invoice_number GLOB '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    invoice_vat        DOUBLE NOT NULL,
    FOREIGN KEY (agreement_id) REFERENCES Agreement(agreement_id)
);

CREATE TABLE IF NOT EXISTS Otherie (
    otherie_id          INTEGER PRIMARY KEY AUTOINCREMENT,
    edition_id          INTEGER NOT NULL,
    otherie_amount      DOUBLE NOT NULL,
    otherie_description TEXT NOT NULL,
    otherie_status      TEXT DEFAULT 'Estimated',
    FOREIGN KEY (edition_id) REFERENCES Edition(edition_id)
);

CREATE TABLE IF NOT EXISTS Movement (
    movement_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    otherie_id          INTEGER,
    invoice_id          INTEGER,
    movement_date       DATE NOT NULL,
    movement_concept    TEXT NOT NULL,
    movement_amount     DOUBLE NOT NULL,
    FOREIGN KEY (otherie_id) REFERENCES Otherie(otherie_id),
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id)
);

CREATE TABLE IF NOT EXISTS GBMember (
	gbmember_id 		INTEGER PRIMARY KEY AUTOINCREMENT,
	gbmember_name		TEXT NOT NULL
);