# COIIPA Sponsorship Management 💼✨

Welcome to **COIIPA Sponsorship Management** – an all-in-one Java desktop application designed to manage events, sponsorships, agreements, invoicing, and financial transactions. This project features a modern Swing-based graphical user interface with flexible layouts provided by MIGLayout, making it an ideal solution for event organizers and sponsorship managers.

---

## 🚀 Overview

COIIPA Sponsorship Management provides a comprehensive solution for handling sponsorships and financial activities. The application allows you to:

- **Manage Events:** Create, list, and close events with statuses like "Planned" and "Closed" 🎫
- **Register Sponsorship Agreements:** Easily record agreements between sponsors and events, including sponsor selection, contact management, and sponsorship levels 🤝
- **Invoice Management:** Generate and send invoices based on agreements, following rules such as issuing invoices at least 4 weeks before an event 📄
- **Financial Reporting:** Monitor and compare estimated vs. actual incomes and expenses with detailed reports and balance calculations 📊
- **Record Other Financial Movements:** Log additional income or expense transactions that are not directly tied to a sponsorship agreement 💰

---

## 🎯 Key Features

- **Event Management**  
  - Create, view, and update events with detailed information such as start/end dates, location, and type.
  - Change event status between "Planned" and "Closed" and verify all financial conditions before closing an event.
  
- **Sponsorship Agreement Registration**  
  - Select sponsors, sponsor contacts, and sponsorship levels from pre-populated lists.
  - Assign Governing Board (GB) members to oversee each agreement.
  - Validate agreement details (date, amount, status) before saving to the database.

- **Financial Status and Reporting**  
  - Compare estimated and actual incomes/expenses for each event.
  - Generate detailed income & expense reports that summarize financial performance.
  - View summaries and breakdowns of sponsor contributions, incomes, and expenses.

- **Invoice Generation & Management**  
  - Generate invoices with automatic number generation and VAT calculations.
  - Validate invoice creation based on event dates and regulatory requirements.
  - Record the sending of invoices and log corresponding financial movements.

- **Payment Registration**  
  - Register payments (partial, exact, or overpayments) associated with sponsorship agreements.
  - Real-time validation of payment amounts with dynamic feedback on remaining amounts.
  - Update agreement statuses to "Paid" when full payment is received.

- **Other Financial Movements**  
  - Record additional income/expense transactions that fall outside the scope of regular sponsorship agreements.
  - Categorize movements as "Estimated" or "Paid" and track their status over time.

- **User-Friendly Interface**  
  - Built with Swing and MIGLayout for a responsive and intuitive GUI.
  - Tables with auto-adjusting columns and real-time validation ensure a smooth user experience.
  - Interactive dialogs and detailed error messages guide users through each process.

---

## 🛠️ Technologies & Libraries

- **Java SE:** Core language and object-oriented programming.
- **Swing:** Robust graphical user interface development.
- **MIGLayout:** Advanced layout management for flexible UI design.
- **SQLite:** A lightweight relational database for persistent data storage.
- **Apache Commons DbUtils & BeanUtils:** Simplify database access and map SQL results to POJOs.
- **Jackson:** JSON serialization/deserialization for data exchange and testing.
- **Emojis:** Enhance documentation readability and friendliness 😄

---

## 📁 Project Structure

```plaintext
COIIPA-Sponsorship-Management/
├── src/main/java/giis/demo/tkrun
│   ├── Controllers       # Business logic and event handling
│   ├── Views             # Swing GUI components for user interaction
│   ├── Models            # Data access and processing logic
│   └── DTOs              # Data Transfer Objects for encapsulating data
├── src/main/java/giis/demo/util
│   ├── Database          # Classes for DB connectivity and scripts execution
│   ├── SwingUtil         # Utility classes for Swing (table adjustments, dialogs, etc.)
│   └── Other Utilities   # Exception handling, file I/O, etc.
└── src/main/resources
    ├── application.properties   # Database configuration (driver, URL, etc.) 🔌
    ├── schema.sql               # SQL script to create the database schema 🛠️
    └── data.sql                 # SQL script to load sample data into the database 📥
