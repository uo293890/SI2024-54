CREATE TABLE IF NOT EXISTS Sponsor (
    sponsor_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    contact_name TEXT,
    contact_number TEXT,
    contact_email TEXT
);

CREATE TABLE IF NOT EXISTS Agreement (
    agreement_id INTEGER PRIMARY KEY AUTOINCREMENT,
    edition_id INTEGER NOT NULL,
    sponsor_id INTEGER NOT NULL,
    level_amount DECIMAL(10, 2) NOT NULL,
    date TEXT NOT NULL,
    negotiator TEXT,
    paid_cancelled INTEGER,
    FOREIGN KEY (edition_id) REFERENCES Edition(edition_id),
    FOREIGN KEY (sponsor_id) REFERENCES Sponsor(sponsor_id)
);

CREATE TABLE IF NOT EXISTS Event (
    event_id INTEGER PRIMARY KEY AUTOINCREMENT,
    edition_id INTEGER NOT NULL,
    title TEXT NOT NULL,
);

CREATE TABLE IF NOT EXISTS Invoice (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    agreement_id INTEGER NOT NULL,
    amount REAL NOT NULL,
    vat_percentage REAL NOT NULL,
    profit_or_nonprofit TEXT NOT NULL,
    FOREIGN KEY (agreement_id) REFERENCES Agreement(id)
);

CREATE TABLE IF NOT EXISTS Date (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    event_id INTEGER NOT NULL,
    initial DATE NOT NULL,
    final DATE NOT NULL,
    FOREIGN KEY (event_id) REFERENCES Event(id)
);

CREATE TABLE IF NOT EXISTS Payment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    invoice_id INTEGER NOT NULL,
    date DATE NOT NULL,
    amount REAL NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(id)
);

CREATE TABLE IF NOT EXISTS SponsorshipAgreements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sponsor_organization_name VARCHAR(255) NOT NULL,
    sponsor_contact_name VARCHAR(255) NOT NULL,
    sponsor_contact_email VARCHAR(255) NOT NULL,
    agreed_amount DECIMAL(10, 2) NOT NULL,
    activity_supported VARCHAR(255) NOT NULL,
    governing_board_member_name VARCHAR(255) NOT NULL,
    governing_board_member_role VARCHAR(255) NOT NULL,
    agreement_date DATE NOT NULL
);