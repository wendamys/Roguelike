# StartScreen / EndScreen + Save/Load Fix Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make save/load actually restore a playable game (map, rooms, items, enemies, player, level), wire autosave into the turn loop, and add `StartScreen` (menu + name input) and `EndScreen` (win/lose + score) per `docs/ARCHITECTURE.md` item 3, with `Controller` expanded into the full application orchestrator (menu → game session → end screen → menu again).

**Architecture:** Two independent domain/datalayer fixes (map reconstruction in `DungeonGenerator`, dedup + wiring in `DungeConverter`/`GameConverter`/`Game`) land first since later presentation tasks depend on `Game`/`DataLayer` actually working. Then the presentation layer: `UIView`'s constructor changes to accept an externally-created `Screen` (shared with the two new screen classes), `renderEndScreen` moves out into a new `EndScreen` class, a new `StartScreen` class owns the menu/name-input/message UI, and `Controller` is rewritten to orchestrate StartScreen → game session → EndScreen → StartScreen again, calling `DataLayer.save(game)` after every turn.

**Tech Stack:** Java 21, Lanterna 3.1.2, Gson (existing datalayer dependency), Gradle, JUnit 5 (existing test infra; no new automated tests planned — same rationale as prior plans: Lanterna-bound presentation code isn't unit-tested in this repo, and there's no existing datalayer test infrastructure to extend).

## Global Constraints

- No changes to gameplay logic itself (movement, combat, item use rules) — only how state is saved/restored/rendered.
- No changes to the DTO/JSON format (`GameDTO`, `DungeDTO`, `RoomDTO`, etc.) — the fix works entirely on the `fromDTO` (restore) side, so existing `data.json` files stay readable.
- `RawKey` is not touched by this plan.
- One shared `Screen`/`Terminal` for the whole application lifetime — created once in `Main`, passed into `StartScreen`, `UIView`, `EndScreen`. None of the three creates its own `Terminal`/`Screen`.
- Autosave (`DataLayer.save(game)`) happens after every successfully processed player turn (i.e., every time `game.processInput(input)` runs), inside the game session loop.
- Terminal size (120x62) is unchanged.
- Code comments/docs follow the existing repo style: Russian, short javadoc-style `/** ... */` blocks above methods, no comment on self-evident lines.
- If "Load game" is chosen and no save file exists, show a message and return to the start menu — do not crash, do not silently start a new game.

---

### Task 1: `DungeonGenerator.rebuildMap` + `DungeConverter` dedup fix

**Files:**
- Modify: `src/main/java/domain/map/DungeonGenerator.java`
- Modify: `src/main/java/datalayer/converter/DungeConverter.java`

**Interfaces:**
- Consumes: nothing new.
- Produces: `public void rebuildMap(List<Room> rooms, List<Corridor> corridors)` on `DungeonGenerator` — carves the given (already-restored) rooms/corridors into a fresh all-wall tile grid, and sets `this.rooms`/`this.corridors` to copies of the given lists. Used by `DungeConverter.fromDTO` in this same task, and indirectly by Task 2's `GameConverter.fromDTO`.

- [ ] **Step 1: Add `rebuildMap` to `DungeonGenerator`**

In `src/main/java/domain/map/DungeonGenerator.java`, insert this new public method directly after the existing `createCorridor` method (right before `getConnectedRooms`, i.e. after the closing brace that currently reads `corridors.add(corridor);\n    }` and before `/**\n     * метод возвращает соседей комнаты...`):

```java
    /**
     * метод перестраивает карту тайлов по уже готовым комнатам и коридорам
     * (используется при загрузке сохранённой игры, без случайной генерации)
     * @param rooms восстановленные комнаты
     * @param corridors восстановленные коридоры
     */
    public void rebuildMap(List<Room> rooms, List<Corridor> corridors) {
        initializeMap();
        this.rooms = new ArrayList<>(rooms);
        this.corridors = new ArrayList<>(corridors);
        for (Room room : rooms) {
            carveRoom(room);
        }
        for (Corridor corridor : corridors) {
            for (Position p : corridor.getPath()) {
                if (isInBounds(p.getX(), p.getY()) && map[p.getX()][p.getY()] == TileType.WALL) {
                    map[p.getX()][p.getY()] = TileType.FLOOR;
                }
            }
        }
    }
```

