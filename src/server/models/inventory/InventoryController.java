package server.models.inventory;


public class InventoryController {

    // Add a product to the repository
    public static boolean addProduct(String branch, String SerialNum, String name, double price, int quantity) {
        InventoryManager inventoryManager = InventoryManager.getInstance(branch);
        Product product = new Product(SerialNum, name, price, quantity, branch);
        boolean isAdeed = inventoryManager.addProduct(product, branch);
        if (isAdeed) {
            return true;
        }
        return false;
    }

    // Remove product from the repository
    public static boolean removeProduct(String branch, String SerialNum, int quantity) {
        InventoryManager inventoryManager = InventoryManager.getInstance(branch);
        boolean isRemoved = inventoryManager.removeProduct(SerialNum, branch, quantity);
        if (isRemoved) {
            return true;
        }
        return false;
    }
}
