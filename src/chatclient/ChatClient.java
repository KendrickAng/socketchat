package chatclient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ChatClient {
    private static final Logger logger = Logger.getGlobal();

    private final Socket socket;

    public ChatClient(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        // TODO allow customization of message
        String message = "Hello from client";

        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            // log the message and don't retry (for now)
            logger.severe(String.format("Error sending message: %s", e.getMessage()));
        }
    }
}
