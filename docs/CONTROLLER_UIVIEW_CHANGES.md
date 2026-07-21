# Что изменилось: Main разбит на Controller / UIView / RawKey

Коротко: задача была из `docs/ARCHITECTURE.md` (пункт 1) — весь код, который жил в одном файле `presentation/Main.java` (создание Lanterna-экрана, отрисовка, игровой цикл, разбор нажатых клавиш), разложить по двум классам с чёткими ролями, а `Main` оставить только точкой входа.

Игровая логика (`domain`) **не менялась** вообще — трогал только presentation-слой. Полная спека и план лежат в `docs/superpowers/specs/2026-07-21-controller-uiview-design.md` и `docs/superpowers/plans/2026-07-21-controller-uiview.md`, если нужны детали обсуждения — здесь только суть для быстрого "въехать".

---

## 1. Новый файл: `presentation/RawKey.java`

Маленький контейнер без единой зависимости от Lanterna:

```java
public class RawKey {
    public RawKey(char character, boolean quit) {...}
    public char getCharacter() {...}
    public boolean isQuit() {...}
}
```

Зачем он вообще нужен: если бы `UIView` отдавал `Controller`'у сырой Lanterna-объект `KeyStroke`, `Controller` пришлось бы импортировать `com.googlecode.lanterna.input.KeyStroke` — и разделение потеряло бы смысл (Controller бы всё равно знал про Lanterna). `RawKey` — это единственное, что "перетекает" из UI-мира наружу, и оно нейтральное.

`quit == true` — это бывший `KeyType.EOF`/`KeyType.Escape`. Если клавиша не символьная (`getKeyStroke().getCharacter() == null`), `character` равен `'\0'`.

---

## 2. Новый файл: `presentation/UIView.java`

Сюда переехало **всё**, что раньше делало Main.java Lanterna-специфичным:
- создание `Terminal`/`Screen` (теперь в конструкторе `UIView()`, размер терминала тот же — `120x62`);
- `start()`/`stop()` — обёртки над `screen.startScreen()`/`screen.stopScreen()`;
- вся отрисовка: `render`, `drawMap`, `drawStatus`, `drawInventory`, `itemsFor`, `titleFor`, `colorFor`, `renderEndScreen` — один в один как было, просто из `static`-методов Main стали обычными методами инстанса;
- `readKey()` — новый метод, раньше эта логика (чтение `screen.readInput()` + проверка `KeyType.EOF`/`Escape`) была размазана по `gameLoop`/`mapKeyToCommand` в Main, теперь она вся здесь и сразу отдаёт `RawKey`:

```java
public RawKey readKey() throws IOException {
    KeyStroke key = screen.readInput();
    boolean quit = key.getKeyType() == KeyType.EOF || key.getKeyType() == KeyType.Escape;
    char character = key.getCharacter() == null ? '\0' : key.getCharacter();
    return new RawKey(character, quit);
}
```

**Важный нюанс:** конструктор `UIView()` теперь объявлен `throws IOException` (создание `Terminal` может кинуть исключение) — раньше это происходило прямо в `main()`, теперь просто на один уровень глубже, но `main()` по-прежнему объявляет `throws IOException`, так что ничего дополнительно ловить не пришлось.

`UIView` ничего не знает о том, что означает нажатая клавиша для игры — это осознанно вынесено в `Controller`.

---

## 3. Новый файл: `presentation/Controller.java`

Сюда переехали `gameLoop` и `mapKeyToCommand` из старого Main — то есть "механизм взаимодействия консоли с игрой", как написано в ТЗ:

```java
public class Controller {
    public Controller(Game game, UIView uiView) {...}

    public void run() throws IOException {
        uiView.start();
        try {
            gameLoop();
        } finally {
            uiView.stop();
        }
    }

    private void gameLoop() throws IOException {
        // тот же цикл: render -> проверка конца игры -> readKey -> mapKeyToCommand -> game.processInput
    }

    private String mapKeyToCommand(RawKey key) {
        // тот же switch по w/a/s/d/e/h/j/k/0-9, только принимает RawKey, а не KeyStroke
    }
}
```

`Controller` **не импортирует ничего из `com.googlecode.lanterna.*`** — это была ключевая цель разбиения. Он знает только про `domain.gameSession.Game` и про свои `UIView`/`RawKey`. Проверено отдельно на ревью: единственный общий тип исключений — `IOException`, это JDK-класс, не Lanterna, так что "протечки" через exception-типы нет.

Раньше `screen.startScreen()`/`screen.stopScreen()` вызывались прямо в `main()` вокруг `try/finally`. Теперь этот `try/finally` живёт в `Controller.run()`.

---

## 4. `presentation/Main.java` — теперь просто точка входа

Было — 226 строк со всей логикой. Стало:

```java
public class Main {
    public static void main(String[] args) throws IOException {
        new Controller(new Game(), new UIView()).run();
    }
}
```

Ничего из старого кода не потерялось — весь функционал переехал в `UIView`/`Controller` дословно, здесь просто их сборка.

---

## Поведение для игрока не изменилось

Отдельно проверяли на ревью тонкий момент: раньше `mapKeyToCommand` сам проверял `key.getCharacter() == null` и в этом случае сразу возвращал `null`. Теперь `UIView.readKey()` превращает `null`-символ в `'\0'`, а `Controller.mapKeyToCommand` делает `Character.toLowerCase('\0')`, что не совпадает ни с одним `case` в свиче — тоже возвращает `null`. Результат идентичен, просто путь чуть другой.

Те же клавиши (`w/a/s/d` — движение, `e/h/j/k` — типы инвентаря, `0-9` — выбор предмета, `Esc` — выход), тот же рендер, те же цвета, тот же экран конца игры, тот же размер терминала (120x62).

## Тестирование

Автотестов на `UIView`/`Controller` не заводили (как и раньше не было на `Main`) — они завязаны на реальный Lanterna `Screen`/`Terminal`, юнит-тестировать там особо нечего. Проверено: компиляция (`./gradlew compileJava`), весь существующий тестовый набор (`./gradlew test`, не менялся, т.к. `domain` не трогали) и запуск (`./gradlew run`) без падения на старте.

**Что не проверено автоматически и стоит прогнать руками:** живое управление в открывшемся окне — движение WASD, открытие панели инвентаря по `e/h/j/k`, выбор предмета цифрой, выход по `Esc`. Разбиение на классы это не должно было изменить, но реальный клавиатурный ввод через GUI-окно я (и субагенты, которые писали код) проверить не можем — этот пункт остался на человека.

## Что дальше (не в этой задаче)

Следующие два пункта того же ТЗ из `docs/ARCHITECTURE.md` — новая раскладка экрана (поле/лог боя/статус/инвентарь по секторам) и `StartScreen`/`EndScreen` с вводом имени и load/save — пойдут отдельными спеками поверх этого разбиения.
