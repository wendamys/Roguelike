package presentation;

import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.input.KeyStroke;
import domain.gameSession.GameFacade;
import java.io.IOException;

public class Controller {
    private final GameFacade game;
    private final Screen screen;

    public Controller(GameFacade game, Screen screen) {
        this.game = game;
        this.screen = screen;
    }

    public void input() throws IOException {
        KeyStroke key = screen.readInput();

        if (key != null) {
            String command = null;

            if(key.getKeyType() == KeyType.Character) {
                switch(Character.toLowerCase(key.getCharacter())) {
                    case 'w' -> command = "w";
                    case 's' -> command = "s";
                    case 'a' -> command = "a";
                    case 'd' -> command = "d";
                    case 'e' -> command = "e";
                    case 'h' -> command = "h";
                    case 'j' -> command = "j";
                    case 'k' -> command = "k";
                    case '0' -> command = "0";
                    case '1' -> command = "1";
                    case '2' -> command = "2";
                    case '3' -> command = "3";
                    case '4' -> command = "4";
                    case '5' -> command = "5";
                    case '6' -> command = "6";
                    case '7' -> command = "7";
                    case '8' -> command = "8";
                    case '9' -> command = "9";
                }
            }

            if (command != null) {
                game.processCommand(command);
            }
        }
    }
}