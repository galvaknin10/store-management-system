package server;

import server.models.credentials.CredentialsFileHandler;
import server.models.customer.CustomerFileHandler;
import server.models.employee.EmployeeFileHandler;
import server.models.inventory.InventoryFileHandler;
import server.models.log.LogFileHandler;
import server.models.report.ReportFileHandler;
import server.utils.ClientInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        initializeRepositories();
        ConcurrentHashMap<String, ClientInfo> connectedClients = new ConcurrentHashMap<>();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());

                RequestHandler requestHandler = new RequestHandler(socket, connectedClients); 
                new Thread(requestHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void initializeRepositories() {
        System.out.println("Initializing repositories...");
        CustomerFileHandler.createDefaultCustomersRepo();
        EmployeeFileHandler.createDefaultEmployeesRepo();
        InventoryFileHandler.createDefaultInventoryRepo();
        CredentialsFileHandler.createDefaultCredentialsRepo();
        ReportFileHandler.createDefaultReportsRepo();
        LogFileHandler.createDefaultLogsRepo();
        System.out.println("Repositories initialized successfully.");
    }
}
