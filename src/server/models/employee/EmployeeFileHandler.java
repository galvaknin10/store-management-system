package server.models.employee;

import com.google.gson.reflect.TypeToken;
import server.utils.FileHandler;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class EmployeeFileHandler {

    protected synchronized static void saveEmployeesToFile(Map<String, Employee> employees, String branch) {
        String fileName = "data/" + branch + "_employees.json";
        FileHandler.createJsonFile(employees, fileName);
    }

    protected static Map<String, Employee> loadEmployeesFromFile(String branch) {
        String fileName = "data/" + branch + "_employees.json";
        Type employeeMapType = new TypeToken<Map<String, Employee>>() {}.getType();

        Map<String, Employee> employees = FileHandler.loadJsonFile(fileName, employeeMapType);

        if (employees == null) {
            System.out.println(branch + " is a brand new branch with no employee information available yet.");
            employees = new HashMap<>();
            saveEmployeesToFile(employees, branch);
        }

        return employees;
    }

    public static void createDefaultEmployeesRepo() {
        FileHandler.ensureDataDirectoryExists();

        // Create Eilat employees file if it doesn't exist
        if (!FileHandler.fileExists("data/EILAT_employees.json")) {
            createDefaultRepo("data/EILAT_employees.json", Map.of(
                "galUser1", new Employee("Gal", "123456789", "050-1234567", "111111", "Eilat", "Manager", "galUser1"),
                "harelUser3", new Employee("Harel", "223456789", "052-7654321", "222222", "Eilat", "Cashier", "harelUser3"),
                "adiUser9", new Employee("Adi", "223456766", "052-7654543", "222253", "Eilat", "Salesperson", "adiUser9")
            ));
        }
        if (!FileHandler.fileExists("data/JERUSALEM_employees.json")) {
            createDefaultRepo("data/JERUSALEM_employees.json", Map.of(
                "naorUser2", new Employee("Naor", "523456789", "055-3216549", "555555", "Jerusalem", "Manager", "naorUser2"),
                "davidUser4", new Employee("David", "623456789", "056-9517538", "666666", "Jerusalem", "Salesperson", "davidUser4"), 
                "dennisUser7", new Employee("Dennis", "623456446", "056-9517342", "666753", "Jerusalem", "Cashier", "dennisUser7")
            ));
        }
    }
    // Helper method to create a default repository
    private static void createDefaultRepo(String fileName, Map<String, Employee> defaultRepo) {
        FileHandler.createJsonFile(defaultRepo, fileName);
    }
}



