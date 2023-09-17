import chatclient.ChatClient;
import chatserver.ChatServer;
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
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s %n");

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
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info(String.format("Server: listening on port %d", serverSocket.getLocalPort()));
            new ChatServer(serverSocket).run();
        } catch (IOException e) {
            // log error and return - exiting the program
            logger.severe(String.format("Error in server socket: %s", e.getMessage()));
        }
    }

    private static void runClient(int port) {
        try (Socket socket = new Socket("localhost", port)) {
            logger.info(String.format("Client: connected to %s:%d", socket.getInetAddress(), socket.getPort()));
            new ChatClient(socket).run();
        } catch (IOException e) {
            // log error and return - exiting the program
            logger.severe(String.format("Error in client socket: %s", e.getMessage()));
        }
    }
}
