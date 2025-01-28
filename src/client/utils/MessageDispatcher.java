package client.utils;

import shared.*;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;


public class MessageDispatcher implements Runnable {
    private final ObjectInputStream input;
    private final BlockingQueue<Response> responseQueue;
    private final BlockingQueue<ChatMessage> chatQueue;
    private volatile boolean running = true;

    public MessageDispatcher(ObjectInputStream input, BlockingQueue<Response> responseQueue, BlockingQueue<ChatMessage> chatQueue) {
        this.input = input;
        this.responseQueue = responseQueue;
        this.chatQueue = chatQueue;
    }
    @Override
    public void run() {
        try {
            while (running) {
                Object obj = input.readObject();
    
                // Dispatch object to the appropriate queue
                if (obj instanceof Response response) {
                    responseQueue.put(response);
                } else if (obj instanceof ChatMessage chatMessage) {
                    chatQueue.put(chatMessage);
                } else {
                    System.err.println("Unexpected object type received: " + obj.getClass().getName());
                }
            }
        } catch (Exception e) {
            if (running) {
                System.err.println("Error in MessageDispatcher: " + e.getMessage());
            }
        }
    }
    
    public void stop() {
        running = false;
    }
}
