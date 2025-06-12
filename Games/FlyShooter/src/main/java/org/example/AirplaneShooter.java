package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class AirplaneShooter extends JPanel implements ActionListener, KeyListener {
    Timer timer = new Timer(16, this);
    ImageIcon playerImg = new ImageIcon(getClass().getResource("/player.png"));
    ImageIcon bulletImg = new ImageIcon(getClass().getResource("/bullet.png"));
    ImageIcon enemyImg = new ImageIcon(getClass().getResource("/enemy.png"));
    ImageIcon bossImg = new ImageIcon(getClass().getResource("/boss.png"));
    ImageIcon enemyBulletImg = new ImageIcon(getClass().getResource("/enemy_bullet.png"));
    Image background = new ImageIcon(getClass().getResource("/background.png")).getImage();

    Rectangle player = new Rectangle(375 + 20, 500 + 10, 50, 70); // Tối ưu hitbox
    ArrayList<Rectangle> bullets = new ArrayList<>();
    ArrayList<Rectangle> enemies = new ArrayList<>();
    ArrayList<EnemyBullet> enemyBullets = new ArrayList<>();
    Rectangle boss = null;

    int score = 0, lives = 3;
    boolean left, right, shooting, gameOver = false, win = false;
    boolean bossAppeared = false;
    int shootCooldown = 0;
    int bossHealth = 30;
    int bossShootTimer = 0;
    int backgroundY = 0;
    Random rand = new Random();

    public AirplaneShooter() {
        setPreferredSize(new Dimension(700, 600));
        setFocusable(true);
        addKeyListener(this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver || win) return;

        backgroundY += 2;
        if (backgroundY >= getHeight()) backgroundY = 0;

        if (left) player.x = Math.max(0, player.x - 5);
        if (right) player.x = Math.min(getWidth() - player.width, player.x + 5);

        if (shooting && shootCooldown == 0) {
            bullets.add(new Rectangle(player.x + 5, player.y, 10, 20));  // Tối ưu hitbox
            bullets.add(new Rectangle(player.x + 35, player.y, 10, 20));
            shootCooldown = 15;
        }
        if (shootCooldown > 0) shootCooldown--;

        Iterator<Rectangle> bi = bullets.iterator();
        while (bi.hasNext()) {
            Rectangle b = bi.next();
            b.y -= 12;
            if (b.y < 0) bi.remove();
        }

        if (!bossAppeared && Math.random() < 0.02) {
            enemies.add(new Rectangle(rand.nextInt(getWidth() - 70), 0, 70, 80)); // Tối ưu hitbox
        }

        Iterator<Rectangle> ei = enemies.iterator();
        while (ei.hasNext()) {
            Rectangle enemy = ei.next();
            enemy.y += 4;

            if (rand.nextDouble() < 0.01) {
                enemyBullets.add(new EnemyBullet(enemy.x + 25, enemy.y + 40, 0, 5));
            }

            if (enemy.y > getHeight()) ei.remove();

            if (enemy.intersects(player)) {
                lives--;
                ei.remove();
                if (lives <= 0) gameOver = true;
            }

            for (Rectangle b : bullets) {
                if (b.intersects(enemy)) {
                    ei.remove();
                    bullets.remove(b);
                    score++;
                    break;
                }
            }
        }

        if (score >= 20 && !bossAppeared) {
            boss = new Rectangle(300 + 30, 0 + 30, 140, 140); // Tối ưu hitbox
            bossAppeared = true;
        }

        if (boss != null) {
            boss.y = Math.min(boss.y + 1, 50);
            bossShootTimer++;
            if (bossShootTimer % 60 == 0) {
                enemyBullets.add(new EnemyBullet(boss.x + 60, boss.y + 80, 0, 6));
                enemyBullets.add(new EnemyBullet(boss.x + 75, boss.y + 80, -2, 6));
                enemyBullets.add(new EnemyBullet(boss.x + 90, boss.y + 80, 2, 6));
            }
            if (bossShootTimer % 240 == 0) {
                bossShootCircle(boss);
            }

            if (boss.intersects(player)) {
                lives--;
                if (lives <= 0) gameOver = true;
            }

            for (Rectangle b : bullets) {
                if (b.intersects(boss)) {
                    bullets.remove(b);
                    bossHealth--;
                    if (bossHealth <= 0) {
                        boss = null;
                        win = true;
                    }
                    break;
                }
            }
        }

        Iterator<EnemyBullet> ebi = enemyBullets.iterator();
        while (ebi.hasNext()) {
            EnemyBullet eb = ebi.next();
            eb.move();

            if (eb.intersects(player)) {
                lives--;
                ebi.remove();
                if (lives <= 0) gameOver = true;
            } else if (eb.y > getHeight() || eb.x < 0 || eb.x > getWidth()) {
                ebi.remove();
            }
        }

        repaint();
    }

    public void bossShootCircle(Rectangle boss) {
        int centerX = boss.x + boss.width / 2;
        int centerY = boss.y + boss.height / 2;
        int numBullets = 16;
        double angleStep = 2 * Math.PI / numBullets;

        for (int i = 0; i < numBullets; i++) {
            double angle = i * angleStep;
            int vx = (int)(Math.cos(angle) * 4);
            int vy = (int)(Math.sin(angle) * 4);
            enemyBullets.add(new EnemyBullet(centerX, centerY, vx, vy));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, backgroundY - getHeight(), getWidth(), getHeight(), null);
        g.drawImage(background, 0, backgroundY, getWidth(), getHeight(), null);

        g.drawImage(playerImg.getImage(), player.x - 20, player.y - 10, 90, 90, this); // Offset lại cho đúng vị trí hình

        for (Rectangle b : bullets)
            g.drawImage(bulletImg.getImage(), b.x, b.y, b.width, b.height, this);

        for (Rectangle e : enemies)
            g.drawImage(enemyImg.getImage(), e.x - 15, e.y - 10, 100, 100, this); // Offset lại hình enemy

        for (EnemyBullet eb : enemyBullets)
            g.drawImage(enemyBulletImg.getImage(), eb.x, eb.y, eb.width, eb.height, this);

        if (boss != null)
            g.drawImage(bossImg.getImage(), boss.x - 30, boss.y - 30, 200, 200, this); // Offset lại boss

        g.setColor(Color.ORANGE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + score + "   Lives: " + lives, 10, 20);

        if (gameOver || win) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.RED);
            g.drawString(gameOver ? "Game Over" : "YOU WIN!", 280, 280);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Nhấn Enter để chơi lại", 300, 320);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) shooting = true;
        if (e.getKeyCode() == KeyEvent.VK_ENTER && (gameOver || win)) restartGame();
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) left = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) right = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) shooting = false;
    }

    public void keyTyped(KeyEvent e) {}

    public void restartGame() {
        score = 0;
        lives = 3;
        boss = null;
        bossHealth = 30;
        gameOver = false;
        win = false;
        bossAppeared = false;
        bullets.clear();
        enemies.clear();
        enemyBullets.clear();
        player = new Rectangle(375 + 20, 500 + 10, 50, 70);
        shootCooldown = 0;
        bossShootTimer = 0;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("\uD83D\uDD25 Airplane Shooter Pro");
        AirplaneShooter game = new AirplaneShooter();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class EnemyBullet extends Rectangle {
    int vx, vy;

    public EnemyBullet(int x, int y, int vx, int vy) {
        super(x, y, 10, 18); // Tối ưu hitbox
        this.vx = vx;
        this.vy = vy;
    }

    public void move() {
        this.x += vx;
        this.y += vy;
    }
}
