package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;




public class SnakeGame extends JFrame implements ActionListener, KeyListener {


    List<Point> snake = new ArrayList<Point>();  //Kreieren einer Liste für einzelne Snake Körperteile
    final static int width = 500, height = 500, boxSize = 5;   // Einstellung für Fensterrahmen
    static int direction = 0;

    final static int LEFT = 1, RIGHT = 2, TOP = 3, BOTTOM = 4;


    Timer timer;
    Point apple = new Point();


    public SnakeGame() {

        addKeyListener(this);
        setResizable(false);
        timer = new Timer(50, this);
        timer.start();
        Point pointHead = new Point((width / boxSize) / 2, (height / boxSize) / 2);
        snake.add(pointHead);
        getNewAppleLocation();
    }


    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g.fillRect(0, 0, 500, 500);
        logic();
        drawSnake(g2D);
        drawFood(g2D);
    }


    private void drawSnake(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        for (Point pt : snake) {
            g2d.fillOval(pt.x * boxSize, pt.y * boxSize, boxSize, boxSize);  //Farbausfüllung jeden Punktes
        }

    }


    private void drawFood(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fillOval(apple.x * boxSize, apple.y * boxSize, boxSize, boxSize); // Selbes Prinzip; Nur für Apfel
    }

    private void logic() {

        if (apple.x == snake.get(0).x && apple.y == snake.get(0).y) {
            getNewAppleLocation();
            Point lastPoint = snake.get(snake.size() - 1);
            snake.add(new Point(lastPoint.x, lastPoint.y));

        }
        moveSnake();
    }


    private void moveSnake() {
        Point pt = new Point(snake.get(0).x, snake.get(0).y);
        int xMovement;
        int yMovement;

        switch (direction) {
            case LEFT -> {
                xMovement = -1;
                yMovement = 0;
            }
            case RIGHT -> {
                xMovement = 1;
                yMovement = 0;
            }
            case TOP -> {
                xMovement = 0;
                yMovement = -1;
            }
            case BOTTOM -> {
                xMovement = 0;
                yMovement = 1;
            }

            default -> xMovement = yMovement = 0;
        }
        pt.setLocation(pt.x + xMovement, pt.y + yMovement); // wo sollte der neue kopf sein
        snake.add(0, pt); // hinzufügen des neuen Kopfes
        snake.remove(snake.size() - 1); // Darauf folgendes Löschen des Schwanzes zum Größenausgleich

        handleCollision();

    }

    /**
     * * regelt das Kollidieren
     */
    private void handleCollision() {

        double x = snake.get(0).x;
        double y = snake.get(0).y;

        if (x > width / boxSize && direction == RIGHT) { //Überschreitung der Fensterrahmen führt zum Ausschied
            System.exit(0);
        }
        if (x < 0 && direction == LEFT) {
            System.exit(0);
        }
        if (y > height / boxSize && direction == BOTTOM) {
            System.exit(0);
        }
        if (y < 0 && direction == TOP) {
            System.exit(0);
        }

        for (int i = 1; i < snake.size(); i++) { // Körperteilkoordinate
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) { // Wenn Körperteil Position = Kopf Position -> Spiel beenden
                System.exit(0);
            }
        }
    }


    private void getNewAppleLocation() {
        Random rand = new Random();
        int delta = boxSize * 2;
        apple.setLocation(rand.nextInt(width / boxSize - 2 * delta) + delta, rand.nextInt(height / boxSize - 2 * delta) + delta);
        /**
         * Formel für zufällige Koordinatenbestimmung innerhalb des Fensters
         */
    }

    public static void main(String[] args) {
        SnakeGame frame = new SnakeGame();
        frame.setSize(width, height);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {     // Welche Taste wird gedrückt
            case KeyEvent.VK_UP:
                if (direction != 4) {   //Wenn nicht 4 = Unten;  geh nach oben
                    direction = 3;
                }
                /**
                 * Selbes Prinzip fortlaufend
                 */
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 3) {
                    direction = 4;
                }
                ;
                break;
            case KeyEvent.VK_LEFT:
                direction = (direction != 2) ? 1 : direction;
                break;
            case KeyEvent.VK_RIGHT:
                direction = (direction != 1) ? 2 : direction;
                break;

            case KeyEvent.VK_P:
                direction = 0;
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint(); // Nach allen Aktionene wird neu gemalt
    }

}
