package model.customer;


public class CustomerController {
    public static void addCustomerToRepo(CustomerManager customerManager, Customer customer, String branch) {
        customerManager.addCustomer(customer);
        customerManager.saveCustomers(branch);
    }

    public static boolean RemoveCustomerFromRepo(CustomerManager customerManager, String customerId, String branch) {
        boolean isRemoveSucceed = customerManager.removeCustomer(customerId);
        if (isRemoveSucceed) {
            customerManager.saveCustomers(branch);
        }
        return isRemoveSucceed;
    }
}