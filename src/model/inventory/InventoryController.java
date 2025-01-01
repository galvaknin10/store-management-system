package model.inventory;


public class InventoryController {

    public static void addProductToRepo(InventoryManager inventoryManager, Product product, String branch) {
        inventoryManager.addProduct(product);
        inventoryManager.saveInventory(branch);
    }

    public static boolean removeProductFromRepo(InventoryManager inventoryManager, String id, String branch) {
        boolean isProductRemoved = inventoryManager.removeProduct(id);
        if (isProductRemoved) {
            inventoryManager.saveInventory(branch);
        }
        return isProductRemoved;
    }
}