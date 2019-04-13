package src.gameobjects;

import src.Collidable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import src.GameWorld;

public class Bullet extends GameObject implements Collidable {

    private int vx, vy, angle;
    private final int speed = 2;
    private boolean alive;
    private String owner;

    public Bullet(int x, int y, int angle, BufferedImage bulletImg, String owner){
        super(x, y, bulletImg);
        this.vx = (int) Math.round(2*Math.cos(Math.toRadians(angle)))*speed;
        this.vy = (int) Math.round(2*Math.sin(Math.toRadians(angle)))*speed;
        this.angle = angle;
        this.owner = owner;
        this.alive = true;
    }

    public String getOwner(){
        return this.owner;
    }

    public boolean isAlive() { return this.alive;}

    @Override
    public void update() {
        if(alive){
            x += vx;
            y += vy;
            checkBorder();
        }
        this.getRec().setLocation(x,y);
    }

    private void checkBorder(){
        alive = (x > 30 && x < GameWorld.WORLD_WIDTH - 46 && y > 32 && y < GameWorld.WORLD_HEIGHT - 46);
    }

    @Override
    public void collision(Collidable obj) {
        /*if(obj instanceof Tank && ((Tank)obj).getTankName() != this.getOwner()){
            alive = false;
        }*/
    }

    @Override
    public void drawImage(Graphics2D g2d) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        if(alive){
            g2d.drawImage(this.img, rotation, null);
        }
    }

}
