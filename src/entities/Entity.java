package entities;

import entities.components.MovementComponent;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.Hitbox;
import tools.Vector;
import tools.TileManager;

/**
 * Abstrakte Basisklasse für alle Entities im Spiel.
 * Enthält grundlegende Eigenschaften wie Position, Größe, Hitbox und Bewegung.
 * Alle spielrelevanten Objekte (Spieler, Gegner, Angriffe etc.) erben von dieser Klasse.
 */
public abstract class Entity {

    /** Richtungskonstante für Norden (oben). */
    public static final int NORTH = 3;

    /** Richtungskonstante für Osten (rechts). */
    public static final int EAST = 0;

    /** Richtungskonstante für Süden (unten). */
    public static final int SOUTH = 1;

    /** Richtungskonstante für Westen (links). */
    public static final int WEST = 2;

    /** Registry für Angriffe. */
    protected AttackRegistry attackRegistry;

    /** Höhe der Entity in Pixeln. */
    protected int height;

    /** Breite der Entity in Pixeln. */
    protected int width;

    /** X-Koordinate der Entity. */
    protected double x;

    /** Y-Koordinate der Entity. */
    protected double y;

    /** Standardgeschwindigkeit der Entity. */
    protected double defaultSpeed = 5;

    /** Bewegungskomponente für tile-basierte Kollision und Bewegung. */
    protected MovementComponent movement;

    /** Hurtbox der Entity für Kollisionsprüfungen. */
    protected Hitbox hurtbox;

    /** Registry aller aktiven Entities. */
    public EntityRegistry registry;

    /** TileManager für tile-basierte Berechnungen. */
    protected TileManager tileManager;

    /** Gibt an ob die Entity tot ist. */
    protected boolean dead;

    /** Z-Ebene für die Rendering-Reihenfolge. */
    protected int z = 1;

    /**
     * Erstellt eine neue Entity mit AttackRegistry.
     *
     * @param x              X-Koordinate
     * @param y              Y-Koordinate
     * @param width          Breite der Entity
     * @param height         Höhe der Entity
     * @param registry       EntityRegistry zum Registrieren der Entity
     * @param attackRegistry AttackRegistry für Angriffe
     * @param tileManager    TileManager für Kollisionen
     */
    public Entity(double x, double y, int width, int height, EntityRegistry registry,
                  AttackRegistry attackRegistry, TileManager tileManager) {
        this.attackRegistry = attackRegistry;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent(tileManager);
        hurtbox = new Hitbox(x, y, width, height);
        registry.register(this);
        this.registry = registry;
        this.tileManager = tileManager;
        dead = false;
    }

