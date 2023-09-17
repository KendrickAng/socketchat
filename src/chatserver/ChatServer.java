package chatserver;

import common.Constants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class ChatServer {
    private static final Logger logger = Logger.getGlobal();

    private final ServerSocket serverSocket;
    private final List<PeerModule> clients;
    private final AtomicBoolean isRunning;

    public ChatServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.clients = Collections.synchronizedList(new ArrayList<>());
        this.isRunning = new AtomicBoolean(true);
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

            System.out.printf("Shutdown: closing %d client(s)\n", clients.size());
            for (PeerModule client : clients) {
                client.close();
            }
            System.out.println("Shutdown: all client(s) closed");
        }));

        // create a server socket that listens on all local addresses on the given port
        Thread t1 = serveClients();
        Thread t2 = acceptConnections();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            logger.info(String.format("Server: thread interrupted: %s\n", e.getMessage()));
        }
    }

    private Thread serveClients() {
        Thread t = new Thread(() -> {
            while (isRunning.get()) {
                // multiple clients may exit in the same cycle
                List<PeerModule> toRemove = Collections.synchronizedList(new ArrayList<>());

                synchronized (clients) {
                    for (PeerModule client : clients) {
                        try {
                            // read input from one client
                            Socket socket = client.getClientSocket();

                            if (socket.getInputStream().available() > 0) {
                                byte[] buffer = new byte[socket.getInputStream().available()];
                                @SuppressWarnings("unused")
                                int read = socket.getInputStream().read(buffer);

                                // convert the client's input to a string
                                String clientInput = new String(buffer, StandardCharsets.UTF_8);

                                // if client input an exit command, remove them from the list of clients
                                if (clientInput.equals(Constants.CLIENT_EXIT_MESSAGE)) {
                                    toRemove.add(client);
                                }

                                // broadcast the client's input to all other clients
                                client.broadcast(clientInput);

                                logger.info(String.format("%s sent message: %s", client.getUniqueIdentifier(), clientInput));
                            }
                        } catch (IOException e) {
                            logger.severe(String.format("Error reading client input: %s", e.getMessage()));
                        }
                    }
                }

                // remove clients that entered an exit input this cycle
                for (PeerModule client : toRemove) {
                    clients.remove(client);
                    logger.info(String.format("Removed client %s", client.getUniqueIdentifier()));
                }
            }
        });
        t.start();

        return t;
    }

    private Thread acceptConnections() {
        Thread t = new Thread(() -> {
            while (isRunning.get()) {
                try {
                    // accept new connection
                    Socket clientSocket = serverSocket.accept();

                    // create a new peer module with the new connection and all existing connections
                    PeerModule newPeer = PeerModule.createWithConnections(
                            clientSocket,
                            currentConnections()
                    );

                    // all clients should know about the new peer
                    PeerConnection clientConnection = PeerConnection.of(clientSocket);
                    for (PeerModule client : clients) {
                        client.addPeer(clientConnection);
                    }

                    // add the new peer to the list of clients
                    clients.add(newPeer);

                    logger.info(String.format("Accepted connection from %s:%d",
                            clientSocket.getInetAddress(), clientSocket.getPort()));
                } catch (IOException e) {
                    logger.severe(String.format("Error accepting connection: %s", e.getMessage()));
                }
            }
        });
        t.start();

        return t;
    }

    private List<PeerConnection> currentConnections() {
        List<PeerConnection> connections = new ArrayList<>();

        for (PeerModule client : clients) {
            connections.add(PeerConnection.of(client.getClientSocket()));
        }

        return connections;
    }
}
