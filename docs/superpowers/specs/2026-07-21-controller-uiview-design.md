# Controller / UIView: вынос логики из Main

Дата: 2026-07-21
Статус: согласован, готов к планированию реализации

## Контекст

Задача описана в `docs/ARCHITECTURE.md`, пункт 1:

> 1. Создать два класса: "Controller" и "UIView", всю логику из main перенести в эти классы:
>     - В "Controller" будет описан механизм взаимодействия консоли с игрой
>     - В "UIView" будет описана логика отрисовки игры
>     - В Main оставить только запуск игры

Это первая из трёх подзадач ТЗ (остальные — новая раскладка экрана и StartScreen/EndScreen с сохранением — пойдут отдельными спеками после этой, чтобы не делать всё вперемешку).

Сейчас весь код (создание `Terminal`/`Screen`, игровой цикл, чтение клавиш, вся отрисовка) находится в одном файле `presentation/Main.java`. Игровая логика (`domain.gameSession.Game`) уже не печатает ничего в консоль — `Game.processInput(String)` возвращает управление, а `Game.getLastMessage()`/`getSelectedInventoryType()` дают presentation-слою всё нужное для отрисовки (см. `docs/RENDERING_CHANGES.md`).

## Цель

Разбить `Main.java` на три класса без изменения игровой логики и без изменения поведения игры для игрока:

- **`UIView`** — всё, что касается Lanterna: создание `Terminal`/`Screen`, отрисовка (карта/статус/инвентарь/конец игры), чтение клавиш.
- **`Controller`** — игровой цикл и трактовка нажатых клавиш как игровых команд (`w/a/s/d`, `e/h/j/k`, `0-9`, выход).
- **`Main`** — только `new Controller(new Game(), new UIView()).run()`.

## Архитектура

### `presentation/RawKey.java` (новый маленький класс)

Единственная точка утечки данных из `UIView` наружу — простой контейнер без единой ссылки на Lanterna:

```java
package presentation;

public class RawKey {
    private final char character;
    private final boolean quit;

    public RawKey(char character, boolean quit) {
        this.character = character;
        this.quit = quit;
    }

    public char getCharacter() { return character; }
    public boolean isQuit() { return quit; }
}
```

`quit == true` для `KeyType.Escape` и `KeyType.EOF`. Если `quit == false` и клавиша не символьная (`getCharacter() == null` у `KeyStroke`), `character` равен `'\0'`.

### `presentation/UIView.java`

Переезжает сюда без изменения логики (только меняются сигнатуры/области видимости, где нужно):
- создание `Terminal` (`DefaultTerminalFactory`, размер `120x62` — как сейчас в Main) и `Screen` — в конструкторе;
- `render`, `drawMap`, `drawStatus`, `drawInventory`, `itemsFor`, `titleFor`, `colorFor`, `renderEndScreen` — как приватные методы, вызываемые из публичных `render(Game)`/`renderEndScreen(Game)`.

Публичный интерфейс:

```java
public UIView();                                    // создаёт Terminal + Screen
public void start() throws IOException;             // screen.startScreen()
public void stop() throws IOException;               // screen.stopScreen()
public void render(Game game) throws IOException;
public void renderEndScreen(Game game) throws IOException;
public RawKey readKey() throws IOException;          // screen.readInput() -> RawKey
```

`readKey()` содержит ту логику, что раньше была в начале `mapKeyToCommand` (проверка `KeyType.EOF`/`Escape`, извлечение символа) — но не решает, какая это игровая команда, только переводит Lanterna-объект в нейтральный `RawKey`.

### `presentation/Controller.java`

```java
public class Controller {
    private final Game game;
    private final UIView uiView;

    public Controller(Game game, UIView uiView) {
        this.game = game;
        this.uiView = uiView;
    }

    public void run() throws IOException {
        uiView.start();
        try {
            gameLoop();
        } finally {
            uiView.stop();
        }
    }

    private void gameLoop() throws IOException { ... }   // как раньше, но через uiView.render/readKey
    private String mapKeyToCommand(RawKey key) { ... }   // тот же switch по символам, что был, только принимает char, не KeyStroke
}
```

`Controller` импортирует только `domain.*` и свои `UIView`/`RawKey` — ни одного класса из `com.googlecode.lanterna.*`.

### `presentation/Main.java`

```java
public class Main {
    public static void main(String[] args) throws IOException {
        new Controller(new Game(), new UIView()).run();
    }
}
```

## Данные и поток управления

Не меняется относительно текущего поведения — переносится дословно:

1. `Controller.run()` → `uiView.start()`.
2. Цикл: `uiView.render(game)` → если игра окончена — `uiView.renderEndScreen(game)`, `uiView.readKey()` (ждём любую клавишу для выхода), `break` → иначе `uiView.readKey()` → если `quit` — выход из цикла → иначе `mapKeyToCommand(key)` → если не `null` — `game.processInput(command)`.
3. `finally`: `uiView.stop()`.

## Что не меняется

- Игровая логика (`domain`) не трогается вообще.
- Поведение для игрока идентично текущему: те же клавиши, тот же рендер, тот же экран конца игры.
- Размер терминала (`120x62`) берётся как есть из текущего `Main.java`.

## Тестирование

Ручных unit-тестов на `Controller`/`UIView` не заводим — они завязаны на реальный `Screen`/`Terminal` (как и раньше). Проверка — компиляция (`./gradlew compileJava`), существующий тестовый набор (`./gradlew test`, не должен сломаться, так как `domain` не менялся) и ручной прогон игры (`./gradlew run`) с проверкой, что движение, бой и инвентарь работают как раньше.

## Границы задачи (что дальше, не в этой спеке)

- Новая раскладка экрана (поле/лог боя/статус/инвентарь по секторам) — отдельная спека после этой.
- `StartScreen`/`EndScreen` с вводом имени и load/save — отдельная спека после раскладки.
