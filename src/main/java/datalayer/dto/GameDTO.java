package datalayer.dto;

import domain.gameSession.Game;

import java.util.ArrayList;
import java.util.List;

public class GameDTO {
    private LevelDTO levelDTO;
    private PlayerDTO playerDTO;
    private BackpackDTO backpackDTO;
    private PositionDTO posLevel;
    private boolean isGameEnded;
    private List<EnemyDTO> enemiesList;
    private List<ItemDTO> itemsList;

    public GameDTO() {
        this.enemiesList = new ArrayList<>();
        this.itemsList = new ArrayList<>();
    }

    public LevelDTO getLevelDTO() { return levelDTO; }
    public void setLevelDTO(LevelDTO levelDTO) { this.levelDTO = levelDTO; }

    public PlayerDTO getPlayerDTO() { return playerDTO; }
    public void setPlayerDTO(PlayerDTO playerDTO) { this.playerDTO = playerDTO; }

    public BackpackDTO getBackpackDTO() { return backpackDTO; }
    public void setBackpackDTO(BackpackDTO backpackDTO) { this.backpackDTO = backpackDTO; }

    public PositionDTO getPosLevel() { return posLevel; }
    public void setPosLevel(PositionDTO posLevel) { this.posLevel = posLevel; }

    public boolean isGameEnded() { return isGameEnded; }
    public void setGameEnded(boolean gameEnded) { isGameEnded = gameEnded; }

    public List<EnemyDTO> getEnemiesList() { return enemiesList; }
    public void setEnemiesList(List<EnemyDTO> enemiesList) { this.enemiesList = enemiesList; }

    public List<ItemDTO> getItemsList() { return itemsList; }
    public void setItemsList(List<ItemDTO> itemsList) { this.itemsList = itemsList; }
}
