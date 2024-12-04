# Store Management System

The **Store Management System** is a Java-based application designed to streamline the management of store operations across different branches. It provides an intuitive interface for both administrators and branch users to manage employees, inventory, and customer records efficiently. 

## Features

### **Admin Features**
- **User Management**: 
  - Add new users (Admin or Branch users).
  - Manage user credentials securely.
- **Employee Management**:
  - Add, edit, and display employee information.
  - Assign employees to specific branches.
- **Inventory Management**:
  - Manage product inventory for each branch.
  - Add, update, and remove inventory items.
- **Customer Management**:
  - Manage customer records across branches.
  - Update customer information and preferences.
- **Branch Support**:
  - Manage operations for multiple branches (e.g., Jerusalem, Eilat).
- **Flexible Navigation**:
  - Return to the login screen or exit the system seamlessly.

### **Branch User Features**
- **Employee Display**:
  - View a list of employees assigned to the branch.
- **Inventory Management**:
  - Add, update, and remove inventory items specific to the branch.
- **Customer Management**:
  - Manage and update customer records associated with the branch.
- **User-Friendly Navigation**:
  - Easily return to the login screen or exit the system.

---

## How It Works

1. **Login Workflow**:
   - Users log in with their credentials.
   - The system identifies the user's role (Admin or Branch User) and redirects them to the appropriate interface.
   
2. **Admin Interface**:
   - Admins can manage users, employees, inventory, and customers across branches.
   - Includes options to return to the login screen or exit the application.

3. **Branch User Interface**:
   - Branch users can manage their specific branch's employees, inventory, and customer records.
   - Includes options to return to the login screen or exit the application.

---

## Technologies Used
- **Programming Language**: Java
- **File Handling**: Used to manage persistent storage for employees, customers, inventory, and credentials.
- **Object-Oriented Design**: Employed to create modular and reusable components like `EmployeeManager`, `InventoryManager`, `CustomerManager`, and `UserCredentialsManager`.

---

## Prerequisites
- Java Development Kit (JDK) 11 or higher.
- A Java IDE (e.g., IntelliJ IDEA, Eclipse, or VS Code) or a terminal for execution.
- Basic knowledge of Java and file handling.

---


