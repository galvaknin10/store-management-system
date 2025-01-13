package server.models.inventory;


public class InventoryController {

    public static boolean addProduct(String branch, String SerialNum, String name, double price, int quantity) {
        InventoryManager inventoryManager = InventoryManager.getInstance(branch);
        Product product = new Product(SerialNum, name, price, quantity, branch);
        boolean isAdeed = inventoryManager.addProduct(product, branch);
        if (isAdeed) {
            return true;
        }
        return false;
    }

    public static boolean removeProduct(String branch, String SerialNum) {
        InventoryManager inventoryManager = InventoryManager.getInstance(branch);
        boolean isRemoved = inventoryManager.removeProduct(SerialNum, branch);
        if (isRemoved) {
            return true;
        }
        return false;
    }
}
