# Controller / UIView Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Split `presentation/Main.java` into `RawKey`, `UIView` (Lanterna rendering + input), and `Controller` (game loop + key-to-command mapping), leaving `Main` as a one-line entry point — with zero behavior change for the player.

**Architecture:** `UIView` owns the Lanterna `Terminal`/`Screen` end-to-end (creation, start/stop, drawing, raw key reading) and exposes only `RawKey` (a plain Lanterna-free DTO) to the rest of the app. `Controller` owns the turn loop and decides what a key means for `Game` (`w/a/s/d`, `e/h/j/k`, `0-9`, quit) — it imports only `domain.*` and its own `UIView`/`RawKey`, never `com.googlecode.lanterna.*`. `Main` wires `Game` + `UIView` into a `Controller` and runs it.

**Tech Stack:** Java 21, Lanterna 3.1.2, Gradle, JUnit 5 (existing test infra; this feature has no new automated tests — see Testing section of the spec).

## Global Constraints

- No changes to any file under `src/main/java/domain/**` — the game logic is out of scope for this plan.
- Player-visible behavior (keys, rendering, colors, end screen, terminal size 120x62) must stay identical to the current `Main.java`.
- `Controller` must not import anything from `com.googlecode.lanterna.*`.
- Code comments/docs follow the existing repo style: Russian, short javadoc-style `/** ... */` blocks above methods, no comment on self-evident lines.

---

### Task 1: `RawKey` — the Lanterna-free key DTO

**Files:**
- Create: `src/main/java/presentation/RawKey.java`

**Interfaces:**
- Consumes: nothing (plain value object).
- Produces: `RawKey(char character, boolean quit)`, `getCharacter(): char`, `isQuit(): boolean`. Used by `UIView.readKey()` (Task 2) and `Controller.mapKeyToCommand` (Task 3).

- [ ] **Step 1: Write `RawKey`**

```java
package presentation;

/**
 * Нейтральный (без Lanterna) контейнер для нажатой клавиши.
 * UIView переводит KeyStroke в RawKey, чтобы Controller не знал о Lanterna.
 */
public class RawKey {
    private final char character;
    private final boolean quit;

    public RawKey(char character, boolean quit) {
        this.character = character;
        this.quit = quit;
    }

    public char getCharacter() {
        return character;
    }

    public boolean isQuit() {
        return quit;
    }
}
```

- [ ] **Step 2: Compile to verify it builds**

Run: `./gradlew compileJava -q`
Expected: no output, exit code 0 (there are no other references to `RawKey` yet, so this only checks the new file parses).

- [ ] **Step 3: Commit**

```bash
git add src/main/java/presentation/RawKey.java
git commit -m "Добавить RawKey - нейтральный DTO для клавиш без зависимости от Lanterna"
```

---

### Task 2: `UIView` — Lanterna ownership (screen, drawing, input)

**Files:**
- Create: `src/main/java/presentation/UIView.java`

**Interfaces:**
- Consumes: `domain.gameSession.Game` (via `render`/`renderEndScreen` parameters), `RawKey` (Task 1).
- Produces:
  - `UIView()` — constructor, creates `Terminal` + `Screen` (120x62), does not start the screen yet.
  - `void start() throws IOException` — `screen.startScreen()`.
  - `void stop() throws IOException` — `screen.stopScreen()`.
  - `void render(Game game) throws IOException`.
  - `void renderEndScreen(Game game) throws IOException`.
  - `RawKey readKey() throws IOException`.
  These four methods are what `Controller` (Task 3) calls.

- [ ] **Step 1: Write `UIView`**

This is a straight move of `Main.java`'s Lanterna-owning code (terminal/screen creation, `render`, `drawMap`, `drawStatus`, `drawInventory`, `itemsFor`, `titleFor`, `colorFor`, `renderEndScreen`) into an instance class, plus the new `readKey()` that replaces the key-reading half of the old `gameLoop`/`mapKeyToCommand`.

```java
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
 * UIView полностью владеет Lanterna: создаёт Terminal/Screen, рисует игру
 * и читает нажатые клавиши, переводя их в нейтральный RawKey.
 * Ничего не знает о том, что означает клавиша для игры - этим занимается Controller.
 */
public class UIView {

    // строка карты сдвинута на 1 вниз, чтобы наверху был статус игрока
    private static final int MAP_ROW_OFFSET = 1;

    private final Screen screen;

    public UIView() throws IOException {
        Terminal terminal = new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(120, 62))
                .createTerminal();
        this.screen = new TerminalScreen(terminal);
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
     * метод рисует экран победы/поражения после окончания игры
     */
    public void renderEndScreen(Game game) throws IOException {
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

Note: `UIView()` constructor now declares `throws IOException` (terminal creation can throw it) — this changes the constructor signature callers must handle, relevant for Task 4.

- [ ] **Step 2: Compile to verify it builds**

Run: `./gradlew compileJava -q`
Expected: no output, exit code 0. `Main.java` still exists with the old code at this point and still compiles independently since it doesn't reference `UIView` yet.

- [ ] **Step 3: Commit**

```bash
git add src/main/java/presentation/UIView.java
git commit -m "Добавить UIView - вынести владение Lanterna (Terminal/Screen, отрисовка, чтение клавиш) из Main"
```

---

### Task 3: `Controller` — game loop + key-to-command mapping

**Files:**
- Create: `src/main/java/presentation/Controller.java`

**Interfaces:**
- Consumes: `domain.gameSession.Game` (`getPlayer().getHealth()`, `isGameEnded()`, `processInput(String)`), `UIView` (`start`, `stop`, `render`, `renderEndScreen`, `readKey`) from Task 2, `RawKey` (`getCharacter`, `isQuit`) from Task 1.
- Produces: `Controller(Game game, UIView uiView)`, `void run() throws IOException`. Used by `Main` (Task 4).

- [ ] **Step 1: Write `Controller`**

```java
package presentation;

