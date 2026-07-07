package domain.characters;

import domain.navigator.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerInitTest {
    private Position mockPosition;
    private Player player;

    @BeforeEach
    void setUp() {
        mockPosition = mock(Position.class);
        player = new Player(mockPosition);
    }

    @Test
    void testInitialStats_ShouldMatchDefaultBalance() {
        assertEquals(500, player.getMaxHealth());
        assertEquals(500, player.getHealth());
        assertEquals(0, player.getGold());
        assertEquals(70, player.getAgility());
        assertEquals(70, player.getStrength());
    }

    @Test
    void testNameAndGoldGettersSetters() {
        player.setName("Hero");
        player.setGold(150);

        assertEquals("Hero", player.getName());
        assertEquals(150, player.getGold());
    }
}
