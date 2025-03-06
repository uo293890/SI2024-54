-- schema.sql
DROP TABLE Event;
CREATE TABLE IF NOT EXISTS Event (
    event_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    title          TEXT NOT NULL,
    event_date     DATE NOT NULL,
    location       TEXT,
    status         TEXT DEFAULT 'Planned',
    description    TEXT,
    created_date   DATE NOT NULL
);

DROP TABLE GBMember;
CREATE TABLE IF NOT EXISTS GBMember (
    gb_member_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name           TEXT NOT NULL,
    role           TEXT,
    email          TEXT,
    phone          TEXT
);

DROP TABLE Sponsor;
CREATE TABLE IF NOT EXISTS Sponsor (
    sponsor_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsor_name       TEXT NOT NULL,
    tax_id             TEXT,
    billing_address    TEXT,
    contact_name       TEXT,
    contact_email      TEXT,
    contact_phone      TEXT
);

DROP TABLE Sponsorship;
CREATE TABLE IF NOT EXISTS Sponsorship (
    sponsorship_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    event_id           INTEGER NOT NULL,
    sponsor_id         INTEGER NOT NULL,
    gb_member_id       INTEGER NOT NULL,
    agreement_date     DATE NOT NULL,
    sponsorship_level  TEXT,
    agreed_amount      REAL NOT NULL,
    sponsorship_status TEXT DEFAULT 'Active',
    FOREIGN KEY (event_id) REFERENCES Event(event_id),
    FOREIGN KEY (sponsor_id) REFERENCES Sponsor(sponsor_id),
    FOREIGN KEY (gb_member_id) REFERENCES GBMember(gb_member_id)
);

DROP TABLE Invoice;
CREATE TABLE IF NOT EXISTS Invoice (
    invoice_id         INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsorship_id     INTEGER NOT NULL,
    agreement_id       INTEGER,
    invoice_date       DATE NOT NULL,
    invoice_number     TEXT NOT NULL,
    recipient_name     TEXT NOT NULL,
    recipient_tax_id   TEXT NOT NULL,
    recipient_address  TEXT,
    base_amount        REAL NOT NULL,
    vat                REAL NOT NULL,
    invoice_amount     REAL NOT NULL,
    sent_date          DATE,
    FOREIGN KEY (sponsorship_id) REFERENCES Sponsorship(sponsorship_id)
);

DROP TABLE Transactions;
CREATE TABLE IF NOT EXISTS Transactions (
    transaction_id  INTEGER PRIMARY KEY AUTOINCREMENT,
    event_id        INTEGER NOT NULL,
    invoice_id      INTEGER,
    transaction_type TEXT NOT NULL,
    status          TEXT NOT NULL,
    record_date     DATE NOT NULL,
    payment_date    DATE,
    concept         TEXT NOT NULL,
    details         TEXT,
    amount          REAL NOT NULL,
    FOREIGN KEY (event_id) REFERENCES Event(event_id),
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id)
);