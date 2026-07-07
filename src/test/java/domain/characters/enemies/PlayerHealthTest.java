package domain.characters;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.backpack.items.Food;
import domain.navigator.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerHealthTest {
    private Player player;

    @BeforeEach
    void setUp() {
        Position mockPosition = mock(Position.class);
        player = new Player(mockPosition);
    }

    @Test
    void testSetUpHealthRegen_ShouldNotExceedMaxHealth() {
        player.setHealth(480);
        player.setUpHealthRegen(50); // Лечим на 50 при нехватке 20

        assertEquals(500, player.getHealth(), "Здоровье должно остановиться на maxHealth (500)");
    }

    @Test
    void testUseItemValue_Food_ShouldHealCorrectly() {
        player.setHealth(300);

        // Создаем мок еды, которая лечит на 50 единиц
        Food mockFood = mock(Food.class);
        when(mockFood.getType()).thenReturn(ItemsType.FOOD);
        when(mockFood.getValue()).thenReturn(50);

        player.useItemValue(mockFood);

        assertEquals(350, player.getHealth());
    }
}
