package domain.ai;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.DirectionType;

import static domain.MathUtils.MathUtils.randomValueDouble;

/**
 * AI для Snake - преследование с 20% шансом наложения дебафа "промах следующей атаки"
 */
public class DebuffAI implements EnemyAI {
    
    private final EnemyAI baseAI = new AggressiveAI();
    
    @Override
    public DirectionType decideMove(Enemies enemy, Player player) {
        return baseAI.decideMove(enemy, player);
    }
    
    /**
     * Проверяет, сработал ли дебаф (20% шанс)
     * @return true если нужно наложить дебаф "промах"
     */
    public boolean tryApplyDebuff() {
        return randomValueDouble() < 0.2;
    }
}
