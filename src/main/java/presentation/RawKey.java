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
