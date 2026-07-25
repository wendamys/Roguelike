package domain.leaderboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeaderboardEntryTest {

    @Test
    void storesNameAndScore() {
        LeaderboardEntry entry = new LeaderboardEntry("Игрок", 123);

        assertEquals("Игрок", entry.getName());
        assertEquals(123, entry.getScore());
    }
}