`initializeMap()`, `carveRoom(Room)`, and `isInBounds(int, int)` are existing private methods in this same class — call them as-is, do not change their visibility. No new imports are needed (`Room`, `Corridor`, `TileType` are in the same package; `Position`, `List`, `ArrayList` are already imported).

- [ ] **Step 2: Fix `DungeConverter.fromDTO` to stop double-inserting enemies/items and call `rebuildMap`**

In `src/main/java/datalayer/converter/DungeConverter.java`, replace the entire `fromDTO` method:

```java
    public static DungeonGenerator fromDTO(DungeDTO dto) {
        if (dto == null) return null;
        
        DungeonGenerator generator = new DungeonGenerator();

        ArrayList<Room> rooms = new ArrayList<>();
        dto.getRoomsDTO().forEach(roomDTO -> rooms.add(RoomConverter.fromDTO(roomDTO)));
        generator.setRooms(rooms);

        ArrayList<Corridor> corridors = new ArrayList<>();
        ArrayList<Room> roomListCopy = new ArrayList<>(rooms);
        dto.getCorridorsDTO().forEach(corridorDTO -> {
            Room room1 = findRoomByPosition(roomListCopy, corridorDTO.getRoom1PositionDTO());
            Room room2 = findRoomByPosition(roomListCopy, corridorDTO.getRoom2PositionDTO());
            if (room1 != null && room2 != null) {
                corridors.add(CorridorConverter.fromDTO(corridorDTO, room1, room2));
            }
        });
        generator.setCorridors(corridors);
        
        // Восстанавливаем всех врагов в комнаты
        ArrayList<Enemies> allEnemies = new ArrayList<>();
        dto.getAllEnemiesListDTO().forEach(enemyDTO -> allEnemies.add(EnemiesConverter.fromDTO(enemyDTO)));
        // Распределяем врагов по комнатам (по позиции)
        for (Enemies enemy : allEnemies) {
            for (Room room : rooms) {
                if (enemy.getPosition().getX() >= room.getPosition().getX() && 
                    enemy.getPosition().getX() < room.getPosition().getX() + room.getWidth() &&
                    enemy.getPosition().getY() >= room.getPosition().getY() && 
                    enemy.getPosition().getY() < room.getPosition().getY() + room.getHeight()) {
                    room.getEnemyList().add(enemy);
                    break;
                }
            }
        }
        
        // Восстанавливаем все предметы в комнаты
        ArrayList<Item> allItems = new ArrayList<>();
        dto.getAllItemsListDTO().forEach(itemDTO -> allItems.add(ItemConverter.fromDTO(itemDTO)));
        // Распределяем предметы по комнатам (по позиции)
        for (domain.backpack.Item item : allItems) {
            for (Room room : rooms) {
                if (item.getPosition().getX() >= room.getPosition().getX() && 
                    item.getPosition().getX() < room.getPosition().getX() + room.getWidth() &&
                    item.getPosition().getY() >= room.getPosition().getY() && 
                    item.getPosition().getY() < room.getPosition().getY() + room.getHeight()) {
                    room.getItemList().add(item);
                    break;
                }
            }
        }
        
        return generator;
    }
```
becomes:
```java
    public static DungeonGenerator fromDTO(DungeDTO dto) {
        if (dto == null) return null;
        
        DungeonGenerator generator = new DungeonGenerator();

        ArrayList<Room> rooms = new ArrayList<>();
        dto.getRoomsDTO().forEach(roomDTO -> rooms.add(RoomConverter.fromDTO(roomDTO)));

        ArrayList<Corridor> corridors = new ArrayList<>();
        ArrayList<Room> roomListCopy = new ArrayList<>(rooms);
        dto.getCorridorsDTO().forEach(corridorDTO -> {
            Room room1 = findRoomByPosition(roomListCopy, corridorDTO.getRoom1PositionDTO());
            Room room2 = findRoomByPosition(roomListCopy, corridorDTO.getRoom2PositionDTO());
            if (room1 != null && room2 != null) {
                corridors.add(CorridorConverter.fromDTO(corridorDTO, room1, room2));
            }
        });

        generator.rebuildMap(rooms, corridors);

        return generator;
    }
```

