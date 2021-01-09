package poltixe.github.flanchobotlibrary;

public class TestBot extends BotClient {
    public TestBot(String username, String plainPassword, char prefix) {
        super(username, plainPassword, prefix);
    }

    @Override
    public void onMessage(String sender, String target, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCommandMessage(String sender, String target, String command, String arguments) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAuthComplete() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBotDisconnect() {
        // TODO Auto-generated method stub

    }
}
