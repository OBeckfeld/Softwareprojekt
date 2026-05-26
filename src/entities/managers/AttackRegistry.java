package entities.managers;

import entities.PlayerTypeEntity;

public interface AttackRegistry {
    void attack(PlayerTypeEntity owner, double x, double y, int height, int width, int duration, int damage);
    void distributeDamage();
}
