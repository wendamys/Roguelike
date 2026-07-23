package domain.map;

import domain.navigator.Position;
import java.util.*;

public class Key {

    private Position position;
    private final ColorKey colorKey;
    private final ArrayList<ColorKey> colorKeyList = new ArrayList<>(4);
    private static final ArrayList<ColorKey> choiceColorList = new ArrayList<>(4);

    public Key() {
        this.colorKey = choiceColorKey();
    }

    /**
     * конструктор для генератора: цвет и позицию назначает он сам,
     * статический список цветов при этом не расходуется
     * @param position позиция ключа на карте
     * @param colorKey цвет ключа
     */
    public Key(Position position, ColorKey colorKey) {
        this.position = position;
        this.colorKey = colorKey;
    }

    public ColorKey getColorKey() {
        return colorKey;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * метод выбирает цвет ключа с учетом уже созданных ключей
     * @return цвет ключа
     */
    private ColorKey choiceColorKey() {
        for (int i = choiceColorList.size(); i < 4; i++) {
            switch (i) {
                case 0 -> {
                    choiceColorList.add(ColorKey.GREEN);
                    return ColorKey.GREEN;
                }
                case 1 -> {
                    choiceColorList.add(ColorKey.BLUE);
                    return ColorKey.BLUE;
                }
                case 2 -> {
                    choiceColorList.add(ColorKey.RED);
                    return ColorKey.RED;
                }
                case 3 -> {
                    choiceColorList.add(ColorKey.YELLOW);
                    return ColorKey.YELLOW;
                }
            }
        }
        return null;
    }

    /**
     * метод выводит список еще не найденных ключей
     */
    public void seeColorKeyList() {
        for(var i: colorKeyList) {
            System.out.println(i);
        }
    }

    /**
     * метод заполняет список цветов ключей, которые требуется найти
     */
    private void fillColorKeyList() {
        colorKeyList.add(ColorKey.GREEN);
        colorKeyList.add(ColorKey.BLUE);
        colorKeyList.add(ColorKey.RED);
        colorKeyList.add(ColorKey.YELLOW);
    }

    /**
     * метод удаляет цвет ключа из списка
     * @param colorKey цвет ключа
     */
    public void removeColorKeyList(ColorKey colorKey) {
        colorKeyList.remove(colorKey);
    }

    @Override
    public String toString() {
        return String.format("%s", colorKey);
    }
}