# Раскладка экрана: поле / статус / инвентарь / лог боя

Дата: 2026-07-21
Статус: согласован, готов к планированию реализации

## Контекст

Задача — пункт 2 из `docs/ARCHITECTURE.md`:

> 2. Отрисовка игры будет иметь вид:
>     - В левой часть экрана отрисовка поля игры
>     - В левой нижней части экрана посередине поля отрисовка уровня игры
>     - В нижней также кол-во предметов и врагов, и лвл
>     - В правой верхней части отображение информации о игроке(без уровня)
>     - В правой средней части отрисовка инвентаря
>     - В правой нижней части отрисовка логов боя

Вторая подзадача из трёх (после разбиения `Main` на `Controller`/`UIView`, см. `docs/superpowers/specs/2026-07-21-controller-uiview-design.md` — на момент написания этой спеки уже смержено в `develop`). Работаем поверх текущего `UIView`/`Controller`/`RawKey`.

Сейчас (`presentation/UIView.java`) раскладка такая:
- карта — весь экран, сдвинута на 1 строку вниз (`MAP_ROW_OFFSET`);
- строка 0 — статус игрока + уровень (`player + "  lvl: " + Level.getLevelUp()`);
- строка `mapHeight + MAP_ROW_OFFSET + 1` — последнее сообщение (`game.getLastMessage()`), одно сообщение, без истории;
- панель инвентаря — справа от карты, но **появляется только когда** игрок нажал `e/h/j/k` (временный оверлей с деталями по одному типу).

## Цель

Разложить экран на 4 зоны без изменения игровой логики:
1. Поле игры — слева, как сейчас.
2. Строка под полем, по центру — уровень + кол-во оставшихся предметов + кол-во живых врагов.
3. Правая верхняя часть — статус игрока (без уровня — он и так не был в `player.toString()`, только уровень отдельно указывался рядом; теперь просто убираем эту приписку).
4. Правая средняя часть — инвентарь: **постоянная сводка** по 4 типам (кол-во каждого), и когда игрок нажал `e/h/j/k` — под сводкой достраивается прежний детальный список с индексами 1-9.
5. Правая нижняя часть — лог последних сообщений игры (не только боевых — вообще все, что раньше шли одним `lastMessage`).

## Изменения в `domain/gameSession/Game.java`

### 1. История сообщений вместо одного `lastMessage`

Было:
```java
private String lastMessage = "";
public String getLastMessage() { return lastMessage; }
```
Во всех местах: `lastMessage = "Предмет использован!";` и т.п.

Стало:
```java
private static final int MESSAGE_LOG_CAPACITY = 10;
private final Deque<String> messageLog = new ArrayDeque<>();

private void addMessage(String message) {
    messageLog.addLast(message);
    if (messageLog.size() > MESSAGE_LOG_CAPACITY) {
        messageLog.removeFirst();
    }
}

public List<String> getMessageLog() {
    return new ArrayList<>(messageLog);
}
```
Каждое `lastMessage = "...";` заменяется на `addMessage("...");`. Порядок в списке — от старого к новому (`getMessageLog().get(0)` — самое старое из хранимых 10, последний элемент — самое новое). `UIView` рисует их сверху вниз в таком же порядке, так что новое оказывается внизу блока лога — как в обычном чат-логе.

`getLastMessage()` убирается — presentation переходит на `getMessageLog()`.

### 2. Фикс утечки в `checkAndCollectItems`

Текущий код (`Game.java:266-270`):
```java
private void checkAndCollectItems() {
    for (var item : allItemList) {
        if (player.getPosition().equals(item.getPosition())) {
            backpack.takeItem(item);
        }
    }
}
```
Предмет никогда не удаляется из `allItemList` после подбора — список только растёт, и подсчёт "оставшихся предметов" без фикса будет врать (никогда не уменьшается). Меняем на:
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
Игровое поведение не меняется (предмет по-прежнему подбирается один раз при контакте) — меняется только то, что список теперь отражает реальность.

## Изменения в `presentation/UIView.java`

Все константы позиций собираются в начале класса, чтобы раскладку было видно одним взглядом:

