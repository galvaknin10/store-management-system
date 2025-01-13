package server.models.customer;


public class Customer {
    private String fullName;    
    private String idNumber;   
    private String phoneNumber;
    private String type;      
    private String branch;
    
    public Customer(String fullName, String idNumber, String phoneNumber, String type, String branch) {
        this.fullName = fullName;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.branch = branch;
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

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Name: " + fullName + ", ID Number: " + idNumber + ", Phone: " + phoneNumber + ", Type: " + type + ", Branch: " + branch;
    }
}

