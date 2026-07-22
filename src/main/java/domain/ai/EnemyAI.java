package domain.ai;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import java.util.function.Predicate;

/**
 * Интерфейс AI врага определяет стратегию поведения врага
 */
public interface EnemyAI {

    /**
     * Метод выбирает направление движения врага
     * @param enemy враг
     * @param player игрок
     * @param walkable проверка проходимости клетки
     * @return направление движения или null, если идти некуда
     */
    DirectionType decideMove(Enemies enemy, Player player, Predicate<Position> walkable);
}
