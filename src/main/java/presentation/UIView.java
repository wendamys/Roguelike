package presentation;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import domain.backpack.Backpack;
import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.gameSession.Game;
import domain.map.Level;
import domain.map.TileType;

import java.io.IOException;
import java.util.List;

/**
 * UIView рисует игру и читает нажатые клавиши через общий Screen,
 * переданный снаружи (создаётся один раз в Main и разделяется со StartScreen/EndScreen).
 * Ничего не знает о том, что означает клавиша для игры - этим занимается Controller.
 */
public class UIView {

    // строка карты сдвинута на 1 вниз, чтобы наверху был статус игрока
    private static final int MAP_ROW_OFFSET = 1;
    private static final int RIGHT_PANEL_X_MARGIN = 3;   // отступ правой панели от края карты
    private static final int RIGHT_TOP_ROW = 0;           // статус игрока
    private static final int RIGHT_INVENTORY_ROW = 20;    // правая средняя: сводка по инвентарю
    private static final int RIGHT_LOG_ROW = 40;          // правая нижняя: лог сообщений

    private final Screen screen;

    public UIView(Screen screen) {
        this.screen = screen;
    }

    public void start() throws IOException {
        screen.startScreen();
    }

    public void stop() throws IOException {
        screen.stopScreen();
    }

    /**
     * метод читает нажатую клавишу и переводит её в нейтральный RawKey
     */
    public RawKey readKey() throws IOException {
        KeyStroke key = screen.readInput();
        boolean quit = key.getKeyType() == KeyType.EOF || key.getKeyType() == KeyType.Escape;
        char character = key.getCharacter() == null ? '\0' : key.getCharacter();
        return new RawKey(character, quit);
    }

    /**
     * метод отрисовывает карту, статус игрока, последнее сообщение и, если открыт, инвентарь
     */
    public void render(Game game) throws IOException {
        screen.clear();
        TextGraphics tg = screen.newTextGraphics();

        drawMap(game, tg);
        drawBottomBar(game, tg);
        drawPlayerStatus(game, tg);
        drawInventory(game, tg);
        drawMessageLog(game, tg);

        screen.refresh();
    }

    /**
     * метод рисует карту подземелья по тайлам, каждый тайл своим цветом
     */
    private void drawMap(Game game, TextGraphics tg) {
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
     * метод рисует строку под полем: уровень, кол-во оставшихся предметов, кол-во живых врагов
     */
    private void drawBottomBar(Game game, TextGraphics tg) {
        long aliveEnemies = game.getAllEnemiesList().stream()
                .filter(enemy -> enemy.getHealth() > 0)
                .count();
        int remainingItems = game.getAllItemList().size();
        String text = "Уровень: " + Level.getLevelUp() +
                "   Предметов: " + remainingItems +
                "   Врагов: " + aliveEnemies;

        int width = game.getGenerator().getMapWidth();
        if (text.length() > width) {
            text = text.substring(0, width);
        }
        int startX = Math.max(0, (width - text.length()) / 2);

        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(startX, game.getGenerator().getMapHeight() + MAP_ROW_OFFSET, text);
    }

    /**
     * метод рисует статус игрока в правой верхней части экрана
     */
    private void drawPlayerStatus(Game game, TextGraphics tg) {
        int panelX = game.getGenerator().getMapWidth() + RIGHT_PANEL_X_MARGIN;
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(panelX, RIGHT_TOP_ROW, game.getPlayer().toString());
    }

    /**
     * метод рисует в правой средней части постоянную сводку по инвентарю,
     * а если игрок выбрал тип предмета (e/h/j/k) - детальный список под ней
     */
    private void drawInventory(Game game, TextGraphics tg) {
        int panelX = game.getGenerator().getMapWidth() + RIGHT_PANEL_X_MARGIN;
        int row = RIGHT_INVENTORY_ROW;

        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(panelX, row++, "Инвентарь:");

        Backpack backpack = game.getBackpack();
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(panelX, row++, "Эликсиры:  " + backpack.getElixirList().size());
        tg.putString(panelX, row++, "Еда:       " + backpack.getFoodList().size());
        tg.putString(panelX, row++, "Свитки:    " + backpack.getScrollList().size());
        tg.putString(panelX, row++, "Оружие:    " + backpack.getWeaponList().size());

        ItemsType selected = game.getSelectedInventoryType();
        if (selected == null) {
            return;
        }

        row++;
        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(panelX, row++, titleFor(selected) + ":");

        List<Item> items = itemsFor(backpack, selected);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
//            tg.putString(panelX, row++, "[" + (i + 1) + "] " + item.getName() + " (" + item.getValue() + ")");
            tg.putString(panelX, row++, "[" + (i + 1) + "] " + item.toString());
        }

        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        tg.putString(panelX, row + 1, "Нажми 1-9 для выбора предмета");
    }

    /**
     * метод рисует в правой нижней части историю последних сообщений игры
     */
    private void drawMessageLog(Game game, TextGraphics tg) {
        int panelX = game.getGenerator().getMapWidth() + RIGHT_PANEL_X_MARGIN;
        int row = RIGHT_LOG_ROW;

        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(panelX, row++, "Лог:");

        tg.setForegroundColor(TextColor.ANSI.WHITE);
        for (String entry : game.getMessageLog()) {
            tg.putString(panelX, row++, "> " + entry);
        }
    }

    /**
     * метод возвращает список предметов рюкзака по типу
     */
    private List<Item> itemsFor(Backpack backpack, ItemsType type) {
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
    private String titleFor(ItemsType type) {
        return switch (type) {
            case ELIXIR -> "Эликсиры";
            case FOOD -> "Еда";
            case SCROLL -> "Свитки";
            case WEAPON -> "Оружие";
            default -> "Инвентарь";
        };
    }

    /**
     * метод возвращает цвет тайла в соответствии с ТЗ (зомби - зеленый, вампир - красный и т.д.)
     */
    private TextColor colorFor(TileType tile) {
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
