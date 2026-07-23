package domain.map;

public enum TileType {
    WALL('#'), FLOOR('.'), LEVEL('*'), PLAYER('@'), PLAYER_STUNNED('?'),
    ELIXIR('E'), SCROLL('S'), WEAPON('W'), FOOD('F'),
    ZOMBIE('z'), OGRE('o'), VAMPIRE('v'), GHOST('g'), SNAKE('s'), MIMIC('m'),
    DOOR_GREEN('+'), DOOR_BLUE('+'), DOOR_RED('+'), DOOR_YELLOW('+'),
    KEY_GREEN('&'), KEY_BLUE('&'), KEY_RED('&'), KEY_YELLOW('&'),
    SHOP('$'),
    B0('0'), B1('1'), B2('2'), B3('3'), B4('4'), B5('5'), B6('6'), B7('7'), B8('8'), B9('9');

    private char symbol;

    TileType(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
