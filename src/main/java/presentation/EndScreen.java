package presentation;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import domain.gameSession.Game;
import domain.map.Level;

import java.io.IOException;

/**
 * EndScreen рисует экран победы/поражения и итоговый счёт поверх последнего кадра игры.
 * Работает через общий Screen, переданный снаружи (как UIView).
 */
public class EndScreen {

    private final Screen screen;

    public EndScreen(Screen screen) {
        this.screen = screen;
    }

    /**
     * метод рисует экран победы/поражения после окончания игры
     */
    public void render(Game game) throws IOException {
        screen.clear();
        TextGraphics tg = screen.newTextGraphics();
        boolean win = game.isGameEnded() && game.getPlayer().getHealth() > 0;

        tg.enableModifiers(SGR.BOLD);
        tg.setForegroundColor(win ? TextColor.ANSI.YELLOW : TextColor.ANSI.RED);
        tg.putString(48, 22, win ? "=== YOU WIN! ===" : "=== GAME OVER ===");
        tg.disableModifiers(SGR.BOLD);

        tg.setForegroundColor(TextColor.ANSI.WHITE);
        int row = 24;
        tg.putString(48, row++, "Игрок:        " + game.getPlayer().getName());
        tg.putString(48, row++, "Уровень:      " + Level.getLevelUp());
        tg.putString(48, row++, "Золото:       " + game.getPlayer().getGold());
        tg.putString(48, row++, "Убито врагов: " + game.getEnemiesKilled());
        tg.putString(48, row++, "Сложность:    " + game.getDifficulty().getLabel());

        row++;
        tg.enableModifiers(SGR.BOLD);
        tg.putString(48, row++, "ИТОГОВЫЙ СЧЁТ: " + game.calculateScore());
        tg.disableModifiers(SGR.BOLD);

        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(40, row + 2, "Нажми любую клавишу для выхода...");
        screen.refresh();
    }
}
