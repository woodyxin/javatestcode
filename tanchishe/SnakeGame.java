import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int UNIT_SIZE = 20;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int DELAY = 100;

    private final LinkedList<Point> snake = new LinkedList<>();
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        placeFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(food.x * UNIT_SIZE, food.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);

            for (Point p : snake) {
                g.setColor(Color.green);
                g.fillRect(p.x * UNIT_SIZE, p.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.white);
            g.drawString("Score: " + (snake.size() - 1), 10, 10);
        } else {
            gameOver(g);
        }
    }

    public void placeFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(WIDTH / UNIT_SIZE), rand.nextInt(HEIGHT / UNIT_SIZE));
    }

    public void move() {
        Point head = new Point(snake.getFirst());
        switch (direction) {
            case 'U': head.y -= 1; break;
            case 'D': head.y += 1; break;
            case 'L': head.x -= 1; break;
            case 'R': head.x += 1; break;
        }

        // Check collisions
        if (head.x < 0 || head.x >= WIDTH / UNIT_SIZE || head.y < 0 || head.y >= HEIGHT / UNIT_SIZE || snake.contains(head)) {
            running = false;
            timer.stop();
        }

        snake.addFirst(head);

        if (head.equals(food)) {
            placeFood();
        } else {
            snake.removeLast();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Game Over", WIDTH / 2 - 90, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Snake Game");
        SnakeGame gamePanel = new SnakeGame();

        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
