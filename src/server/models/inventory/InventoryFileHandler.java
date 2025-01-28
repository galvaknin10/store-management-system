package server.models.inventory;

import com.google.gson.reflect.TypeToken;
import server.utils.FileHandler;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class InventoryFileHandler {

    // Save inventory to a JSON file specific to a branch
    protected synchronized static void saveInventoryToFile(Map<String, Product> inventory, String branch) {
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
                "P001", new Product("P001", "Men's Leather Jacket", 199.99, 20, "Eilat"),
                "P002", new Product("P002", "Women's Winter Coat", 249.99, 30, "Eilat"),
                "P003", new Product("P003", "Classic Blue Jeans", 49.99, 50, "Eilat"),
                "P004", new Product("P004", "Formal Dress Shirt", 39.99, 40, "Eilat"),
                "P005", new Product("P005", "Running Shoes", 89.99, 25, "Eilat"),
                "P006", new Product("P006", "Summer T-Shirt", 19.99, 60, "Eilat"),
                "P007", new Product("P007", "Casual Sneakers", 69.99, 35, "Eilat")

            ));
        }

        // Create Jerusalem inventory file if it doesn't exist
        if (!FileHandler.fileExists("data/JERUSALEM_inventory.json")) {
            createDefaultRepo("data/JERUSALEM_inventory.json", Map.of(
                "P003", new Product("P003", "Men's Hoodie", 39.99, 25, "Jerusalem"),
                "P004", new Product("P004", "Women's Maxi Dress", 59.99, 15, "Jerusalem"),
                "P005", new Product("P005", "Leather Handbag", 129.99, 10, "Jerusalem"),
                "P006", new Product("P006", "Men's Formal Trousers", 49.99, 20, "Jerusalem"),
                "P007", new Product("P007", "Women's High Heels", 89.99, 5, "Jerusalem"),
                "P008", new Product("P008", "Kids' Winter Jacket", 59.99, 12, "Jerusalem"),
                "P009", new Product("P009", "Sports Backpack", 79.99, 18, "Jerusalem")
            ));
        }
    }

    // Helper method to create a default repository
    private static void createDefaultRepo(String fileName, Map<String, Product> defaultRepo) {
        FileHandler.createJsonFile(defaultRepo, fileName);
    }
}


