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
