package datalayer.converter;

import datalayer.dto.EnemiesDTO;
import domain.characters.Enemies;
import domain.characters.enemies.*;

public class EnemiesConverter {

    public static EnemiesDTO toDTO(Enemies enemies) {
        if(enemies == null) return null;

        EnemiesDTO dto = new EnemiesDTO();
        dto.setType(enemies.getType());
        dto.setName(enemies.getName());
        dto.setHealth(enemies.getHealth());
        dto.setMaxHealth(enemies.getMaxHealth());
        dto.setAgility(enemies.getAgility());
        dto.setStrength(enemies.getStrength());
        dto.setHostility(enemies.getHostility());
        dto.setDifficulty(enemies.getDifficulty());
        dto.setInvisible(enemies.getIsInvisible());
        dto.setStunned(enemies.getIsStunned());
        dto.setMimicking(enemies.getIsMimicking());
        dto.setPositionDTO(PositionConverter.toDTO(enemies.getPosition()));
        return dto;
    }

    public static Enemies fromDTO(EnemiesDTO dto) {
        if(dto == null) return null;

        Enemies enemy = null;
        switch (dto.getType()) {
            case ZOMBIE -> enemy = new Zombie(PositionConverter.fromDTO(dto.getPositionDTO()), dto.getDifficulty());
            case OGRE -> enemy = new Ogre(PositionConverter.fromDTO(dto.getPositionDTO()), dto.getDifficulty());
            case VAMPIRE -> enemy = new Vampire(PositionConverter.fromDTO(dto.getPositionDTO()), dto.getDifficulty());
            case SNAKE -> enemy = new Snake(PositionConverter.fromDTO(dto.getPositionDTO()), dto.getDifficulty());
            case GHOST -> enemy = new Ghost(PositionConverter.fromDTO(dto.getPositionDTO()), dto.getDifficulty());
            case MIMIC -> enemy = new Mimic(PositionConverter.fromDTO(dto.getPositionDTO()), dto.getDifficulty());
        }
        if (enemy != null) {
            enemy.setName(dto.getName());
            enemy.setHealth(dto.getHealth());
            enemy.setMaxHealth(dto.getMaxHealth());
            enemy.setAgility(dto.getAgility());
            enemy.setStrength(dto.getStrength());
            enemy.setHostility(dto.getHostility());
            enemy.setIsInvisible(dto.isInvisible());
            enemy.setIsStunned(dto.isStunned());
            enemy.setIsMimicking(dto.isMimicking());
        }
        return enemy;
    }
}
