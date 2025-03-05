-- Tabla Event
CREATE TABLE Event (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

-- Tabla Edition
CREATE TABLE Edition (
    edition_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    place VARCHAR(255),
    planned_completed BOOLEAN,
    FOREIGN KEY (event_id) REFERENCES Event(event_id)
);

-- Tabla EstimatedIncomeExpenses
CREATE TABLE EstimatedIncomeExpenses (
    income_expense_id INT AUTO_INCREMENT PRIMARY KEY,
    edition_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    paid_cancelled BOOLEAN,
    description TEXT,
    FOREIGN KEY (edition_id) REFERENCES Edition(edition_id)
);

-- Tabla Sponsor
CREATE TABLE Sponsor (
    sponsor_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_name VARCHAR(255),
    contact_number VARCHAR(50),
    contact_email VARCHAR(255)
);

-- Tabla Agreement
CREATE TABLE Agreement (
    agreement_id INT AUTO_INCREMENT PRIMARY KEY,
    edition_id INT NOT NULL,
    sponsor_id INT NOT NULL,
    level_amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    negotiator VARCHAR(255),
    paid_cancelled BOOLEAN,
    FOREIGN KEY (edition_id) REFERENCES Edition(edition_id),
    FOREIGN KEY (sponsor_id) REFERENCES Sponsor(sponsor_id)
);

-- Tabla Invoice
CREATE TABLE Invoice (
    invoice_id INT AUTO_INCREMENT PRIMARY KEY,
    agreement_id INT NOT NULL,
    date DATE NOT NULL,
    vat DECIMAL(5, 2),
    FOREIGN KEY (agreement_id) REFERENCES Agreement(agreement_id)
);

-- Tabla Movements
CREATE TABLE Movements (
    movement_id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT,
    date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    concept TEXT,
    FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id)
);