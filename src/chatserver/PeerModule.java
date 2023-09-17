package chatserver;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// Represents a peer node that connects to other peers in the network.
public class PeerModule {
    private final Logger logger = Logger.getGlobal();

    private final Socket clientSocket;
    private final List<PeerConnection> peerConnections;

    private PeerModule(Socket clientSocket, List<PeerConnection> peerConnections) {
        this.clientSocket = clientSocket;
        this.peerConnections = peerConnections;
    }

    public static PeerModule create(Socket clientSocket) {
        return new PeerModule(clientSocket, new ArrayList<>());
    }

    public static PeerModule createWithConnections(Socket clientSocket, List<PeerConnection> peerConnections) {
        return new PeerModule(clientSocket, peerConnections);
    }

    public void addPeer(PeerConnection peer) {
        peerConnections.add(peer);
    }

    public void removePeer(PeerConnection peer) {
        peerConnections.remove(peer);
    }

    public void broadcast(String message) {
        for (PeerConnection peerConnection : peerConnections) {
            peerConnection.sendMessage(String.format("%s: %s", getUniqueIdentifier(), message));
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public String getUniqueIdentifier() {
        return String.format("%s:%d", clientSocket.getInetAddress(), clientSocket.getPort());
    }

    public void close() {
        // nothing yet
    }
}
