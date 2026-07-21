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
        drawStatus(game, tg);
        drawInventory(game, tg);

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
     * метод рисует строку статуса игрока сверху и последнее игровое сообщение
     */
    private void drawStatus(Game game, TextGraphics tg) {
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
    private void drawInventory(Game game, TextGraphics tg) {
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
