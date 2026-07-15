package domain.ai;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.DirectionType;

/**
 * AI для Vampire - преследование с регенерацией 10% от урона при атаке
 */
public class RegenAI implements EnemyAI {
    
    private final EnemyAI baseAI = new AggressiveAI();
    
    @Override
    public DirectionType decideMove(Enemies enemy, Player player) {
        return baseAI.decideMove(enemy, player);
    }
    
    /**
     * Рассчитывает количество регенерации (10% от урона)
     * @param damage урон, нанесенный игроку
     * @return количество здоровья для восстановления
     */
    public int calculateRegen(int damage) {
        return damage / 10;
    }
}
