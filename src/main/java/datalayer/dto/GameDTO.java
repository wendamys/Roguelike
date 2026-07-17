package datalayer.dto;

import java.util.ArrayList;

public class GameDTO {
    private PlayerDTO playerDTO;
    private BackpackDTO backpackDTO;
    private LevelDTO levelDTO;
    private DungeonGeneratorDTO dungeonGeneratorDTO;

    public GameDTO() {}

    public PlayerDTO getPlayerDTO() { return playerDTO; }
    public void setPlayerDTO(PlayerDTO playerDTO) { this.playerDTO = playerDTO; }

    public BackpackDTO getBackpackDTO() { return backpackDTO; }
    public void setBackpackDTO(BackpackDTO backpackDTO) {this.backpackDTO = backpackDTO; }

    public LevelDTO getLevelDTO() {return levelDTO;}
    public void setLevelDTO(LevelDTO levelDTO) {this.levelDTO = levelDTO;}

    public DungeonGeneratorDTO getDungeonGeneratorDTO() {return dungeonGeneratorDTO;}
    public void setDungeonGeneratorDTO(DungeonGeneratorDTO dungeonGeneratorDTO) {this.dungeonGeneratorDTO = dungeonGeneratorDTO;}
}