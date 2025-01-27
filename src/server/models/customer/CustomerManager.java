package server.models.customer;

import java.util.HashMap;
import java.util.Map;


public class CustomerManager {
    
    private static final HashMap<String, CustomerManager> instances = new HashMap<>();

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
    protected boolean addCustomer(Customer customer, String branch) {
        if (customers.containsKey(customer.getIdNumber())) {
            return false; // Customer already exists
        }
        customers.put(customer.getIdNumber(), customer);
        CustomerFileHandler.saveCustomersToFile(customers, branch);
        return true; // Successfully added
    }
    

    // Remove a customer by their ID number
    protected boolean removeCustomer(String idNumber, String branch) {
        if (customers.remove(idNumber) != null) {
            CustomerFileHandler.saveCustomersToFile(customers, branch);
            return true;
        }
        return false;
    }

    // Retrieve all customers
    public Map<String, Customer> getAllCustomers() {
        return customers;
    }

    public Customer getCustomer(String id) {
        return customers.get(id);
    }

    public double getDiscountForType(String type) {
        switch (type.toLowerCase()) {
            case "vip":
                return 0.20; // 20% discount
            case "returning":
                return 0.10; // 10% discount
            case "new":
                return 0.05; // 5% discount
            default:
                return 0.0;  // No discount
        }
    }

    public Object[] calculateDiscount(String customerId, double totalSum) {
        String customerType = getCustomer(customerId).getType();
        double discount = getDiscountForType(customerType);
        double discountedTotal = totalSum * (1 - discount);
        return new Object[]{customerType, discount, discountedTotal};
    }
}

