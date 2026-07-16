package datalayer.dto;

import domain.navigator.Position;

public class PositionDTO {
    private int x;
    private int y;
    private boolean isClose;

    public PositionDTO() {}

    public PositionDTO(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PositionDTO(int x, int y, boolean isClose) {
        this.x = x;
        this.y = y;
        this.isClose = isClose;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public boolean getIsClose() { return isClose; }
    public void setIsClose(boolean isClose) { this.isClose = isClose; }

    public static PositionDTO toDTO(Position position) {
        if (position == null) return null;
        return new PositionDTO(position.getX(), position.getY(), position.getIsClose());
    }

    public Position toEntity() {
        Position position = new Position(x, y);
        position.setIsClose(isClose);
        return position;
    }
}
