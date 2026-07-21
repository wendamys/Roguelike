# Screen Layout Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the single-message, single-panel presentation layout with the 5-zone layout from `docs/ARCHITECTURE.md` item 2 (field / bottom bar with level+counts / player status top-right / persistent inventory summary + detail middle-right / message log bottom-right), backed by a small `Game` message-history mechanism and a leaked-item-count bugfix.

**Architecture:** `Game` replaces its single `lastMessage` field with a capped FIFO message log (`addMessage`/`getMessageLog`), and fixes `checkAndCollectItems` to actually remove picked-up items from `allItemList`. `UIView.render` is restructured into five draw methods (`drawMap`, `drawBottomBar`, `drawPlayerStatus`, `drawInventory`, `drawMessageLog`) replacing the old `drawStatus`/`drawInventory` pair, with `drawInventory` now always drawing the 4-type summary and conditionally appending the detail list.

**Tech Stack:** Java 21, Lanterna 3.1.2, Gradle, JUnit 5 (existing test infra; no new automated tests planned — Lanterna-bound code, matches prior plan's precedent).

## Global Constraints

- No behavior change to movement, combat, or item-use rules — only how state is surfaced/rendered.
- The bottom-bar text (`Уровень: N   Предметов: X   Врагов: Y`) must be centered strictly within `[0, mapWidth)` and must never extend into the right panel (`mapWidth + RIGHT_PANEL_X_MARGIN` and beyond) or past the map's right edge — truncate to `mapWidth` if ever longer.
- `Controller`/`RawKey` are not touched by this plan.
- Terminal size (120x62) is unchanged.
- Code comments/docs follow the existing repo style: Russian, short javadoc-style `/** ... */` blocks above methods, no comment on self-evident lines.
- "Remaining items"/"alive enemies" counts reflect current state (post-pickup / post-death), not the level's original spawn count.

---

### Task 1: `Game` — message log + leaked-item fix

**Files:**
- Modify: `src/main/java/domain/gameSession/Game.java`

**Interfaces:**
- Consumes: nothing new.
- Produces: `void addMessage(String message)` (private, replaces all `lastMessage = "...";` assignments), `List<String> getMessageLog()` (public, returns oldest-to-newest, capped at 10). `getLastMessage()` and the `lastMessage` field are removed. `getAllItemList()` signature is unchanged but its contents now shrink on pickup. Used by `UIView.drawMessageLog`/`drawBottomBar` in Task 2.

- [ ] **Step 1: Replace the `lastMessage` field with a capped message log**

In `src/main/java/domain/gameSession/Game.java`, add these imports alongside the existing `java.util.*` imports (currently `import java.util.ArrayList;`, `import java.util.List;`, `import java.util.Locale;`, `import java.util.Scanner;` at lines 18-21):

```java
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
```

Replace this field (line 39):
```java
    private String lastMessage = ""; // Последнее игровое сообщение, для presentation-слоя
```
with:
```java
    private static final int MESSAGE_LOG_CAPACITY = 10;
    private final Deque<String> messageLog = new ArrayDeque<>(); // История последних сообщений, для presentation-слоя
```

Replace this getter (lines 95-97):
```java
    public String getLastMessage() {
        return lastMessage;
    }
```
with:
```java
    /**
     * метод добавляет сообщение в лог, отбрасывая самое старое при переполнении
     * @param message игровое сообщение
     */
    private void addMessage(String message) {
        messageLog.addLast(message);
        if (messageLog.size() > MESSAGE_LOG_CAPACITY) {
            messageLog.removeFirst();
        }
    }

    /**
     * метод возвращает историю сообщений от старых к новым
     * @return список сообщений, не более MESSAGE_LOG_CAPACITY
     */
    public List<String> getMessageLog() {
        return new ArrayList<>(messageLog);
    }
```

- [ ] **Step 2: Replace every `lastMessage = "...";` assignment with `addMessage("...");`**

There are 5 occurrences. Replace each exactly (same message text, same call sites):

In `parseDirection` (currently lines 182-186):
```java
                    if (backpack.useItemByIndex(index, selectedInventoryType, player)) {
                        lastMessage = "Предмет использован!";
                    } else {
                        lastMessage = "Предмет с этим индексом не найден!";
                    }
```
becomes:
```java
                    if (backpack.useItemByIndex(index, selectedInventoryType, player)) {
                        addMessage("Предмет использован!");
                    } else {
                        addMessage("Предмет с этим индексом не найден!");
                    }
```

In `attackEnemy` (currently line 259):
```java
        lastMessage = "Атака врага: " + enemy.getType();
```
becomes:
```java
        addMessage("Атака врага: " + enemy.getType());
```

In `handleInventoryCommand`, first occurrence (currently lines 281-285):
```java
                    if (backpack.useItemByIndex(index, selectedInventoryType, player)) {
                        lastMessage = "Предмет использован!";
                    } else {
                        lastMessage = "Предмет с этим индексом не найден!";
                    }
```
becomes:
```java
                    if (backpack.useItemByIndex(index, selectedInventoryType, player)) {
                        addMessage("Предмет использован!");
                    } else {
                        addMessage("Предмет с этим индексом не найден!");
                    }
```

In `handleInventoryCommand`, second occurrence (currently lines 306-310):
```java
                    if (backpack.useItemByIndex(index, type, player)) {
                        lastMessage = "Предмет использован!";
                    } else {
                        lastMessage = "Неверный индекс предмета!";
                    }
```
becomes:
```java
                    if (backpack.useItemByIndex(index, type, player)) {
                        addMessage("Предмет использован!");
                    } else {
                        addMessage("Неверный индекс предмета!");
                    }
```

In `handleInventoryTypeSelection` (currently line 339):
```java
            lastMessage = "Введите цифру 1-9 для выбора предмета:";
```
becomes:
```java
            addMessage("Введите цифру 1-9 для выбора предмета:");
```

- [ ] **Step 3: Fix the leaked-item bug in `checkAndCollectItems`**

Replace (currently lines 266-272):
```java
    private void checkAndCollectItems() {
        for (var item : allItemList) {
            if (player.getPosition().equals(item.getPosition())) {
                backpack.takeItem(item);
            }
        }
    }
```
with:
```java
    private void checkAndCollectItems() {
        allItemList.removeIf(item -> {
            if (player.getPosition().equals(item.getPosition())) {
                backpack.takeItem(item);
                return true;
            }
            return false;
        });
    }
```

- [ ] **Step 4: Compile and run the existing test suite**

Run: `./gradlew compileJava -q`
Expected: no output, exit code 0.

Run: `./gradlew test -q`
Expected: no output, exit code 0. (No test currently references `lastMessage`/`getLastMessage` — confirm with a grep before running: `grep -rn "getLastMessage\|lastMessage" src/test` should print nothing; if it does print something, stop and report it rather than proceeding, since that test needs updating as part of this task.)

- [ ] **Step 5: Commit**

```bash
git add src/main/java/domain/gameSession/Game.java
git commit -m "Заменить lastMessage на историю сообщений, починить утечку в checkAndCollectItems"
```

---

### Task 2: `UIView` — five-zone layout

**Files:**
- Modify: `src/main/java/presentation/UIView.java`

**Interfaces:**
- Consumes: `Game.getMessageLog()`, fixed `Game.getAllItemList()` from Task 1; `Game.getAllEnemiesList()`, `Game.getBackpack()`, `Game.getPlayer()`, `Game.getSelectedInventoryType()`, `Game.getGenerator()` (all pre-existing, unchanged signatures); `domain.map.Level.getLevelUp()` (pre-existing, unchanged).
- Produces: no change to `UIView`'s public interface (`start`, `stop`, `render`, `renderEndScreen`, `readKey` keep their exact signatures) — `Controller` needs no changes.

- [ ] **Step 1: Add layout constants and imports**

In `src/main/java/presentation/UIView.java`, replace the single constant (line 31):
```java
    // строка карты сдвинута на 1 вниз, чтобы наверху был статус игрока
    private static final int MAP_ROW_OFFSET = 1;
```
with:
```java
    // строка карты сдвинута на 1 вниз, чтобы наверху был статус игрока
    private static final int MAP_ROW_OFFSET = 1;
    private static final int RIGHT_PANEL_X_MARGIN = 3;   // отступ правой панели от края карты
    private static final int RIGHT_TOP_ROW = 0;           // статус игрока
    private static final int RIGHT_INVENTORY_ROW = 20;    // правая средняя: сводка по инвентарю
    private static final int RIGHT_LOG_ROW = 40;          // правая нижняя: лог сообщений
```

Add `domain.characters.Enemies` to the imports (needed for the alive-enemy count), alongside the existing `domain.backpack.*`/`domain.gameSession.Game`/`domain.map.*` imports (lines 13-18):
```java
import domain.backpack.Backpack;
import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.characters.Enemies;
import domain.gameSession.Game;
import domain.map.Level;
import domain.map.TileType;
```

- [ ] **Step 2: Replace `render()` to call all five draw methods**

Replace (currently lines 63-72):
```java
    public void render(Game game) throws IOException {
        screen.clear();
        TextGraphics tg = screen.newTextGraphics();

        drawMap(game, tg);
        drawStatus(game, tg);
        drawInventory(game, tg);

        screen.refresh();
    }
```
with:
```java
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
```

- [ ] **Step 3: Remove `drawStatus`, add `drawBottomBar` and `drawPlayerStatus`**

Remove this whole method (currently lines 91-103):
```java
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
```

Add these two methods in its place:
```java
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
```

(`Enemies` import from Step 1 is used implicitly via the stream's inferred type; no explicit `Enemies` reference is written in this code, but the import stays for symmetry with how the file names its domain types explicitly. If the compiler flags it unused, that's fine — remove it in that case, since `filter(enemy -> ...)` infers the type without needing the import. Note this explicitly in your report either way.)

- [ ] **Step 4: Rewrite `drawInventory` to show a persistent summary plus the conditional detail list**

Replace (currently lines 105-129):
```java
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
```
with:
```java
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
        tg.putString(panelX, row++, "Эликсиры: " + backpack.getElixirList().size());
        tg.putString(panelX, row++, "Еда: " + backpack.getFoodList().size());
        tg.putString(panelX, row++, "Свитки: " + backpack.getScrollList().size());
        tg.putString(panelX, row++, "Оружие: " + backpack.getWeaponList().size());

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
            tg.putString(panelX, row++, "[" + (i + 1) + "] " + item.getName() + " (" + item.getValue() + ")");
        }

        tg.setForegroundColor(TextColor.ANSI.YELLOW);
        tg.putString(panelX, row + 1, "Нажми 1-9 для выбора предмета");
    }
```

`itemsFor`/`titleFor` (lines 131-155 in the current file) are unchanged — leave them exactly as they are.

- [ ] **Step 5: Add `drawMessageLog`**

Add this new method right after `drawInventory` (before `itemsFor`):
```java
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
```

- [ ] **Step 6: Compile to verify it builds**

Run: `./gradlew compileJava -q`
Expected: no output, exit code 0.

If the `Enemies` import from Step 1 causes an "unused import" build warning (not a hard error under default `javac` flags, but check the compiler output for it), remove the `import domain.characters.Enemies;` line — the lambda in `drawBottomBar` doesn't need it to compile.

- [ ] **Step 7: Run the existing test suite**

Run: `./gradlew test -q`
Expected: no output, exit code 0.

- [ ] **Step 8: Manual smoke test**

Run the game (`./gradlew run` or however you normally launch it) and check in the opened window:
- the map still renders in the same place with the same colors,
- a new line appears directly under the map, centered, reading `Уровень: 1   Предметов: N   Врагов: M` with plausible counts,
- the top-right corner shows `Health: .../... Agility: ... Strength: ... Gold: ...` with no `lvl:` suffix,
- a permanent inventory summary (`Эликсиры: 0`, `Еда: 0`, `Свитки: 0`, `Оружие: 0` at game start) is visible on the right around the middle of the screen,
- pressing `e`/`h`/`j`/`k` shows the detailed list appended below that summary, same as before,
- moving onto an item tile increases the matching summary count and decreases "Предметов" in the bottom bar,
- attacking/using items appends to a visible log block in the lower-right area, most recent message at the bottom, capped at 10 lines total.

- [ ] **Step 9: Commit**

```bash
git add src/main/java/presentation/UIView.java
git commit -m "Разложить экран на 5 зон: поле, нижняя строка, статус игрока, инвентарь, лог"
```

---

## Self-Review Notes

- **Spec coverage:** all 5 zones from `docs/superpowers/specs/2026-07-21-screen-layout-design.md` are covered — field (unchanged `drawMap`), bottom bar (`drawBottomBar`), player status top-right (`drawPlayerStatus`), inventory summary+detail middle-right (`drawInventory`), message log bottom-right (`drawMessageLog`). The `Game` message-log mechanism and the `allItemList` leak fix (both explicitly required by the spec for the bottom bar's counts to be meaningful) are Task 1.
- **No placeholders:** every step shows the literal before/after code.
- **Type consistency checked:** `Game.getMessageLog(): List<String>` (Task 1) matches the type iterated in `UIView.drawMessageLog` (Task 2); `Game.addMessage(String)` signature matches every call site rewritten in Task 1 Step 2; `UIView`'s five draw methods all take `(Game game, TextGraphics tg)` consistently, matching the existing `drawMap` signature already in the file.
- **Constraint check:** the Global Constraints bullet about the bottom bar never overlapping the right panel is enforced by `drawBottomBar`'s explicit `text.length() > width` truncation and `Math.max(0, ...)` centering, both bounded to `[0, mapWidth)` — this was the specific concern raised during design review.
