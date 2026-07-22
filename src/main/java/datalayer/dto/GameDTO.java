package datalayer.dto;

public class GameDTO {
    private PlayerDTO playerDTO;
    private BackpackDTO backpackDTO;
    private LevelDTO levelDTO;
    private DungeDTO dungeDTO;
    private String difficulty;
    private boolean[][] explored;
    private int enemiesKilled;

    public GameDTO() {}

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public boolean[][] getExplored() { return explored; }
    public void setExplored(boolean[][] explored) { this.explored = explored; }

    public int getEnemiesKilled() { return enemiesKilled; }
    public void setEnemiesKilled(int enemiesKilled) { this.enemiesKilled = enemiesKilled; }

    public PlayerDTO getPlayerDTO() { return playerDTO; }
    public void setPlayerDTO(PlayerDTO playerDTO) { this.playerDTO = playerDTO; }

    public BackpackDTO getBackpackDTO() { return backpackDTO; }
    public void setBackpackDTO(BackpackDTO backpackDTO) {this.backpackDTO = backpackDTO; }

    public LevelDTO getLevelDTO() {return levelDTO;}
    public void setLevelDTO(LevelDTO levelDTO) {this.levelDTO = levelDTO;}

    public DungeDTO getDungeDTO() {return dungeDTO;}
    public void setDungeDTO(DungeDTO dungeDTO) {this.dungeDTO = dungeDTO;}
}