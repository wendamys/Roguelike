package domain.ai;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.DirectionType;

/**
 * Интерфейс AI врага определяет стратегию поведения врага
 */
public interface EnemyAI {
    
    /**
     * Метод выбирает направление движения врага
     * @param enemy враг
     * @param player игрок
     * @return направление движения
     */
    DirectionType decideMove(Enemies enemy, Player player);
}
