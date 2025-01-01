package model.employee;

import com.google.gson.reflect.TypeToken;
import utils.FileHandlerUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class EmployeeFileHandler {

    // Save a map of employees to the JSON file specific to a branch
    protected static void saveEmployeesToFile(Map<String, Employee> employees, String branch) {
        String fileName = "data/" + branch + "_employees.json";
        FileHandlerUtils.createJsonFile(employees, fileName);
    }

    // Load a map of employees from a JSON file specific to a branch
    protected static Map<String, Employee> loadEmployeesFromFile(String branch) {
        String fileName = "data/" + branch + "_employees.json";
        Type employeeMapType = new TypeToken<Map<String, Employee>>() {}.getType();

        Map<String, Employee> employees = FileHandlerUtils.loadJsonFile(fileName, employeeMapType);

        if (employees == null) {
            System.out.println(branch + " is a brand new branch with no employee information available yet.");
            employees = new HashMap<>();
            saveEmployeesToFile(employees, branch);
        }

        return employees;
    }

    // Create a default repository of employees for all branches
    public static void createDefaultEmployeesRepo() {
        FileHandlerUtils.ensureDataDirectoryExists();

        // Create Eilat employees file if it doesn't exist
        if (!FileHandlerUtils.fileExists("data/EILAT_employees.json")) {
            createDefaultRepo("data/EILAT_employees.json", Map.of(
                "galUser1", new Employee("Gal Vaknin", "123456789", "050-1234567", "111111", "Eilat", "Manager", "galUser1"),
                "danUser3", new Employee("Dan", "223456789", "052-7654321", "222222", "Eilat", "Cashier", "danUser3")
            ));
        }

        // Create Jerusalem employees file if it doesn't exist
        if (!FileHandlerUtils.fileExists("data/JERUSALEM_employees.json")) {
            createDefaultRepo("data/JERUSALEM_employees.json", Map.of(
                "rioUser2", new Employee("Rio", "523456789", "055-3216549", "555555", "Jerusalem", "Manager", "rioUser2"),
                "davidUser4", new Employee("David", "623456789", "056-9517538", "666666", "Jerusalem", "Salesperson", "davidUser4")
            ));
        }
    }
    // Helper method to create a default repository
    private static void createDefaultRepo(String fileName, Map<String, Employee> defaultRepo) {
        FileHandlerUtils.createJsonFile(defaultRepo, fileName);
    }
}



