package server;

import models.Employee;
import utils.CustomerFileHandler;
import utils.EmployeeFileHandler;
import utils.InventoryFileHandler;

import java.util.HashMap;
import java.util.Map;


public class EmployeeManager {

    private static final Map<String, EmployeeManager> instances = new HashMap<>();

    static {
        // Automatically create instances for predefined branches
        initializeBranch("EILAT");
        initializeBranch("JERUSALEM");
    }

    // Use a HashMap to store employees, with the employee ID as the key
    private Map<String, Employee> employees;


    private EmployeeManager(String branch) {
        this.employees = EmployeeFileHandler.loadEmployeesFromFile(branch);
}

    private static void initializeBranch(String branch) {
        EmployeeManager instance = new EmployeeManager(branch);
        instances.put(branch, instance);
}

    // Public method to get an instance for a specific branch
    public static EmployeeManager getInstance(String branch) {
        if (instances.containsKey(branch)) {
            return instances.get(branch);
        } else {
            System.out.println("Branch not recognized. Please verify the branch name and try again.");
            return null;  // Return null to indicate that the branch is not valid
        }
    }

    // Add an employee
    public void addEmployee(Employee employee) {
        employees.put(employee.getEmployeeId(), employee);
    }

    // Remove an employee by ID
    public boolean removeEmployee(String employeeId) {
        return employees.remove(employeeId) != null;
    }

    // Get all employees as a collection
    public Map<String, Employee> getAllEmployees() {
        return employees;
    }

    public String getEmployeeUserName(String employeeId) {
        Employee employe = employees.get(employeeId);
        return employe.getUserName();
    }

    // Save employees back to the JSON file
    public void saveEmployees(String branch) {
        EmployeeFileHandler.saveEmployeesToFile(employees, branch);
    }
}

