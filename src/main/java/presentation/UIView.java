package presentation;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import domain.backpack.Backpack;
import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.characters.Player;
import domain.gameSession.Game;
import domain.map.FogOfWar;
import domain.map.Level;
import domain.map.TileType;
import domain.shop.Shop;

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
    private static final int RIGHT_TOP_ROW = 1;           // статус игрока, вровень с картой
    private static final int RIGHT_INVENTORY_ROW = 20;    // правая средняя: сводка по инвентарю
    private static final int SHOP_PANEL_X_OFFSET = 18;    // сдвиг панели магазина правее инвентаря
    private static final int PANEL_GAP = 2;               // отступ логов от блоков выше

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
        // инвентарь и магазин стоят рядом, лог начинается под самым длинным из них
        int inventoryBottom = drawInventory(game, tg);
        int shopBottom = drawShop(game, tg);
        drawMessageLog(game, tg, Math.max(inventoryBottom, shopBottom) + PANEL_GAP);

        screen.refresh();
    }

    /**
     * метод рисует карту подземелья по тайлам, каждый тайл своим цветом
     */
    private void drawMap(Game game, TextGraphics tg) {
        TileType[][] map = game.getGenerator().getMap();
        FogOfWar fog = game.getFog();
        int width = game.getGenerator().getMapWidth();
        int height = game.getGenerator().getMapHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TileType tile = map[x][y];
                if (fog.isVisible(x, y)) {
                    tg.setForegroundColor(colorFor(tile));
                    tg.putString(x, y + MAP_ROW_OFFSET, String.valueOf(tile.getSymbol()));
                } else if (fog.isExplored(x, y)) {
                    tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
                    tg.putString(x, y + MAP_ROW_OFFSET, String.valueOf(geometrySymbolOf(tile)));
                } else {
                    tg.putString(x, y + MAP_ROW_OFFSET, " ");
                }
            }
        }
    }

    /**
     * метод возвращает символ геометрии клетки: враги и предметы в разведанной,
     * но невидимой зоне не показываются - под ними всегда пол
     */
    private char geometrySymbolOf(TileType tile) {
        return switch (tile) {
            // двери и магазин - часть геометрии, они остаются видны в разведанной зоне
            case WALL, FLOOR, LEVEL, SHOP,
                 DOOR_GREEN, DOOR_BLUE, DOOR_RED, DOOR_YELLOW -> tile.getSymbol();
            default -> TileType.FLOOR.getSymbol();
        };
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
        Player player = game.getPlayer();
        int row = RIGHT_TOP_ROW;

        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        tg.putString(panelX, row++, player.getName());

        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(panelX, row++, "Health:   " + player.getHealth() + "/" + player.getMaxHealth());
        tg.putString(panelX, row++, "Agility:  " + player.getBuffAgility());
        tg.putString(panelX, row++, "Strength: " + player.getBuffStrength());
        tg.putString(panelX, row++, "Weapon:   +" + player.getCurrentWeaponValue());
        tg.putString(panelX, row++, "Gold:     " + player.getGold());

        if (!player.getKeys().isEmpty()) {
            tg.putString(panelX, row++, "Ключи:    " + game.getGenerator().getKeys());
        }
    }

    /**
     * метод рисует в правой средней части постоянную сводку по инвентарю,
     * а если игрок выбрал тип предмета (e/h/j/k) - детальный список под ней
     */
    private int drawInventory(Game game, TextGraphics tg) {
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
            return row;
        }

        row++;
        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(panelX, row++, titleFor(selected) + ":");

        List<Item> items = itemsFor(backpack, selected);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            tg.putString(panelX, row++, "[" + (i + 1) + "] " + item.toString());
        }

        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        tg.putString(panelX, ++row, "Нажми 1-9 для выбора предмета");
        return row;
    }

    /**
     * метод рисует панель магазина справа от инвентаря, если магазин открыт
     */
    private int drawShop(Game game, TextGraphics tg) {
        if (!game.isShopOpen()) {
            return RIGHT_INVENTORY_ROW;
        }

        int panelX = game.getGenerator().getMapWidth() + RIGHT_PANEL_X_MARGIN + SHOP_PANEL_X_OFFSET;
        int row = RIGHT_INVENTORY_ROW;

        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(panelX, row++, "Магазин:");

        // тот же ровный блок, что и у инвентаря: тип, количество, цена за штуку
        Shop shop = game.getShop();
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(panelX, row++, "Эликсиры:  " + shop.getElixirList().size() +
                (shop.getElixirList().isEmpty() ? "" : " (" + shop.priceOf(shop.getElixirList().getFirst()) + ")"));
        tg.putString(panelX, row++, "Еда:       " + shop.getFoodList().size() +
                (shop.getFoodList().isEmpty() ? "" : " (" + shop.priceOf(shop.getFoodList().getFirst()) + ")"));
        tg.putString(panelX, row++, "Свитки:    " + shop.getScrollList().size() +
                (shop.getScrollList().isEmpty() ? "" : " (" + shop.priceOf(shop.getScrollList().getFirst()) + ")"));
        tg.putString(panelX, row++, "Оружие:    " + shop.getWeaponList().size() +
                (shop.getWeaponList().isEmpty() ? "" : " (" + shop.priceOf(shop.getWeaponList().getFirst()) + ")"));

        row++;
        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        tg.putString(panelX, row, "Золото: " + game.getPlayer().getGold());
        return row;
    }

    /**
     * метод рисует в правой нижней части историю последних сообщений игры
     */
    private void drawMessageLog(Game game, TextGraphics tg, int startRow) {
        int panelX = game.getGenerator().getMapWidth() + RIGHT_PANEL_X_MARGIN;
        // лог идёт сразу под инвентарём и магазином, но не вылезает за низ экрана
        int maxRow = screen.getTerminalSize().getRows() - 2 - Game.getMessageLogCapacity();
        int row = 40;

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
     * метод возвращает название типа предмета - единственный маппинг ItemsType на подпись,
     * его используют и заголовок инвентаря, и строки магазина
     * (getName() у предметов возвращает односимвольную букву и для панелей не годится)
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
            case DOOR_GREEN, KEY_GREEN -> TextColor.ANSI.GREEN;
            case DOOR_BLUE, KEY_BLUE -> TextColor.ANSI.BLUE;
            case DOOR_RED, KEY_RED -> TextColor.ANSI.RED;
            case DOOR_YELLOW, KEY_YELLOW -> TextColor.ANSI.YELLOW;
            case SHOP -> TextColor.ANSI.MAGENTA;
            case LEVEL -> TextColor.ANSI.YELLOW;
            case WALL -> TextColor.ANSI.WHITE;
            case FLOOR -> TextColor.ANSI.BLACK_BRIGHT;
            default -> TextColor.ANSI.WHITE;
        };
    }
}
