/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.util.ArrayList;

/**
 *
 * @author Levente
 */
public class Player {
    
    private int x, y;
    private int dir = 0;
    private int speed = 60;
    private int count = 0;
    private ArrayList<Position> snake;
    private boolean doNotShorten = false;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        
        snake = new ArrayList<>();
        
        snake.add(new Position(x, y));
    }
    
    public void update()
    {
        count++;
        
        if(count == speed)
        {
            switch(dir) {
                case 0: y--;
                        break;
                case 1: x--;
                        break;
                case 2: y++;
                        break;
                case 3: x++;
                        break;
            }
            
            snake.add(0, new Position(x, y));
            
            if(!doNotShorten)
                snake.remove(snake.size()-1);
            else
                doNotShorten = false;
            
            count = 0;
        }
    }

    public void doNotShorten() {
        doNotShorten = true;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void speedUp()
    {
        speed = (int)(speed * 0.9);
    }
    
    public void speedDown()
    {
        speed = (int)(speed * 1.1);
    }

    public void setDir(int dir) {
        if(dir < 4 && dir >= 0)
        {
            this.dir = dir;
        }
    }
    
    public int getLength()
    {
        return snake.size();
    }
    
    public Position getPos(int i)
    {
        return snake.get(i);
    }
    
    
    
}
