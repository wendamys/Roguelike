package domain.battle;

public class BattleInfoType {
    boolean isFight = false;
    public boolean vampireFirstAttack = true;
    boolean ogreCoolDown = false;
    boolean playerAsSleep = true;
    
    // Новые флаги для уникальных способностей
    boolean isStunned = false;           // Игрок пропускает ход
    boolean isMissNextAttack = false;    // Следующая атака игрока промахивается
    boolean isGhostInvisible = false;    // Ghost в инвизе
}
