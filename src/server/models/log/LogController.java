package server.models.log;


public class LogController {

    // Add a log entry for a specific branch
    public static void addRecordToLog(String branch, String action, String details) {
        LogManager logManager = LogManager.getInstance(branch);
        logManager.addLog(action, details, branch);

    }

    public static void logUserCreation(String branch, String userName) {
        addRecordToLog(branch, "Create User Name", "User Name created: " + userName);
    }

    public static void logUserRemoval(String branch, String userName) {
        addRecordToLog(branch, "Remove User Name", "User name removed: " + userName);
    }

    public static void logEmployeeRemoval(String branch, String employeeName) {
        addRecordToLog(branch, "Remove Employee", "Employee removed: " + employeeName);
    }

    // Add a log entry for creating an employee
    public static void logEmployeeCreation(String branch, String employeeName) {
        addRecordToLog(branch, "Create Employee", "Employee created: " + employeeName);
    }

    public static void logCustomerRemoval(String branch, String customerName) {
        addRecordToLog(branch, "Remove Customer", "Customer created: " + customerName);
    }

    // Add a log entry for creating a customer
    public static void logCustomerCreation(String branch, String customerName) {
        addRecordToLog(branch, "Create Customer", "Customer created: " + customerName);
    }

    // Add a log entry for selling a product
    public static void logProductSale(String branch, String productName, int quantity) {
        addRecordToLog(branch, "Sell Product", "Product sold: " + productName + ", Quantity: " + quantity);
    }

    // Add a log entry for starting a chat
    public static void logChatInitiation(String branch, String fromUser, String toUser) {
        addRecordToLog(branch, "Chat Initiation", "Chat started between " + fromUser + " and " + toUser);
    }

    // Add a log entry for saving chat content
    public static void logChatContent(String branch, String fromUser, String toUser, String chatContent) {
        addRecordToLog(branch, "Chat Content Saved", "Chat between " + fromUser + " and " + toUser + ": " + chatContent);
    }
}

