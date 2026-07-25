package datalayer.converter;

import datalayer.dto.LeaderboardEntryDTO;
import domain.leaderboard.LeaderboardEntry;

public class LeaderboardEntryConverter {

    public static LeaderboardEntryDTO toDTO(LeaderboardEntry entry) {
        if (entry == null) return null;

        LeaderboardEntryDTO dto = new LeaderboardEntryDTO();
        dto.setName(entry.getName());
        dto.setScore(entry.getScore());
        return dto;
    }

    public static LeaderboardEntry fromDTO(LeaderboardEntryDTO dto) {
        if (dto == null) return null;

        return new LeaderboardEntry(dto.getName(), dto.getScore());
    }
}
