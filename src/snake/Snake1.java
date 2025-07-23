package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Random;

public class Snake1 extends JFrame {

    ImagenSnake imagenSnake;
    Point snake;
    //Point lastSnake;
    Point comida;
    ArrayList<Point> listaPosiciones = new ArrayList<Point>();

    int longitud = 2;

    int width = 640;
    int height = 480;

    int widthPoint = 10;
    int heightPoint = 10;

    String direccion = "RIGHT";
    long frequency = 50;

    boolean gameOver = false;
    int score = 0;// New:variable de puntuación
    boolean pausado = false; // New; variable pausa

    public Snake1() { // Inicialización del juego:Se configura la ventana, se inicia el juego, se añade el panel gráfico, y se lanza el hilo que mueve la serpiente.

        setTitle("Snake");

        // Obtener tamaño de pantalla completo
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Redondear tamaño de pantalla al múltiplo más cercano de 10 ajsutando width y height al múltiplo más cercano de widthPoint y heightPoint
        width = (screenSize.width / widthPoint) * widthPoint;
        height = (screenSize.height / heightPoint) * heightPoint;

        startGame();
        imagenSnake = new ImagenSnake();
        this.getContentPane().add(imagenSnake);

        this.addKeyListener(new Teclas());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // o usar pantalla completa exclusiva
        setVisible(true);

        Momento momento = new Momento();
        Thread trid = new Thread(momento);
        trid.start();
    }

    public void startGame() { // Metodo: - Posiciona la serpiente y la comida en lugares iniciales./- Reinicia el cuerpo de la serpiente.
        score = 0;
        comida = new Point(200, 100);
        snake = new Point(320, 240);
        listaPosiciones = new ArrayList<Point>();
        listaPosiciones.add(snake);

        longitud = listaPosiciones.size();
    }

    public void generarComida() { //Metodo: Este método genera una nueva posición aleatoria para la comida, asegurándose de que esté dentro de los límites y alineada a la cuadrícula de movimiento.
        Random rnd = new Random();
        comida.x = rnd.nextInt(width / widthPoint) * widthPoint;
        comida.y = rnd.nextInt(height / heightPoint) * heightPoint;

        /* se utilizan las ddos lineas anteriores  reemplazando las siguientes lineas comentariadas generando que el codigo quede mas corto y claro, adicionalmente permite qie la comida este alineada con el borde visible del juego
        comida.x = (rnd.nextInt(width)) + 5;
        if ((comida.x % 5) > 0) {
            comida.x = comida.x - (comida.x % 5);
        }

        if (comida.x < 5) {
            comida.x = comida.x + 10;
        }
        if (comida.x > width) {
            comida.x = comida.x - 10;
        }

        comida.y = (rnd.nextInt(height)) + 5;
        if ((comida.y % 5) > 0) {
            comida.y = comida.y - (comida.y % 5);
        }

        if (comida.y > height) {
            comida.y = comida.y - 10;
        }
        if (comida.y < 0) {
            comida.y = comida.y + 10;
        }*/
    }

    public void actualizar() {

        listaPosiciones.add(0, new Point(snake.x, snake.y));
        listaPosiciones.remove(listaPosiciones.size() - 1);

        for (int i = 1; i < listaPosiciones.size(); i++) {
            Point point = listaPosiciones.get(i);
            if (snake.x == point.x && snake.y == point.y) {
                gameOver = true;
            }
        }

        if ((snake.x > (comida.x - 10) && snake.x < (comida.x + 10)) && (snake.y > (comida.y - 10) && snake.y < (comida.y + 10))) {
            listaPosiciones.add(0, new Point(snake.x, snake.y));
            score += 10;
            generarComida();

        }

        imagenSnake.repaint();

    }

    public static void main(String[] args) {
        Snake1 snake1 = new Snake1();
    }

    public class ImagenSnake extends JPanel {

        public void paintComponent(Graphics g) {//Clase: - Dibuja el fondo, la serpiente, la comida y el mensaje de Game Over si aplica

            super.paintComponent(g);

            if (gameOver) {
                g.setColor(new Color(0, 0, 0));
            } else {
                g.setColor(new Color(255, 255, 255));
            }
            g.setColor(Color.BLACK);// New: mostrar puntuacion en pantalla
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 25); // 

            g.setColor(new Color(0, 0, 255));

            if (listaPosiciones.size() > 0) {
                for (int i = 0; i < listaPosiciones.size(); i++) {
                    Point p = (Point) listaPosiciones.get(i);
                    g.fillRect(p.x, p.y, widthPoint, heightPoint);
                }
            }

            g.setColor(new Color(255, 0, 0));
            g.fillRect(comida.x, comida.y, widthPoint, heightPoint);

            if (gameOver) {
                g.setFont(new Font("TimesRoman", Font.BOLD, 40));
                g.drawString("GAME OVER", 300, 200);
                g.drawString("SCORE " + (listaPosiciones.size() - 1), 300, 240);

                g.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g.drawString("N to Start New Game", 100, 320);
                g.drawString("ESC to Exit", 100, 340);
            }

        }
    }

    public class Teclas extends java.awt.event.KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) { // Clase teclas: - Cambia la dirección de la serpiente según la tecla presionada./- Reinicia el juego con N./- Sale del juego con ESC.

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

                if (direccion != "LEFT") {
                    direccion = "RIGHT";

                }
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (direccion != "RIGHT") {
                    direccion = "LEFT";
                }
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (direccion != "DOWN") {
                    direccion = "UP";
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (direccion != "UP") {
                    direccion = "DOWN";
                }
            } else if (e.getKeyCode() == KeyEvent.VK_P) { // tevcla de pausa
                pausado = !pausado;
            } else if (e.getKeyCode() == KeyEvent.VK_N) {
                gameOver = false;
                startGame();
            }
        }

    }

    public class Momento extends Thread { // Clase Momento: - Es un hilo que se ejecuta constantemente./- Cada frequency milisegundos, mueve la serpiente en la dirección actual./- Si la serpiente sale de los límites, aparece por el otro lado (efecto de "teletransporte").

        private long last = 0;

        public Momento() {

        }

        public void run() {
            while (true) {
                if ((System.currentTimeMillis() - last) > frequency) {
                    if (!gameOver && !pausado) {
                        // Movimiento
                        if (direccion.equals("RIGHT")) {
                            snake.x += widthPoint;
                            if (snake.x >= width) {// se incluyo el = para corregir el rango de juego
                                snake.x = 0;
                            }
                        } else if (direccion.equals("LEFT")) {
                            snake.x -= widthPoint;
                            if (snake.x < 0) {
                                snake.x = width - widthPoint;
                            }
                        } else if (direccion.equals("UP")) {
                            snake.y -= heightPoint;
                            if (snake.y < 0) {
                                snake.y = height;
                            }
                        } else if (direccion.equals("DOWN")) {
                            snake.y += heightPoint;
                            if (snake.y >= height) {// se incluyo el = para corregir el rango de juego
                                snake.y = 0;
                            }
                        }

                        actualizar(); // ← Solo se actualiza si hay movimiento
                    }

                    last = System.currentTimeMillis();
                }
            }
        }

    }

}
