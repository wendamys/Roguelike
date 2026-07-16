package datalayer.converter;

import datalayer.dto.EnemyTypeDTO;import datalayer.dto.EnemyDTO;
import domain.characters.Enemies;
import domain.characters.enemies.*;
import domain.navigator.Position;

public class EnemyConverter {
    public static EnemyDTO toDTO(Enemies enemy) {
        if (enemy == null) return null;
        EnemyDTO dto = new EnemyDTO();
        dto.setHealth(enemy.getHealth());
        dto.setMaxHealth(enemy.getMaxHealth());
        dto.setAgility(enemy.getAgility());
        dto.setStrength(enemy.getStrength());
        dto.setHostility(enemy.getHostility());
        dto.setType(enemy.getType() != null ? EnemyTypeDTO.valueOf(enemy.getType().name()) : null);
        dto.setIsInvisible(enemy.getIsInvisible());
        dto.setIsStunned(enemy.getIsStunned());
        dto.setIsMimicking(enemy.getIsMimicking());
        dto.setPosition(PositionConverter.toDTO(enemy.getPosition()));
        return dto;
    }

    public static Enemies fromDTO(EnemyDTO dto) {
        if (dto == null) return null;
        
        EnemyTypeDTO type = dto.getType();
        if (type == null) return null;
        
        Position position = PositionConverter.fromDTO(dto.getPosition());
        
        switch (type) {
            case ZOMBIE:
                Zombie zombie = new Zombie(position);
                zombie.setHealth(dto.getHealth());
                zombie.setMaxHealth(dto.getMaxHealth());
                zombie.setAgility(dto.getAgility());
                zombie.setStrength(dto.getStrength());
                zombie.setHostility(dto.getHostility());
                zombie.setIsInvisible(dto.getIsInvisible());
                zombie.setIsStunned(dto.getIsStunned());
                zombie.setIsMimicking(dto.getIsMimicking());
                return zombie;
            case OGRE:
                Ogre ogre = new Ogre(position);
                ogre.setHealth(dto.getHealth());
                ogre.setMaxHealth(dto.getMaxHealth());
                ogre.setAgility(dto.getAgility());
                ogre.setStrength(dto.getStrength());
                ogre.setHostility(dto.getHostility());
                ogre.setIsInvisible(dto.getIsInvisible());
                ogre.setIsStunned(dto.getIsStunned());
                ogre.setIsMimicking(dto.getIsMimicking());
                return ogre;
            case VAMPIRE:
                Vampire vampire = new Vampire(position);
                vampire.setHealth(dto.getHealth());
                vampire.setMaxHealth(dto.getMaxHealth());
                vampire.setAgility(dto.getAgility());
                vampire.setStrength(dto.getStrength());
                vampire.setHostility(dto.getHostility());
                vampire.setIsInvisible(dto.getIsInvisible());
                vampire.setIsStunned(dto.getIsStunned());
                vampire.setIsMimicking(dto.getIsMimicking());
                return vampire;
            case SNAKE:
                Snake snake = new Snake(position);
                snake.setHealth(dto.getHealth());
                snake.setMaxHealth(dto.getMaxHealth());
                snake.setAgility(dto.getAgility());
                snake.setStrength(dto.getStrength());
                snake.setHostility(dto.getHostility());
                snake.setIsInvisible(dto.getIsInvisible());
                snake.setIsStunned(dto.getIsStunned());
                snake.setIsMimicking(dto.getIsMimicking());
                return snake;
            case MIMIC:
                Mimic mimic = new Mimic(position);
                mimic.setHealth(dto.getHealth());
                mimic.setMaxHealth(dto.getMaxHealth());
                mimic.setAgility(dto.getAgility());
                mimic.setStrength(dto.getStrength());
                mimic.setHostility(dto.getHostility());
                mimic.setIsInvisible(dto.getIsInvisible());
                mimic.setIsStunned(dto.getIsStunned());
                mimic.setIsMimicking(dto.getIsMimicking());
                return mimic;
            case GHOST:
                Ghost ghost = new Ghost(position);
                ghost.setHealth(dto.getHealth());
                ghost.setMaxHealth(dto.getMaxHealth());
                ghost.setAgility(dto.getAgility());
                ghost.setStrength(dto.getStrength());
                ghost.setHostility(dto.getHostility());
                ghost.setIsInvisible(dto.getIsInvisible());
                ghost.setIsStunned(dto.getIsStunned());
                ghost.setIsMimicking(dto.getIsMimicking());
                return ghost;
            default:
                return null;
        }
    }
}
