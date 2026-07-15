package domain.ai;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.DirectionType;

/**
 * AI для Mimic - стоит на месте, имитирует предмет
 * После атаки игрока в позицию Mimic начинается преследование
 */
public class AmbushAI implements EnemyAI {
    
    private final EnemyAI baseAI = new AggressiveAI();
    private boolean isMimicking = true;
    
    @Override
    public DirectionType decideMove(Enemies enemy, Player player) {
        if (isMimicking) {
            return null;
        }
        return baseAI.decideMove(enemy, player);
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
