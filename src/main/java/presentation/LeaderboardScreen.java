package presentation;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import domain.leaderboard.LeaderboardEntry;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * LeaderboardScreen рисует таблицу лидеров (топ игроков по счёту) и ждёт любую клавишу
 * для возврата в стартовое меню. Работает через общий Screen, переданный снаружи
 * (как StartScreen/EndScreen).
 */
public class LeaderboardScreen {

    private static final String[] BANNER = {
            " ____   ___   ____ _   _ _____ ",
            "|  _ \\ / _ \\ / ___| | | | ____|",
            "| |_) | | | | |  _| | | |  _|  ",
            "|  _ <| |_| | |_| | |_| | |___ ",
            "|_| \\_\\\\___/ \\____|\\___/|_____|"
    };

    private final Screen screen;

    public LeaderboardScreen(Screen screen) {
        this.screen = screen;
    }

    /**
     * метод рисует таблицу лидеров и ждёт любую клавишу
     */
    public void render(List<LeaderboardEntry> entries) throws IOException {
        List<LeaderboardEntry> sorted = entries.stream()
                .sorted(Comparator.comparingInt(LeaderboardEntry::getScore).reversed())
                .toList();

        screen.clear();
        TextGraphics tg = screen.newTextGraphics();

        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        tg.enableModifiers(SGR.BOLD);
        for (int i = 0; i < BANNER.length; i++) {
            tg.putString(40, 21 + i, BANNER[i]);
        }
        tg.disableModifiers(SGR.BOLD);

        int row = 27 + BANNER.length;
        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(46, row++, "ТАБЛИЦА ЛИДЕРОВ");
        row++;

        if (sorted.isEmpty()) {
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(42, row, "Таблица лидеров пуста.");
        } else {
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            for (int i = 0; i < sorted.size(); i++) {
                LeaderboardEntry entry = sorted.get(i);
                String line = String.format("%2d. %-20s %d", i + 1, entry.getName(), entry.getScore());
                tg.putString(40, row++, line);
            }
        }

        tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        tg.putString(38, row + 2, "Нажми любую клавишу для возврата в меню...");
        screen.refresh();
        screen.readInput();
    }
}
