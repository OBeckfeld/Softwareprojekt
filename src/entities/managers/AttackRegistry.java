package entities.managers;

import entities.Player;
import entities.PlayerTypeEntity;

public interface AttackRegistry {
    void attack(PlayerTypeEntity owner, double x, double y, int height, int width, int duration, int damage);

    void grabOwner(PlayerTypeEntity source);

    void distributeDamage();
}
