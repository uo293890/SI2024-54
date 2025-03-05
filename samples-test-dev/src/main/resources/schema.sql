CREATE TABLE IF NOT EXISTS Sponsor (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Agreement (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sponsor_id INTEGER NOT NULL,
    kind TEXT NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (sponsor_id) REFERENCES Sponsor(id)
);

CREATE TABLE IF NOT EXISTS Event (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    agreement_id INTEGER NOT NULL,
    title TEXT NOT NULL,
    price REAL NOT NULL,
    amount INTEGER NOT NULL,
    FOREIGN KEY (agreement_id) REFERENCES Agreement(id)
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