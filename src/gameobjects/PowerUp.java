package src.gameobjects;

import src.Collidable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUp extends GameObject implements Collidable{

    private boolean visible;

    public PowerUp(int x, int y, BufferedImage img){
        super(x, y, img);
        this.visible = true;
    }

    @Override
    public void update() {

    }

    @Override
    public void collision(Collidable obj) {
        if(obj instanceof Tank){
            visible = false;
        }
    }

    @Override
    public void drawImage(Graphics2D g2d) {
        if (visible){
            g2d.drawImage(this.img, x, y, null);
        }
    }
}
