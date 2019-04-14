package src;

import javax.swing.*;
import src.gameobjects.*;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class GameWorld extends JPanel {

    public static final int SCREEN_WIDTH = 961, SCREEN_HEIGHT = 720;
    public static final int WORLD_WIDTH = 1920 ,WORLD_HEIGHT = 1440;
    private BufferedImage world, leftScreen, rightScreen;
    private Graphics2D buffer;
    private JFrame jf;
    private Tank t1, t2;
    private BufferedImage backgroundImg;
    private BufferedImage tankImg , lifeImg;
    private BufferedImage bulletImg , smallExplosionImg, largeExplosionImg;
    private BufferedImage wallImg, breakableWallImg;
    private boolean player1Won = false;
    private boolean player2Won = false;

    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    public void addGameObject(GameObject obj){
        this.gameObjects.add(obj);
    }

    public static void main(String[] args) {
        GameWorld game = new GameWorld();
        game.init();
        try {
            while (true) {
                game.gameUpdate();
                game.checkCollisions();
                game.repaint();
                if(game.player1Won || game.player2Won){
                    break;
                }
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {

        }
    }

    private void init(){
        this.jf = new JFrame("Tank Wars");
        this.world = new BufferedImage(GameWorld.WORLD_WIDTH, GameWorld.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);

        try {
            backgroundImg = ImageIO.read(getClass().getResource("/resources/Background.bmp"));
            tankImg = ImageIO.read(getClass().getResource("/resources/tank1.png"));
            lifeImg = ImageIO.read(getClass().getResource("/resources/life.gif"));
            bulletImg = ImageIO.read(getClass().getResource("/resources/Weapon.gif"));
            smallExplosionImg = ImageIO.read(getClass().getResource("/resources/Explosion_small.gif"));
            largeExplosionImg = ImageIO.read(getClass().getResource("/resources/Explosion_large.gif"));
            wallImg = ImageIO.read(getClass().getResource("/resources/Wall2.gif"));
            breakableWallImg = ImageIO.read(getClass().getResource("/resources/Wall1.gif"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        createMap();

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);

        this.jf.setSize(GameWorld.SCREEN_WIDTH + 9, GameWorld.SCREEN_HEIGHT + 38);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);
    }

    // Reads Map.txt file to populate game world with objects
    private void createMap() {
        try{
            InputStream in = getClass().getResourceAsStream("/resources/Map.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/Map.txt")));
            String line;
            int j = 0; // Keeps track of rows

            while((line = br.readLine())!= null){ // Reads Map.txt file line by line
                String[] tokens = line.split(" ");
                for(int i = 0; i < tokens.length; i++){
                    switch(tokens[i]) {
                        case "1":
                            gameObjects.add(new Wall(i*32, j*32, wallImg,null, false));
                            break;
                        case "2":
                            gameObjects.add(new Wall(i*32,j*32, breakableWallImg,smallExplosionImg,true));
                            break;
                        case "3":
                            t1 = new Tank(i*32,j*32,0,0,0, tankImg, bulletImg, "Player1", this);
                            t1.setSmallExplosion(smallExplosionImg);
                            t1.setLargeExplosion(largeExplosionImg);
                            gameObjects.add(t1);    // Add the tank object to the gameObjects ArrayList
                            TankControls tc1 = new TankControls(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_F);
                            this.jf.addKeyListener(tc1);
                            break;
                        case "4":
                            t2 = new Tank(i*32,j*32,0,0,0, tankImg, bulletImg, "Player2", this);
                            t2.setSmallExplosion(smallExplosionImg);
                            t2.setLargeExplosion(largeExplosionImg);
                            gameObjects.add(t2);
                            TankControls tc2 = new TankControls(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);
                            this.jf.addKeyListener(tc2);
                            break;
                        case "5":
                            gameObjects.add(new PowerUp(i*32, j*32, lifeImg));
                            break;
                        default:
                            break;
                    }
                }
                j++;
            }

            br.close();

        } catch (IOException e) {
            System.out.println("File failed to open!");
        }
    }

    // Update all the game objects and remove dead objects from gameObjects ArrayList
    private void gameUpdate() {
        for(int i=0; i < gameObjects.size(); i++){
            if(gameObjects.get(i).isAlive()){
                gameObjects.get(i).update();
            }else{
                if(gameObjects.get(i) instanceof Tank) {
                    if (((Tank) gameObjects.get(i)).getTankName().equals("Player2")) {
                        player1Won = true;
                    } else {
                        player2Won = true;
                    }
                }
                gameObjects.remove(i);
                i--;
            }
            if(gameObjects.get(i) instanceof Wall){
                if (((Wall) gameObjects.get(i)).isBreakable() && !gameObjects.get(i).isAlive()) {
                    gameObjects.remove(i);
                    i--;
                }
            }
        }
    }

    // Check game object collisions
    private void checkCollisions(){
        for(int i=0; i < gameObjects.size(); i++) {
            for (int j = 0; j < gameObjects.size(); j++) {
                Collidable co1 = (Collidable) gameObjects.get(i);
                Collidable co2 = (Collidable) gameObjects.get(j);
                if (((GameObject) co1).getRec().getBounds().intersects(((GameObject) co2).getRec().getBounds())) {
                    co1.collision(co2);
                    co2.collision(co1);
                }
            }
        }
    }

    // Update x coordinate of split screen relative to the tank
    private int getScreenXCoord(Tank tank){
        int xCoord = tank.getX();
        if (xCoord < SCREEN_WIDTH / 4) {
            xCoord = SCREEN_WIDTH / 4;
        }
        if (xCoord > WORLD_WIDTH - SCREEN_WIDTH / 4) {
            xCoord = WORLD_WIDTH - SCREEN_WIDTH / 4;
        }
        return xCoord - SCREEN_WIDTH / 4;
    }

    // Update y coordinate of split screen relative to the tank
    private int getScreenYCoord(Tank tank){
        int yCoord = tank.getY();
        if (yCoord < SCREEN_HEIGHT / 2) {
            yCoord = SCREEN_HEIGHT / 2;
        }
        if (yCoord > WORLD_HEIGHT - SCREEN_HEIGHT / 2) {
            yCoord = WORLD_HEIGHT - SCREEN_HEIGHT / 2;
        }
        return yCoord - SCREEN_HEIGHT / 2;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        buffer = world.createGraphics();
        super.paintComponent(g2);

        // Refresh background tiles
        for(int i = 0; i < WORLD_WIDTH; i+= 320){
            for(int j = 0; j < WORLD_HEIGHT; j+= 240)
                buffer.drawImage( backgroundImg, i, j, 320, 240, this);
        }

        // Draw all objects
        for(int i=0; i<gameObjects.size(); i++){
            gameObjects.get(i).drawImage(buffer);
        }

        leftScreen = world.getSubimage(getScreenXCoord(t1),getScreenYCoord(t1), SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        rightScreen = world.getSubimage(getScreenXCoord(t2),getScreenYCoord(t2), SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        g2.drawImage(leftScreen, 0, 0, null);
        g2.drawImage(rightScreen, SCREEN_WIDTH / 2 + 1, 0, null); // Right screen with division line

        // Draw mini map
        g2.drawImage(world, SCREEN_WIDTH / 2 - WORLD_WIDTH / 8 / 2, SCREEN_HEIGHT - WORLD_HEIGHT / 8, WORLD_WIDTH / 8, WORLD_HEIGHT / 8, null);

        // Draw lives
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("Player1: " ,10, 28);
        g2.drawString("Player2: " ,SCREEN_WIDTH / 2 + 10, 28);

        int x = 110;
        for(int i = 0; i < t1.getLives(); i++){
            g2.drawImage(lifeImg, x, 10, null);
            x += lifeImg.getWidth();
        }

        int x1 = SCREEN_WIDTH / 2 + 110;
        for(int i = 0; i < t2.getLives(); i++){
            g2.drawImage(lifeImg, x1, 10, null);
            x1 += lifeImg.getWidth();
        }

        // Display winner
        if(player1Won || player2Won){
            g2.setFont(new Font("SansSerif", Font.BOLD, 72));
            g2.setColor(Color.WHITE);
            g2.drawString("GAME OVER ", SCREEN_WIDTH / 4 + 50, SCREEN_HEIGHT / 4);
            if(player1Won) {
                g2.drawString("PLAYER1 WINS! ", SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2);
            }
            if(player2Won) {
                g2.drawString("PLAYER2 WINS! ", SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2);
            }
        }

    }
}

