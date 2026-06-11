package domain;

import domain.characters.enemies.interfaces.RandomDirection;
import domain.navigator.DirectionType;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomDirectionTest implements RandomDirection {

    @Test
    void randomDirectionShouldNeverReturnNull() {
        DirectionType result = this.randomDirection();
        assertNotNull(result, "Метод не должен возвращать null согласно контракту");
    }

    @Test
    void randomDirectionShouldReturnOnlyValidDirections() {
        DirectionType result = this.randomDirection();
        boolean isValid = Arrays.asList(DirectionType.values()).contains(result);
        assertTrue(isValid, "Возвращенное значение должно быть одним из допустимых DirectionType");
    }
}