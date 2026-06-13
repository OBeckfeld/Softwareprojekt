package entities;
import entities.components.MovementComponent;
import entities.managers.AttackManager;
import entities.managers.EntityRegistry;
import tools.TileManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Set;


public class ViewBox extends Entity {
    public ViewBox(double x, double y, int height, int width, EntityRegistry registry, TileManager tileManager) {
        super(x,y,height,width,registry, tileManager);
    }
}