Reasoning (do not skip this even though it looks like a pure deletion): `RoomConverter.fromDTO` (unchanged, already existing) already populates each restored `Room`'s own `enemyList`/`itemList` directly from that room's `RoomDTO`. The deleted block re-added the *same* enemies/items a second time by matching positions from the separate flat `DungeDTO.getAllEnemiesListDTO()`/`getAllItemsListDTO()` lists — so every room would end up with double the enemies/items it was saved with. Removing that block is the fix, not a simplification.

- [ ] **Step 3: Remove now-unused imports in `DungeConverter.java`**

At the top of the file, remove these two lines (no longer referenced now that the redistribution block is gone):
```java
import domain.backpack.Item;
import domain.characters.Enemies;
```
Leave `import datalayer.dto.*;`, `import domain.map.Corridor;`, `import domain.map.DungeonGenerator;`, `import domain.map.Room;`, and `import java.util.ArrayList;` as they are — all still used.

- [ ] **Step 4: Compile to verify it builds**

Run: `./gradlew compileJava -q`
Expected: no output, exit code 0.

- [ ] **Step 5: Run the existing test suite**

Run: `./gradlew test -q`
Expected: no output, exit code 0.

- [ ] **Step 6: Commit**

```bash
git add src/main/java/domain/map/DungeonGenerator.java src/main/java/datalayer/converter/DungeConverter.java
git commit -m "Чинить восстановление карты и дублирование врагов/предметов при загрузке"
```

---

### Task 2: `Game` restore methods + `GameConverter.fromDTO` wiring

**Files:**
- Modify: `src/main/java/domain/gameSession/Game.java`
- Modify: `src/main/java/datalayer/converter/GameConverter.java`

**Interfaces:**
- Consumes: `DungeonGenerator.rebuildMap` (Task 1, already committed on this branch).
- Produces: `Game.setPosLevel(Position)`, `Game.setLevel(int)`, `Game.restoreItemsAndEnemies()`, `Game.placeRestoredEntitiesOnMap()` — all public, all used by `GameConverter.fromDTO` in this same task. No other file depends on these yet.

- [ ] **Step 1: Add `setPosLevel` and `setLevel` to `Game`**

In `src/main/java/domain/gameSession/Game.java`, add these two methods directly after the existing `getPosLevel()` getter (which currently reads):
```java
    public Position getPosLevel() {
        return posLevel;
    }
```
insert after it:
```java

    public void setPosLevel(Position posLevel) {
        this.posLevel = posLevel;
    }

    public void setLevel(int level) {
        this.level = level;
    }
```

- [ ] **Step 2: Add `restoreItemsAndEnemies` and `placeRestoredEntitiesOnMap`**

Directly after the existing `initializeGame()` method (which currently ends with the closing brace right before `/**\n     * метод запускает игру\n     */\n    public void start() {`), insert these two new public methods:

```java

    /**
     * метод восстанавливает плоские списки предметов и врагов по комнатам
     * (используется при загрузке — комнаты уже содержат восстановленные списки)
     */
    public void restoreItemsAndEnemies() {
        allItemList.clear();
        allEnemiesList.clear();
        for (Room room : rooms) {
            if (room != rooms.getFirst()) {
                allItemList.addAll(room.getItemList());
                allEnemiesList.addAll(room.getEnemyList());
            }
        }
    }

    /**
     * метод расставляет игрока, предметы, врагов и выход на восстановленной карте
     * (используется при загрузке, не добавляет предметы/врагов повторно в комнаты)
     */
    public void placeRestoredEntitiesOnMap() {
        generator.createPlayer(player);
        for (Room room : rooms) {
            if (room != rooms.getFirst()) {
                generator.createItem(room);
                generator.createEnemies(room);
            }
            if (room == rooms.getLast()) {
                posLevel = generator.createLevel(room);
            }
        }
    }
```

