package server.models.inventory;

import com.google.gson.reflect.TypeToken;
import server.utils.FileHandler;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class InventoryFileHandler {

    // Save inventory to a JSON file specific to a branch
    protected static void saveInventoryToFile(Map<String, Product> inventory, String branch) {
        String fileName = "data/" + branch + "_inventory.json";
        FileHandler.createJsonFile(inventory, fileName);
    }

    // Load inventory from a JSON file specific to a branch
    protected static Map<String, Product> loadInventoryFromFile(String branch) {
        String fileName = "data/" + branch + "_inventory.json";
        Type inventoryType = new TypeToken<Map<String, Product>>() {}.getType();

        Map<String, Product> inventory = FileHandler.loadJsonFile(fileName, inventoryType);

        if (inventory == null) {
            System.out.println(branch + " is a brand new branch with no inventory information available yet.");
            inventory = new HashMap<>();
            saveInventoryToFile(inventory, branch);
        }

        return inventory;
    }

    // Create default inventory repositories
    public static void createDefaultInventoryRepo() {
        FileHandler.ensureDataDirectoryExists();

        // Create Eilat inventory file if it doesn't exist
        if (!FileHandler.fileExists("data/EILAT_inventory.json")) {
            createDefaultRepo("data/EILAT_inventory.json", Map.of(
                "P001", new Product("P001", "iPhone 14 Pro", 1099.99, 20, "Eilat"),
                "P002", new Product("P002", "Samsung Galaxy S23", 899.99, 30, "Eilat"),
                "P003", new Product("P003", "MacBook Air M2", 1199.99, 15, "Eilat"),
                "P004", new Product("P004", "Dell XPS 13", 1399.99, 10, "Eilat"),
                "P005", new Product("P005", "Sony WH-1000XM5 Headphones", 399.99, 25, "Eilat"),
                "P006", new Product("P006", "Apple Watch Series 8", 499.99, 18, "Eilat"),
                "P007", new Product("P007", "JBL Charge 5 Speaker", 149.99, 40, "Eilat")
            ));
        }

        // Create Jerusalem inventory file if it doesn't exist
        if (!FileHandler.fileExists("data/JERUSALEM_inventory.json")) {
            createDefaultRepo("data/JERUSALEM_inventory.json", Map.of(
                "P003", new Product("P003", "Google Pixel 8", 799.99, 25, "Jerusalem"),
                "P004", new Product("P004", "OnePlus 11", 699.99, 15, "Jerusalem"),
                "P005", new Product("P005", "Asus ROG Phone 7", 999.99, 10, "Jerusalem"),
                "P006", new Product("P006", "Xiaomi 13 Pro", 749.99, 20, "Jerusalem"),
                "P007", new Product("P007", "Sony Xperia 1 V", 1099.99, 5, "Jerusalem"),
                "P008", new Product("P008", "Apple iPad Pro 12.9", 1199.99, 12, "Jerusalem"),
                "P009", new Product("P009", "Samsung Galaxy Tab S9", 899.99, 18, "Jerusalem")
            ));
        }
    }

    // Helper method to create a default repository
    private static void createDefaultRepo(String fileName, Map<String, Product> defaultRepo) {
        FileHandler.createJsonFile(defaultRepo, fileName);
    }
}


