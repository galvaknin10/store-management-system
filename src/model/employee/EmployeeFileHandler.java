package model.employee;

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

public class EmployeeFileHandler {

    // Save a map of employees to the JSON file specific to a branch
    protected static void saveEmployeesToFile(Map<String, Employee> employees, String branch) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String fileName = "data/" + branch + "_employees.json"; // Dynamically create the file name based on the branch

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(employees, writer);
        } catch (IOException e) {
            System.err.println("Error occurred while trying to append the employee to the repository.");
        }
    }

    // Load a map of employees from a JSON file specific to a branch
    protected static Map<String, Employee> loadEmployeesFromFile(String branch) {
        String fileName = "data/" + branch + "_employees.json"; // Dynamically create the file name based on the branch
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println(branch + " is a brand new branch with no employee information available yet.");
            Map<String, Employee> employeeRepo = new HashMap<>();
            createJsonFile(employeeRepo, fileName);
            return employeeRepo;
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            Type employeeMapType = new TypeToken<Map<String, Employee>>() {}.getType();
            return gson.fromJson(reader, employeeMapType);
        } catch (IOException e) {
            System.err.println("Error loading employees for branch '" + branch + "': " + e.getMessage());
            return new HashMap<>(); // Return an empty map in case of error
        }
    }

    // Create a default JSON file with sample employees for a specific branch
    public static void createDefaultEmployeesRepo() {
        File eilatFile = new File("data/EILAT_employees.json");
        File jerusalemFile = new File("data/JERUSALEM_employees.json");

        if (!eilatFile.exists()) {
            Map<String, Employee> eilatRepo = new HashMap<>();
            eilatRepo.put("galUser1", new Employee("Gal Vaknin", "123456789", "050-1234567", "111111", "Eilat", "Manager", "galUser1"));
            eilatRepo.put("danUser3", new Employee("Dan", "223456789", "052-7654321", "222222", "Eilat", "Cashier", "danUser3"));
            createJsonFile(eilatRepo, "data/EILAT_employees.json");
        }

        if (!jerusalemFile.exists()) {
            Map<String, Employee> jerusalemRepo = new HashMap<>();
            jerusalemRepo.put("rioUser2", new Employee("Rio", "523456789", "055-3216549", "555555", "Jerusalem", "Manager", "rioUser2"));
            jerusalemRepo.put("davidUser4", new Employee("David", "623456789", "056-9517538", "666666", "Jerusalem", "Salesperson", "davidUser4"));
            createJsonFile(jerusalemRepo, "data/JERUSALEM_employees.json");
        }
    }

    // Create a default JSON file with sample employees for a specific branch
    private static void createJsonFile(Map<String, Employee> employeeRepo, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(employeeRepo, writer);
        } catch (IOException e) {
            System.err.println("Error occurred while creating specific JSON file: '" + fileName + "': " + e.getMessage());
        }
    }
}


