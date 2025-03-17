# 📊 Financial Report System

## 📌 Overview
The **Financial Report System** is a tool designed to generate and display financial reports for activities based on income and expenses. The system allows users to track financial data related to different activities and sponsorship agreements, providing detailed financial insights.

### 🔹 Components:
- **📄 FinancialReportDTO**: A Data Transfer Object (DTO) representing financial data for activities, including income, expenses, and balance calculations.
- **💾 FinancialReportModel**: Manages database interactions, retrieving financial data for different activities based on user-selected filters.
- **🖥 FinancialReportController**: Connects the model and view, managing user actions such as filtering data and updating the UI.
- **📊 FinancialReportView**: A graphical user interface (GUI) that enables users to visualize and analyze financial reports efficiently.

## 🚀 Features
- 📂 Load and filter financial data based on date range and activity status.
- 📅 Automatically set default date range to the current year.
- 📜 Display financial reports in a tabular format.
- 📈 Calculate total estimated and actual income and expenses.
- 🔄 Update totals dynamically based on filtered results.
- 📊 Present data summaries, including balance calculations.
- ⚠️ Handle database errors and invalid user inputs.

## 🛠 Technologies Used
- ☕ Java (Swing for GUI development)
- 🗄 SQL (for database queries and financial data storage)
- 🔗 JDBC (for database connectivity)
- 🏗 MVC (Model-View-Controller) design pattern
- 🖥 Java AWT & Swing (for UI components)

## 🏗 Installation & Setup
### ⚙️ Prerequisites
- ☕ Java 8 or later
- 🗄 A relational database (e.g., MySQL, PostgreSQL)
- 🔗 JDBC driver for database connectivity
- 🖥 An IDE such as IntelliJ IDEA or Eclipse (optional for development)

### 📥 Steps to Run the Project
1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/financial-report.git
   ```
2. Set up the database schema and tables (SQL scripts provided in the `database` folder).
3. Configure the database connection in `Database.java` by updating the credentials and connection URL.
4. Compile and run the application:
   ```sh
   javac -d bin src/giis/demo/tkrun/*.java
   java -cp bin giis.demo.tkrun.FinancialReportView
   ```

## 📂 Project Structure
```
src/
├── giis/demo/tkrun/
│   ├── 📄 FinancialReportDTO.java   # Data model for financial reports
│   ├── 💾 FinancialReportModel.java # Handles database queries and calculations
│   ├── 🖥 FinancialReportController.java # Controls user interactions
│   ├── 📊 FinancialReportView.java  # GUI interface for generating financial reports
│
├── giis/demo/util/
│   ├── 🔗 Database.java             # Handles database connection
│
├── database/
│   ├── 📜 schema.sql                # SQL script for database schema
```

## 📌 How It Works
1. **📆 Date Selection**: The user selects a start and end date to filter financial data.
2. **📜 Activity Status**: Users can filter activities based on their status (e.g., Planned, Ongoing, Completed).
3. **📊 Generate Report**:
   - Retrieves financial data from the database.
   - Displays activity names, financial estimates, and actual income/expenses.
   - Calculates and shows balance (estimated vs. actual).
4. **📈 Totals Calculation**:
   - Summarizes estimated and actual income and expenses.
   - Displays the overall financial balance.
5. **⚠️ Error Handling**: Provides error messages for database issues or incorrect inputs.

## 🔮 Future Enhancements
- 📄 Add an export feature to generate financial reports in PDF or Excel format.
- 📧 Implement email notifications for financial updates.
- ⚡ Optimize SQL queries for better performance.
- 🔑 Implement role-based access control for different users (e.g., administrators, financial officers).
- 🌐 Develop a web-based version for better accessibility.
- 📈 Provide advanced financial analytics and reporting features.

## 📜 License
This project is licensed under the MIT License.

## 📬 Contact
For any issues or contributions, please open an issue on GitHub or contact the repository owner.
