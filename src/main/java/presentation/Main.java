package presentation;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import domain.backpack.Backpack;
import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.gameSession.Game;
import domain.map.Level;
import domain.map.TileType;

import java.io.IOException;
import java.util.List;

/**
 * Presentation-слой: вся отрисовка игры (карта, статус, инвентарь) выполняется здесь через Lanterna.
 * Сама игровая логика целиком в domain.gameSession.Game — отсюда только читаются данные
 * (карта, статус игрока, содержимое рюкзака) и передаются нажатия клавиш через game.processInput().
 */
public class Main {

    // строка карты сдвинута на 1 вниз, чтобы наверху был статус игрока
    private static final int MAP_ROW_OFFSET = 1;

    public static void main(String[] args) throws IOException {
        Game game = new Game();

        Terminal terminal = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(180, 80))
                .createTerminal();

        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        try {
            gameLoop(game, screen);
        } finally {
            screen.stopScreen();
        }
    }

    /**
     * метод запускает основной игровой цикл: отрисовка -> чтение клавиши -> ход
     * @param game игровая сессия
     * @param screen экран Lanterna
     */
    private static void gameLoop(Game game, Screen screen) throws IOException {
        boolean running = true;
        while (running) {
            render(game, screen);

            if (game.getPlayer().getHealth() <= 0 || game.isGameEnded()) {
                renderEndScreen(game, screen);
                screen.readInput();
                break;
            }

            KeyStroke key = screen.readInput();
            if (key.getKeyType() == KeyType.EOF || key.getKeyType() == KeyType.Escape) {
                running = false;
                continue;
            }

            String input = mapKeyToCommand(key);
            if (input != null) {
                game.processInput(input);
            }
        }
    }

    /**
     * переводит нажатую клавишу в текстовую команду, которую понимает Game.processInput
     * @param key нажатая клавиша
     * @return команда или null, если клавиша не используется
     */
    private static String mapKeyToCommand(KeyStroke key) {
        if (key.getCharacter() == null) {
            return null;
        }
        char c = Character.toLowerCase(key.getCharacter());
        if (c == 'w' || c == 'a' || c == 's' || c == 'd' ||
                c == 'e' || c == 'h' || c == 'j' || c == 'k' ||
                (c >= '0' && c <= '9')) {
            return String.valueOf(c);
        }
        return null;
    }

    /**
     * метод отрисовывает карту, статус игрока, последнее сообщение и, если открыт, инвентарь
     */
    private static void render(Game game, Screen screen) throws IOException {
        screen.clear();
        TextGraphics tg = screen.newTextGraphics();

        drawMap(game, tg);
        drawStatus(game, tg);
        drawInventory(game, tg);

        screen.refresh();
    }

    /**
     * метод рисует карту подземелья по тайлам, каждый тайл своим цветом
     */
    private static void drawMap(Game game, TextGraphics tg) {
        TileType[][] map = game.getGenerator().getMap();
        int width = game.getGenerator().getMapWidth();
        int height = game.getGenerator().getMapHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TileType tile = map[x][y];
                tg.setForegroundColor(colorFor(tile));
                tg.putString(x, y + MAP_ROW_OFFSET, String.valueOf(tile.getSymbol()));
            }
        }
    }

    /**
     * метод рисует строку статуса игрока сверху и последнее игровое сообщение
     */
    private static void drawStatus(Game game, TextGraphics tg) {
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(0, 0, game.getPlayer() + "  lvl: " + Level.getLevelUp());

        if (!game.getLastMessage().isEmpty()) {
            int height = game.getGenerator().getMapHeight();
            tg.setForegroundColor(TextColor.ANSI.YELLOW);
            tg.putString(0, height + MAP_ROW_OFFSET + 1, game.getLastMessage());
        }
    }

    /**
     * метод рисует панель рюкзака справа от карты, когда игрок выбрал тип предмета (e/h/j/k)
     */
    private static void drawInventory(Game game, TextGraphics tg) {
        ItemsType type = game.getSelectedInventoryType();
        if (type == null) {
            return;
        }

        int panelX = game.getGenerator().getMapWidth() + 3;
        int row = MAP_ROW_OFFSET;

        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(panelX, row++, titleFor(type) + ":");

        List<Item> items = itemsFor(game.getBackpack(), type);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            tg.putString(panelX, row++, "[" + (i + 1) + "] " + item.getName() + " (" + item.getValue() + ")");
        }

        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        tg.putString(panelX, row + 1, "Нажми 1-9 для выбора предмета");
    }

    /**
     * метод возвращает список предметов рюкзака по типу
     */
    private static List<Item> itemsFor(Backpack backpack, ItemsType type) {
        return switch (type) {
            case ELIXIR -> backpack.getElixirList();
            case FOOD -> backpack.getFoodList();
            case SCROLL -> backpack.getScrollList();
            case WEAPON -> backpack.getWeaponList();
            default -> List.of();
        };
    }

    /**
     * метод возвращает заголовок панели инвентаря по типу предмета
     */
    private static String titleFor(ItemsType type) {
        return switch (type) {
            case ELIXIR -> "Эликсиры";
            case FOOD -> "Еда";
            case SCROLL -> "Свитки";
            case WEAPON -> "Оружие";
            default -> "Инвентарь";
        };
    }

    /**
     * метод рисует экран победы/поражения после окончания игры
     */
    private static void renderEndScreen(Game game, Screen screen) throws IOException {
        TextGraphics tg = screen.newTextGraphics();
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.enableModifiers(SGR.BOLD);
        String message = game.isGameEnded() && game.getPlayer().getHealth() > 0
                ? "=== You win! === Final score: " + game.getPlayer().getGold() + " gold. Нажми любую клавишу для выхода."
                : "=== GAME OVER === Final score: " + game.getPlayer().getGold() + " gold. Нажми любую клавишу для выхода.";
        tg.putString(2, 0, message);
        screen.refresh();
    }

    /**
     * метод возвращает цвет тайла в соответствии с ТЗ (зомби - зеленый, вампир - красный и т.д.)
     */
    private static TextColor colorFor(TileType tile) {
        return switch (tile) {
            case ZOMBIE -> TextColor.ANSI.GREEN;
            case VAMPIRE -> TextColor.ANSI.RED;
            case OGRE -> TextColor.ANSI.YELLOW;
            case GHOST, SNAKE, MIMIC -> TextColor.ANSI.WHITE;
            case PLAYER, PLAYER_STUNNED -> TextColor.ANSI.WHITE;
            case ELIXIR, SCROLL, WEAPON, FOOD -> TextColor.ANSI.CYAN;
            case LEVEL -> TextColor.ANSI.YELLOW;
            case WALL -> TextColor.ANSI.WHITE;
            case FLOOR -> TextColor.ANSI.BLACK_BRIGHT;
            default -> TextColor.ANSI.WHITE;
        };
    }
}