No new imports needed — `Room` is already imported via `domain.map.*` at the top of `Game.java`.

- [ ] **Step 3: Rewrite `GameConverter.fromDTO` to wire everything together**

Replace the entire file `src/main/java/datalayer/converter/GameConverter.java` with:

```java
package datalayer.converter;

import datalayer.dto.GameDTO;
import domain.gameSession.Game;
import domain.map.DungeonGenerator;

public class GameConverter {

    public static GameDTO toDTO(Game game) {
        if(game == null) return null;

        GameDTO dto = new GameDTO();
        dto.setPlayerDTO(PlayerConverter.toDTO(game.getPlayer()));
        dto.setBackpackDTO(BackpackConverter.toDTO(game.getBackpack()));
        dto.setLevelDTO(LevelConverter.toDTO());
        dto.setDungeDTO(DungeConverter.toDTO(game.getGenerator()));

        return dto;
    }

    public static Game fromDTO(GameDTO dto) {
        if(dto == null) return null;

        Game game = new Game();
        game.setPlayer(PlayerConverter.fromDTO(dto.getPlayerDTO()));
        game.setBackpack(BackpackConverter.fromDTO(dto.getBackpackDTO()));
        LevelConverter.fromDTO(dto.getLevelDTO());
        game.setLevel(dto.getLevelDTO().getLevelUp());

        DungeonGenerator generator = DungeConverter.fromDTO(dto.getDungeDTO());
        game.setGenerator(generator);
        game.setRooms(generator.getRooms());

        game.restoreItemsAndEnemies();
        game.placeRestoredEntitiesOnMap();

        return game;
    }
}
```

