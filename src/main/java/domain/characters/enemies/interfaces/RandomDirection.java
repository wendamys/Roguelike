package domain.characters.enemies.interfaces;
import domain.navigator.DirectionType;
import java.util.Random;

public interface RandomDirection {

    /**
     * метод {@link #randomDirection()} выбирает рандомно направление движения
     *
     * @return направление движения
     */
    default DirectionType randomDirection() {
        Random random = new Random();
        int randomNumber = random.nextInt(4) + 1;
        System.out.println(randomNumber);
        return switch (randomNumber) {
            case 1 -> DirectionType.FORWARD;
            case 2 -> DirectionType.DOWN;
            case 3 -> DirectionType.RIGHT;
            case 4 -> DirectionType.LEFT;
            default -> null;
        };
    }
}

