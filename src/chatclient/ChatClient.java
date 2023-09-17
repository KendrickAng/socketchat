package chatclient;

import common.Constants;

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
        System.out.println("Welcome to ChatServer!");
        System.out.println("Enter your message below (press Enter to send):");
        System.out.printf("To quit, send the message '%s'\n", Constants.CLIENT_EXIT_MESSAGE);

        while (true) {
            String line = System.console().readLine();

            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(line.getBytes());
                outputStream.flush();
                logger.info(String.format("Sent message: %s", line));
            } catch (IOException e) {
                // log the message and don't retry (for now)
                logger.severe(String.format("Error sending message: %s", e.getMessage()));
            }

            if (line.equals(Constants.CLIENT_EXIT_MESSAGE)) {
                System.out.println(Constants.CLIENT_GOODBYE_MESSAGE);
                break;
            }
        }
    }
}
