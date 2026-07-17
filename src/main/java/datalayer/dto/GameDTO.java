package datalayer.dto;

public class GameDTO {
    private PlayerDTO playerDTO;
    private BackpackDTO backpackDTO;

    public PlayerDTO getPlayerDTO() { return playerDTO; }
    public void setPlayerDTO(PlayerDTO playerDTO) { this.playerDTO = playerDTO; }

    public BackpackDTO getBackpackDTO() { return backpackDTO; }
    public void setBackpackDTO(BackpackDTO backpackDTO) {this.backpackDTO = backpackDTO; }
}
