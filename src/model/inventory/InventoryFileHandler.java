package model.inventory;

import com.google.gson.reflect.TypeToken;
import utils.FileHandlerUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class InventoryFileHandler {

    // Save inventory to a JSON file specific to a branch
    protected static void saveInventoryToFile(Map<String, Product> inventory, String branch) {
        String fileName = "data/" + branch + "_inventory.json";
        FileHandlerUtils.createJsonFile(inventory, fileName);
    }

    // Load inventory from a JSON file specific to a branch
    protected static Map<String, Product> loadInventoryFromFile(String branch) {
        String fileName = "data/" + branch + "_inventory.json";
        Type inventoryType = new TypeToken<Map<String, Product>>() {}.getType();

        Map<String, Product> inventory = FileHandlerUtils.loadJsonFile(fileName, inventoryType);

        if (inventory == null) {
            System.out.println(branch + " is a brand new branch with no inventory information available yet.");
            inventory = new HashMap<>();
            saveInventoryToFile(inventory, branch);
        }

        return inventory;
    }

    // Create default inventory repositories
    public static void createDefaultInventoryRepo() {
        FileHandlerUtils.ensureDataDirectoryExists();

        // Create Eilat inventory file if it doesn't exist
        if (!FileHandlerUtils.fileExists("data/EILAT_inventory.json")) {
            createDefaultRepo("data/EILAT_inventory.json", Map.of(
                "P001", new Product("P001", "iPhone 14 Pro", 1099.99, 20, "Eilat"),
                "P002", new Product("P002", "Samsung Galaxy S23", 899.99, 30, "Eilat")
            ));
        }

        // Create Jerusalem inventory file if it doesn't exist
        if (!FileHandlerUtils.fileExists("data/JERUSALEM_inventory.json")) {
            createDefaultRepo("data/JERUSALEM_inventory.json", Map.of(
                "P003", new Product("P003", "Google Pixel 8", 799.99, 25, "Jerusalem"),
                "P004", new Product("P004", "OnePlus 11", 699.99, 15, "Jerusalem")
            ));
        }
    }

    // Helper method to create a default repository
    private static void createDefaultRepo(String fileName, Map<String, Product> defaultRepo) {
        FileHandlerUtils.createJsonFile(defaultRepo, fileName);
    }
}


