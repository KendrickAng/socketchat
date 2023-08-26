import config.Config;
import flag.BoolVar;
import flag.Flag;
import flag.IntegerVar;
import flag.StringVar;
import log.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getGlobal();

    public static void main(String[] args) {
        StringVar flagLogLevel = Flag.string("log-level", Log.INFO, String.format("Logging level. One of %s", String.join(",", Log.LEVELS)));
        BoolVar flagServer = Flag.bool("server", false, "Run in server mode");
        BoolVar flagClient = Flag.bool("client", false, "Run in client mode");
        IntegerVar flagPort = Flag.integer("port", 9090, "Server mode: port to listen for connections. Client mode: port to connect to");
        Flag.parse(args);

        Config config = new Config(
                /*logLevel=*/flagLogLevel.getValue(),
                /*isServer=*/flagServer.getValue(),
                /*isClient=*/flagClient.getValue(),
                /*port=*/flagPort.getValue());
        config.validate();

        System.out.println("Starting program with the following configs:");
        System.out.println("  Log Level: " + config.getLogLevel());
        System.out.println("  Port:      " + config.getPort());
        if (config.getIsClient()) System.out.println("  Mode:      Client");
        if (config.getIsServer()) System.out.println("  Mode:      Server");

        Logger.getGlobal().setLevel(Level.parse(flagLogLevel.getValue()));

        run(config);
    }

    private static void run(Config config) {
        if (config.getIsServer()) {
            runServer(config.getPort());
        } else if (config.getIsClient()) {
            runClient(config.getPort());
        } else {
            throw new AssertionError("unreachable");
        }
    }

    private static void runServer(int port) {
        // create a server socket that listens on all local addresses on the given port
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info(String.format("Listening on port %d", port));

            AtomicBoolean listening = new AtomicBoolean(true);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutting down server");
                listening.set(false);
                Thread.currentThread().interrupt();
                logger.info("Server shutdown complete");
            }));

            while (listening.get()) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    logger.info(String.format("Accepted connection from %s:%d%n", clientSocket.getInetAddress(), clientSocket.getPort()));

                    handleConnectionInThread(clientSocket);
                } catch (IOException e) {
                    logger.severe(String.format("Error accepting connection: %s", e.getMessage()));
                }
            }
        } catch (IOException e) {
            logger.severe(String.format("Error creating server socket: %s", e.getMessage()));
        }
    }

    private static void runClient(int port) {
        try (Socket socket = new Socket("localhost", port)) {
            logger.info(String.format("Connected to %s:%d", socket.getInetAddress(), socket.getPort()));

            String message = "Hello from client";
            socket.getOutputStream().write(message.getBytes());
        } catch (IOException e) {
            logger.severe(String.format("Error creating client socket: %s", e.getMessage()));
        }
    }

    private static void handleConnectionInThread(Socket clientSocket) {
        HandlerThread handlerThread = new HandlerThread(clientSocket);
        handlerThread.start();

        try {
            handlerThread.join();
        } catch (InterruptedException e) {
            logger.severe(String.format("Error handling connection: %s", e.getMessage()));
        }
    }
}

class HandlerThread extends Thread {

    private static final Logger logger = Logger.getGlobal();
    private final Socket clientSocket;

    public HandlerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            byte[] inputBytes = clientSocket.getInputStream().readAllBytes();

            String msg = new String(inputBytes, Charset.defaultCharset());

            logger.info(String.format("Received message from %s:%s: %s",
                    clientSocket.getInetAddress(), clientSocket.getPort(), msg));
        } catch (IOException e) {
            logger.severe(String.format("Error handling connection: %s", e.getMessage()));
        }
    }
}
