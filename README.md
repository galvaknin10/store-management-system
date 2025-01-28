# Store Management System

The **Store Management System** is a client-server application developed in Java. It enables efficient management of store operations across multiple branches through a centralized server and client interface. The system is designed for administrators and branch users to manage employees, inventory, and customer records effectively.

---

## Features

### **Server Features**
- **User Authentication**:
  - Handles secure login for Admins and Branch Users.
- **Branch Operations**:
  - Centralized management of multiple branches.
- **Data Management**:
  - Stores employee, inventory, and customer data for all branches.
- **Communication**:
  - Facilitates client-server communication using socket programming.

### **Client Features**
- **User Authentication**:
  - Login to the system and access branch-specific or admin functionalities.
- **Employee Management**:
  - Add, view, and update employee details (admin or branch-specific).
- **Inventory Management**:
  - Add, update, and remove products from the inventory.
- **Customer Management**:
  - Add and update customer information specific to a branch.
- **User-Friendly Navigation**:
  - Easy-to-use interface for seamless interaction.

---

## Project Structure

```plaintext
store-management-system/
├── README.md                   # Project documentation
├── run-client.bat              # Batch file to run the client
├── run-server.bat              # Batch file to run the server
├── structure.txt               # Generated file for folder structure
├── .vscode/                    # VS Code configuration files
│   ├── launch.json
│   └── settings.json
├── bin/                        # Compiled Java classes
│   ├── client/
│   │   ├── Client.class
│   │   ├── cli/
│   │   │   ├── ChatUtils.class
│   │   │   ├── CustomerScreen.class
│   │   │   ├── EmployeeScreen.class
│   │   │   ├── InventoryScreen.class
│   │   │   ├── LoginScreen.class
│   │   │   ├── ManagerScreen.class
│   │   │   ├── RegisterUtils.class
│   │   │   ├── ReportScreen.class
│   │   │   ├── ScreensUtils.class
│   │   │   ├── TransactionScreen.class
│   │   │   └── UserScreen.class
│   │   └── utils/
│   │       ├── ChatListener.class
│   │       ├── MessageDispatcher.class
│   │       └── RequestSender.class
│   ├── server/
│   │   ├── Server.class
│   │   ├── models/
│   │   │   ├── credentials/
│   │   │   │   ├── CredentialController.class
│   │   │   │   ├── Credentials.class
│   │   │   │   ├── CredentialsFileHandler.class
│   │   │   │   └── CredentialsManager.class
│   │   │   ├── customer/
│   │   │   │   ├── Customer.class
│   │   │   │   ├── CustomerController.class
│   │   │   │   ├── CustomerFileHandler.class
│   │   │   │   └── CustomerManager.class
│   │   │   ├── employee/
│   │   │   │   ├── Employee.class
│   │   │   │   ├── EmployeeController.class
│   │   │   │   ├── EmployeeFileHandler.class
│   │   │   │   └── EmployeeManager.class
│   │   │   ├── inventory/
│   │   │   │   ├── InventoryController.class
│   │   │   │   ├── InventoryFileHandler.class
│   │   │   │   ├── InventoryManager.class
│   │   │   │   └── Product.class
│   │   │   ├── log/
│   │   │   │   ├── Log.class
│   │   │   │   ├── LogController.class
│   │   │   │   ├── LogFileHandler.class
│   │   │   │   └── LogManager.class
│   │   │   └── report/
│   │   │       ├── Report.class
│   │   │       ├── ReportController.class
│   │   │       ├── ReportFileHandler.class
│   │   │       └── ReportManager.class
│   │   └── utils/
│   │       ├── ChatManager.class
│   │       ├── ChatSession.class
│   │       ├── ClientInfo.class
│   │       ├── FileHandler.class
│   │       ├── RequestHandler.class
│   │       └── SessionManager.class
│   └── shared/
│       ├── ChatMessage.class
│       ├── Request.class
│       └── Response.class
├── data/                       # Data storage files
│   ├── EILAT_credentials.json
│   ├── EILAT_customers.json
│   ├── EILAT_employees.json
│   ├── EILAT_inventory.json
│   ├── EILAT_logs.json
│   ├── EILAT_reports.json
│   ├── JERUSALEM_credentials.json
│   ├── JERUSALEM_customers.json
│   ├── JERUSALEM_employees.json
│   ├── JERUSALEM_inventory.json
│   ├── JERUSALEM_logs.json
│   └── JERUSALEM_reports.json
├── lib/                        # External libraries
│   └── gson-2.8.8.jar
└── src/                        # Source code
    ├── client/
    │   ├── Client.java
    │   ├── cli/
    │   │   ├── ChatUtils.java
    │   │   ├── CustomerScreen.java
    │   │   ├── EmployeeScreen.java
    │   │   ├── InventoryScreen.java
    │   │   ├── LoginScreen.java
    │   │   ├── ManagerScreen.java
    │   │   ├── RegisterUtils.java
    │   │   ├── ReportScreen.java
    │   │   ├── ScreensUtils.java
    │   │   ├── TransactionScreen.java
    │   │   └── UserScreen.java
    │   └── utils/
    │       ├── ChatListener.java
    │       ├── MessageDispatcher.java
    │       └── RequestSender.java
    ├── server/
    │   ├── Server.java
    │   ├── models/
    │   │   ├── credentials/
    │   │   │   ├── CredentialController.java
    │   │   │   ├── Credentials.java
    │   │   │   ├── CredentialsFileHandler.java
    │   │   │   └── CredentialsManager.java
    │   │   ├── customer/
    │   │   │   ├── Customer.java
    │   │   │   ├── CustomerController.java
    │   │   │   ├── CustomerFileHandler.java
    │   │   │   └── CustomerManager.java
    │   │   ├── employee/
    │   │   │   ├── Employee.java
    │   │   │   ├── EmployeeController.java
    │   │   │   ├── EmployeeFileHandler.java
    │   │   │   └── EmployeeManager.java
    │   │   ├── inventory/
    │   │   │   ├── InventoryController.java
    │   │   │   ├── InventoryFileHandler.java
    │   │   │   ├── InventoryManager.java
    │   │   │   └── Product.java
    │   │   ├── log/
    │   │   │   ├── Log.java
    │   │   │   ├── LogController.java
    │   │   │   ├── LogFileHandler.java
    │   │   │   └── LogManager.java
    │   │   └── report/
    │   │       ├── Report.java
    │   │       ├── ReportController.java
    │   │       ├── ReportFileHandler.java
    │   │       └── ReportManager.java
    │   └── utils/
    │       ├── ChatManager.java
    │       ├── ChatSession.java
    │       ├── ClientInfo.java
    │       ├── FileHandler.java
    │       ├── RequestHandler.java
    │       └── SessionManager.java
    └── shared/
        ├── ChatMessage.java
        ├── Request.java
        └── Response.java
```

