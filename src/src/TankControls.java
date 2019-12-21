package src;

import src.gameobjects.Tank;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class TankControls implements KeyListener {

    private final Tank player;
    private final int up, down, right, left, shoot;

    TankControls(Tank tank, int up, int down, int left, int right, int shoot){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.shoot = shoot;
        this.player = tank;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        int pressedKey = e.getKeyCode();

        if(pressedKey == up){
            this.player.toggleUpPressed();
        }
        if(pressedKey == down){
            this.player.toggleDownPressed();
        }
        if(pressedKey == left){
            this.player.toggleLeftPressed();
        }
        if(pressedKey == right){
            this.player.toggleRightPressed();
        }
        if(pressedKey == shoot){
            this.player.toggleShootPressed();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int releasedKey = e.getKeyCode();

        if(releasedKey == up){
            this.player.unToggleUpPressed();
        }
        if(releasedKey == down){
            this.player.unToggleDownPressed();
        }
        if(releasedKey == left){
            this.player.unToggleLeftPressed();
        }
        if(releasedKey == right){
            this.player.unToggleRightPressed();
        }
        if(releasedKey == shoot){
            this.player.unToggleShootPressed();
        }
    }
}
