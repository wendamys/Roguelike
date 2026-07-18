package datalayer.dto;

public class GameDTO {
    private PlayerDTO playerDTO;
    private BackpackDTO backpackDTO;
    private LevelDTO levelDTO;
    private DungeDTO dungeDTO;

    public GameDTO() {}

    public PlayerDTO getPlayerDTO() { return playerDTO; }
    public void setPlayerDTO(PlayerDTO playerDTO) { this.playerDTO = playerDTO; }

    public BackpackDTO getBackpackDTO() { return backpackDTO; }
    public void setBackpackDTO(BackpackDTO backpackDTO) {this.backpackDTO = backpackDTO; }

    public LevelDTO getLevelDTO() {return levelDTO;}
    public void setLevelDTO(LevelDTO levelDTO) {this.levelDTO = levelDTO;}

    public DungeDTO getDungeDTO() {return dungeDTO;}
    public void setDungeDTO(DungeDTO dungeDTO) {this.dungeDTO = dungeDTO;}
}