package server.models.customer;

import server.models.log.LogController;


public class CustomerController {

    // Add a customer to the repository
    public static boolean addCustomer(String branch, String customerId, String name, String phoneNumber, String type) {
        CustomerManager customerManager = CustomerManager.getInstance(branch);
        Customer customer = new Customer(name, customerId, phoneNumber, type, branch);
        boolean isAdded = customerManager.addCustomer(customer,branch);
        if (isAdded) {
            LogController.logCustomerCreation(branch, name);
            return true;
        }
        return false;
    }

    // Remove a customer from the repository
    public static boolean removeCustomer(String branch, String customerId) {
        CustomerManager customerManager = CustomerManager.getInstance(branch);
        String name = customerManager.getCustomer(customerId).getName();
        boolean isRemoved = customerManager.removeCustomer(customerId, branch);
        if (isRemoved) {
            LogController.logCustomerRemoval(branch, name);
            return true;
        }
        return false;
    }
}
