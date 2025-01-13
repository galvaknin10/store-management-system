package server.models.employee;

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
    protected boolean addEmployee(Employee employee, String branch) {
        if (employees.containsKey(employee.getUserName())) {
            return false;
        }
        employees.put(employee.getUserName(), employee);
        EmployeeFileHandler.saveEmployeesToFile(employees, branch);
        return true;
    }

    // Remove an employee by ID
    protected boolean removeEmployee(String userName, String branch) {
        if (employees.remove(userName) != null) {
            EmployeeFileHandler.saveEmployeesToFile(employees, branch);
            return true;
        }
        return false;
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
}

