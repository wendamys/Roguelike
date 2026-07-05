package domain.characters;

import domain.backpack.Item;
import domain.backpack.ItemsType;
import domain.backpack.items.Elixir;
import domain.backpack.items.Scroll;
import domain.backpack.items.Weapon;
import domain.navigator.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerAllBuffsTest {
    private Player player;

    @BeforeEach
    void setUp() {
        Position mockPosition = mock(Position.class);
        player = new Player(mockPosition);
    }

    // ==========================================
    // ТЕСТЫ ОРУЖИЯ
    // ==========================================

    @Test
    void testUseWeapon_ShouldIncreaseBuffStrength() {
        int baseBuffStrength = player.getBuffStrength();

        Weapon mockWeapon = mock(Weapon.class);
        when(mockWeapon.getType()).thenReturn(ItemsType.WEAPON);
        when(mockWeapon.getValue()).thenReturn(25);

        player.useItemValue(mockWeapon);

        assertEquals(baseBuffStrength + 25, player.getBuffStrength());
    }

    // ==========================================
    // ТЕСТЫ СВИТКОВ
    // ==========================================

    @Test
    void testUseScroll_Health_ShouldIncreaseMaxHealth() {
        Scroll mockScroll = mock(Scroll.class);
        when(mockScroll.getType()).thenReturn(ItemsType.SCROLL);
        when(mockScroll.getValue()).thenReturn(100);

        // метод getSubType() вернёт "HEALTH" в виде объекта

        when(mockScroll.getSubType()).thenAnswer(inv ->
                Enum.valueOf((Class<Enum>) inv.getMethod().getReturnType(), "HEALTH")
        );

        player.useItemValue(mockScroll);

        assertEquals(600, player.getMaxHealth());
    }

    @Test
    void testUseScroll_Agility_ShouldIncreaseBuffAgility() {
        int baseAgility = player.getBuffAgility();
        Scroll mockScroll = mock(Scroll.class);
        when(mockScroll.getType()).thenReturn(ItemsType.SCROLL);
        when(mockScroll.getValue()).thenReturn(15);
        when(mockScroll.getSubType()).thenAnswer(inv ->
                Enum.valueOf((Class<Enum>) inv.getMethod().getReturnType(), "AGILITY")
        );

        player.useItemValue(mockScroll);

        assertEquals(baseAgility + 15, player.getBuffAgility());
    }

    @Test
    void testUseScroll_Strength_ShouldIncreaseBuffStrength() {
        int baseStrength = player.getBuffStrength();
        Scroll mockScroll = mock(Scroll.class);
        when(mockScroll.getType()).thenReturn(ItemsType.SCROLL);
        when(mockScroll.getValue()).thenReturn(20);
        when(mockScroll.getSubType()).thenAnswer(inv ->
                Enum.valueOf((Class<Enum>) inv.getMethod().getReturnType(), "STRENGTH")
        );

        player.useItemValue(mockScroll);

        assertEquals(baseStrength + 20, player.getBuffStrength());
    }

    // ==========================================
    // ТЕСТЫ ЭЛИКСИРОВ
    // ==========================================

    @Test
    void testUseElixir_Health_ShouldHealPlayer() {
        player.setHealth(300);
        Elixir mockElixir = mock(Elixir.class);
        when(mockElixir.getType()).thenReturn(ItemsType.ELIXIR);
        when(mockElixir.getValue()).thenReturn(50);
        when(mockElixir.getSubType()).thenAnswer(inv ->
                Enum.valueOf((Class<Enum>) inv.getMethod().getReturnType(), "HEALTH")
        );

        player.useItemValue(mockElixir);

        assertEquals(350, player.getHealth());
    }

    @Test
    void testUseElixir_Agility_ShouldIncreaseBuffAgility() {
        int baseAgility = player.getBuffAgility();
        Elixir mockElixir = mock(Elixir.class);
        when(mockElixir.getType()).thenReturn(ItemsType.ELIXIR);
        when(mockElixir.getValue()).thenReturn(12);
        when(mockElixir.getSubType()).thenAnswer(inv ->
                Enum.valueOf((Class<Enum>) inv.getMethod().getReturnType(), "AGILITY")
        );

        player.useItemValue(mockElixir);

        assertEquals(baseAgility + 12, player.getBuffAgility());
    }
}
