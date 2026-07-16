package presentation;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import datalayer.DataLayer;
import datalayer.dto.EnemyDTO;
import datalayer.dto.GameDTO;
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

        DataLayer.save(game);
        GameDTO gameDTO = DataLayer.loadDTO();

        Terminal terminal = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(180, 70))
                .createTerminal();

        Screen screen = new TerminalScreen(terminal);
        TextGraphics tg = screen.newTextGraphics();
        screen.startScreen();
        tg.putString(gameDTO.getPlayerDTO().getPosition().getX(), gameDTO.getPlayerDTO().getPosition().getY(), "@");
        List<EnemyDTO> enemiesDTO = gameDTO.getEnemiesList();
        for (EnemyDTO enemyDTO : enemiesDTO) {
            tg.putString(enemyDTO.getPosition().getX(), enemyDTO.getPosition().getY(), "!");
        }


        screen.refresh();


    }
}