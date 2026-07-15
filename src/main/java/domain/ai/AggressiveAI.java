package domain.ai;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.DirectionType;

import static domain.navigator.DirectionType.random;

/**
 * Базовое поведение врага - преследование игрока в радиусе агра
 * и случайное движение вне агра
 */
public class AggressiveAI implements EnemyAI {
    
    @Override
    public DirectionType decideMove(Enemies enemy, Player player) {
        DirectionType convergence = enemy.convergenceIsHostility(player);
        if (convergence != null) {
            return convergence;
        }
        return random();
    }
}