package model.inventory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class InventoryFileHandler {

    // Save inventory to a JSON file specific to a branch
    protected static void saveInventoryToFile(Map<String, Product> inventory, String branch) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String fileName = "data/" + branch + "_inventory.json"; // Dynamically create the file name based on the branch

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(inventory, writer);
        } catch (IOException e) {
            System.err.println("Error occurred while trying to append the inventory to the repository.");
        }
    }

    // Load inventory from a JSON file specific to a branch
    protected static Map<String, Product> loadInventoryFromFile(String branch) {
        String fileName = "data/" + branch + "_inventory.json"; // Dynamically create the file name based on the branch
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println(branch + " is a brand new branch with no inventory information available yet.");
            Map<String, Product> inventoryRepo = new HashMap<>();
            createJsonFile(inventoryRepo, fileName);
            return inventoryRepo;
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            Type inventoryType = new TypeToken<Map<String, Product>>() {}.getType();
            return gson.fromJson(reader, inventoryType);
        } catch (IOException e) {
            System.err.println("Error loading inventory for branch '" + branch + "': " + e.getMessage());
            return new HashMap<>(); // Return an empty map in case of error
        }
    }

    public static void createDefaultInventoryRepo() {
        File eilatFile = new File("data/EILAT_inventory.json");
        File jerusalemFile = new File("data/JERUSALEM_inventory.json");
    
        if (!eilatFile.exists()) {
            Map<String, Product> eilatInventory = new HashMap<>();
            eilatInventory.put("P001", new Product("P001", "iPhone 14 Pro", 1099.99, 20, "Eilat"));
            eilatInventory.put("P002", new Product("P002", "Samsung Galaxy S23", 899.99, 30, "Eilat"));
            createJsonFile(eilatInventory, "data/EILAT_inventory.json");
        }
    
        if (!jerusalemFile.exists()) {
            Map<String, Product> jerusalemInventory = new HashMap<>();
            jerusalemInventory.put("P003", new Product("P003", "Google Pixel 8", 799.99, 25, "Jerusalem"));
            jerusalemInventory.put("P004", new Product("P004", "OnePlus 11", 699.99, 15, "Jerusalem"));
            createJsonFile(jerusalemInventory, "data/JERUSALEM_inventory.json");
        }
    }
    

    // Create a default inventory JSON file for a specific branch
    private static void createJsonFile(Map<String, Product> inventoryRepo, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(inventoryRepo, writer);
        } catch (IOException e) {
            System.err.println("Error occurred while creating specific JSON file: '" + fileName + "': " + e.getMessage());
        }
    }
}

