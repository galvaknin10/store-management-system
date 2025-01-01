package model.employee;

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
    protected void addEmployee(Employee employee) {
        employees.put(employee.getUserName(), employee);
    }

    // Remove an employee by ID
    protected boolean removeEmployee(String userName) {
        return employees.remove(userName) != null;
    }

    // Get all employees as a collection
    public Map<String, Employee> getAllEmployees() {
        return employees;
    }

    public Employee getEmployee(String userName) {
        Employee employee = employees.get(userName);
        return employee;
    }


    public boolean validateEmployeeId(String userName) {
        return employees.containsKey(userName);
    }

    // Save employees back to the JSON file
    protected void saveEmployees(String branch) {
        EmployeeFileHandler.saveEmployeesToFile(employees, branch);
    }
}

