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

    public ColorKey getColorKey() {
        return colorKey;
    }

    /**
     * метод {@link #choiceColorList} выбирает цвет ключа с учетом уже созданных ключей
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
     * метод {@link #seeColorKeyList()} выводит список еще не найденных ключей
     */
    public void seeColorKeyList() {
        for(var i: colorKeyList) {
            System.out.println(i);
        }
    }

    /**
     * метод {@link #fillColorKeyList()} заполняет список цветов ключей, которые требуется найти
     */
    private void fillColorKeyList() {
        colorKeyList.add(ColorKey.GREEN);
        colorKeyList.add(ColorKey.BLUE);
        colorKeyList.add(ColorKey.RED);
        colorKeyList.add(ColorKey.YELLOW);
    }

    /**
     * метод {@link #removeColorKeyList(ColorKey)} удаляет цвет ключа из списка
     * @param colorKey цвет ключа
     */
    public void removeColorKeyList(ColorKey colorKey) {
        colorKeyList.remove(colorKey);
    }

    @Override
    public String toString() {
        return String.format("");
    }
}
