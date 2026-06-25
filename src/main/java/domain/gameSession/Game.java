package domain.gameSession;

import domain.characters.Player;

public class Game {
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    Game() {}
}
