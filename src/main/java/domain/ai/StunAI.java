package domain.ai;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import java.util.function.Predicate;

import static domain.MathUtils.MathUtils.randomValueDouble;

/**
 * AI для Ogre - преследование игрока с 20% шансом стана
 */
public class StunAI implements EnemyAI {
    
    private final EnemyAI baseAI = new AggressiveAI();
    
    @Override
    public DirectionType decideMove(Enemies enemy, Player player, Predicate<Position> walkable) {
        return baseAI.decideMove(enemy, player, walkable);
    }
    
    /**
     * Проверяет, сработал ли стан (20% шанс)
     * @return true если игрок должен быть в стане
     */
    public boolean tryStun() {
        return randomValueDouble() < 0.2;
    }
}
