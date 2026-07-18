package presentation;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import datalayer.DataLayer;
import datalayer.dto.GameDTO;
import domain.backpack.Item;
import domain.backpack.items.Elixir;
import domain.gameSession.Game;
import domain.map.DungeonGenerator;
import domain.map.Room;
import domain.navigator.Position;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Game game = new Game();
        DataLayer.save(game);
        GameDTO dto = new GameDTO();
        dto = DataLayer.loadDTO();


        Terminal terminal = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(180, 80))
                .createTerminal();

        Screen screen = new TerminalScreen(terminal);
        TextGraphics tg = screen.newTextGraphics();
        screen.startScreen();

        tg.putString(dto.getPlayerDTO().getPositionDTO().getX(),
                dto.getPlayerDTO().getPositionDTO().getY(),
                "@"
        );
        for (var item : dto.getDungeDTO().getAllItemsListDTO()) {
            tg.putString(item.getPositionDTO().getX(), item.getPositionDTO().getY(), item.getName());
        }
        for (var enemy : dto.getDungeDTO().getAllEnemiesListDTO()) {
            tg.putString(enemy.getPositionDTO().getX(), enemy.getPositionDTO().getY(), enemy.getName());
        }

        screen.refresh();

    }
}