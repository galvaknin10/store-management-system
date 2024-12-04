package models;

public class Customer {
    private String fullName;    // Customer's full name
    private String idNumber;    // Customer's ID
    private String phoneNumber; // Customer's phone number
    private String type;        // Customer type: New, Returning, VIP
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

