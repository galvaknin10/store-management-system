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
├── bin/                        # Compiled Java classes
├── lib/                        # External libraries (e.g., Gson for JSON handling)
├── src/                        # Source code
│   ├── client/                 # Client-side logic
│   │   ├── Client.java         # Main client class
│   │   ├── CustomerManager.java
│   │   ├── EmployeeManager.java
│   │   └── InventoryManager.java
│   ├── server/                 # Server-side logic
│   │   ├── Server.java         # Main server class
│   │   ├── BranchManager.java
│   │   ├── UserAuthentication.java
│   │   └── DataHandler.java
├── run-client.bat              # Batch file to run the client
├── run-server.bat              # Batch file to run the server
└── README.md                   # Project documentation
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




