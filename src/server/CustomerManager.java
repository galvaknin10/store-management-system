package server;

import models.Customer;
import utils.CustomerFileHandler;
import utils.EmployeeFileHandler;

import java.util.HashMap;
import java.util.Map;


public class CustomerManager {
    private static final Map<String, CustomerManager> instances = new HashMap<>();

    static {
        // Automatically create instances for predefined branches
        initializeBranch("EILAT");
        initializeBranch("JERUSALEM");
    }
    
    private Map<String, Customer> customers;
    
    private CustomerManager(String branch) {
        this.customers = CustomerFileHandler.loadCustomersFromFile(branch);
    }
    
    private static void initializeBranch(String branch) {
        CustomerManager instance = new CustomerManager(branch);
        instances.put(branch, instance);
    }
    
    public static CustomerManager getInstance(String branch) {
        if (instances.containsKey(branch)) {
            return instances.get(branch);
        } else {
            System.out.println("Branch not recognized. Please verify the branch name and try again.");
            return null;  // Return null to indicate that the branch is not valid
        }
    }
    

    // Add a new customer to the map
    public void addCustomer(Customer customer) {
        customers.put(customer.getIdNumber(), customer);
    }

    // Remove a customer by their ID number
    public boolean removeCustomer(String idNumber) {
        return customers.remove(idNumber) == null;
    }

    // Retrieve all customers
    public Map<String, Customer> getAllCustomers() {
        return customers;
    }

    // Find a customer by their ID number
    public Customer findCustomerById(String idNumber) {
        return customers.get(idNumber);
    }

    // Save customers for the branch
    public void saveCustomers(String branch) {
        CustomerFileHandler.saveCustomersToFile(customers, branch);
    }
}

