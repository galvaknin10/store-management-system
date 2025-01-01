package model.customer;

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

public class CustomerFileHandler {


    // Save customers to JSON file
    protected static void saveCustomersToFile(Map<String, Customer> customers, String branch) {
        String fileName = "data/" + branch + "_customers.json"; 
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Dynamically create the file name based on the branch
    
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(customers, writer);
            System.out.println("Customer successfully appended to the repository.");
        } catch (IOException e) {
            System.err.println("Error occurred while trying to append the customer to the repository.");
        }
    }


    protected static Map<String, Customer> loadCustomersFromFile(String branch) {
        String fileName = "data/" + branch + "_customers.json"; // File name for branch-specific customers
        File file = new File(fileName);
    
        if (!file.exists()) {
            System.out.println(branch + " is a brand new branch with no customer information available yet.");
            Map<String, Customer> customerRepo = new HashMap<>();
            createJsonFile(customerRepo, fileName);

        }
    
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            Type customerMapType = new TypeToken<Map<String, Customer>>() {}.getType();
            return gson.fromJson(reader, customerMapType);
        } catch (IOException e) {
            System.err.println("Error loading customers for branch '" + branch + "': " + e.getMessage());
        }
        return new HashMap<>(); // Return an empty map in case of error
    }


    public static void createDefaultCustomersRepo() {
        File eilatFile = new File("data/EILAT_customers.json");
        File jerusalemFile = new File("data/JERUSALEM_customers.json");

        if (!eilatFile.exists()) {
            Map<String, Customer> eilatRepo = new HashMap<>();
            eilatRepo.put("123456789", new Customer("Alice Brown", "123456789", "050-1234567", "New", "Eilat"));
            eilatRepo.put("987654321", new Customer("Bob Green", "987654321", "052-7654321", "VIP", "Eilat"));
            eilatRepo.put("111222333", new Customer("Charlie Blue", "111222333", "053-9876543", "Regular", "Eilat"));
            eilatRepo.put("444555666", new Customer("Diana Pink", "444555666", "054-6549873", "New", "Eilat"));
            createJsonFile(eilatRepo, "data/EILAT_customers.json");
        }

        if (!jerusalemFile.exists()) {
            Map<String, Customer> jerusalemRepo = new HashMap<>();
            jerusalemRepo.put("555666777", new Customer("Eve White", "555666777", "055-3216549", "New", "Jerusalem"));
            jerusalemRepo.put("888999000", new Customer("Frank Black", "888999000", "056-9517538", "VIP", "Jerusalem"));
            jerusalemRepo.put("123123123", new Customer("Grace Yellow", "123123123", "057-8529637", "VIP", "Jerusalem"));
            jerusalemRepo.put("321321321", new Customer("Hank Orange", "321321321", "058-1478523", "Regular", "Jerusalem"));
            createJsonFile(jerusalemRepo, "data/JERUSALEM_customers.json");
        }
    }

    // Create a default JSON file with sample customers for a specific branch
    private static void createJsonFile(Map<String, Customer> customerRepo, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(customerRepo, writer);
        } catch (IOException e) {
            System.err.println("Error occurred while creating specific JSON file: '" + fileName + "': " + e.getMessage());
        }
    }
}





