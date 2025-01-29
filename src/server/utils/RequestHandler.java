package server.utils;

import server.models.credentials.*;
import server.models.customer.*;
import server.models.employee.*;
import server.models.inventory.*;
import server.models.log.*;
import server.models.report.*;
import shared.ChatMessage;
import shared.Request;
import shared.Response;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import com.google.gson.Gson;


public class RequestHandler implements Runnable {
    private final Socket socket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private ClientInfo clientInfo;
    private final ConcurrentHashMap<String, ClientInfo> connectedClients;

    public RequestHandler(Socket socket, ObjectInputStream input, ObjectOutputStream output,
                          ConcurrentHashMap<String, ClientInfo> connectedClients) {
        this.socket = socket;
        this.input = input;
        this.output = output;
        this.connectedClients = connectedClients;
    }

    @Override
    public void run() {
        try {
            // Continuously listen for client requests in a loop
            while (true) {
                // Read an object sent by the client
                Object requestObj = input.readObject();
    
                // Check if the received object is a valid Request
                if (requestObj instanceof Request request) {
                    // Handle the request and generate a response
                    Response response = handleRequest(request);
    
                    // Send the response back to the client
                    output.writeObject(response);
                    output.flush(); // Ensure the response is sent immediately
                } else {
                    // Log a message if the object is not a valid Request
                    System.out.println("Invalid request from client.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions (e.g., client disconnects or invalid data)
            if (clientInfo != null) {
                System.err.println(clientInfo.getUsername() + " Disconnected from the system.");
            } else {
                System.err.println("Unknown client Disconnected from the system.");
            }
        } finally {
            // Ensure all resources (streams and socket) are closed properly
            closeStreams();
        }
    }
        
    private void closeStreams() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ex) {
            System.err.println("Error closing client resources: " + ex.getMessage());
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
                    return new Response(false, null, "Failed to add product. Please try again.");
                }
            }
            case "REMOVE_PRODUCT" -> {
                Object[] productInfo = (Object[]) data;
                String branch = (String) productInfo[0];
                String serialNum = (String) productInfo[1];
                int quantity = (int) productInfo[2];

                if (InventoryController.removeProduct(branch, serialNum, quantity)) {
                    return new Response(true, null, "*Product removed successfully*");
                } else {
                    return new Response(false, null, "Failed to remove product, Check quantity or serial number...");
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
                    return new Response(false, null, "Customer not found in the system.");
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
            
                if (connectedClients.containsKey(username)) {
                    return new Response(false, null, "This user is already connected.");
                }

                CredentialsManager credentialsManager = CredentialsManager.getInstance(branch);
                if (credentialsManager.authenticate(username, password)) {
                    EmployeeManager employeeManager = EmployeeManager.getInstance(branch);
                    String role = employeeManager.getEmployee(username).getRole();

                    // Add to connected clients after successful authentication
                    clientInfo = new ClientInfo(username, branch, role, output);
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
            case "CHECK_IF_MANAGER" -> {
                String userName = (String) data;
                EmployeeManager employeeManagerEilat = EmployeeManager.getInstance("EILAT");
                EmployeeManager employeeManagerJerusalem = EmployeeManager.getInstance("JERUSALEM");

                Employee option1 = employeeManagerEilat.getEmployee(userName);
                Employee option2 = employeeManagerJerusalem.getEmployee(userName);

                if ((option1 != null && option1.getRole().equals("Manager")) || (option2 != null && option2.getRole().equals("Manager"))) {
                    return new Response(true, null, "Sender is a manager.");
                } else {
                    return new Response(false, null, "Sender is not a manager.");
                }
            }
            case "REMOVE_EMPLOYEE" -> {
                Object[] employeeInfo = (Object[]) data;
                String branch = (String) employeeInfo[0];
                String userName = (String) employeeInfo[1];
                if (EmployeeController.removeEmployee(branch, userName)) {
                    return new Response(true, null, "*Employee removed successfully!*");
                } else {
                    return new Response(false, null, "Failed to remove employee");
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
                    return new Response(false, null, "Failed retrieving customers list.");
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
            case "START_CHAT" -> {
                String requesterName = (String) data; // Requester username
                String partnerUserName = ChatManager.getInstance(connectedClients).startChat(requesterName);
                if (partnerUserName != null) {
                    return new Response(true, partnerUserName, "*Available client has been found!*");
                } else {
                    return new Response(false, null, "All clients are currently busy. There's a chance that a busy client will return to you, or you can try again later.");
                }
            }
            case "ADD_CHAT_SESSION" -> {
                Object[] massageInfo = (Object[]) data;
                String sessionId = (String) massageInfo[0];
                String userName = (String) massageInfo[1];
                String partnerUserName = (String) massageInfo[2];
                
                if (ChatManager.getInstance(connectedClients).confirmChat(sessionId, userName, partnerUserName)) {
                    return new Response(true, null, "*Chat successfully created!*");
                } else {
                    return new Response(false, null, "Failed creating chat.");
                }
            }
            case "SEND_MESSAGE" -> {
                Object[] massageInfo = (Object[]) data;
                String sessionId = (String) massageInfo[0];
                ChatMessage message = (ChatMessage) massageInfo[1];

                if (ChatManager.getInstance(connectedClients).sendMessage(message)) {
                    SessionManager.trackMessages(sessionId, message);

                    return new Response(true, null, "*Message sent successfully.*");
                } else {
                    return new Response(false, null, "Failed to send the message.");
                }
            }
            case "END_CHAT" -> {
                String sessionId = (String) data; // Username ending the chat
                if (ChatManager.getInstance(connectedClients).endChat(sessionId)) {
                    SessionManager.removeStagedChat(sessionId);
                    return new Response(true, null, "*Successfully exit the chat.*");
                } else {
                    return new Response(false, null, "Failed to exit chat.");
                }
            }
            case "MAKE_PARTNER_ONLINE" -> {
                Object[] massageInfo = (Object[]) data;
                String sessionId = (String) massageInfo[0];
                String userName = (String) massageInfo[1];
                ChatSession session = SessionManager.getSession(sessionId);

                if (session == null) {
                    return new Response(false, null, "Can't reach the partner now");
                }

                if (userName.equals(session.getPartnerUserName())) {
                    session.setClientBOnline(true);
                }
                return new Response(true, null, "*The partner is now online.*");
            }
            case "IS_PARTNER_TURN" -> {
                Object[] sessionInfo = (Object[]) data;
                String sessionId = (String) sessionInfo[0];
                String partnerUserName = (String) sessionInfo[1];
                ChatSession session = SessionManager.getSession(sessionId);
                boolean partnerTurn = true;

                if (partnerUserName.equals(session.getPartnerUserName())) {
                    partnerTurn = session.isClientBTurn();
                }
                
                if (partnerUserName.equals(session.getUserName())) {
                    partnerTurn = session.isClientATurn();
                }

                if (partnerTurn) {
                    return new Response(true, null, "Its partner turn.");
                } else {
                    return new Response(false, null, "Its your turn.");
                }
            }
            case "IS_PARTNER_ONLINE" -> {
                Object[] sessionInfo = (Object[]) data;
                String sessionId = (String) sessionInfo[0];
                String partnerUserName = (String) sessionInfo[1];
                ChatSession session = SessionManager.getSession(sessionId);
                boolean partnerOnline = true;

                if (partnerUserName.equals(session.getPartnerUserName())) {
                    partnerOnline = session.isClientBOnline();
                }
                
                if (partnerUserName.equals(session.getUserName())) {
                    partnerOnline = session.isClientAOnline();
                }

                if (partnerOnline) {
                    return new Response(true, null, "*Partner is active in a live chat.*");
                } else {
                    return new Response(false, null, "Partner is offline.");
                }
            }
            case "IS_PARTNER_EXIT" -> {
                Object[] sessionInfo = (Object[]) data;
                String sessionId = (String) sessionInfo[0];
                String partnerUserName = (String) sessionInfo[1];
                ChatSession session = SessionManager.getSession(sessionId);
                boolean partnerExit = true;

                if (partnerUserName.equals(session.getPartnerUserName())) {
                    partnerExit = session.isClientBExit();
                }
                
                if (partnerUserName.equals(session.getUserName())) {
                    partnerExit = session.isClientAExit();
                }

                if (partnerExit) {
                    return new Response(true, null, "*Partner exit from chat.*");
                } else {
                    return new Response(false, null, "Partner didnt exit from chat.");
                }
            }
            case "UPDATE_TURN" -> {
                Object[] sessionInfo = (Object[]) data;
                String sessionId = (String) sessionInfo[0];
                String userName = (String) sessionInfo[1];
                String partnerUserName = (String) sessionInfo[2];
                ChatSession session = SessionManager.getSession(sessionId);

                if (session == null) {
                    return new Response(false, null, "Can't reach partner now...");
                }

                if (userName.equals(session.getUserName()) && partnerUserName.equals(session.getPartnerUserName())) {
                    session.setClientATurn(false);
                    session.setClientBTurn(true);
                }

                if (userName.equals(session.getPartnerUserName()) && partnerUserName.equals(session.getUserName())) {
                    session.setClientBTurn(false);
                    session.setClientATurn(true);
                }
                
                return new Response(true, null, "State updated.");
            }
            case "UPDATE_EXIT" -> {
                Object[] sessionInfo = (Object[]) data;
                String sessionId = (String) sessionInfo[0];
                String userName = (String) sessionInfo[1];
                ChatSession session = SessionManager.getSession(sessionId);

                if (session == null) {
                    return new Response(false, null, "Failed to exit from chat");
                }
                
                if (userName.equals(session.getUserName())) {
                    session.setClientAExit(true);
                }

                if (userName.equals(session.getPartnerUserName())) {
                    session.setClientBExit(true);
                }

                return new Response(true, null, "You have successfully exited the chat.");
            }
            case "LOG_CHAT" -> {
                String sessionId = (String) data;
                List<ChatMessage> chatLog = SessionManager.getSortedChatLog(sessionId);
                if (chatLog != null) {
                    for (ChatMessage message : chatLog) {
                        LogController.logChatContent(message);
                    }
                    return new Response(true, null, "*Chat logged successfully.*");
                } else {
                    return new Response(false, null, "Failed to log chat, chat log is empty.");
                }
            }
            case "INTERRUPT_LIVE_CHAT" -> {
                ConcurrentHashMap<String, List<ChatMessage>> chatLogs = SessionManager.getChatLogs();

                if (chatLogs.isEmpty()) {
                    return new Response(false, null, "There is no any active chat right now.");
                }
 
                Object[] keys = chatLogs.keySet().toArray();
                
                if (keys.length == 0) {
                    return null;
                }
  
                int randomIndex = ThreadLocalRandom.current().nextInt(keys.length);
                String randomSessionId = (String) keys[randomIndex];
                List<ChatMessage> chatLog = SessionManager.getSortedChatLog(randomSessionId);
                

                ChatSession sessionInfo = SessionManager.getSession(randomSessionId);
                String userName;

                if (sessionInfo.isClientATurn()) {
                    userName = sessionInfo.getPartnerUserName();
                } else {
                    userName = sessionInfo.getUserName();
                }

                return new Response(true, new Object[]{userName, chatLog}, "Found some live chat for you!");
            }
            case "IS_MANAGRE_INTERRUPT" -> {
                Object[] sessionInfo = (Object[]) data;
                String sessionId = (String) sessionInfo[0];
                String userName = (String) sessionInfo[1];
                ChatSession session = SessionManager.getSession(sessionId);
                boolean userNameExit = true;

                if (userName.equals(session.getUserName())) {
                    userNameExit = session.isClientAExit();
                }
                
                if (userName.equals(session.getPartnerUserName())) {
                    userNameExit = session.isClientBExit();
                }

                if (userNameExit) {
                    return new Response(true, null, "Manager interrupt.");
                } else {
                    return new Response(false, null, "Manaegr doesn't interrupt.");
                }
            } 
            case "DISCONNECT" -> {
                String userName = (String) data;

                if (connectedClients.containsKey(userName)) {
                    connectedClients.remove(userName);
                    System.out.println(clientInfo.getUsername() + " Logout from the system");
                } else {
                    System.out.println("Unknown client disconnected from the server.");
                }
                return new Response(true, null, "Disconnected successfully.");
            }
            default -> {
                return new Response(false, null, "Unknown action: " + action);
            }
        }
    }
}


