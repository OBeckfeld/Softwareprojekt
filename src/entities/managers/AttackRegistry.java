package entities.managers;

import entities.Player;
import entities.PlayerTypeEntity;

public interface AttackRegistry {
    /**
     * Erstellt eine neue Attacke mit Besitzer, Position, Größe,
     * Dauer und Schaden.
     */
    void attack(PlayerTypeEntity owner, double x, double y, int height, int width, int duration, int damage);

    /**
     * Verteilt den Schaden aller aktiven Attacken auf getroffene Entities.
     */
    void distributeDamage();

    /**
     * Erstellt eine neue Attacke mit Besitzer, Position, Größe,
     * Dauer, Schaden und optionaler Rüstungsdurchdringung.
     */
    void attack(PlayerTypeEntity owner, double x, double y, int height, int width, int duration, int damage, boolean armorPierce);

    /**
     * Speichert den Verursacher einer Attacke für die spätere Schadensverarbeitung.
     */
    void grabOwner(PlayerTypeEntity source);
}