```java
private static final int MAP_ROW_OFFSET = 1;
private static final int RIGHT_PANEL_X_MARGIN = 3;           // отступ правой панели от края карты
private static final int RIGHT_TOP_ROW = 0;                  // статус игрока
private static final int RIGHT_INVENTORY_ROW = 20;           // правая средняя: сводка по инвентарю
private static final int RIGHT_LOG_ROW = 40;                 // правая нижняя: лог сообщений
private static final int MESSAGE_LOG_VISIBLE_LINES = 10;
```

(`RIGHT_INVENTORY_ROW`/`RIGHT_LOG_ROW` — условная разбивка по высоте экрана 62 строки: верх ~0-15 статус, середина ~20-35 инвентарь+детальный список, низ ~40-60 лог. Если после первого прогона окажется тесно — подвинуть в реализации, это не архитектурное решение.)

### Нижняя строка под картой (уровень + предметы + враги)

```java
private void drawBottomBar(Game game, TextGraphics tg) {
    int aliveEnemies = (int) game.getAllEnemiesList().stream().filter(e -> e.getHealth() > 0).count();
    int remainingItems = game.getAllItemList().size();
    String text = "Уровень: " + Level.getLevelUp() +
            "   Предметов: " + remainingItems +
            "   Врагов: " + aliveEnemies;

    int width = game.getGenerator().getMapWidth();
    // не даём строке вылезти за пределы поля (в правую панель или за экран) -
    // центрируем строго внутри [0, mapWidth), при необходимости обрезаем
    if (text.length() > width) {
        text = text.substring(0, width);
    }
    int startX = Math.max(0, (width - text.length()) / 2);

    tg.setForegroundColor(TextColor.ANSI.WHITE);
    tg.putString(startX, game.getGenerator().getMapHeight() + MAP_ROW_OFFSET, text);
}
```
Это заменяет старое `drawStatus`'ное приклеивание уровня к статусу игрока и старую строку `lastMessage` под картой — обе убираются из своих старых мест.

### Правая верхняя часть — статус игрока

```java
private void drawPlayerStatus(Game game, TextGraphics tg) {
    int panelX = game.getGenerator().getMapWidth() + RIGHT_PANEL_X_MARGIN;
    tg.setForegroundColor(TextColor.ANSI.WHITE);
    tg.putString(panelX, RIGHT_TOP_ROW, game.getPlayer().toString());
}
```
`player.toString()` уже не содержит уровня (`"Health: %d/%d Agility: %d Strength: %d Gold: %d"`) — раньше уровень приклеивался отдельно в `UIView`, теперь просто не приклеиваем.

### Правая средняя часть — инвентарь (постоянная сводка + деталь по нажатию)

```java
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

    row++; // пустая строка-разделитель перед детальным списком
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
`itemsFor`/`titleFor` — без изменений, переиспользуются как есть.

### Правая нижняя часть — лог сообщений

```java
private void drawMessageLog(Game game, TextGraphics tg) {
    int panelX = game.getGenerator().getMapWidth() + RIGHT_PANEL_X_MARGIN;
    int row = RIGHT_LOG_ROW;

    tg.setForegroundColor(TextColor.ANSI.CYAN);
    tg.putString(panelX, row++, "Лог:");

    tg.setForegroundColor(TextColor.ANSI.WHITE);
    List<String> log = game.getMessageLog();
    for (String entry : log) {
        tg.putString(panelX, row++, "> " + entry);
    }
}
```
Показывает все хранимые сообщения (до `MESSAGE_LOG_CAPACITY` = 10 в `Game`) сверху вниз, старое сверху / новое снизу.

### `render()` собирает все зоны

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
(`drawStatus` в старом виде удаляется — его обязанности разошлись по `drawBottomBar` и `drawPlayerStatus`.)

## Что не меняется

- `Controller`, `RawKey` — не трогаются вообще.
- Управление (WASD, e/h/j/k, 0-9, Esc) — то же самое.
- Цвета тайлов на карте (`colorFor`) — без изменений.
- Экран конца игры (`renderEndScreen`) — без изменений (не входит в эту раскладку по ТЗ).
- Размер терминала (120x62) — без изменений.

## Границы задачи (что дальше, не в этой спеке)

- `StartScreen`/`EndScreen` с вводом имени и load/save — третья подзадача ТЗ, отдельная спека после этой.
