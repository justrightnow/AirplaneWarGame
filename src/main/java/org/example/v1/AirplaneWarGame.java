package org.example.v1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AirplaneWarGame extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private PlayerPlane player;
    private List<EnemyPlane> enemies;
    private List<Bullet> bullets;
    private Random random;

    public AirplaneWarGame() {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        addKeyListener(this);
        player = new PlayerPlane(400, 500);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        random = new Random();
        timer = new Timer(15, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
        for (EnemyPlane enemy : enemies) {
            enemy.draw(g);
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.move();
        for (Bullet bullet : bullets) {
            bullet.move();
        }
        for (EnemyPlane enemy : enemies) {
            enemy.move();
        }
        checkCollisions();
        spawnEnemies();
        repaint();
    }

    private void checkCollisions() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Rectangle bulletRect = bullet.getBounds();
            Iterator<EnemyPlane> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                EnemyPlane enemy = enemyIterator.next();
                Rectangle enemyRect = enemy.getBounds();
                if (bulletRect.intersects(enemyRect)) {
                    bulletIterator.remove();
                    enemyIterator.remove();
                    break;
                }
            }
        }
    }

    private void spawnEnemies() {
        if (random.nextInt(100) < 3) { // 3% chance to spawn an enemy each frame
            enemies.add(new EnemyPlane(random.nextInt(800), 0));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            player.setDx(-2);
        } else if (key == KeyEvent.VK_RIGHT) {
            player.setDx(2);
        } else if (key == KeyEvent.VK_SPACE) {
            bullets.add(new Bullet(player.getX() + 22, player.getY()));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            player.setDx(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Airplane War Game");
        AirplaneWarGame game = new AirplaneWarGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class PlayerPlane {
    private int x, y, dx;
    private Image playerImage;

    public PlayerPlane(int x, int y) {
        this.x = x;
        this.y = y;
        playerImage = new ImageIcon(getClass().getResource("player_plane.png")).getImage();
    }

    public void move() {
        x += dx;
        if (x < 0) {
            x = 0;
        }
        if (x > 750) {
            x = 750; // Assuming the player plane's width is 50
        }
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g) {
        g.drawImage(playerImage, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50);
    }
}

class EnemyPlane {
    private int x, y;
    private Image enemyImage;

    public EnemyPlane(int x, int y) {
        this.x = x;
        this.y = y;
        enemyImage = new ImageIcon(getClass().getResource("enemy_plane.png")).getImage();
    }

    public void move() {
        y += 2; // Move downwards
        if (y > 600) { // Remove if out of screen
            y = -50;
            x = new Random().nextInt(800);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(enemyImage, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50);
    }
}

class Bullet {
    private int x, y;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        y -= 4; // Move upwards
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 5, 10); // Simple rectangle representation
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 5, 10);
    }
}
