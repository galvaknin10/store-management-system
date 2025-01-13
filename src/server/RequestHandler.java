package server;

import server.models.credentials.*;
import server.models.customer.*;
import server.models.employee.*;
import server.models.inventory.*;
import server.models.log.*;
import server.models.report.*;
import server.utils.ClientInfo;
import shared.Request;
import shared.Response;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;


public class RequestHandler implements Runnable {
    private final Socket socket;
    private final ConcurrentHashMap<String, ClientInfo> connectedClients;
    private ClientInfo clientInfo;

    public RequestHandler(Socket socket, ConcurrentHashMap<String, ClientInfo> connectedClients) {
        this.socket = socket;
        this.connectedClients = connectedClients;
    }

    @Override
    public void run() {
        try (
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())
        ) {
            while (true) {
                Object requestObj = input.readObject();
                if (requestObj instanceof Request request) {
                    Response response = handleRequest(request);
                    output.writeObject(response);
                } else {
                    System.out.println("Invalid request from client.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Client disconnected: " + e.getMessage());
            if (clientInfo != null) {
                connectedClients.remove(clientInfo.getUsername());
                System.out.println("Client removed: " + clientInfo);
            }
        }
    }
    
    
    private Response handleRequest(Request request) {
        String action = request.getAction();
        Object data = request.getData();

        switch (action) {
            case "VIEW_INVENTORY" -> {
                String branch = (String) data;
                InventoryManager inventoryManager = InventoryManager.getInstance(branch);
                Map<String, Product> inventory = inventoryManager.getInventory();
                if (inventory == null || inventory.isEmpty()) {
                    return new Response(false, null, "Failed retrieving inventory.");
                } else {
                    Map<String, Map<String, Object>> inventoryAsMaps = new HashMap<>();
                    for (Map.Entry<String, Product> entry : inventory.entrySet()) {
                        String serialNum = entry.getKey();
                        Product product = entry.getValue();
                        // Convert Employee to Map<String, Object> using Gson
                        Map<String, Object> productMap = new Gson().fromJson(new Gson().toJson(product), Map.class);
                        inventoryAsMaps.put(serialNum, productMap);
                    }
                    return new Response(true, inventoryAsMaps, "*Inventory retrieved successfully*");
                }
            }
            case "GET_PRODUCT_DETAILS" -> {
                Object[] productInfo = (Object[]) data;
                String branch = (String) productInfo[0];
                String serialNum = (String) productInfo[1];
            
                InventoryManager inventoryManager = InventoryManager.getInstance(branch);
                Product product = inventoryManager.getProduct(serialNum);
            
                if (product != null) {
                    // Convert the Product to a Map using Gson
                    Map<String, Object> productMap = new Gson().fromJson(new Gson().toJson(product), Map.class);
                    return new Response(true, productMap, "*Product retrieved successfully*");
                } else {
                    return new Response(false, null, "Failed retrieving product. Product not found.");
                }
            }
            
            case "ADD_PRODUCT" -> {
                Object[] productInfo = (Object[]) data;
                String branch = (String) productInfo[0];
                String serialNum = (String) productInfo[1];
                String name = (String) productInfo[2];
                double price = (double) productInfo[3];
                int quantity = (int) productInfo[4];
                if (InventoryController.addProduct(branch, serialNum, name, price, quantity)) {
                    return new Response(true, null, "*Product added successfully*");
                } else {
                    return new Response(false, null, "Failed adding product.");
                }
            }
            case "REMOVE_PRODUCT" -> {
                Object[] productInfo = (Object[]) data;
                String branch = (String) productInfo[0];
                String serialNum = (String) productInfo[1];
                if (InventoryController.removeProduct(branch, serialNum)) {
                    return new Response(true, null, "*Product removed successfully*");
                } else {
                    return new Response(false, null, "Failed removing product.");
                }
            }
            case "FIND_CUSTOMER" -> {
                Object[] customerInfo = (Object[]) data;
                String branch = (String) customerInfo[0];
                String id = (String) customerInfo[1];
                CustomerManager customerManager = CustomerManager.getInstance(branch);
                if (customerManager.getCustomer(id) != null) {
                    return new Response(true, null, "*Customer is in the system*");
                } else {
                    return new Response(false, null, "Customer was not found in the system.");
                }
            }
            case "CALCULATE_DISCOUNT" -> {
                Object[] customerInfo = (Object[]) data;
                String branch = (String) customerInfo[0];
                String customerId = (String) customerInfo[1];
                double totalSum = (double) customerInfo[2];
                CustomerManager customerManager = CustomerManager.getInstance(branch);
                Object[] discountInfo = customerManager.calculateDiscount(customerId, totalSum);
                if (totalSum == 0) {
                    return new Response(false, null, "No products selected. Total sum is 0. Please try again.");
                } else {
                    return new Response(true, discountInfo, "*Discount information retrieved successfully*");
                }
            }
            case "CHECKOUT" -> {
                Object[] checkoutData = (Object[]) data;
                String branch = (String) checkoutData[0];
                Map<String, Integer> cart = (Map<String, Integer>) checkoutData[1];
                InventoryManager inventoryManager = InventoryManager.getInstance(branch);
                if (inventoryManager.checkOut(cart, branch)) {
                    return new Response(true, null, "*Transaction completed successfully!*");
                } else {
                    return new Response(false, null, "Failed completing transaction, check your cart.");
                }
            }
            case "VALIDATE_BRANCH" -> {
                String branch = (String) data;
                if ("EILAT".equalsIgnoreCase(branch) || "JERUSALEM".equalsIgnoreCase(branch)) {
                    return new Response(true, branch.toUpperCase(), "*Branch validated successfully*");
                } else {
                    return new Response(false, null, "Invalid branch name.");
                }
            }
            case "REGISTER_USER" -> {
                Object[] userData = (Object[]) data;
                String userName = (String) userData[0];
                String password = (String) userData[1];
                String branch = (String) userData[2];

                if (CredentialController.addUser(userName, password, branch)) {
                    return new Response(true, null, "*User registered successfully*");
                } else {
                    return new Response(false, null, "Failed to register user, check user name or password.");
                }
            }
            case "AUTHENTICATE_USER" -> {
                Object[] credentials = (Object[]) data;
                String username = (String) credentials[0];
                String password = (String) credentials[1];
                String branch = (String) credentials[2];
            
                synchronized (connectedClients) { // Ensure thread-safe access
                    if (connectedClients.containsKey(username)) {
                        return new Response(false, null, "Username is already connected.");
                    }
                }

                CredentialsManager credentialsManager = CredentialsManager.getInstance(branch);
                if (credentialsManager.authenticate(username, password)) {
                    // Add to connected clients after successful authentication
                    clientInfo = new ClientInfo(username, branch, socket);
                    connectedClients.put(username, clientInfo);
                    System.out.println("Client authenticated: " + clientInfo);
                    return new Response(true, null, "*Authentication successful*");
                } else {
                    return new Response(false, null, "Invalid username or password.");
                }
            }
            case "GET_EMPLOYEE_ROLE" -> {
                Object[] employeeInfo = (Object[]) data;
                String username = (String) employeeInfo[0];
                String branch = (String) employeeInfo[1];
                EmployeeManager employeeManager = EmployeeManager.getInstance(branch);
                Employee employee = employeeManager.getEmployee(username);

                if (employee != null) {
                    return new Response(true, employee.getRole(), "*Role retrieved successfully*");
                } else {
                    return new Response(false, null, "Employee not found.");
                }
            }
            case "REMOVE_EMPLOYEE" -> {
                Object[] employeeInfo = (Object[]) data;
                String branch = (String) employeeInfo[0];
                String userName = (String) employeeInfo[1];
                if (EmployeeController.removeEmployee(branch, userName)) {
                    return new Response(true, null, "*Employee removed successfully!*");
                } else {
                    return new Response(false, null, "Failed removing employee");
                }
            }
            case "VIEW_EMPLOYEES" -> {
                String branch = (String) data;
                EmployeeManager employeeManager = EmployeeManager.getInstance(branch);
                Map<String, Employee> employees = employeeManager.getAllEmployees();

                if (employees == null || employees.isEmpty()) {
                    return new Response(false, null, "Failed retrieving employee list.");
                } else {
                    // Convert employees to Map<String, Map<String, Object>>
                    Map<String, Map<String, Object>> employeesAsMaps = new HashMap<>();
                    for (Map.Entry<String, Employee> entry : employees.entrySet()) {
                        String id = entry.getKey();
                        Employee employee = entry.getValue();
                        // Convert Employee to Map<String, Object> using Gson
                        Map<String, Object> employeeMap = new Gson().fromJson(new Gson().toJson(employee), Map.class);
                        employeesAsMaps.put(id, employeeMap);
                    }
                    return new Response(true, employeesAsMaps, "*Employee list retrieved successfully*");
                }
            }
            case "ADD_CUSTOMER" -> {
                Object[] customerInfo = (Object[]) data;
                String branch = (String) customerInfo[0];
                String customerId = (String) customerInfo[1];
                String name = (String) customerInfo[2];
                String phoneNumber = (String) customerInfo[3];
                String type = (String) customerInfo[4];
                if (CustomerController.addCustomer(branch, customerId, name, phoneNumber, type)) {
                    return new Response(true, null, "*Customer added successfully*");
                } else {
                    return new Response(false, null, "Failed to add customer.");
                }
            }
            case "REMOVE_CUSTOMER" -> {
                Object[] customerInfo = (Object[]) data;
                String branch = (String) customerInfo[0];
                String customerId = (String) customerInfo[1];
                if (CustomerController.removeCustomer(branch, customerId)) {
                    return new Response(true, null, "*Customer removed successfully!*");
                } else {
                    return new Response(false, null, "Failed to remove customer.");
                }
            }
            case "VIEW_CUSTOMERS" -> {
                String branch = (String) data;
                CustomerManager customerManager = CustomerManager.getInstance(branch);
                Map<String, Customer> customers = customerManager.getAllCustomers();
                if (customers == null || customers.isEmpty()) {
                    return new Response(false, null, "Failed retrieving inventory.");
                } else {
                    Map<String, Map<String, Object>> customersAsMaps = new HashMap<>();
                    for (Map.Entry<String, Customer> entry : customers.entrySet()) {
                        String id = entry.getKey();
                        Customer customer = entry.getValue();
                        // Convert Employee to Map<String, Object> using Gson
                        Map<String, Object> customerMap = new Gson().fromJson(new Gson().toJson(customer), Map.class);
                        customersAsMaps.put(id, customerMap);
                    }
                    return new Response(true, customersAsMaps, "*Customer list retrieved successfully*");
                }
            }
            case "GET_REPORT" -> {
                Object[] reportInfo = (Object[]) data;
                String branch = (String) reportInfo[0];
                String date = (String) reportInfo[1];
                ReportManager reportManager = ReportManager.getInstance(branch);
                Report report = reportManager.getReport(date);
                if (report == null) {
                    return new Response(false, null, "Failed retrieving specific report.");
                } else {
                    Map<String, Object> reportMap = new Gson().fromJson(new Gson().toJson(report), Map.class);
                    return new Response(true, reportMap, "*Report retrieved successfully*");
                }
            }
            case "VIEW_REPORTS" -> {
                String branch = (String) data;
                ReportManager reportManager = ReportManager.getInstance(branch);
                Map<String, Report> reports = reportManager.getAllReports();
                if (reports == null || reports.isEmpty()) {
                    return new Response(false, null, "Failed retrieving reports.");
                } else {
                    // Convert employees to Map<String, Map<String, Object>>
                    Map<String, Map<String, Object>> reportsAsMaps = new HashMap<>();
                    for (Map.Entry<String, Report> entry : reports.entrySet()) {
                        String date = entry.getKey();
                        Report report = entry.getValue();
                        // Convert Employee to Map<String, Object> using Gson
                        Map<String, Object> reportMap = new Gson().fromJson(new Gson().toJson(report), Map.class);
                        reportsAsMaps.put(date, reportMap);
                    }
                    return new Response(true, reportsAsMaps, "*Reports retrieved successfully*");
                }
            }
            case "DELETE_REPORT" -> {
                Object[] reportInfo = (Object[]) data;
                String branch = (String) reportInfo[0];
                String date = (String) reportInfo[1];
                if (ReportController.removeReport(branch, date)) {
                    return new Response(true, null, "*Report successfully removed*");
                } else {
                    return new Response(false, null, "Failed to remove specfic report.");
                }
            }
            case "ADD_EMPLOYEE" -> {
                Object[] employeeInfo = (Object[]) data;
                String branch = (String) employeeInfo[0];
                String employeeId = (String) employeeInfo[1];
                String name = (String) employeeInfo[2];
                String phoneNumber = (String) employeeInfo[3];
                String accountNumber = (String) employeeInfo[4];
                String role = (String) employeeInfo[5];
                String userName = (String) employeeInfo[6];
                if (EmployeeController.addEmployee(branch, employeeId, name, phoneNumber, accountNumber, role, userName)) {
                    return new Response(true, null, "*Employee added successfully*");
                } else {
                    return new Response(false, null, "Failed to add employee.");
                }
            }           
            case "GET_LOGS" -> {
                String branch = (String) data;
                LogManager logManager = LogManager.getInstance(branch);
                List<Log> logs = logManager.getLogs();
            
                if (logs == null || logs.isEmpty()) {
                    return new Response(false, null, "No logs available.");
                } else {
                    // Convert List<Log> to List<Map<String, Object>> using Gson
                    List<Map<String, Object>> logsAsMaps = new ArrayList<>();
                
                    for (Log log : logs) {
                        // Convert each Log object to a JSON string and then back to a Map
                        Map<String, Object> logMap = new Gson().fromJson(new Gson().toJson(log), Map.class);
                        logsAsMaps.add(logMap);
                    }
                    
                    return new Response(true, logsAsMaps, "*Logs retrieved successfully*");
                }
            }       
            default -> {
                return new Response(false, null, "Unknown action: " + action);
            }
        }
    }
}


