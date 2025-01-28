package server.models.employee;


public class Employee {
    private String name;    
    private String id;   
    private String phoneNumber;
    private String accountNumber; 
    private String branch;   
    private String role;       
    private String userName;

    public Employee(String name, String id, String phoneNumber, String accountNumber, 
                    String branch, String role, String userName) {
        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.role = role;
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
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

    public String getRole() {
        return role;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", ID Number: " + id + ", Phone: " + phoneNumber + ", Account Number" + accountNumber + 
               ", Branch: " + branch + ", Position: " + role + ", User Name In System: " + userName;
    }
    
}

