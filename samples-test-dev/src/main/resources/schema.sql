-- Tabla Event
CREATE TABLE IF NOT EXISTS Event (
    event_id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL
);

-- Tabla Edition
CREATE TABLE IF NOT EXISTS Edition (
    edition_id INTEGER PRIMARY KEY AUTOINCREMENT,
    event_id INTEGER NOT NULL,
    title TEXT NOT NULL,
    date TEXT NOT NULL,
    end_date TEXT,
    status TEXT,
    place TEXT,
    planned_completed INTEGER,
    FOREIGN KEY (event_id) REFERENCES Event(event_id)
);
-- Tabla EstimatedIncomeExpenses
CREATE TABLE IF NOT EXISTS EstimatedIncomeExpenses (
    income_expense_id INTEGER PRIMARY KEY AUTOINCREMENT,
    edition_id INTEGER NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    paid_cancelled INTEGER,
    description TEXT,
    FOREIGN KEY (edition_id) REFERENCES Edition(edition_id)
);

-- Tabla Sponsor
CREATE TABLE IF NOT EXISTS Sponsor (
    sponsor_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    contact_name TEXT,
    contact_number TEXT,
    contact_email TEXT
);

-- Tabla Agreement
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

-- Tabla Invoice
CREATE TABLE IF NOT EXISTS Invoice (
    invoice_id INTEGER PRIMARY KEY AUTOINCREMENT,
    invoice_date TEXT NOT NULL,
    invoice_number TEXT NOT NULL UNIQUE,
    recipient_name TEXT NOT NULL,
    recipient_tax_id TEXT NOT NULL,
    recipient_address TEXT NOT NULL,
    base_amount DECIMAL(10, 2) NOT NULL,
    vat DECIMAL(5, 2) NOT NULL,
    total_amount DECIMAL(10, 2) GENERATED ALWAYS AS (base_amount * (1 + vat/100)) STORED,
    sent_date TEXT,
    agreement_id INTEGER, -- Permitir NULL
    FOREIGN KEY (agreement_id) REFERENCES Agreement(agreement_id)
);

-- Tabla Movements
CREATE TABLE IF NOT EXISTS Movements (
    movement_id INTEGER PRIMARY KEY AUTOINCREMENT,
    invoice_id INTEGER,
    date TEXT NOT NULL,  -- SQLite usa TEXT para fechas
    amount DECIMAL(10, 2) NOT NULL,
    concept TEXT,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id)
);

-- Vista para el reporte financiero
CREATE VIEW IF NOT EXISTS financial_reports AS
SELECT 
    ed.edition_id,
    e.title AS activity,
    ed.date AS start_date,
    ed.end_date,
    ed.status,
    SUM(ag.level_amount) AS total_income,
    SUM(eie.amount) AS total_expenses,
    (SUM(ag.level_amount) - SUM(eie.amount)) AS balance
FROM Edition ed
JOIN Event e ON ed.event_id = e.event_id
LEFT JOIN Agreement ag ON ed.edition_id = ag.edition_id
LEFT JOIN EstimatedIncomeExpenses eie ON ed.edition_id = eie.edition_id
GROUP BY ed.edition_id;