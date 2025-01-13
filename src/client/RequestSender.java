package client;

import shared.Request;
import shared.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class RequestSender {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public RequestSender(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        //System.out.println("Connected to the server!");
    }
    
    public Response sendRequest(Request request) {
        try {
            System.out.println("Sending request: " + request.getAction());
            output.writeObject(request); // Send the request to the server
            output.flush();
    
            synchronized (input) { // Synchronize access to the input stream
                Response response = (Response) input.readObject(); // Receive the server's response
                System.out.println("Recive respond");
                if (response == null) {
                    System.err.println("Null response received from server.");
                    return new Response(false, null, "Server error: Null response.");
                }
                return response;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during communication: " + e.getMessage());
            return new Response(false, null, "Communication error.");
        }
    }
    

    public ObjectInputStream getInputStream() {
        return input;
    }
        
    public void closeConnection() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