import domain.gameSession.Game;

import java.io.IOException;

/**
 * Controller описывает механизм взаимодействия консоли с игрой:
 * ведёт игровой цикл и переводит нажатые клавиши в команды Game.processInput.
 * Ничего не знает о Lanterna - только о UIView и RawKey.
 */
public class Controller {

    private final Game game;
    private final UIView uiView;

    public Controller(Game game, UIView uiView) {
        this.game = game;
        this.uiView = uiView;
    }

    /**
     * метод запускает игру: старт экрана, игровой цикл, гарантированная остановка экрана
     */
    public void run() throws IOException {
        uiView.start();
        try {
            gameLoop();
        } finally {
            uiView.stop();
        }
    }

    /**
     * метод запускает основной игровой цикл: отрисовка -> чтение клавиши -> ход
     */
    private void gameLoop() throws IOException {
        boolean running = true;
        while (running) {
            uiView.render(game);

            if (game.getPlayer().getHealth() <= 0 || game.isGameEnded()) {
                uiView.renderEndScreen(game);
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

- [ ] **Step 2: Compile to verify it builds**

Run: `./gradlew compileJava -q`
Expected: no output, exit code 0.

- [ ] **Step 3: Commit**

```bash
git add src/main/java/presentation/Controller.java
git commit -m "Добавить Controller - игровой цикл и трактовка клавиш вынесены из Main"
```

---

### Task 4: Shrink `Main` to the entry point and remove the old code

**Files:**
- Modify: `src/main/java/presentation/Main.java` (full rewrite — the old content is now duplicated in `UIView`/`Controller`)

**Interfaces:**
- Consumes: `Game` (`domain.gameSession.Game`), `Controller`/`UIView` (Tasks 2-3).
- Produces: nothing further consumes `Main`.

- [ ] **Step 1: Replace the whole file**

```java
package presentation;

import domain.gameSession.Game;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new Controller(new Game(), new UIView()).run();
    }
}
```

- [ ] **Step 2: Compile to verify it builds**

Run: `./gradlew compileJava -q`
Expected: no output, exit code 0. This is the point where duplicate method definitions would show up as unused-but-compiling — check there are no leftover references to the deleted `Main` methods anywhere else in the repo:

Run: `grep -rn "Main\." src/main/java/presentation | grep -v "class Main"`
Expected: no output (nothing outside `Main.java` calls its old static methods).

- [ ] **Step 3: Run the existing test suite**

Run: `./gradlew test -q`
Expected: no output, exit code 0 (same as before this change — `domain` wasn't touched, so no existing test should change behavior).

- [ ] **Step 4: Manual smoke test**

Run: `./gradlew run` (or however you normally launch it), and check in the opened terminal/window:
- the map renders in the same place and colors as before,
- `w/a/s/d` moves the player,
- pressing `e`/`h`/`j`/`k` opens the inventory panel on the right with the same layout as before,
- picking `1`-`9` uses an item and the message line updates,
- `Esc` quits cleanly (no exception, screen restored).

- [ ] **Step 5: Commit**

```bash
git add src/main/java/presentation/Main.java
git commit -m "Свести Main к точке входа: new Controller(new Game(), new UIView()).run()"
```

---

## Self-Review Notes

- **Spec coverage:** all three spec deliverables (`RawKey`, `UIView`, `Controller`, shrunk `Main`) map to Tasks 1-4; the spec's "what's not in this plan" section (screen layout, StartScreen/EndScreen) is intentionally untouched.
- **No placeholders:** every step has literal code or an exact command with expected output.
- **Type consistency checked:** `RawKey(char, boolean)` / `getCharacter()` / `isQuit()` used identically in Task 2 (`readKey`) and Task 3 (`mapKeyToCommand`); `UIView` method names (`start`, `stop`, `render`, `renderEndScreen`, `readKey`) match what `Controller` calls in Task 3 and what the spec's interface section defined.
- **Constructor exception note:** `UIView()` throws `IOException` (terminal creation can fail) — `Main` in Task 4 already declares `throws IOException` on `main`, so this propagates without extra handling.
