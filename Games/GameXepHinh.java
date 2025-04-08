package Games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;



public class GameXepHinh extends JPanel implements ActionListener, KeyListener {

    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;
    private Timer timer;
    private boolean isRunning;
    private boolean[][] board;
    private Vector<Squar> v;
    private Squar tt; // Tâm khối
    private int type; // Loại khối
    private int score; // Điểm số hiện tại
    private int speed; // Tốc độ trò chơi


    public GameXepHinh() {
        setPreferredSize(new Dimension(300, 600));
        setBackground(Color.BLACK);
        board = new boolean[BOARD_HEIGHT][BOARD_WIDTH];
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board[i][j] = true;
            }
        }

        speed = 450; // Bắt đầu trò chơi với tốc độ ban đầu (500ms).
        timer = new Timer(speed, this); // Khoảng thời gian delay ban đầu.
        isRunning = true;
        score = 0;
        type = 1;
        tt = new Squar(0, BOARD_WIDTH / 2); // Đặt khối tại vị trí khởi đầu.
        v = initV();

        addKeyListener(this);
        setFocusable(true);
        timer.start();

    }

    private void mergeToBoard() {
        for (Squar sq : v) {
            board[sq.getX()][sq.getY()] = false;
        }
        clearLines();
    }

    private void clearLines() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            boolean fullLine = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j]) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                score +=100;
                if (score % 300 == 0) {
                    // Tăng tốc độ trò chơi khi điểm tăng.
                    speed = Math.max(100, speed - 25); // Giảm thời gian delay nhưng không giảm nhỏ hơn 100ms.
                    timer.setDelay(speed); // Cập nhật thời gian delay cho Timer.
                }
                removeLine(i);
            }
        }
    }

    private void removeLine(int row) {
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board[i][j] = board[i - 1][j];
            }
        }
        for (int j = 0; j < BOARD_WIDTH; j++) {
            board[0][j] = true;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ khối hiện tại
        g.setColor(getColorByType(type));
        for (Squar sq : v) {
            g.fillRect(sq.getY() * 30, sq.getX() * 30, 30, 30);
        }

        // Vẽ các khối đã cố định trên bảng
        g.setColor(Color.RED);
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (!board[i][j]) {
                    g.fillRect(j * 30, i * 30, 30, 30);
                }
            }
        }

        // Hiển thị điểm số
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Speed: " + speed + "ms", 10, 40); // Hiển thị tốc độ hiện tại.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            down();
            if (!check(board)) {
                up();
                mergeToBoard();
                resetBlock();
                if (!check(board)) {
                    isRunning = false;
                    JOptionPane.showMessageDialog(this, "Game Over!\nScore: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            repaint();
        }
    }

    private void down() {
        for (int i = 0; i < v.size(); i++) {
            v.set(i, new Squar(v.get(i).getX() + 1, v.get(i).getY()));
        }
        tt = new Squar(tt.getX() + 1, tt.getY());
    }

    private void up() {
        for (int i = 0; i < v.size(); i++) {
            v.set(i, new Squar(v.get(i).getX() - 1, v.get(i).getY()));
        }
        tt = new Squar(tt.getX() - 1, tt.getY());
    }

    private void left() {
        for (int i = 0; i < v.size(); i++) {
            v.set(i, new Squar(v.get(i).getX(), v.get(i).getY() - 1));
        }
        tt = new Squar(tt.getX(), tt.getY() - 1);
    }

    private void right() {
        for (int i = 0; i < v.size(); i++) {
            v.set(i, new Squar(v.get(i).getX(), v.get(i).getY() + 1));
        }
        tt = new Squar(tt.getX(), tt.getY() + 1);
    }

    private boolean check(boolean[][] board) {
        for (Squar sq : v) {
            int x = sq.getX();
            int y = sq.getY();
            if (x < 0 || x >= BOARD_HEIGHT || y < 0 || y >= BOARD_WIDTH || !board[x][y]) {
                return false;
            }
        }
        return true;
    }

    private void resetBlock() {
        tt = new Squar(0, BOARD_WIDTH / 2);
        type = (int) (Math.random() * 7) + 1;
        v = initV();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (isRunning) {
            switch (key) {
                case KeyEvent.VK_A: // Di chuyển trái
                    left();
                    if (!check(board)) right();
                    repaint();
                    break;
                case KeyEvent.VK_D: // Di chuyển phải
                    right();
                    if (!check(board)) left();
                    repaint();
                    break;
                case KeyEvent.VK_S: // Di chuyển xuống
                    down();
                    if (!check(board)) {
                        up();
                        mergeToBoard();
                        resetBlock();
                        if (!check(board)) {
                            isRunning = false;
                            JOptionPane.showMessageDialog(this, "Game Over!\nScore: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    repaint();
                    break;
                case KeyEvent.VK_SPACE: // Xoay khối
                    rotate();
                    if (!check(board)) rotateBack();
                    repaint();
                    break;
            }
        }else if (key == KeyEvent.VK_R) { // Nhấn "R" để chơi lại
            resetGame();
            repaint();
        }
    }

    private void resetGame() {
        // Xóa bảng game
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board[i][j] = true;
            }
        }
        // Reset trạng thái game
        score = 0;
        speed = 450;
        type = 1;
        tt = new Squar(0, BOARD_WIDTH / 2);
        v = initV();

        // Chạy lại Timer
        isRunning = true;
        timer.setDelay(speed);
        timer.start();
    }


    private void rotate() {
        Vector<Squar> newV = new Vector<>();
        for (Squar sq : v) {
            int x = tt.getX() + sq.getY() - tt.getY();
            int y = tt.getY() - sq.getX() + tt.getX();
            newV.add(new Squar(x, y));
        }
        v = newV;
    }

    private void rotateBack() {
        Vector<Squar> newV = new Vector<>();
        for (Squar sq : v) {
            int x = tt.getX() - sq.getY() + tt.getY();
            int y = tt.getY() + sq.getX() - tt.getX();
            newV.add(new Squar(x, y));
        }
        v = newV;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Xếp Hình");
        GameXepHinh gamePanel = new GameXepHinh();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private Vector<Squar> initV() {
        Vector<Squar> result = new Vector<>();
        switch (type) {
            case 1: // Khối hình T
                result.add(new Squar(tt.getX(), tt.getY()));
                result.add(new Squar(tt.getX(), tt.getY() - 1));
                result.add(new Squar(tt.getX(), tt.getY() + 1));
                result.add(new Squar(tt.getX() + 1, tt.getY()));
                break;
            case 2: // Khối hình L
                result.add(new Squar(tt.getX(), tt.getY()));
                result.add(new Squar(tt.getX() + 1, tt.getY()));
                result.add(new Squar(tt.getX() + 2, tt.getY()));
                result.add(new Squar(tt.getX() + 2, tt.getY() + 1));
                break;
            case 3: // Khối hình I
                result.add(new Squar(tt.getX(), tt.getY()));
                result.add(new Squar(tt.getX() + 1, tt.getY()));
                result.add(new Squar(tt.getX() + 2, tt.getY()));
                result.add(new Squar(tt.getX() + 3, tt.getY()));
                break;
            case 4: // Khối hình U
                result.add(new Squar(tt.getX(), tt.getY() - 1));
                result.add(new Squar(tt.getX(), tt.getY()));
                result.add(new Squar(tt.getX(), tt.getY() + 1));
                result.add(new Squar(tt.getX() + 1, tt.getY() - 1));
                result.add(new Squar(tt.getX() + 1, tt.getY() + 1));
                break;
            case 5: // Khối hình O
                result.add(new Squar(tt.getX(), tt.getY()));
                result.add(new Squar(tt.getX(), tt.getY() + 1));
                result.add(new Squar(tt.getX() + 1, tt.getY()));
                result.add(new Squar(tt.getX() + 1, tt.getY() + 1));
                break;
            case 6: // Khối chữ T
                result.add(new Squar(tt.getX(), tt.getY()));
                result.add(new Squar(tt.getX(), tt.getY() - 1));
                result.add(new Squar(tt.getX() + 1, tt.getY()));
                result.add(new Squar(tt.getX(), tt.getY() + 1));
                break;
            case 7: // Khối chữ J
                result.add(new Squar(tt.getX(), tt.getY() - 1));
                result.add(new Squar(tt.getX() + 1, tt.getY() - 1));
                result.add(new Squar(tt.getX() + 1, tt.getY()));
                result.add(new Squar(tt.getX() + 1, tt.getY() + 1));
                break;
        }
        return result;
    }

    private Color getColorByType(int type) {
        switch (type) {
            case 1:
                return Color.cyan;
            case 2:
                return Color.MAGENTA;
            case 3:
                return Color.ORANGE;
            case 4:
                return Color.GREEN;
            case 5:
                return Color.YELLOW;
            case 6:
                return Color.GREEN.darker();
            case 7:
                return Color.PINK;
            default:
                return Color.WHITE;
        }
    }
}

class Squar {
    private final int x, y;

    public Squar(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}