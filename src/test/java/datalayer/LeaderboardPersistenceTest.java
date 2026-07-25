package datalayer;

import domain.leaderboard.LeaderboardEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeaderboardPersistenceTest {

    private static final File FILE = new File("leaderboard.json");

    @BeforeEach
    @AfterEach
    void cleanup() {
        if (FILE.exists()) {
            FILE.delete();
        }
    }

    @Test
    void loadReturnsEmptyListWhenFileMissing() {
        List<LeaderboardEntry> entries = DataLayer.loadLeaderboard();

        assertTrue(entries.isEmpty());
    }

    @Test
    void saveAndLoadRoundTrip() {
        DataLayer.saveLeaderboard(List.of(
                new LeaderboardEntry("Алиса", 100),
                new LeaderboardEntry("Боб", 50)
        ));

        List<LeaderboardEntry> loaded = DataLayer.loadLeaderboard();

        assertEquals(2, loaded.size());
        assertEquals("Алиса", loaded.get(0).getName());
        assertEquals(100, loaded.get(0).getScore());
    }

    @Test
    void addEntrySortsDescendingByScore() {
        DataLayer.addLeaderboardEntry(new LeaderboardEntry("Низкий", 10));
        DataLayer.addLeaderboardEntry(new LeaderboardEntry("Высокий", 90));
        DataLayer.addLeaderboardEntry(new LeaderboardEntry("Средний", 50));

        List<LeaderboardEntry> loaded = DataLayer.loadLeaderboard();

        assertEquals(3, loaded.size());
        assertEquals("Высокий", loaded.get(0).getName());
        assertEquals("Средний", loaded.get(1).getName());
        assertEquals("Низкий", loaded.get(2).getName());
    }

    @Test
    void addEntryTrimsToTop10() {
        for (int i = 0; i < 12; i++) {
            DataLayer.addLeaderboardEntry(new LeaderboardEntry("Игрок" + i, i));
        }

        List<LeaderboardEntry> loaded = DataLayer.loadLeaderboard();

        assertEquals(10, loaded.size());
        assertEquals(11, loaded.get(0).getScore());
        assertEquals(2, loaded.get(9).getScore());
    }
}
