package presentation;

import datalayer.DataLayer;
import domain.gameSession.DifficultyType;
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
                    DifficultyType difficulty = startScreen.selectDifficulty();
                    String name = startScreen.readPlayerName();
                    Game game = new Game(difficulty);
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
