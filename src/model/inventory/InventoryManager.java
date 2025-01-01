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
    
    protected void addProduct(Product product) {
        inventory.put(product.getId(), product);
    }

    protected boolean removeProduct(String productId) {
        // Remove the product and check if it was present
        return inventory.remove(productId) != null;
    }
    
    public void sellProducts(Map<String, Integer> cart, String branch) {
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();
    
            Product product = inventory.get(productId);
            if (product != null) {
                if (product.getQuantity() > quantity) {
                    product.adjustQuantity(quantity);
                    saveInventory(branch); // Save changes to inventory
                    System.out.println(quantity + " units of " + product.getName() + " sold.");
                } else if (product.getQuantity() == quantity) {
                    inventory.remove(productId); // Remove the product from the inventory
                    saveInventory(branch);   // Save changes to inventory
                    System.out.println(product.getName() + " sold out and removed from inventory.");
                } else {
                    System.out.println("Insufficient stock for " + product.getName() + " (Requested: " + quantity + ", Available: " + product.getQuantity() + ").");
                }
            } else {
                System.out.println("Product with ID " + productId + " not found!");
            }
        }
    }

    public Product getProduct(String serialNum) {
        return inventory.get(serialNum);
    }
    

    public Map<String, Product> getInventory() {
        return inventory;
    }

    // Save credentials back to the JSON file
    protected void saveInventory(String branch) {
        InventoryFileHandler.saveInventoryToFile(inventory, branch);
    }
}