(`toDTO` is unchanged from the current file — only `fromDTO` changes, plus the new `import domain.map.DungeonGenerator;` needed for the local `generator` variable's explicit type.)

- [ ] **Step 4: Compile to verify it builds**

Run: `./gradlew compileJava -q`
Expected: no output, exit code 0.

- [ ] **Step 5: Run the existing test suite**

Run: `./gradlew test -q`
Expected: no output, exit code 0.

- [ ] **Step 6: Manual save/load smoke test**

This is the one place in this plan where a manual check is essential, since there's no automated test covering datalayer round-tripping. Do this even if you're a background agent that can't play the game interactively — you can still exercise the code path with a tiny throwaway Java snippet, or by temporarily adding a `main`-style call. At minimum, verify by reading the code path once more end-to-end (`GameConverter.toDTO` → JSON → `GameConverter.fromDTO`) that every field that matters for resuming play (player position/stats, backpack contents, dungeon tiles, room list, item/enemy positions, exit position, current level number) is actually populated after `fromDTO`, with no field silently left at its `new Game()` default. Note in your report whether you found a way to actually run this round-trip (e.g. via a temporary test) or only re-traced it by reading.

- [ ] **Step 7: Commit**

```bash
git add src/main/java/domain/gameSession/Game.java src/main/java/datalayer/converter/GameConverter.java
git commit -m "Довосстанавливать rooms/allItemList/allEnemiesList/posLevel/level при загрузке Game"
```

---

### Task 3: `UIView` shared-`Screen` constructor + extract `EndScreen`

**Files:**
- Modify: `src/main/java/presentation/UIView.java`
- Create: `src/main/java/presentation/EndScreen.java`

**Interfaces:**
- Consumes: `com.googlecode.lanterna.screen.Screen` (passed into both constructors from outside — created in `Main`, Task 5).
- Produces: `UIView(Screen)` (constructor signature change — no longer creates its own `Terminal`/`Screen`, no longer declares `throws IOException` on the constructor itself since it does no I/O there anymore); `EndScreen(Screen)`, `EndScreen.render(Game): void throws IOException`. `UIView.renderEndScreen` is removed. `Controller` (Task 5) will be the only consumer of both.

**Note:** after this task, `presentation/Controller.java` will fail to compile (it still calls `uiView.renderEndScreen(game)` and constructs `new UIView()` with no arguments) — this is expected and matches the pattern from the previous plan (Controller/UIView split), where a task boundary temporarily breaks the whole-project build until the dependent task lands. Task 5 fixes this. Judge this task's diff on the correctness of `UIView.java`/`EndScreen.java` themselves.

- [ ] **Step 1: Change `UIView`'s constructor to accept an external `Screen`, remove `renderEndScreen`**

Replace the entire file `src/main/java/presentation/UIView.java` with:

```java
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
```

Note what changed versus the current file: the constructor no longer creates a `Terminal`/`Screen` (removed the `com.googlecode.lanterna.TerminalSize`, `com.googlecode.lanterna.terminal.DefaultTerminalFactory`, `com.googlecode.lanterna.terminal.Terminal`, `com.googlecode.lanterna.screen.TerminalScreen` imports along with it — none of those types are used anymore in this file), it now takes a `Screen` parameter and stores it; `renderEndScreen` and its only-needed-there import `com.googlecode.lanterna.SGR` are removed entirely (moved to `EndScreen` in Step 2).

- [ ] **Step 2: Create `EndScreen`**

Create `src/main/java/presentation/EndScreen.java`:

```java
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
```

This is the exact body of the old `UIView.renderEndScreen` (including that it deliberately does **not** call `screen.clear()` first — the win/lose message is meant to overlay the last rendered game frame, not replace it).

- [ ] **Step 3: Confirm `UIView.java` and `EndScreen.java` compile on their own merits**

Run: `./gradlew compileJava -q 2>&1 | grep -v "Controller.java" || true`

Expected: the only errors, if any, should be in `Controller.java` (expected per this task's note above — it hasn't been updated to the new constructors/API yet). If you see any error whose file path is `UIView.java` or `EndScreen.java`, that's a real problem in this task — fix it before moving on. `Main.java` will also show an error (it still calls `new UIView()` with no arguments) — that's expected too, fixed in Task 5.

- [ ] **Step 4: Commit**

```bash
git add src/main/java/presentation/UIView.java src/main/java/presentation/EndScreen.java
git commit -m "UIView принимает общий Screen снаружи; вынести EndScreen из UIView"
```

(Skip the usual "run test suite" step here — the project doesn't compile end-to-end until Task 5 lands, same known gap pattern as before.)

---

### Task 4: `StartScreen`

**Files:**
- Create: `src/main/java/presentation/StartScreen.java`

**Interfaces:**
- Consumes: `com.googlecode.lanterna.screen.Screen` (passed in from `Main`, Task 5).
- Produces: `StartScreen(Screen)`, `StartScreen.Choice` enum (`START`, `LOAD`, `EXIT`), `StartScreen.selectChoice(): Choice throws IOException`, `StartScreen.readPlayerName(): String throws IOException`, `StartScreen.showMessage(String): void throws IOException`. Used by `Controller` in Task 5.

This task is independent of Tasks 1-3 (touches only a new file, no shared symbols) and could in principle be done in parallel with them, but is listed after them for narrative order.

- [ ] **Step 1: Create `StartScreen`**

Create `src/main/java/presentation/StartScreen.java`:

```java
package presentation;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

/**
 * StartScreen рисует стартовое меню (Start/Load/Exit), экран ввода имени игрока
 * и одноразовые сообщения (например, "Сохранение не найдено"). Работает через общий Screen,
 * переданный снаружи (как UIView/EndScreen).
 */
public class StartScreen {

    public enum Choice { START, LOAD, EXIT }

    private static final String[] MENU_LABELS = {"Start game", "Load game", "Exit"};

    private final Screen screen;

    public StartScreen(Screen screen) {
        this.screen = screen;
    }

    /**
     * метод рисует меню и ждёт, пока игрок выберет пункт (W/S - навигация, Enter - подтверждение)
     */
    public Choice selectChoice() throws IOException {
        Choice[] choices = Choice.values();
        int selected = 0;
        while (true) {
            drawMenu(selected);
            KeyStroke key = screen.readInput();
            if (key.getKeyType() == KeyType.Character && key.getCharacter() != null) {
                char c = Character.toLowerCase(key.getCharacter());
                if (c == 'w') {
                    selected = (selected - 1 + choices.length) % choices.length;
                } else if (c == 's') {
                    selected = (selected + 1) % choices.length;
                }
            } else if (key.getKeyType() == KeyType.Enter) {
                return choices[selected];
            } else if (key.getKeyType() == KeyType.Escape || key.getKeyType() == KeyType.EOF) {
                return Choice.EXIT;
            }
        }
    }

    /**
     * метод рисует поле ввода имени и построчно читает ввод до Enter
     * @return введённое имя, "Игрок" если пусто
     */
    public String readPlayerName() throws IOException {
        StringBuilder name = new StringBuilder();
        while (true) {
            drawNamePrompt(name.toString());
            KeyStroke key = screen.readInput();
            if (key.getKeyType() == KeyType.Enter) {
                break;
            } else if (key.getKeyType() == KeyType.Backspace) {
                if (name.length() > 0) {
                    name.deleteCharAt(name.length() - 1);
                }
            } else if (key.getKeyType() == KeyType.Character && key.getCharacter() != null) {
                name.append(key.getCharacter());
            }
        }
        String result = name.toString().trim();
        return result.isEmpty() ? "Игрок" : result;
    }

    /**
     * метод показывает сообщение и ждёт любую клавишу
     */
    public void showMessage(String message) throws IOException {
        screen.clear();
        TextGraphics tg = screen.newTextGraphics();
        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        tg.putString(2, 2, message + " Нажми любую клавишу...");
        screen.refresh();
        screen.readInput();
    }

    /**
     * метод рисует меню с подсветкой выбранного пункта
     */
    private void drawMenu(int selectedIndex) throws IOException {
        screen.clear();
        TextGraphics tg = screen.newTextGraphics();
        for (int i = 0; i < MENU_LABELS.length; i++) {
            tg.setForegroundColor(i == selectedIndex ? TextColor.ANSI.YELLOW : TextColor.ANSI.WHITE);
            tg.putString(2, 2 + i, (i == selectedIndex ? "> " : "  ") + MENU_LABELS[i]);
        }
        screen.refresh();
    }

    /**
     * метод рисует строку ввода имени игрока
     */
    private void drawNamePrompt(String currentInput) throws IOException {
        screen.clear();
        TextGraphics tg = screen.newTextGraphics();
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(2, 2, "Введите имя: " + currentInput);
        screen.refresh();
    }
}
```

`Choice.values()` returns `{START, LOAD, EXIT}` in declaration order, matching `MENU_LABELS`'s `{"Start game", "Load game", "Exit"}` order index-for-index — this is what makes `choices[selected]` in `selectChoice()` correct. Keep the enum's declared order and the array's order in sync if either is ever edited.

- [ ] **Step 2: Compile to verify it builds on its own merits**

Run: `./gradlew compileJava -q 2>&1 | grep -v "Controller.java\|Main.java" || true`

Expected: no errors reported for `StartScreen.java`. (`Controller.java`/`Main.java` errors are expected until Task 5, per Task 3's note.)

- [ ] **Step 3: Commit**

```bash
git add src/main/java/presentation/StartScreen.java
git commit -m "Добавить StartScreen: меню Start/Load/Exit, ввод имени, сообщения"
```

---

### Task 5: `Controller` becomes the app orchestrator; `Main` wires the shared `Screen`

**Files:**
- Modify: `src/main/java/presentation/Controller.java`
- Modify: `src/main/java/presentation/Main.java`

**Interfaces:**
- Consumes: `StartScreen` (Task 4: `selectChoice()`, `readPlayerName()`, `showMessage(String)`), `UIView` (Task 3: `UIView(Screen)`, `start()`, `stop()`, `render(Game)`, `readKey()`), `EndScreen` (Task 3: `EndScreen(Screen)`, `render(Game)`), `RawKey` (unchanged), `DataLayer.save(Game)`/`DataLayer.load(): Game` (unchanged, already existing in `datalayer.DataLayer`).
- Produces: `Controller(StartScreen, UIView, EndScreen)`, `Controller.run(): void throws IOException`. This is the last task — after it, the whole project compiles and runs end-to-end.

- [ ] **Step 1: Rewrite `Controller`**

Replace the entire file `src/main/java/presentation/Controller.java` with:

```java
package presentation;

import datalayer.DataLayer;
import domain.gameSession.Game;

import java.io.IOException;

/**
 * Controller ведёт весь жизненный цикл приложения: стартовое меню, игровые сессии, экран конца игры.
 * Ничего не знает о Lanterna - только о StartScreen/UIView/EndScreen и RawKey.
 */
public class Controller {

    private final StartScreen startScreen;
    private final UIView uiView;
    private final EndScreen endScreen;

    public Controller(StartScreen startScreen, UIView uiView, EndScreen endScreen) {
        this.startScreen = startScreen;
        this.uiView = uiView;
        this.endScreen = endScreen;
    }

    /**
     * метод запускает приложение: старт экрана, цикл меню/игры, гарантированная остановка экрана
     */
    public void run() throws IOException {
        uiView.start();
        try {
            appLoop();
        } finally {
            uiView.stop();
        }
    }

    /**
     * метод ведёт цикл стартового меню: выбор Start/Load/Exit и запуск игровой сессии
     */
    private void appLoop() throws IOException {
        boolean appRunning = true;
        while (appRunning) {
            switch (startScreen.selectChoice()) {
                case EXIT -> appRunning = false;
                case START -> {
                    String name = startScreen.readPlayerName();
                    Game game = new Game();
                    game.getPlayer().setName(name);
                    runGameSession(game);
                }
                case LOAD -> {
                    Game loaded = DataLayer.load();
                    if (loaded == null) {
                        startScreen.showMessage("Сохранение не найдено.");
                    } else {
                        runGameSession(loaded);
                    }
                }
            }
        }
    }

    /**
     * метод запускает основной игровой цикл одной сессии: отрисовка -> чтение клавиши -> ход -> автосохранение
     */
    private void runGameSession(Game game) throws IOException {
        boolean running = true;
        while (running) {
            uiView.render(game);

            if (game.getPlayer().getHealth() <= 0 || game.isGameEnded()) {
                endScreen.render(game);
                uiView.readKey();
                break;
            }

            RawKey key = uiView.readKey();
            if (key.isQuit()) {
                running = false;
                continue;
            }

            String input = mapKeyToCommand(key);
            if (input != null) {
                game.processInput(input);
                DataLayer.save(game);
            }
        }
    }

    /**
     * переводит нажатую клавишу в текстовую команду, которую понимает Game.processInput
     * @param key нажатая клавиша
     * @return команда или null, если клавиша не используется
     */
    private String mapKeyToCommand(RawKey key) {
        char c = Character.toLowerCase(key.getCharacter());
        if (c == 'w' || c == 'a' || c == 's' || c == 'd' ||
                c == 'e' || c == 'h' || c == 'j' || c == 'k' ||
                (c >= '0' && c <= '9')) {
            return String.valueOf(c);
        }
        return null;
    }
}
```

- [ ] **Step 2: Rewrite `Main`**

Replace the entire file `src/main/java/presentation/Main.java` with:

```java
package presentation;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Terminal terminal = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(120, 62))
                .createTerminal();
        Screen screen = new TerminalScreen(terminal);

        StartScreen startScreen = new StartScreen(screen);
        UIView uiView = new UIView(screen);
        EndScreen endScreen = new EndScreen(screen);

        new Controller(startScreen, uiView, endScreen).run();
    }
}
```

- [ ] **Step 3: Compile to verify the whole project builds**

Run: `./gradlew compileJava -q`
Expected: no output, exit code 0. This is the point where all the previous tasks' known/expected gaps close — there should be zero compile errors anywhere now.

- [ ] **Step 4: Run the existing test suite**

Run: `./gradlew test -q`
Expected: no output, exit code 0.

- [ ] **Step 5: Manual smoke test**

Run the game (`./gradlew run` or however you normally launch it) and check in the opened window:
- a start menu appears with "Start game" / "Load game" / "Exit", `W`/`S` moves the highlighted selection, wrapping around at the ends.
- choosing "Start game" prompts for a name (typing works, Backspace removes a character, Enter confirms), then the game begins as before (map, movement, inventory, combat all behave exactly as before this branch).
- moving/attacking triggers a save (check `data.json` is updated after a move — its content should change turn to turn).
- quit the game (Esc) mid-session — relaunch — choose "Load game" — verify: the map layout matches what you last saw, the player is at the same position with the same stats/gold, remaining enemies/items are in the same places (not doubled), and playing continues normally from there.
- choosing "Load game" after deleting/renaming `data.json` shows "Сохранение не найдено." and returns to the menu without crashing.
- reaching game over or winning shows the same win/lose + score screen as before, any key returns to the start menu (not to the OS/terminal) — confirm the loop actually returns to the menu rather than exiting the whole app.
- "Exit" from the start menu closes the application cleanly.

- [ ] **Step 6: Commit**

```bash
git add src/main/java/presentation/Controller.java src/main/java/presentation/Main.java
git commit -m "Controller ведёт весь цикл приложения (меню/игра/конец); Main создаёт общий Screen"
```

---

## Self-Review Notes

- **Spec coverage:** all deliverables from `docs/superpowers/specs/2026-07-21-start-end-screen-design.md` are covered — map/dedup fix (Task 1), `Game`/`GameConverter` restore wiring (Task 2), shared-`Screen` `UIView` + `EndScreen` extraction (Task 3), `StartScreen` (Task 4), `Controller`-as-orchestrator + `Main` wiring + autosave (Task 5).
- **No placeholders:** every step shows literal before/after code or an exact command with expected output.
- **Type consistency checked:** `DungeonGenerator.rebuildMap(List<Room>, List<Corridor>)` (Task 1) matches the call `generator.rebuildMap(rooms, corridors)` in the rewritten `DungeConverter.fromDTO` (Task 1, same task); `Game.setLevel(int)`/`restoreItemsAndEnemies()`/`placeRestoredEntitiesOnMap()` (Task 2) match the calls in the rewritten `GameConverter.fromDTO` (Task 2, same task); `UIView(Screen)`/`EndScreen(Screen)` (Task 3) and `StartScreen(Screen)` (Task 4) all match the constructor calls in `Main` (Task 5); `Controller(StartScreen, UIView, EndScreen)` (Task 5) matches how `Main` constructs it (Task 5, same task); `StartScreen.Choice` values (`START`/`LOAD`/`EXIT`) match the `switch` cases used in `Controller.appLoop` (Task 5).
- **Cross-task compile gaps are intentional and match established precedent:** Tasks 3 and 4 leave the whole project non-compiling (Controller/Main still reference the old APIs) until Task 5 lands — this mirrors how the prior Controller/UIView plan handled `Game.getLastMessage()` removal vs. `UIView` update, and was accepted by that plan's reviewers as a legitimate consequence of splitting work across files that share a symbol, not a defect. Each task's compile-check step is scoped to acknowledge this explicitly rather than mask it.
- **DTO format constraint honored:** no task adds, renames, or removes a field on any `*DTO` class — `GameDTO`, `DungeDTO`, `RoomDTO`, `LevelDTO`, `PlayerDTO`, etc. are untouched; the fix is entirely in the `fromDTO` (restore) code paths of `DungeConverter`/`GameConverter`, plus new restore-only methods on `Game`/`DungeonGenerator`.
- **Known pre-existing quirk, explicitly out of scope:** `Room`'s constructor (`new Room(x, y)`) randomizes `width`/`height`/`roomType`/`capacityEnemy`/`capacityItem` before `RoomConverter.fromDTO` overwrites `width`/`height` with the saved values — so a restored room's `roomType`/capacity fields may not match its restored dimensions. This was true before this plan and isn't read anywhere at runtime after initial generation (confirmed: `roomType`/capacity are only consumed during the original `addEnemyList`/`addItemList` calls inside the constructor, which happen before `RoomConverter.fromDTO`'s override and are not re-run on restore). Not fixed here — flagging so a reviewer doesn't mistake it for something this plan introduced.
