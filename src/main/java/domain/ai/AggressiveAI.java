package domain.ai;

import domain.characters.Enemies;
import domain.characters.Player;
import domain.navigator.DirectionType;
import domain.navigator.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static domain.MathUtils.MathUtils.randomNumber;

/**
 * Базовое поведение врага - преследование игрока в радиусе агра
 * и случайное движение вне агра
 */
public class AggressiveAI implements EnemyAI {

    @Override
    public DirectionType decideMove(Enemies enemy, Player player, Predicate<Position> walkable) {
        DirectionType convergence = enemy.convergenceIsHostility(player, walkable);
        if (convergence != null) {
            return convergence;
        }
        return randomWalkable(enemy, walkable);
    }

    /**
     * метод выбирает случайное направление среди проходимых,
     * чтобы враг вне агра не утыкался в стену
     * @param enemy враг
     * @param walkable проверка проходимости клетки
     * @return направление движения или null, если идти некуда
     */
    private DirectionType randomWalkable(Enemies enemy, Predicate<Position> walkable) {
        List<DirectionType> options = new ArrayList<>();
        for (DirectionType dT : DirectionType.values()) {
            if (walkable.test(enemy.getPosition().posDir(dT))) {
                options.add(dT);
            }
        }
        if (options.isEmpty()) {
            return null;
        }
        return options.get(randomNumber(0, options.size() - 1));
    }
}
