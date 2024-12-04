package models;

import java.util.jar.Attributes.Name;

public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String branch;

    // Constructor
    public Product(String id, String name, String category, double price, int quantity, String branch) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price + '$';
        this.quantity = quantity;
        this.branch = branch;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Getter for quantity
    public int getQuantity() {
        return quantity;
    }

    // Optionally, add setters if needed
    public void adjustQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", ID: " + id + ", Category: " + category + ", Price: " + price + ", Quantity: " + quantity + " Branch: " + branch;
    }
}

