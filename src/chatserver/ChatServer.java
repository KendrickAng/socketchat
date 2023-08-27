package chatserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ChatServer {
    private static final Logger logger = Logger.getGlobal();

    private final ServerSocket serverSocket;
    private final List<Thread> synchronizedThreads;

    public ChatServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.synchronizedThreads = Collections.synchronizedList(new ArrayList<>());
    }

    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // have to use sout here because logger is already shut down in a separate shutdown hook
                System.out.println("Shutdown: closing server socket");
                serverSocket.close();
                System.out.println("Shutdown: server socket closed");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.printf("Shutdown: closing %d client thread(s)\n", synchronizedThreads.size());
            for (Thread thread : synchronizedThreads) {
                thread.interrupt();
            }
            System.out.println("Shutdown: all client threads closed");
        }));

        // create a server socket that listens on all local addresses on the given port
        acceptConnections(serverSocket);
    }

    private void acceptConnections(ServerSocket serverSocket) {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                logger.info(String.format("Accepted connection from %s:%d",
                        clientSocket.getInetAddress(), clientSocket.getPort()));

                handleConnectionInThread(clientSocket);
            } catch (IOException e) {
                logger.severe(String.format("Error accepting connection: %s", e.getMessage()));
            }
        }
    }

    private void handleConnectionInThread(Socket clientSocket) throws IOException {
        HandlerThread handlerThread = new HandlerThread(clientSocket.getInputStream());
        synchronizedThreads.add(handlerThread);
        handlerThread.start();

        try {
            handlerThread.join(Duration.ofSeconds(30));
        } catch (InterruptedException e) {
            logger.info(String.format("Server: thread interrupted: %s\n", e.getMessage()));
        } finally {
            synchronizedThreads.remove(handlerThread);
        }
    }
}
