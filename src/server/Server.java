package server;

import server.models.credentials.CredentialsFileHandler;
import server.models.customer.CustomerFileHandler;
import server.models.employee.EmployeeFileHandler;
import server.models.inventory.InventoryFileHandler;
import server.models.log.LogFileHandler;
import server.models.report.ReportFileHandler;
import server.utils.ClientInfo;
import server.utils.RequestHandler;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        initializeRepositories();
    
        // A thread-safe map to store connected client information
        ConcurrentHashMap<String, ClientInfo> connectedClients = new ConcurrentHashMap<>();
    
        // Try to create a ServerSocket to listen for incoming client connections
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
    
            // Infinite loop to continuously accept client connections
            while (true) {
                // Accept a new client connection
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());
    
                try {
                    // Create streams for communication with the client
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
    
                    // Create a RequestHandler to manage the client connection
                    // Pass the socket, input/output streams, and the connectedClients map
                    RequestHandler requestHandler = new RequestHandler(socket, input, output, connectedClients);
    
                    // Start a new thread to handle this client's requests
                    new Thread(requestHandler).start();
    
                } catch (IOException e) {
                    // Handle errors while initializing streams for the client
                    System.err.println("Error initializing streams for client: " + e.getMessage());
                    // Close the socket to release resources
                    socket.close();
                }
            }
        } catch (IOException e) {
            // Handle server-level errors (e.g., unable to bind to the port)
            System.err.println("Server error: " + e.getMessage());
        }
    }
    

    private static void initializeRepositories() {
        CustomerFileHandler.createDefaultCustomersRepo();
        EmployeeFileHandler.createDefaultEmployeesRepo();
        InventoryFileHandler.createDefaultInventoryRepo();
        CredentialsFileHandler.createDefaultCredentialsRepo();
        ReportFileHandler.createDefaultReportsRepo();
        LogFileHandler.createDefaultLogsRepo();
    }
}
