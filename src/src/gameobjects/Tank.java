package src.gameobjects;

import src.Collidable;
import src.GameWorld;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Tank extends GameObject implements Collidable {

    private int vx, vy, angle, health = 10, lives = 3;
    private int initX, initY, prevX, prevY;
    private final int R = 2, ROTATIONSPEED = 2;
    private boolean UpPressed, DownPressed, RightPressed, LeftPressed, ShootPressed;
    private long firingTimer = System.currentTimeMillis();
    private BufferedImage bulletImg, smallExplosionImg, largeExplosionImg;
    private GameWorld gw;
    private String tankName;
    private int count = 0;

    public Tank(int x, int y, int vx, int vy, int angle, BufferedImage tankImg, BufferedImage bulletImg, String tankName, GameWorld gw) {
        super(x, y, tankImg);
        this.vx = vx;
        this.vy = vy;
        this.initX = x;
        this.initY = y;
        this.angle = angle;
        this.bulletImg = bulletImg;
        this.tankName = tankName;
        this.gw = gw;
    }

    private int getHealth() { return this.health;}

    public int getLives() { return this.lives;}

    public String getTankName() { return this.tankName;}

    public void setSmallExplosion(BufferedImage explosion){
        this.smallExplosionImg = explosion;
    }

    public void setLargeExplosion(BufferedImage explosion){
        this.largeExplosionImg = explosion;
    }

    public void toggleUpPressed() {
        this.UpPressed = true;
    }

    public void toggleDownPressed() {
        this.DownPressed = true;
    }

    public void toggleRightPressed() {
        this.RightPressed = true;
    }

    public void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    public void toggleShootPressed() { this.ShootPressed = true;}

    public void unToggleUpPressed() {
        this.UpPressed = false;
    }

    public void unToggleDownPressed() {
        this.DownPressed = false;
    }

    public void unToggleRightPressed() {
        this.RightPressed = false;
    }

    public void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    public void unToggleShootPressed() { this.ShootPressed = false;}

    @Override
    public void update() {
        this.prevX = x;
        this.prevY = y;

        if(this.UpPressed){
            this.moveForwards();
        }
        if(this.DownPressed){
            this.moveBackwards();
        }
        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.ShootPressed ){
            this.shoot();
        }
        this.getRec().setLocation(x,y);
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void shoot(){
        if(System.currentTimeMillis() - firingTimer > 1000) { // Limit firing to one bullet per second
            Bullet blt = new Bullet(x + 17, y + 17, angle, bulletImg, smallExplosionImg, tankName);
            gw.addGameObject(blt);
            firingTimer = System.currentTimeMillis();
        }
    }

    private void checkBorder() {
        if (x < 40) {
            x = 40;
        }
        if (x >= GameWorld.WORLD_WIDTH - 88) {
            x = GameWorld.WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameWorld.WORLD_HEIGHT - 88) {
            y = GameWorld.WORLD_HEIGHT - 88;
        }
    }

    private void depleteHealth() {
        if(health > 0){
            health--;
        }else{
            if(lives > 0 && health == 0){
                lives --;
                x = initX;
                y = initY;
                health = 10;
            }
            if (lives == 0){
                alive = false;
            }
        }
    }

    @Override
    public void collision(Collidable obj) {
        if(obj instanceof Wall || (obj instanceof Tank && !((Tank)obj).getTankName().equals(this.tankName))) {
           this.x = prevX;
           this.y = prevY;
        }
        if(obj instanceof Bullet && ((Bullet)obj).getOwner() != this.tankName){
            this.depleteHealth();
        }
        if (obj instanceof PowerUp && lives < 4) {
            count++; // keeps track of double tank and powerUp collision
            if (count == 2) {
                lives++;
                count = 0;
            }
        }
    }

    @Override
    public void drawImage(Graphics2D g2d) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        if(this.alive) {
            g2d.drawImage(this.img, rotation, null);
            g2d.setColor(Color.green);
            g2d.fillRect(x ,y + (int)getRec().getHeight() + 10, getHealth()*5, 10); // Draw health bar
        }else{
            g2d.drawImage(this.largeExplosionImg, x, y, null);
        }
    }

}
