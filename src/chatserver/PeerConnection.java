package chatserver;

import java.net.Socket;
import java.nio.CharBuffer;
import java.util.logging.Logger;

// Encapsulates a connection to a peer node.
public class PeerConnection {
    private final Logger logger = Logger.getGlobal();
    private final Socket peer;

    private PeerConnection(Socket peer) {
        this.peer = peer;
    }

    public static PeerConnection of(Socket socket) {
        return new PeerConnection(socket);
    }

    public void sendMessage(String message) {
        try {
            peer.getOutputStream().write(message.getBytes());
        } catch (Exception e) {
            logger.severe(String.format("Error sending message to peer %s:%d: %s",
                    peer.getInetAddress(), peer.getPort(), e.getMessage()));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PeerConnection other) {
            return peer.getInetAddress().equals(other.peer.getInetAddress())
                    && peer.getPort() == other.peer.getPort();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return peer.getInetAddress().hashCode() + peer.getPort();
    }
}
