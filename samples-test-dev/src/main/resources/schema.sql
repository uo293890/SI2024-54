DROP TABLE IF EXISTS Movement;
DROP TABLE IF EXISTS Otherie;
DROP TABLE IF EXISTS Payment;
DROP TABLE IF EXISTS Invoice;
DROP TABLE IF EXISTS Agreement;
DROP TABLE IF EXISTS Sponsor;
DROP TABLE IF EXISTS Edition;
DROP TABLE IF EXISTS Event;
DROP TABLE IF EXISTS GBMember;

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
    sponsorship_fee    DOUBLE NOT NULL CHECK (sponsorship_fee >= 0), 
    FOREIGN KEY (event_id) REFERENCES Event(event_id)
);

CREATE TABLE IF NOT EXISTS Sponsor (
    sponsor_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsor_name       TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS GBMember (
    gbmember_id        INTEGER PRIMARY KEY AUTOINCREMENT,
    gbmember_name      TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Agreement (
    agreement_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    edition_id         INTEGER NOT NULL,
    sponsor_id         INTEGER NOT NULL,
    gbmember_id        INTEGER NOT NULL,
    contact_name       TEXT NOT NULL,
    contact_number     TEXT NOT NULL CHECK(contact_number LIKE '+%'),
    contact_email      TEXT NOT NULL CHECK(contact_email LIKE '%@%'),
    agreement_date     DATE NOT NULL,
    agreement_amount   DOUBLE NOT NULL CHECK (agreement_amount >= 0), 
    agreement_status   TEXT DEFAULT 'Estimated',
    early_invoice_request DATE,
    FOREIGN KEY (edition_id) REFERENCES Edition(edition_id),
    FOREIGN KEY (sponsor_id) REFERENCES Sponsor(sponsor_id),
    FOREIGN KEY (gbmember_id) REFERENCES GBMember(gbmember_id)
);

CREATE TABLE IF NOT EXISTS Invoice (
    invoice_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    agreement_id       INTEGER NOT NULL,
    invoice_date       DATE NOT NULL,
    invoice_number     TEXT NOT NULL CHECK (invoice_number GLOB '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    invoice_vat        DOUBLE NOT NULL,
    recipient_name     TEXT NOT NULL,
    recipient_tax_id   TEXT NOT NULL,
    recipient_address  TEXT NOT NULL,
    sent_date          DATE,
    FOREIGN KEY (agreement_id) REFERENCES Agreement(agreement_id)
);

CREATE TABLE IF NOT EXISTS Payment (
    payment_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    invoice_id         INTEGER,
    payment_date       DATE NOT NULL,
    payment_amount     DOUBLE NOT NULL CHECK (payment_amount >= 0),
    payment_type       TEXT CHECK(payment_type IN ('Standard', 'Refund', 'Second Payment')) DEFAULT 'Standard', 
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id)
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
    movement_amount     DOUBLE NOT NULL CHECK (movement_amount >= 0),
    payment_status      TEXT CHECK(payment_status IN ('Estimated', 'Paid', 'Compensation')) DEFAULT 'Estimated', 
    movement_notes      TEXT, 
    FOREIGN KEY (otherie_id) REFERENCES Otherie(otherie_id),
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id)
);
