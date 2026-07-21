# StartScreen / EndScreen + рабочий Save/Load

Дата: 2026-07-21
Статус: согласован, готов к планированию реализации

## Контекст

Задача — пункт 3 из `docs/ARCHITECTURE.md`:

> 3. Создать два класса: "StartScreen" и "EndScreen":
>     - "StartScreen" будет отвечать за отрисовку стартового экрана
>     - Кнопки стартового экрана: "Start game", "Load game", "Exit"
>     - Добавить ввод имени игрока при нажатии старт гейм
>     - Лоад гейм, загрузка с последней позии игрока и полностью его данных
>     - "EndScreen" будет отвечать за отрисовку экрана конца игры и вывод "score"

Третья, последняя подзадача ТЗ (после разбиения `Main` на `Controller`/`UIView` и раскладки экрана — обе уже реализованы, первая смержена в `develop`, вторая ждёт своего PR). Ветка для этой задачи (`feature/wendamys-start-end-screen`) создана от актуального `origin/develop`, независимо от нерешённого PR по раскладке экрана.

### Обнаруженные проблемы в существующем коде

При разборе задачи "Load game — загрузка... полностью его данных" выяснилось, что сохранение/загрузка сейчас физически не может восстановить рабочую игру:

1. **`DataLayer.save()`/`load()` нигде не вызываются в игровом процессе.** Раньше был разовый демо-вызов в старом `Main.java`, которого не стало после разбиения на `Controller`/`UIView`. Сейчас сохранение не происходит никогда.
2. **Карта тайлов не восстанавливается.** `DungeConverter.fromDTO` (`datalayer/converter/DungeConverter.java`) создаёт `new DungeonGenerator()` (весь `TileType[][]` — стены из `initializeMap()`), заполняет только списки `rooms`/`corridors`, но никогда не "вырезает" пол в массиве тайлов по восстановленным комнатам/коридорам. После загрузки `generator.getMap()` был бы сплошной стеной.
3. **Дублирование врагов/предметов при загрузке.** `RoomConverter.fromDTO` уже кладёт врагов/предметы каждой комнаты из `RoomDTO.getEnemiesListDTO()`/`getItemListDTO()`. Но следом `DungeConverter.fromDTO` *ещё раз* распределяет тех же врагов/предметы из плоских `DungeDTO.getAllEnemiesListDTO()`/`getAllItemsListDTO()` по совпадению позиции в комнату — то есть после загрузки в каждой комнате было бы вдвое больше врагов/предметов, чем сохранялось.
4. **`Game` не забирает восстановленное состояние.** `GameConverter.fromDTO` восстанавливает только `player`, `backpack` и `generator` — но не `rooms`, `allItemList`, `allEnemiesList`, `posLevel` и внутренний счётчик `level` самого `Game`. Эти поля остаются от случайно сгенерированного уровня, который создаёт конструктор `new Game()` внутри `fromDTO`.

Все три фикса делаются в этой же задаче — без них "Load game" будет либо падать, либо загружать нерабочее подземелье.

## Цель

1. Починить save/load так, чтобы загрузка реально восстанавливала игру (карта, игрок, враги, предметы, инвентарь, уровень, выход).
2. Добавить автосохранение после каждого хода игрока.
3. `StartScreen` — меню (Start game / Load game / Exit), ввод имени игрока при старте.
4. `EndScreen` — экран конца игры (win/lose + счёт золота), вынесенный из `UIView.renderEndScreen`.
5. `Controller` расширяется до оркестратора всего приложения (меню → игра → конец игры → снова меню), вместо только игрового цикла.
6. Один общий `Screen`/`Terminal` на всё приложение — создаётся один раз в `Main`, передаётся в `StartScreen`, `UIView`, `EndScreen`.

## Архитектура

### `domain/map/DungeonGenerator.java` — восстановление карты

Новый публичный метод, использующий уже существующую (но приватную) логику вырезания:

```java
/**
 * метод перестраивает карту тайлов по уже готовым комнатам и коридорам
 * (используется при загрузке сохранённой игры, без случайной генерации)
 * @param rooms восстановленные комнаты
 * @param corridors восстановленные коридоры
 */
public void rebuildMap(List<Room> rooms, List<Corridor> corridors) {
    initializeMap(); // сброс в стены
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
`initializeMap()`, `carveRoom(Room)`, `isInBounds(int,int)` уже существуют как приватные методы — переиспользуются как есть, видимость не меняется (вызываются изнутри того же класса).

### `datalayer/converter/DungeConverter.java` — фикс дублирования + вызов перестройки карты

В `fromDTO`:
- убрать блок, который повторно распределяет `dto.getAllEnemiesListDTO()`/`getAllItemsListDTO()` по комнатам через сравнение позиций (строки, где `allEnemies`/`allItems` заново кладутся в `room.getEnemyList()`/`getItemList()`) — комнаты уже получили свои списки через `RoomConverter.fromDTO`.
- после того как `rooms`/`corridors` восстановлены и присвоены генератору, вызвать `generator.rebuildMap(rooms, corridors)` вместо `generator.setRooms(rooms)`/`generator.setCorridors(corridors)` по отдельности (`rebuildMap` сам их сохраняет).

### `domain/gameSession/Game.java` — довосстановление состояния

Новые методы:

```java
public void setPosLevel(Position posLevel) {
    this.posLevel = posLevel;
}

