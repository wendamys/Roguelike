package presentation;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

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
