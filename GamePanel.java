/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Levente
 */
public class GamePanel extends JPanel implements Runnable{
    
    private JFrame window;
    
    private Thread animator;
    
    private Graphics dbg;
    private Image dbImage = null;
    
    private Game game;
    
    private volatile boolean running = false;
    private volatile int gameOver = 0;
    
    private static int WIDTH, HEIGHT;
    
    private static int BLOCK_SIZE = 50;
    private static double BLOCK_REAL_WIDTH, BLOCK_REAL_HEIGHT;
    
    public GamePanel()
    {
        initPanel();
        initWindow();
        
        initGame();
    }
    
    private void renderGame(Graphics g)
    {
        g.setColor(Color.gray);
        g.fillRect(0, 50, WIDTH, HEIGHT-50);
        g.setColor(Color.cyan);
        g.fillRect((int)BLOCK_REAL_WIDTH, (int)(50+BLOCK_REAL_HEIGHT), (int)(WIDTH-2*BLOCK_REAL_WIDTH), (int)(HEIGHT-50-2*BLOCK_REAL_HEIGHT));
        
        g.setColor(Color.red);
        for(int i=0; i<game.getPlayer1().getLength(); i++)
            g.fillRect((int)(BLOCK_REAL_WIDTH*game.getPlayer1().getPos(i).getX())+(int)(BLOCK_REAL_WIDTH), 50+(int)(BLOCK_REAL_HEIGHT*game.getPlayer1().getPos(i).getY())+(int)(BLOCK_REAL_HEIGHT), (int)(BLOCK_REAL_WIDTH), (int)(BLOCK_REAL_HEIGHT));
        
        g.setColor(Color.yellow);
        for(int i=0; i<game.getPlayer2().getLength(); i++)
            g.fillRect((int)(BLOCK_REAL_WIDTH*game.getPlayer2().getPos(i).getX())+(int)(BLOCK_REAL_WIDTH), 50+(int)(BLOCK_REAL_HEIGHT*game.getPlayer2().getPos(i).getY())+(int)(BLOCK_REAL_HEIGHT), (int)(BLOCK_REAL_WIDTH), (int)(BLOCK_REAL_HEIGHT));
                
        g.setColor(Color.blue);
        for(int i=0; i<game.getFoodCount(); i++)
            g.fillOval((int)(BLOCK_REAL_WIDTH*game.getFood(i).getX())+(int)(BLOCK_REAL_WIDTH), 50+(int)(BLOCK_REAL_HEIGHT*game.getFood(i).getY())+(int)(BLOCK_REAL_HEIGHT), (int)(BLOCK_REAL_WIDTH), (int)(BLOCK_REAL_HEIGHT));
        
        g.setColor(Color.orange);
        for(int i=0; i<game.getFasterCount(); i++)
            g.fillOval((int)(BLOCK_REAL_WIDTH*game.getFaster(i).getX())+(int)(BLOCK_REAL_WIDTH), 50+(int)(BLOCK_REAL_HEIGHT*game.getFaster(i).getY())+(int)(BLOCK_REAL_HEIGHT), (int)(BLOCK_REAL_WIDTH), (int)(BLOCK_REAL_HEIGHT));
        
        g.setColor(Color.black);
        for(int i=0; i<game.getSlowerCount(); i++)
            g.fillOval((int)(BLOCK_REAL_WIDTH*game.getSlower(i).getX())+(int)(BLOCK_REAL_WIDTH), 50+(int)(BLOCK_REAL_HEIGHT*game.getSlower(i).getY())+(int)(BLOCK_REAL_HEIGHT), (int)(BLOCK_REAL_WIDTH), (int)(BLOCK_REAL_HEIGHT));
    }
    
    private void renderGameOverMsg(Graphics g)
    {
        renderGame(g);
        
        g.setColor(Color.RED);
        
        g.setFont(new Font("Serif", Font.BOLD, 40));
        g.drawString("GAME OVER! PLAYER " + (3-gameOver) + " WON!", 10, 45);
    }

    
    private void testPress(int x, int y)
    {
        
    }