    /**
     * Erstellt eine neue Entity ohne AttackRegistry.
     *
     * @param x           X-Koordinate
     * @param y           Y-Koordinate
     * @param width       Breite der Entity
     * @param height      Höhe der Entity
     * @param registry    EntityRegistry zum Registrieren der Entity
     * @param tileManager TileManager für Kollisionen
     */
    public Entity(double x, double y, int width, int height, EntityRegistry registry,
                  TileManager tileManager) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent(tileManager);
        hurtbox = new Hitbox(x, y, width, height);
        registry.register(this);
        this.registry = registry;
    }

    /**
     * Erstellt eine neue Entity mit angepasster Hitbox-Größe.
     * Die Hitbox wird zentriert und am unteren Rand der Entity positioniert.
     *
     * @param x            X-Koordinate
     * @param y            Y-Koordinate
     * @param width        Breite der Entity
     * @param height       Höhe der Entity
     * @param registry     EntityRegistry zum Registrieren der Entity
     * @param tileManager  TileManager für Kollisionen
     * @param hitBoxWidth  Breite der Hitbox
     * @param hitBoxHeight Höhe der Hitbox
     */
    public Entity(double x, double y, int width, int height, EntityRegistry registry,
                  TileManager tileManager, int hitBoxWidth, int hitBoxHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent(tileManager);
        double hitboxX = x + (width - hitBoxWidth) / 2.0;
        double hitboxY = y + (height - hitBoxHeight);
        hurtbox = new Hitbox(hitboxX, hitboxY, hitBoxWidth, hitBoxHeight);
        registry.register(this);
        this.registry = registry;
    }

    /**
     * Gibt die Hurtbox der Entity zurück.
     *
     * @return Hurtbox der Entity
     */
    public Hitbox getHurtbox() { return hurtbox; }

    /**
     * Gibt die Z-Ebene der Entity zurück.
     *
     * @return Z-Ebene
     */
    public int getZ() { return z; }

    /**
     * Setzt die Z-Ebene der Entity.
     *
     * @param z Neue Z-Ebene
     */
    public void setZ(int z) { this.z = z; }

    /**
     * Gibt die X-Koordinate der Entity zurück.
     *
     * @return X-Koordinate
     */
    public double getX() { return x; }

    /**
     * Setzt die X-Koordinate der Entity.
     *
     * @param x Neue X-Koordinate
     */
    public void setX(double x) { this.x = x; }

    /**
     * Gibt die Y-Koordinate der Entity zurück.
     *
     * @return Y-Koordinate
     */
    public double getY() { return y; }

    /**
     * Setzt die Y-Koordinate der Entity.
     *
     * @param y Neue Y-Koordinate
     */
    public void setY(double y) { this.y = y; }

    /**
     * Gibt die Höhe der Entity zurück.
     *
     * @return Höhe in Pixeln
     */
    public int getHeight() { return height; }

    /**
     * Gibt die Breite der Entity zurück.
     *
     * @return Breite in Pixeln
     */
    public int getWidth() { return width; }

    /**
     * Gibt die EntityRegistry zurück.
     *
     * @return EntityRegistry
     */
    public EntityRegistry getRegistry() { return registry; }

    /**
     * Wird jeden Tick aufgerufen.
     * Wird in Unterklassen überschrieben um Entity-spezifisches Verhalten zu implementieren.
     */
    public void update() {}

    /**
     * Entfernt die Entity aus der Registry.
     */
    protected void unregister() { registry.unregister(this); }

    /**
     * Berechnet und gibt die Mittelpunkt-Koordinaten der Entity zurück.
     *
     * @return Array mit X- und Y-Koordinate des Mittelpunkts
     */
    public double[] getCenter() {
        double cX = x + width / 2;
        double cY = y + height / 2;
        return new double[]{cX, cY};
    }

    /**
     * Bewegt die Entity anhand eines Vektors.
     *
     * @param vector Bewegungsvektor
     */
    public void move(Vector vector) { movement.applyVector(this, vector); }

    /**
     * Berechnet die Richtung zu einem Zielpunkt.
     * Gibt die Richtungskonstante (NORTH, EAST, SOUTH, WEST) zurück
     * die am besten zur Richtung zum Ziel passt.
     *
     * @param targetX X-Koordinate des Ziels
     * @param targetY Y-Koordinate des Ziels
     * @return Richtungskonstante
     */
    protected int getDirectionTo(double targetX, double targetY) {
        double xDis = Math.abs(x - targetX);
        double yDis = Math.abs(y - targetY);
        int xDir = (x - targetX > 0) ? WEST : EAST;
        int yDir = (y - targetY > 0) ? NORTH : SOUTH;
        return (xDis > yDis) ? xDir : yDir;
    }

    /**
     * Gibt den Versatz in X- und Y-Richtung für eine bestimmte Richtung zurück.
     *
     * @param dir Richtungskonstante (NORTH, EAST, SOUTH, WEST)
     * @return Array mit X- und Y-Versatz
     */
    public int[] getOffsetCoords(int dir) {
        if (dir == NORTH) return new int[]{0, -1};
        if (dir == EAST)  return new int[]{1, 0};
        if (dir == SOUTH) return new int[]{0, 1};
        return new int[]{-1, 0};
    }

    /**
     * Gibt die entgegengesetzte Richtung zurück.
     *
     * @param dir Richtungskonstante
     * @return Entgegengesetzte Richtungskonstante
     */
    public int getOppDirection(int dir) {
        if (dir == NORTH) return SOUTH;
        if (dir == SOUTH) return NORTH;
        if (dir == EAST)  return WEST;
        return EAST;
    }

    /**
     * Gibt die Anzahl der Skillpunkte zurück die beim Tod dieser Entity vergeben werden.
     *
     * @return Skillpunkte beim Tod
     */
    public int getPointsOnDeath() { return 1; }

    /**
     * Gibt an ob die Entity tot ist.
     *
     * @return true wenn die Entity tot ist, sonst false
     */
    public boolean isDead() { return dead; }
}