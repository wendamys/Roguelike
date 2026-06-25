package domain.gameSession;

public class GameSession {
    private final Game game;

    public GameSession() {
        this.game = new Game();
    }

    public Game getGame() {
        return game;
    }
}