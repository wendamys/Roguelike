package domain.map;

public enum TileType {
    WALL('#'), FLOOR('.'), LEVEL('*'),
    PLAYER('@'), PLAYER_STUNNED('?'), STORE('$'),
    ELIXIR('E'), SCROLL('S'), WEAPON('W'), FOOD('F'),
    ZOMBIE('z'), OGRE('o'), VAMPIRE('v'), GHOST('g'), SNAKE('s'), MIMIC('m');

    private char symbol;

    TileType(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
