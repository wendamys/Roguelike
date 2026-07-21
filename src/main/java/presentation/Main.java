package presentation;

import domain.gameSession.Game;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new Controller(new Game(), new UIView()).run();
    }
}
