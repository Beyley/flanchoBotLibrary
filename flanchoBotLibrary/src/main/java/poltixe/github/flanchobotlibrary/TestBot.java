package poltixe.github.flanchobotlibrary;

public class TestBot extends BotClient {
    public TestBot(String username, String plainPassword, char prefix) {
        super(username, plainPassword, prefix);
    }

    @Override
    public void onMessage(String sender, String target, String message) {

    }

    @Override
    public void onCommandMessage(String sender, String target, String command, String[] arguments) {

    }

    @Override
    public void onAuthComplete() {

    }

    @Override
    public void onBotDisconnect() {

    }
}
