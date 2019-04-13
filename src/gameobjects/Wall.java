package src.gameobjects;

import src.Collidable;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject implements Collidable {

    private final boolean breakable;
    private int wallHealth = 2;
    private boolean alive;
    BufferedImage explosionImg;

    public Wall(int x, int y, BufferedImage wallImg, BufferedImage explosionImg, boolean breakable){
        super(x, y, wallImg);
        this.explosionImg = explosionImg;
        this.breakable = breakable;
        if(breakable){
            this.alive = true;
        }
    }

    public boolean isBreakable(){
        return this.breakable;
    }

    public boolean isAlive(){
        return this.alive;
    }

    public void depleteHealth(int val) {
        if(wallHealth - val > 0){
            wallHealth -= val;
        }else{
            wallHealth = 0;
            this.alive = false;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void collision(Collidable obj) {
        /*if(obj instanceof Bullet){
           if(this.breakable && this.alive) {
                   depleteHealth(1);

               }
           }*/
    }

    @Override
    public void drawImage(Graphics2D g2d) {
        if ((breakable && alive) || !breakable){
            g2d.drawImage(this.img, x, y, null);
        }
        if(breakable && !alive){
            g2d.drawImage(this.explosionImg, x, y, null);
        }
    }

}
