package datalayer.dto;

import domain.map.Level;

public class LevelDTO {
    private int levelUp;

    public LevelDTO() {}

    public LevelDTO(Level level) {
        if (level == null) return;
        this.levelUp = level.getLevelUp();
    }

    public int getLevelUp() { return levelUp; }
    public void setLevelUp(int levelUp) { this.levelUp = levelUp; }
}
