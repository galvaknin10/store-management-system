package client.utils;

import shared.Request;
import shared.Response;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;


public class RequestSender {
    private final ObjectOutputStream output;
    private final BlockingQueue<Response> responseQueue;

    public RequestSender(ObjectOutputStream output, BlockingQueue<Response> responseQueue) {
        this.output = output;
        this.responseQueue = responseQueue;
    }

    public Response sendRequest(Request request) {
        try {
            // Send the request
            output.writeObject(request);
            output.flush();
    
            // Wait for the response from the queue
            return responseQueue.take();
        } catch (Exception e) {
            System.err.println("Error during communication: " + e.getMessage());
            return new Response(false, null, "Communication error.");
        }
    }    
}

