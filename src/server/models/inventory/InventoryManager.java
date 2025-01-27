package server.models.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import server.models.log.LogController;
import server.models.report.ReportController;


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
        this.inventory = new ConcurrentHashMap<>(InventoryFileHandler.loadInventoryFromFile(branch));
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
    
    protected synchronized boolean addProduct(Product product, String branch) {
        if (inventory.containsKey(product.getSerialNum())) {
            return false;
        }
        inventory.put(product.getSerialNum(), product);
        InventoryFileHandler.saveInventoryToFile(inventory, branch);
        return true;
    }

    protected synchronized boolean removeProduct(String productId, String branch, int quantity) {
        if (inventory.get(productId) == null) {
            return false;
        }

        int currQuantity = inventory.get(productId).getQuantity();

        if (quantity < currQuantity) {
            inventory.get(productId).adjustQuantity(quantity);
        } else if (quantity == currQuantity) {
            inventory.remove(productId);
        } else {
            return false;
        }

        InventoryFileHandler.saveInventoryToFile(inventory, branch);
        return true;
    }
    
    public synchronized boolean checkOut(Map<String, Integer> cart, String branch) {
        if (cart.isEmpty()) {
            return false;
        }

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();
    
            Product product = inventory.get(productId);
            if (product != null) {
                if (product.getQuantity() > quantity) {
                    product.adjustQuantity(quantity);            
                } else if (product.getQuantity() == quantity) {
                    inventory.remove(productId); // Remove the product from the inventory
                } else {
                    return false;
                }
                InventoryFileHandler.saveInventoryToFile(inventory, branch);
                String productName = getProduct(productId).getName();
                LogController.logProductSale(branch, productName, quantity);
                ReportController.updateReport(branch, cart);
            }
        }
        return true;
    }


    public Product getProduct(String serialNum) {
        return inventory.get(serialNum);
    }

    public Map<String, Product> getInventory() {
        return inventory;
    }
}