    private void addListeners()
    {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e)
            {
                testPress(e.getX(), e.getY());
            }
        });
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    stopGame();
                if(e.getKeyCode() == KeyEvent.VK_W)
                    game.getPlayer1().setDir(0);
                if(e.getKeyCode() == KeyEvent.VK_A)
                    game.getPlayer1().setDir(1);
                if(e.getKeyCode() == KeyEvent.VK_S)
                    game.getPlayer1().setDir(2);
                if(e.getKeyCode() == KeyEvent.VK_D)
                    game.getPlayer1().setDir(3);
                if(e.getKeyCode() == KeyEvent.VK_UP)
                    game.getPlayer2().setDir(0);
                if(e.getKeyCode() == KeyEvent.VK_LEFT)
                    game.getPlayer2().setDir(1);
                if(e.getKeyCode() == KeyEvent.VK_DOWN)
                    game.getPlayer2().setDir(2);
                if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                    game.getPlayer2().setDir(3);
            }
        });
    }
    
    @Override
    public void run()
    {   
        long beforeTime, timeDiff, sleepTime;
        final long period = 10;
        
        beforeTime = System.currentTimeMillis();
        
        running = true;
        while(running)
        {
            gameUpdate();
            gameRender();
            paintScreen();
            
            timeDiff = System.currentTimeMillis()-beforeTime;
            sleepTime = period - timeDiff;
            
            if(sleepTime < 0)
                sleepTime = 5;
            
            try
            {
                Thread.sleep(sleepTime);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            
            beforeTime = System.currentTimeMillis();
        }
        
        System.exit(0);
    }
    
    private void isGameOver()
    {
        Position p1 = game.getPlayer1().getPos(0);
        Position p2 = game.getPlayer2().getPos(0);
        
        if(p1.equals(p2))
        {
            gameOver = 3;
            return;
        }
        if(p1.getX() < 0 || p1.getY() < 0 || p1.getX() >= (int)(WIDTH/BLOCK_SIZE - 2) || p1.getY() >= (int)((HEIGHT-50)/BLOCK_SIZE - 2))
        {
            gameOver = 1;
            return;
        }
        if(p2.getX() < 0 || p2.getY() < 0 || p2.getX() >= (int)(WIDTH/BLOCK_SIZE - 2) || p2.getY() >= (int)((HEIGHT-50)/BLOCK_SIZE - 2))
        {
            gameOver = 2;
            return;
        }
        for(int i=1; i<game.getPlayer2().getLength(); i++)
        {
            if(p1.equals(game.getPlayer2().getPos(i)))
            {
                gameOver = 1;
                return;
            }
            if(p2.equals(game.getPlayer2().getPos(i)))
            {
                gameOver = 2;
                return;
            }
            
        }
            
        for(int i=1; i<game.getPlayer1().getLength(); i++)
        {
            if(p2.equals(game.getPlayer1().getPos(i)))
            {
                gameOver = 2;
                return;
            }
            if(p1.equals(game.getPlayer1().getPos(i)))
            {
                gameOver = 1;
                return;
            }
        }
        
        
    }
    
    private void gameUpdate()
    {
        isGameOver();
        
        if(gameOver == 0)
            game.update();
    }
    
    private void gameRender()
    {
        if(dbImage == null)
        {
            dbImage = createImage(WIDTH, HEIGHT);
            if(dbImage == null)
            {
                System.err.println("ERROR: dbImage is null");
                return;
            }
            else
            {
                dbg = dbImage.getGraphics();
            }
        }
        
        dbg.setColor(Color.black);
        dbg.fillRect(0, 0, WIDTH, HEIGHT);
        
        if(gameOver != 0)
            renderGameOverMsg(dbg);
        else 
            renderGame(dbg);
    }
    
    private void paintScreen()
    {
        Graphics g;
        
        try
        {
            g = this.getGraphics();
            if((g != null) && (dbImage != null))
                g.drawImage(dbImage, 0, 0, null);
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
        catch(Exception e)
        {
            System.err.println("GRAPHICS ERROR: " + e.getMessage());
        }
    }

    public void startGame()
    {
        if(animator == null || !running)
        {
            animator = new Thread(this);
            animator.start();
        }
    }
    
    public void stopGame()
    {
        running = false;
    }    
    
    private void initPanel()
    {
        setBackground(Color.black);
        
        setFocusable(true);
        requestFocus();
        addListeners();
    }
    
    private void initWindow()
    {
        window = new JFrame("Snake");
        
        window.add(this);
        
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setUndecorated(true);
        window.setVisible(true);
        
        WIDTH = window.getWidth();
        HEIGHT = window.getHeight();
    }
    
    private void initGame()
    {
        game = new Game(1, (int)(WIDTH/BLOCK_SIZE - 2), (int)((HEIGHT-50)/BLOCK_SIZE - 2));
        
        BLOCK_REAL_WIDTH = 1.0*WIDTH/((int)(WIDTH/BLOCK_SIZE));
        BLOCK_REAL_HEIGHT = 1.0*(HEIGHT-50)/((int)((HEIGHT-50)/BLOCK_SIZE));
    }
    
}
