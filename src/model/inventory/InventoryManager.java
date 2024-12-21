package model.inventory;

import java.util.HashMap;
import java.util.Map;


public class InventoryManager {
    
    private static final Map<String, InventoryManager> instances = new HashMap<>();

    static {
        // Automatically create instances for predefined branches
        initializeBranch("EILAT");
        initializeBranch("JERUSALEM");
    }

    // Use a HashMap to store products, with the product ID as the key
    private Map<String, Product> inventory;

    // Private constructor to prevent external instantiation
    private InventoryManager(String branch) {
        this.inventory = InventoryFileHandler.loadInventoryFromFile(branch);
    }

    // Method to initialize instances for a specific branch
    private static void initializeBranch(String branch) {
        InventoryManager instance = new InventoryManager(branch);
        instances.put(branch, instance);
    }

    // Public method to get an instance for a specific branch
    public static InventoryManager getInstance(String branch) {
        if (instances.containsKey(branch)) {
            return instances.get(branch);
        } else {
            System.out.println("Branch not recognized. Please verify the branch name and try again.");
            return null;  // Return null to indicate that the branch is not valid
        }
    }
    
    public void addProduct(Product product) {
        inventory.put(product.getId(), product);
    }

    public boolean removeProduct(String productId) {
        // Remove the product and check if it was present
        return inventory.remove(productId) != null;
    }
    
    public void sellProduct(String productId, int quantity) {
        Product product = inventory.get(productId);
        if (product != null && product.getQuantity() >= quantity) {
            product.adjustQuantity(-quantity);
        } else {
            System.out.println("Insufficient stock or product not found!");
        }
    }

    public Map<String, Product> getInventory() {
        return inventory;
    }

    // Save credentials back to the JSON file
    public void saveInventory(String branch) {
        InventoryFileHandler.saveInventoryToFile(inventory, branch);
    }
}