---

## Installation and Setup

### **Prerequisites**
- **Java Version**: Ensure Java 23.0.1 or higher is installed.
  - Check your Java version using:
    ```bash
    java -version
    ```
- **IDE or Terminal**:
  - A Java IDE (e.g., IntelliJ IDEA, Eclipse, VS Code) or terminal with Java configured.

### **Steps to Run**

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/galvaknin10/store-management-system.git
   cd store-management-system
   ```

2. **Compile the Code** (if needed):
   ```bash
   javac -cp lib/gson-2.8.8.jar -d bin src/**/*.java
   ```

3. **Start the Server**:
   - Run the `run-server.bat` file:
     ```bash
     ./run-server.bat
     ```
   - The server will start listening on the designated port.

4. **Start the Client**:
   - Run the `run-client.bat` file:
     ```bash
     ./run-client.bat
     ```
   - Login as an Admin or Branch User to access the system.

---

## How It Works

1. **Client-Server Communication**:
   - The server handles authentication, data storage, and business logic.
   - Clients communicate with the server via socket programming to perform actions like managing employees, inventory, and customers.

2. **Branch-Specific Operations**:
   - Admins manage all branches, while branch users manage specific branch data.

3. **Data Persistence**:
   - Files are used to store data (e.g., employee records, inventory, customer details).

---

## Technologies Used
- **Programming Language**: Java
- **Libraries**: Gson for JSON parsing
- **Architecture**: Client-Server using socket programming
- **File Handling**: Persistent data storage for employees, inventory, and customer records

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.

---

Feel free to explore the code and contribute to the project!






