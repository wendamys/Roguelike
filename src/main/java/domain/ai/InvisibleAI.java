package domain.ai;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import java.util.function.Predicate;

/**
 * AI для Ghost - уходит в инвиз после срабатывания агра
 * и остается невидимым до тех пор, пока не окажется на соседней клетке с игроком
 */
public class InvisibleAI implements EnemyAI {
    
    private final EnemyAI baseAI = new AggressiveAI();
    private boolean isInvisible = false;
    
    @Override
    public DirectionType decideMove(Enemies enemy, Player player, Predicate<Position> walkable) {
        // Если агр сработал, уходим в инвиз
        if (enemy.convergenceIsHostility(player, walkable) != null) {
            isInvisible = true;
        }

        // Если в инвизе, проверяем, дошли ли до соседней клетки
        if (isInvisible) {
            if (player.getPosition().distanceTo(enemy.getPosition()) <= 1) {
                isInvisible = false;
            }
            return baseAI.decideMove(enemy, player, walkable);
        }

        return baseAI.decideMove(enemy, player, walkable);
    }
    
    /**
     * Проверяет, находится ли враг на соседней клетке с игроком
     * @param enemy враг
     * @param player игрок
     * @return true если на соседней клетке
     */
    public boolean isNextToPlayer(Enemies enemy, Player player) {
        Position enemyPos = enemy.getPosition();
        Position playerPos = player.getPosition();
        int dx = Math.abs(enemyPos.getX() - playerPos.getX());
        int dy = Math.abs(enemyPos.getY() - playerPos.getY());
        return dx <= 1 && dy <= 1;
    }
}
