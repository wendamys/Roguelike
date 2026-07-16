package domain.navigator;

import domain.characters.Character;

public class MovementSystem {
    public void moveDir(DirectionType directionType, Character character) {
        Position currentPosition = character.getPosition();
        Position newPosition = directionType.applyTo(currentPosition);
        character.setPosition(newPosition);
    }
}
