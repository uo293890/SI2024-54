# 🧾 Invoice Management System

## 📌 Overview
This project is an invoice management system designed to generate and send invoices related to sponsorship agreements for activities. The system follows the simplified Spanish invoicing regulations and includes the following components:

- **🗂 InvoiceDTO**: A Data Transfer Object (DTO) representing an invoice with details such as invoice number, agreement ID, recipient details, and tax information.
- **💾 InvoiceModel**: Handles database interactions for storing and retrieving invoice-related data, including agreements, activities, and invoice history.
- **🖥 InvoiceController**: Manages interactions between the model and the view, processing user actions such as loading activities, generating invoices, and updating invoice statuses.
- **📊 InvoiceView**: A graphical user interface (GUI) built with Java Swing that allows users to manage invoices easily.

## 🚀 Features
- 📂 Load available activities and sponsorship agreements from the database.
- 🔍 Select agreements related to a specific activity.
- 🧾 Generate invoices automatically, assigning a unique invoice number.
- ✍️ Allow users to review invoice details, including recipient name, tax ID, and address.
- 💾 Store invoices and update their status when they are sent.
- ⚠️ Provide user-friendly error handling and messages.
- 📜 Display invoice history and allow users to track invoice statuses.
- 👥 Support multi-user access with future role-based access control.
- ✅ Validate data inputs to prevent incorrect invoice creation.

## 🛠 Technologies Used
- ☕ Java (Swing for GUI development)
- 🗄 SQL (for database operations)
- 🔗 JDBC (for database connectivity)
- 📦 Object-Oriented Programming principles (OOP)
- 🏗 MVC (Model-View-Controller) design pattern

## 🏗 Installation & Setup
### ⚙️ Prerequisites
- ☕ Java 8 or later
- 🗄 A relational database (e.g., MySQL, PostgreSQL)
- 🔗 JDBC driver for database connectivity
- 🖥 An IDE such as IntelliJ IDEA or Eclipse (optional for development)

### 📥 Steps to Run the Project
1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/invoice-management.git
   ```
2. Set up the database schema and tables (SQL scripts provided in the `database` folder).
3. Configure the database connection in `Database.java` by updating the credentials and connection URL.
4. Compile and run the application:
   ```sh
   javac -d bin src/giis/demo/tkrun/*.java
   java -cp bin giis.demo.tkrun.InvoiceView
   ```

## 📂 Project Structure
```
src/
├── giis/demo/tkrun/
│   ├── 🧾 InvoiceDTO.java        # Data model for invoices
│   ├── 💾 InvoiceModel.java      # Handles database interactions
│   ├── 🖥 InvoiceController.java # Controls user interactions
│   ├── 📊 InvoiceView.java       # GUI interface for managing invoices
│
├── giis/demo/util/
│   ├── 🔗 Database.java          # Handles database connection
│
├── database/
│   ├── 📜 schema.sql             # SQL script for database schema
```

## 📌 How It Works
1. **📑 Activity Selection**: The user selects an activity from the dropdown menu.
2. **📜 Load Agreements**: The system retrieves all sponsorship agreements related to the selected activity.
3. **🧾 Invoice Generation**:
   - The user selects an agreement.
   - The system assigns a unique invoice number.
   - The system pre-fills recipient details (name, tax ID, address).
   - The user reviews and confirms the invoice.
4. **📩 Invoice Sending**:
   - Once an invoice is generated, the user can send it.
   - The system updates the database, marking the invoice as sent.
   - The invoice history is updated to track sent invoices.
5. **⚠️ Error Handling**: If an error occurs (e.g., missing data or database failure), the user receives a message.
6. **📊 Invoice Tracking**: The system allows users to check invoice history, view sent invoices, and track pending invoices.

## 🔮 Future Enhancements
- 📄 Add a PDF export feature for invoices.
- 📧 Implement email integration to send invoices automatically.
- ⚡ Improve database queries for better performance.
- 🔑 Implement role-based access control for different users (e.g., secretary, administrator).
- 🌐 Develop a web-based version for better accessibility.
- 💰 Include automatic VAT calculations based on Spanish tax regulations.
- 📈 Provide an analytics dashboard to track invoice trends.

## 📜 License
This project is licensed under the MIT License.

## 📬 Contact
For any issues or contributions, please open an issue on GitHub or contact the repository owner.
