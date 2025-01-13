package server.models.inventory;

public class Product {
    private String serialNum;
    private String name;
    private double price;
    private int quantity;
    private String branch;

    // Constructor
    public Product(String serialNum, String name, double price, int quantity, String branch) {
        this.serialNum = serialNum;
        this.name = name;
        this.price = price + '$';
        this.quantity = quantity;
        this.branch = branch;
    }

    // Getter for id
    public String getSerialNum() {
        return serialNum;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    // Getter for quantity
    public int getQuantity() {
        return quantity;
    }

    // Optionally, add setters if needed
    public void adjustQuantity(int soldunits) {
        this.quantity = this.quantity - soldunits;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Serial Number: " + serialNum + ", Price: " + price + "$" + ", Quantity: " + quantity + ", Branch: " + branch;
    }
}

