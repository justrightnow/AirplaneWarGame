package org.example.v1;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
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
    private boolean gameOver;
    private int score; // 新的实例变量来存储分数



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

        gameOver = false;
        score = 0; // 初始化分数为0
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

        if (gameOver) {
            Font gameOverFont = new Font("Times New Roman", Font.BOLD, 32);
            Graphics2D g2D = (Graphics2D) g;
            g2D.setFont(gameOverFont);
            g2D.drawString("Game Over", 300, 300);
        }

        // 在面板上显示分数
        Font scoreFont = new Font("Times New Roman", Font.PLAIN, 18);
        Graphics2D g2D2 = (Graphics2D) g;
        g2D2.setFont(scoreFont);
        g2D2.drawString("Score: " + score, 10, 20);
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

        // 检查玩家与敌机是否碰撞
        if (playerCollision()) {
            gameOver = true;

            timer.stop();
        }

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
                    // 增加得分
                    score++;
                    break;
                }
            }
        }
    }

    private void spawnEnemies() {
        if (random.nextInt(100) < 3) {
            // 3% chance to spawn an enemy each frame
            enemies.add(new EnemyPlane(random.nextInt(800), 0));
        }
    }

    private boolean playerCollision() {
        Rectangle playerRect = player.getBounds();
        for (EnemyPlane enemy : enemies) {
            Rectangle enemyRect = enemy.getBounds();
            if (playerRect.intersects(enemyRect)) {
                return true;
            }
        }
        return false;
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
    private Image image;

    public PlayerPlane(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            // 加载图片
            image = ImageIO.read(new File("/Users/weike/Pictures/飞机.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move() {
        x += dx;
        if (x < 0) {
            x = 0;
        }
        if (x > 750) {
            x = 750; // 假设玩家飞机的宽度是50
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
        // 绘制图片
        g.drawImage(image, x, y, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }
}

class EnemyPlane {
    private int x, y;
    private Image image;

    public EnemyPlane(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            // 加载图片
            image = ImageIO.read(new File("/Users/weike/Pictures/飞机敌.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move() {
        y += 2; // 向下移动
        if (y > 600) { // 如果超出屏幕，重置位置
            y = -50;
            x = new Random().nextInt(800);
        }
    }

    public void draw(Graphics g) {
        // 绘制图片
        g.drawImage(image, x, y, null);
    }

    public Rectangle getBounds() {
        // 返回图片的边界框
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }
}

class Bullet {
    private int x, y;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        y -= 4; // 向上移动
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 5, 20); // 简单的矩形表示
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 5, 10);
    }
}



