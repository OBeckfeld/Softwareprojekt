package tools;

public class Hitbox {
    double x, y, width, height;

    /**
     * Erstellt eine neue Hitbox mit Position, Breite und Höhe.
     */
    public Hitbox(double x, double y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Gibt die X-Position der Hitbox zurück.
     */
    public double getX(){return x;}

    /**
     * Gibt die Y-Position der Hitbox zurück.
     */
    public double getY(){return y;}

    /**
     * Gibt die Breite der Hitbox zurück.
     */
    public double getWidth(){return width;}

    /**
     * Gibt die Höhe der Hitbox zurück.
     */
    public double getHeight(){return height;}

    /**
     * Setzt die X-Position der Hitbox.
     */
    public void setX(double x){this.x = x;}

    /**
     * Setzt die Y-Position der Hitbox.
     */
    public void setY(double y){this.y = y;}

    /**
     * Prüft, ob sich diese Hitbox mit einer anderen Hitbox überschneidet.
     * Gibt true zurück, wenn sich die beiden Hitboxen berühren oder überlappen.
     */
    public boolean intersects(Hitbox otherHitbox){
        if(x < otherHitbox.x + otherHitbox.width && x + width > otherHitbox.x && y < otherHitbox.y + otherHitbox.height && y + height > otherHitbox.y){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Setzt die Position der Hitbox auf die angegebenen X- und Y-Koordinaten.
     */
    public void setLocation(double x, double y){
        this.x = x;
        this.y = y;
    }
}