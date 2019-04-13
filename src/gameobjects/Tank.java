package src.gameobjects;

import src.Collidable;
import src.GameWorld;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Tank extends GameObject implements Collidable {

    private int vx, vy, angle, health = 10, lives =3;
    private final int R = 2, ROTATIONSPEED = 2;
    private boolean UpPressed, DownPressed, RightPressed, LeftPressed, ShootPressed, alive;
    private BufferedImage bulletImg, smallExplosionImg, largeExplosionImg;
    private GameWorld gw;
    private String tankName;

    public Tank(int x, int y, int vx, int vy, int angle, BufferedImage tankImg, BufferedImage bulletImg, String tankName, GameWorld gw) {
        super(x, y, tankImg);
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.alive = true;
        this.bulletImg = bulletImg;
        this.tankName = tankName;
        this.gw = gw;
    }

    public int getHealth() { return this.health;}

    public int getLives() { return this.lives;}

    public String getTankName() { return this.tankName;}

    //public void setGameWorld(GameWorld gw) { this.gw = gw;}

    public void setSmallExplosion(BufferedImage explosion){
        this.smallExplosionImg = explosion;
    }

    public void setLargeExplosion(BufferedImage explosion){
        this.largeExplosionImg = explosion;
    }

    public boolean isAlive() {
        return this.alive;
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
        this.getRec().setLocation(x, y);

        if (this.UpPressed || this.DownPressed) {
            this.move();
        }
        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.ShootPressed){
            this.shoot();
        }
    }

    public void move(){
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        if(this.UpPressed){
            x += vx;
            y += vy;
        }
        if(this.DownPressed){
            x -= vx;
            y -= vy;
        }
        checkBorder();
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void shoot(){
        Bullet blt = new Bullet(x + 17, y + 17, angle, bulletImg, tankName);
        gw.addGameObject(blt);
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

    public void addHealth(int val) {
        if(health + val < 100){
            health += val;
        }
    }
    private void depleteHealth(int val) {
        if(health - val > 0){
            health -= val;
        }else{
            health = 0;
            alive = false;
            if(lives > 0){
                lives --;
            }
        }
    }

    @Override
    public void collision(Collidable obj) {
        /*if(obj instanceof Wall) {
            if(this.UpPressed){
                x -= x;
                y -= y;
            }
            if(this.DownPressed){
                x += vx;
                y += vy;
            }
        }
        if(obj instanceof Bullet ){
            this.depleteHealth(1);
        }*/
    }

    @Override
    public void drawImage(Graphics2D g2d) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        if(this.alive) {
            g2d.drawImage(this.img, rotation, null);

            // Draw health bar
            g2d.setColor(Color.green);
            g2d.fillRect( this.x , this.y + (int)getRec().getHeight() + 10, getHealth()*5, 10);
        }
    }

}
