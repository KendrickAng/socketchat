package config;

import log.Log;

public class Config {
    private String logLevel;
    private boolean isServer;
    private boolean isClient;
    private int port;

    public Config(String logLevel, boolean isServer, boolean isClient, int port) {
        this.logLevel = logLevel;
        this.isServer = isServer;
        this.isClient = isClient;
        this.port = port;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public boolean getIsServer() {
        return isServer;
    }

    public boolean getIsClient() {
        return isClient;
    }

    public int getPort() {
        return port;
    }

    public void validate() throws RuntimeException {
        validateLogLevel();
        validateServerAndClient();
    }

    private void validateLogLevel() throws RuntimeException {
        if (!Log.LEVELS.contains(logLevel)) {
            throw new RuntimeException(String.format("Invalid log level: %s", logLevel));
        }
    }

    private void validateServerAndClient() throws RuntimeException {
        if (isServer && isClient) {
            throw new RuntimeException("Cannot run in both server and client mode");
        }
        if (!isServer && !isClient) {
            throw new RuntimeException("Must run in either server or client mode");
        }
    }
}
