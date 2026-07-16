package presentation;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import domain.characters.Enemies;
import domain.characters.enemies.Zombie;
import domain.gameSession.Game;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Game game = new Game();
//        game.start();

        Terminal terminal = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(180, 70))
                .createTerminal();

        Screen screen = new TerminalScreen(terminal);
        TextGraphics tg = screen.newTextGraphics();
        screen.startScreen();
        tg.putString(game.getPlayer().getPosition().getX(), game.getPlayer().getPosition().getY(), "@");
        List<Enemies> enemiesList = game.getAllEnemiesList();
        for (Enemies enemies : enemiesList) {
            switch (enemies.getType()) {
                case OGRE -> tg.putString(enemies.getPosition().getX(), enemies.getPosition().getY(), "o");
            }
        }
        screen.refresh();

    }
}