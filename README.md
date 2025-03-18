# 📌 SI2024-54 - Financial and Activity Management System

## 🏆 Overview
The **SI2024-54** project is a **Financial and Activity Management System** designed to streamline the handling of sponsorship agreements, invoicing, and financial reports related to various activities. The system follows Spanish regulations for simplified invoicing and financial reporting, allowing users to generate, send, and track invoices, as well as review financial statements of activities.

### 🔹 Key Features:
- 📂 **Invoice Management**: Generate, send, and track invoices for sponsorship agreements.
- 📊 **Financial Reporting**: View income and expenses per activity, including estimated and actual balances.
- 🛠 **Database Integration**: Store and retrieve financial records efficiently.
- 🖥 **GUI Interface**: Java Swing-based graphical user interface for easy data management.
- 🔄 **Automated Processing**: Auto-set default financial periods and generate invoice IDs dynamically.
- ⚡ **Error Handling**: Provides validation checks and error handling for a robust user experience.

## 📁 Project Structure
```
SI2024-54/
├── src/
│   ├── giis/demo/tkrun/
│   │   ├── 📄 InvoiceDTO.java        # Data model for invoices
│   │   ├── 💾 InvoiceModel.java      # Handles database interactions for invoices
│   │   ├── 🖥 InvoiceController.java # Controls user interactions for invoices
│   │   ├── 📊 InvoiceView.java       # GUI for invoice management
│   │   ├── 📄 FinancialReportDTO.java  # Data model for financial reports
│   │   ├── 💾 FinancialReportModel.java # Handles database interactions for reports
│   │   ├── 🖥 FinancialReportController.java # Controls user interactions for reports
│   │   ├── 📊 FinancialReportView.java  # GUI for financial reports
│   │
│   ├── giis/demo/util/
│   │   ├── 🔗 Database.java         # Handles database connection
│   │   ├── ⚠️ ApplicationException.java  # Custom exception handling
│   │   ├── 🖥 SwingUtil.java         # Utility functions for UI components
│
├── database/
│   ├── 📜 schema.sql          # SQL script to create tables
│   ├── 📜 seed_data.sql       # Sample data for testing
│
├── test/
│   ├── giis/demo/tkrun/ut/
│   │   ├── ✅ TestInscripcion.java # Unit tests for activity registration
│   │   ├── ✅ TestUpdates.java # Unit tests for financial updates
│
├── docs/
│   ├── 📖 User_Manual.pdf    # System documentation for users
│   ├── 📄 API_Documentation.html # Generated API reference
│
├── reports/
│   ├── 📊 test-reports/       # Test execution and coverage reports
│   ├── 📈 financial-reports/  # Generated financial reports
│
├── pom.xml                   # Maven configuration for project build
├── README.md                 # This document
```

## 🏗 Installation & Setup
### ⚙️ Prerequisites
- ☕ Java 8 or later
- 🗄 A relational database (e.g., MySQL, PostgreSQL)
- 🔗 JDBC driver for database connectivity
- 🖥 An IDE such as IntelliJ IDEA or Eclipse (optional for development)

### 📥 Steps to Run the Project
1. **Clone the repository:**
   ```sh
   git clone https://github.com/your-username/SI2024-54.git
   ```
2. **Set up the database:**
   - Create the necessary database schema:
     ```sh
     mysql -u root -p < database/schema.sql
     ```
   - Populate with sample data (optional):
     ```sh
     mysql -u root -p < database/seed_data.sql
     ```
3. **Configure database credentials:**
   - Edit `Database.java` to match your database settings:
     ```java
     private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
     private static final String USER = "your_user";
     private static final String PASSWORD = "your_password";
     ```
4. **Compile and run the application:**
   ```sh
   javac -d bin src/giis/demo/tkrun/*.java
   java -cp bin giis.demo.tkrun.Main
   ```

## 🎯 How It Works
1. **🧾 Invoice Management:**
   - Select an activity and sponsorship agreement.
   - Generate and send invoices based on agreements.
   - Store invoice details and track payment status.
2. **📊 Financial Reporting:**
   - Select a date range and filter activities by status.
   - View income and expenses in a detailed table.
   - Calculate total balances for estimated and actual income/expenses.
3. **🛠 Database Management:**
   - All financial records and invoices are stored in a relational database.
   - Queries are optimized for performance.
4. **🖥 User Interface:**
   - Java Swing-based UI allows easy selection of filters and data viewing.

## 📜 License
This project is licensed under the MIT License.

## 📬 Contact
For any issues or contributions, please open an issue on GitHub or contact the repository owner.

