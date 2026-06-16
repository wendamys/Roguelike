package domain.map;

 // Cell -> terrain (Что за клетка)
 // Cell -> entities (Кто там стоит)
 // Cell -> flags (Состояние)
import domain.map.TerrainType;

public enum Cell {
    TerrainType,
    Entities;
    boolean flags;
}
