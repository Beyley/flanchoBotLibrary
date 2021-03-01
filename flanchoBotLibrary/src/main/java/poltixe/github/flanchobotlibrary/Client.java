package poltixe.github.flanchobotlibrary;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.parser.ParseException;

public class Client extends WebSocketClient {
    BotClient botClient;

    public Client(URI serverURI, BotClient botClient) {
        super(serverURI);
        this.botClient = botClient;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // botClient.connect();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        this.botClient.console.printError("Connection to Flandrecho Failed! Reason: " + reason + ", Code: " + code);

        this.botClient.connected = false;
        this.botClient.stoptimers();
        this.botClient.onBotDisconnect();

        this.botClient.doReconnect = true;

        // try {
        // this.botClient.client = new Client(new URI(this.botClient.ip),
        // this.botClient);
        // this.botClient.client.setTcpNoDelay(true);
        // } catch (URISyntaxException e) {
        // }
        // this.botClient.client.setTcpNoDelay(true);

        // this.botClient.connect();
    }

    @Override
    public void onMessage(String message) {
    }

    @Override
    public void onMessage(ByteBuffer message) {
        try {
            this.botClient.readPacket(message);
        } catch (InterruptedException | IOException | ParseException e) {
        }
    }

    @Override
    public void onError(Exception ex) {
        this.botClient.console.printError("Connection to Flandrecho Failed! Reason: " + ex.getMessage());

        this.botClient.connected = false;
        this.botClient.stoptimers();
        this.botClient.onBotDisconnect();

        this.botClient.doReconnect = true;

        // try {
        // this.botClient.client = new Client(new URI(this.botClient.ip),
        // this.botClient);
        // this.botClient.client.setTcpNoDelay(true);
        // } catch (URISyntaxException e) {
        // }

        // this.botClient.connect();
    }
}
