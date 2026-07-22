package domain.ai;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import java.util.function.Predicate;

/**
 * AI для Mimic - стоит на месте, имитирует предмет
 * После атаки игрока в позицию Mimic начинается преследование
 */
public class AmbushAI implements EnemyAI {
    
    private final EnemyAI baseAI = new AggressiveAI();
    private boolean isMimicking = true;
    
    @Override
    public DirectionType decideMove(Enemies enemy, Player player, Predicate<Position> walkable) {
        if (isMimicking) {
            return null;
        }
        // В агрессивном режиме всегда преследуем игрока, без случайного движения
        DirectionType convergence = enemy.convergenceIsHostility(player, walkable);
        if (convergence != null) {
            return convergence;
        }
        // Если игрок не в радиусе агра - идем к нему (без random())
        return enemy.convergence(player, walkable);
    }
    
    /**
     * Активирует атаку - Mimic становится агрессивным
     * @return true если Mimic был в режиме имитации
     */
    public boolean activateAttack() {
        boolean wasMimicking = isMimicking;
        isMimicking = false;
        return wasMimicking;
    }
    
    /**
     * Проверяет, находится ли Mimic в режиме имитации
     * @return true если имитирует предмет
     */
    public boolean isMimicking() {
        return isMimicking;
    }
}
