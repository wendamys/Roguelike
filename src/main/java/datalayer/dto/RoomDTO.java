package datalayer.dto;

import domain.map.Room;
import domain.map.RoomType;

public class RoomDTO {
    private int width;
    private int height;
    private int area;
    private int capacityEnemy;
    private int capacityItem;
    private RoomTypeDTO roomType;
    private PositionDTO position;

    public RoomDTO() {}

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getArea() { return area; }
    public void setArea(int area) { this.area = area; }

    public int getCapacityEnemy() { return capacityEnemy; }
    public void setCapacityEnemy(int capacityEnemy) { this.capacityEnemy = capacityEnemy; }

    public int getCapacityItem() { return capacityItem; }
    public void setCapacityItem(int capacityItem) { this.capacityItem = capacityItem; }

    public RoomTypeDTO getRoomType() { return roomType; }
    public void setRoomType(RoomTypeDTO roomType) { this.roomType = roomType; }

    public PositionDTO getPosition() { return position; }
    public void setPosition(PositionDTO position) { this.position = position; }
}
