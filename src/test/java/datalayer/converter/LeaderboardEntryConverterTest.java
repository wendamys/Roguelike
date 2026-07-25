package datalayer.converter;

import datalayer.dto.LeaderboardEntryDTO;
import domain.leaderboard.LeaderboardEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LeaderboardEntryConverterTest {

    @Test
    void toDtoAndBackPreservesData() {
        LeaderboardEntry entry = new LeaderboardEntry("Герой", 250);

        LeaderboardEntryDTO dto = LeaderboardEntryConverter.toDTO(entry);
        LeaderboardEntry restored = LeaderboardEntryConverter.fromDTO(dto);

        assertEquals("Герой", dto.getName());
        assertEquals(250, dto.getScore());
        assertEquals("Герой", restored.getName());
        assertEquals(250, restored.getScore());
    }

    @Test
    void handlesNulls() {
        assertNull(LeaderboardEntryConverter.toDTO(null));
        assertNull(LeaderboardEntryConverter.fromDTO(null));
    }
}
