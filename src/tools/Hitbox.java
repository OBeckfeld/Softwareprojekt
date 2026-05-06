package tools;

public class Hitbox {
    double x, y, width, height;

    public Hitbox(double x, double y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX(){return x;}
    public double getY(){return y;}
    public double getWidth(){return width;}
    public double getHeight(){return height;}
    public void setX(double x){this.x = x;}
    public void setY(double y){this.y = y;}

    public boolean intersects(Hitbox otherHitbox){
        if(x < otherHitbox.x + otherHitbox.width && x + width > otherHitbox.x && y < otherHitbox.y + otherHitbox.height && y + height > otherHitbox.y){
            return true;
        }
        else{
            return false;
        }
    }

    public void setLocation(double x, double y){
        this.x = x;
        this.y = y;
    }
}
