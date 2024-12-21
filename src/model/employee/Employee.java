package model.employee;

public class Employee {
    private String fullName;    
    private String idNumber;   
    private String phoneNumber;
    private String accountNumber; 
    private String branch;   
    private String employeeId; 
    private String role;       
    private String userName;

    public Employee(String fullName, String idNumber, String phoneNumber, String accountNumber, 
                    String branch, String employeeId, String role, String userName) {
        this.fullName = fullName;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.employeeId = employeeId;
        this.role = role;
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBranch() {
        return branch;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getRole() {
        return role;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "Name: " + fullName + ", ID Number: " + idNumber + ", Phone: " + phoneNumber + ", Account Number" + accountNumber + 
               ", Branch: " + branch + ", Employee Number: " + employeeId + ", Position: " + role + ", User Name In System: " + userName;
    }
    
}

