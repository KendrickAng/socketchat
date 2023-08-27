package chatserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class HandlerThread extends Thread {

    private static final Logger logger = Logger.getGlobal();

    private final InputStream inputStream;

    public HandlerThread(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            byte[] inputBytes = inputStream.readAllBytes();
            String msg = new String(inputBytes, Charset.defaultCharset());
            logger.info(String.format("Received message: %s", msg));
        } catch (IOException e) {
            logger.severe(String.format("Error handling connection: %s", e.getMessage()));
        }
    }
}
