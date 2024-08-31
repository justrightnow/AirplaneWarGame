//package org.example;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PlaneWarGame extends JFrame {
//
//    private Player player;
//    private List<Enemy> enemies;
//    private List<Bullet> bullets;
//    private boolean gameOver;
//
//    public PlaneWarGame() {
//        initGame();
//        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                handleKeyPress(e);
//            }
//        });
//        setFocusable(true);
//        setTitle("飞机大战");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(800, 600);
//        setLocationRelativeTo(null);
//        setVisible(true);
//
//        new Thread(() -> {
//            while (!gameOver) {
//                updateGame();
//                repaint();
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void initGame() {
//        player = new Player(350, 550, 50, 50);
//        enemies = new ArrayList<>();
//        bullets = new ArrayList<>();
//        gameOver = false;
//        spawnEnemy();
//    }
//
//    private void handleKeyPress(KeyEvent e) {
//        int keyCode = e.getKeyCode();
//        if (keyCode == KeyEvent.VK_LEFT) {
//            player.moveLeft();
//        } else if (keyCode == KeyEvent.VK_RIGHT) {
//            player.moveRight();
//        } else if (keyCode == KeyEvent.VK_SPACE) {
//            bullets.add(player.shoot());
//        }
//    }
//
//    private void updateGame() {
//        player.update();
//        updateBullets();
//        updateEnemies();
//        checkCollisions();
//    }
//
//    private void updateBullets() {
//        for (Bullet bullet : bullets) {
//            bullet.move();
//        }
//        bullets.removeIf(bullet -> bullet.getY() < 0);
//    }
//
//    private void updateEnemies() {
//        if (Math.random() < 0.01) {
//            spawnEnemy();
//        }
//        for (Enemy enemy : enemies) {
//            enemy.move();
//        }
//        enemies.removeIf(enemy -> enemy.get > getHeight());
//    }
//
//    private void spawnEnemy() {
//        int x = (int) (Math.random() * (getWidth() - 50));
//        int y = -50;
//        enemies.add(new Enemy(x, y, 50, 50));
//    }
//
//    private void checkCollisions() {
//        for (Enemy enemy : enemies) {
//            if (player.isCollidingWith(enemy)) {
//                gameOver = true;
//                return;
//            }
//        }
//        for (Bullet bullet : bullets) {
//            for (Enemy enemy : enemies) {
//                if (bullet.isCollidingWith(enemy)) {
//                    bullets.remove(bullet);
//                    enemies.remove(enemy);
//                    break;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void paint(Graphics g) {
//        super.paint(g);
//        if (!gameOver) {
//            player.draw(g);
//            for (Enemy enemy : enemies) {
//                enemy.draw(g);
//            }
//            for (Bullet bullet : bullets) {
//                bullet.draw(g);
//            }
//        } else {
//            gameOver(g);
//        }
//    }
//
//    private void gameOver(Graphics g) {
//        g.setColor(Color.RED);
//        g.setFont(new Font("Arial", Font.BOLD, 30));
//        g.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new PlaneWarGame());
//    }
//}
//
//class Player {
//    private int x;
//    private int y;
//    private int width;
//    private int height;
//
//    public Player(int x, int y, int width, int height) {
//        this.x = x;
//        this.y = y;
//        this.width = width;
//        this.height = height;
//    }
//
//    public void moveLeft() {
//        x -= 5;
//        if (x < 0) {
//            x = 0;
//        }
//    }
//
//    public void moveRight() {
//        x += 5;
//        if (x > this.width - width) {
//            x = this.width - width;
//        }
//    }
//
//    public Bullet shoot() {
//        return new Bullet(x + width / 2, y, 5, 10);
//    }
//
//    public void update() {
//        // 可以添加额外的更新逻辑，例如限制移动速度等
//    }
//
//    public void draw(Graphics g) {
//        g.setColor(Color.BLUE);
//        g.fillRect(x, y, width, height);
//    }
//
//    public boolean isCollidingWith(Enemy enemy) {
//        return x < enemy.x + enemy.width && x + width > enemy.x && y < enemy.y + enemy.height && y + height > enemy.y;
//    }
//}
//
//class Enemy {
//    int x;
//    int y;
//    int width;
//    int height;
//
//    public Enemy(int x, int y, int width, int height) {
//        this.x = x;
//        this.y = y;
//        this.width = width;
//        this.height = height;
//    }
//
//    public void move() {
//        y += 2;
//    }
//
//    public void draw(Graphics g) {
//        g.setColor(Color.RED);
//        g.fillRect(x, y, width, height);
//    }
//
//    public boolean isCollidingWith(Bullet bullet) {
//        return x < bullet.getX() + bullet.getWidth() && x + width > bullet.getX() && y < bullet.getY() + bullet.getHeight() && y + height > bullet.getY();
//    }
//
//    public boolean getY() {
//        return false;
//    }
//}
//
//class Bullet {
//     int x;
//     int y;
//     int width;
//     int height;
//
//    public Bullet(int x, int y, int width, int height) {
//        this.x = x;
//        this.y = y;
//        this.width = width;
//        this.height = height;
//    }
//
//    public void move() {
//        y -= 10;
//    }
//
//    public void draw(Graphics g) {
//        g.setColor(Color.YELLOW);
//        g.fillRect(x, y, width, height);
//    }
//
//    public int getX() {
//        return x;
//    }
//
//    public int getY() {
//        return y;
//    }
//
//    public int getWidth() {
//        return width;
//    }
//
//    public int getHeight() {
//        return height;
//    }
//
//    public boolean isCollidingWith(Enemy enemy) {
//        return x < enemy.x + enemy.width && x + width > enemy.x && y < enemy.y + enemy.height && y + height > enemy.y;
//    }
//}
