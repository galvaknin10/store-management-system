package server.models.customer;

import com.google.gson.reflect.TypeToken;
import server.utils.FileHandler;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class CustomerFileHandler {

    protected synchronized static void saveCustomersToFile(Map<String, Customer> customers, String branch) {
        String fileName = "data/" + branch + "_customers.json";
        FileHandler.createJsonFile(customers, fileName);
    }

    protected static Map<String, Customer> loadCustomersFromFile(String branch) {
        String fileName = "data/" + branch + "_customers.json";
        Type customerMapType = new TypeToken<Map<String, Customer>>() {}.getType();

        Map<String, Customer> customers = FileHandler.loadJsonFile(fileName, customerMapType);

        if (customers == null) {
            System.out.println(branch + " is a brand new branch with no customer information available yet.");
            customers = new HashMap<>();
            saveCustomersToFile(customers, branch);
        }

        return customers;
    }

    public static void createDefaultCustomersRepo() {
        FileHandler.ensureDataDirectoryExists();

        // Create Eilat customers file if it doesn't exist
        if (!FileHandler.fileExists("data/EILAT_customers.json")) {
            createDefaultRepo("data/EILAT_customers.json", Map.of(
                "308270394", new Customer("Yael Cohen", "308270394", "054-1234567", "New", "EILAT"),
                "212345678", new Customer("Avi Levi", "212345678", "052-7654321", "VIP", "EILAT"),
                "317852963", new Customer("Noa Bar", "317852963", "053-9876543", "Returning", "EILAT"),
                "401256987", new Customer("Itay Klein", "401256987", "050-6549873", "New", "EILAT")
            ));
        }

        // Create Jerusalem customers file if it doesn't exist
        if (!FileHandler.fileExists("data/JERUSALEM_customers.json")) {
            createDefaultRepo("data/JERUSALEM_customers.json", Map.of(
                "555666777", new Customer("Tamar Shamir", "555666777", "055-3216549", "New", "Jerusalem"),
                "888999000", new Customer("Yossi Peretz", "888999000", "056-9517538", "VIP", "Jerusalem"),
                "123123123", new Customer("Shira Avrahami", "123123123", "057-8529637", "VIP", "Jerusalem"),
                "321321321", new Customer("Nadav Ben-David", "321321321", "058-1478523", "Returning", "Jerusalem")
            ));
        }
    }

    private static void createDefaultRepo(String fileName, Map<String, Customer> defaultRepo) {
        FileHandler.createJsonFile(defaultRepo, fileName);
    }
}



