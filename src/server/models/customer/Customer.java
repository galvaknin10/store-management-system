package server.models.customer;


public class Customer {
    private String name;    
    private String id;   
    private String phoneNumber;
    private String type;      
    private String branch;
    
    public Customer(String name, String id, String phoneNumber, String type, String branch) {
        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public String getIdNumber() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", ID Number: " + id + ", Phone: " + phoneNumber + ", Type: " + type + ", Branch: " + branch;
    }
}

