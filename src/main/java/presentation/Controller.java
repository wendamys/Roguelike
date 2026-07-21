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
