package presentation;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import domain.gameSession.Game;

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
        TextGraphics tg = screen.newTextGraphics();
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.enableModifiers(SGR.BOLD);
        String message = game.isGameEnded() && game.getPlayer().getHealth() > 0
                ? "=== You win! === Final score: " + game.getPlayer().getGold() + " gold. Нажми любую клавишу для выхода."
                : "=== GAME OVER === Final score: " + game.getPlayer().getGold() + " gold. Нажми любую клавишу для выхода.";
        tg.putString(2, 0, message);
        screen.refresh();
    }
}
