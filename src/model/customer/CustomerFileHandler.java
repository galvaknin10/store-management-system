package model.customer;

import com.google.gson.reflect.TypeToken;
import utils.FileHandlerUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CustomerFileHandler {

    // Save customers to JSON file
    protected static void saveCustomersToFile(Map<String, Customer> customers, String branch) {
        String fileName = "data/" + branch + "_customers.json";
        FileHandlerUtils.createJsonFile(customers, fileName);
    }

    // Load customers from JSON file
    protected static Map<String, Customer> loadCustomersFromFile(String branch) {
        String fileName = "data/" + branch + "_customers.json";
        Type customerMapType = new TypeToken<Map<String, Customer>>() {}.getType();

        Map<String, Customer> customers = FileHandlerUtils.loadJsonFile(fileName, customerMapType);

        if (customers == null) {
            System.out.println(branch + " is a brand new branch with no customer information available yet.");
            customers = new HashMap<>();
            saveCustomersToFile(customers, branch);
        }

        return customers;
    }

    // Create default customer repositories
    public static void createDefaultCustomersRepo() {
        FileHandlerUtils.ensureDataDirectoryExists();

        // Create Eilat customers file if it doesn't exist
        if (!FileHandlerUtils.fileExists("data/EILAT_customers.json")) {
            createDefaultRepo("data/EILAT_customers.json", Map.of(
                "123456789", new Customer("Alice Brown", "123456789", "050-1234567", "New", "Eilat"),
                "987654321", new Customer("Bob Green", "987654321", "052-7654321", "VIP", "Eilat"),
                "111222333", new Customer("Charlie Blue", "111222333", "053-9876543", "Regular", "Eilat"),
                "444555666", new Customer("Diana Pink", "444555666", "054-6549873", "New", "Eilat")
            ));
        }

        // Create Jerusalem customers file if it doesn't exist
        if (!FileHandlerUtils.fileExists("data/JERUSALEM_customers.json")) {
            createDefaultRepo("data/JERUSALEM_customers.json", Map.of(
                "555666777", new Customer("Eve White", "555666777", "055-3216549", "New", "Jerusalem"),
                "888999000", new Customer("Frank Black", "888999000", "056-9517538", "VIP", "Jerusalem"),
                "123123123", new Customer("Grace Yellow", "123123123", "057-8529637", "VIP", "Jerusalem"),
                "321321321", new Customer("Hank Orange", "321321321", "058-1478523", "Regular", "Jerusalem")
            ));
        }
    }

    private static void createDefaultRepo(String fileName, Map<String, Customer> defaultRepo) {
        String filePath = "data/" + fileName;
        if (!FileHandlerUtils.fileExists(filePath)) {
            FileHandlerUtils.createJsonFile(defaultRepo, filePath);
        }
    }
}



