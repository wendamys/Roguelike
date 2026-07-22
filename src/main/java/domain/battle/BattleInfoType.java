package domain.battle;

import java.util.ArrayList;
import java.util.List;

public class BattleInfoType {
    boolean isFight = false;
    public boolean vampireFirstAttack = true;
    boolean ogreCoolDown = false;
    boolean playerAsSleep = true;
    
    // Новые флаги для уникальных способностей
    boolean isStunned = false;           // Игрок пропускает ход
    boolean isMissNextAttack = false;    // Следующая атака игрока промахивается
    boolean isGhostInvisible = false;    // Ghost в инвизе

    // События боя за текущий ход, Game переливает их в лог сообщений и очищает
    private final List<String> events = new ArrayList<>();

    public List<String> getEvents() {
        return events;
    }
}
