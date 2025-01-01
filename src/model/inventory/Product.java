package model.inventory;

public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private String branch;

    // Constructor
    public Product(String id, String name, double price, int quantity, String branch) {
        this.id = id;
        this.name = name;
        this.price = price + '$';
        this.quantity = quantity;
        this.branch = branch;
    }

    // Getter for id
    public String getId() {
        return id;
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
        return "Name: " + name + ", Serial Number: " + id + ", Price: " + price + "$" + ", Quantity: " + quantity + ", Branch: " + branch;
    }
}

