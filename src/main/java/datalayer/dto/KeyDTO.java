package datalayer.dto;

/**
 * DTO ещё не подобранного ключа, лежащего на карте
 */
public class KeyDTO {
    private String color;
    private PositionDTO positionDTO;

    public KeyDTO() {}

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public PositionDTO getPositionDTO() { return positionDTO; }
    public void setPositionDTO(PositionDTO positionDTO) { this.positionDTO = positionDTO; }
}