public void setLevel(int level) {
    this.level = level;
}

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
(`allItemList`/`allEnemiesList` уже существуют как поля; `allEnemiesList` объявлен `final` — метод очищает и заполняет заново, не переприсваивает ссылку.)

### `datalayer/converter/GameConverter.java` — дирижирует восстановлением

```java
public static Game fromDTO(GameDTO dto) {
    if (dto == null) return null;

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
```

### Автосохранение

В новом верхнеуровневом игровом цикле (см. ниже, в `Controller`) после каждого успешного хода:
```java
game.processInput(input);
DataLayer.save(game);
```

### Общий `Screen` на все экраны

`presentation/UIView.java` — конструктор меняется, чтобы принимать готовый `Screen` вместо создания своего:
```java
public UIView(Screen screen) {
    this.screen = screen;
}
```
(Создание `Terminal`/`Screen` переезжает в `Main`.) `start()`/`stop()` в `UIView` остаются — но теперь их вызывает `Controller` один раз на весь жизненный цикл приложения, а не одну игровую сессию.

`renderEndScreen(Game game)` **переезжает** из `UIView` в новый `EndScreen` — в `UIView` этот метод удаляется.

### `presentation/StartScreen.java` — новый класс

```java
public class StartScreen {
    public enum Choice { START, LOAD, EXIT }

    private final Screen screen;

    public StartScreen(Screen screen) { this.screen = screen; }

    /**
     * метод рисует меню и ждёт, пока игрок выберет пункт (W/S - навигация, Enter - подтверждение)
     */
    public Choice selectChoice() throws IOException { ... }

    /**
     * метод рисует поле ввода имени и построчно читает ввод до Enter
     * @return введённое имя, "Игрок" если пусто
     */
    public String readPlayerName() throws IOException { ... }

    /**
     * метод показывает сообщение (например, "Сохранение не найдено") и ждёт любую клавишу
     */
    public void showMessage(String message) throws IOException { ... }
}
```
Навигация меню: подсвеченный пункт (текущий выбор, например жёлтым/инверсией), `W`/`S` двигают выделение по кругу (3 пункта), `Enter` подтверждает. Ввод имени — посимвольное чтение через `screen.readInput()`: печатаемый символ добавляется к строке и перерисовывается, `Backspace` (`KeyType.Backspace`) удаляет последний символ, `Enter` завершает ввод.

### `presentation/EndScreen.java` — новый класс

```java
public class EndScreen {
    private final Screen screen;

    public EndScreen(Screen screen) { this.screen = screen; }

    /**
     * метод рисует экран победы/поражения и итоговый счёт (перенесено из UIView.renderEndScreen)
     */
    public void render(Game game) throws IOException { ... }
}
```
Содержимое — то, что сейчас в `UIView.renderEndScreen`, без изменений логики.

### `presentation/Controller.java` — оркестратор всего приложения

```java
public class Controller {
    private final StartScreen startScreen;
    private final UIView uiView;
    private final EndScreen endScreen;

    public Controller(StartScreen startScreen, UIView uiView, EndScreen endScreen) {
        this.startScreen = startScreen;
        this.uiView = uiView;
        this.endScreen = endScreen;
    }

    public void run() throws IOException {
        uiView.start();
        try {
            appLoop();
        } finally {
            uiView.stop();
        }
    }

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
                        startScreen.showMessage("Сохранение не найдено");
                    } else {
                        runGameSession(loaded);
                    }
                }
            }
        }
    }

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

    private String mapKeyToCommand(RawKey key) { /* без изменений */ }
}
```
`RawKey` не меняется. `Controller` по-прежнему не импортирует `com.googlecode.lanterna.*` напрямую (только через `StartScreen`/`UIView`/`EndScreen`, которые сами инкапсулируют Lanterna).

### `presentation/Main.java`

```java
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

## Что не меняется

- Игровая логика хода/боя/инвентаря — без изменений.
- Раскладка экрана (5 зон из предыдущей задачи) — если её PR ещё не влит на момент реализации этой задачи, `UIView` в этой ветке работает со старой (уже смерженной в `develop`) версией `drawStatus`/`drawInventory`; конфликт слияния между двумя PR (если оба тронут одни строки `UIView.java`) разрешается на GitHub как обычно, не в рамках этой задачи.
- `Backpack`, `AttackSystem`, `Enemies`/AI — не трогаются.

## Тестирование

Автотестов на `StartScreen`/`EndScreen` не заводим (Lanterna-зависимый код, как и `UIView`/`Controller` раньше). Для доменного/datalayer-фикса (`DungeonGenerator.rebuildMap`, `Game.restoreItemsAndEnemies`/`placeRestoredEntitiesOnMap`, `DungeConverter`/`GameConverter`) — тоже без новых юнит-тестов по умолчанию плана (данных о существующих тестах на datalayer нет), но ручная проверка обязательна: сохранить игру, перезапустить, загрузить, убедиться что карта/враги/предметы/позиция игрока совпадают с моментом сохранения.

## Границы задачи

Это последняя из трёх подзадач ТЗ — после неё весь `docs/ARCHITECTURE.md` выполнен.
