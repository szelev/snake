/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.util.ArrayList;
import java.util.Random;


/**
 *
 * @author Levente
 */
public class Game {
    
    private int level;
    private int sizeX, sizeY;
    private Player player1, player2;
    private Random rand;
    
    private ArrayList<Position> foods, slowers, fasters;
    
    public Game(int level, int sizeX, int sizeY) {
        this.level = level;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        
        rand = new Random(System.currentTimeMillis());

        
        player1 = new Player(rand.nextInt(sizeX), rand.nextInt(sizeY));
        player2 = new Player(rand.nextInt(sizeX), rand.nextInt(sizeY));
        
        foods = new ArrayList<>();
        slowers = new ArrayList<>();
        fasters = new ArrayList<>();
    }

    
    public void update()
    {
        if(rand.nextDouble() < 0.002)
            foods.add(new Position(rand.nextInt(sizeX), rand.nextInt(sizeY)));
        
        for(int i=0; i<foods.size(); i++)
        {
            if(foods.get(i).getX() == player1.getX() && foods.get(i).getY() == player1.getY())
            {
                player1.doNotShorten();
                foods.remove(i);
            } else if(foods.get(i).getX() == player2.getX() && foods.get(i).getY() == player2.getY())
            {
                player2.doNotShorten();
                foods.remove(i);
            }
        }
        
        if(rand.nextDouble() < 0.0008)
            fasters.add(new Position(rand.nextInt(sizeX), rand.nextInt(sizeY)));
        
        for(int i=0; i<fasters.size(); i++)
        {
            if(fasters.get(i).getX() == player1.getX() && fasters.get(i).getY() == player1.getY())
            {
                player1.speedUp();
                fasters.remove(i);
            } else if(fasters.get(i).getX() == player2.getX() && fasters.get(i).getY() == player2.getY())
            {
                player2.speedUp();
                fasters.remove(i);
            }
        }
        
        
        if(rand.nextDouble() < 0.0003)
            slowers.add(new Position(rand.nextInt(sizeX), rand.nextInt(sizeY)));
        if(rand.nextDouble() < 0.0003 && slowers.size() > 0)
            slowers.remove(0);
        
        for(int i=0; i<slowers.size(); i++)
        {
            if(slowers.get(i).getX() == player1.getX() && slowers.get(i).getY() == player1.getY())
            {
                player1.speedDown();
                slowers.remove(i);
            } else if(slowers.get(i).getX() == player2.getX() && slowers.get(i).getY() == player2.getY())
            {
                player2.speedDown();
                slowers.remove(i);
            }
        }
        
        
        player1.update();
        player2.update();
    }

    public int getFoodCount()
    {
        return foods.size();
    }
    
    public Position getFood(int i)
    {
        return foods.get(i);
    }
    
    public int getSlowerCount()
    {
        return slowers.size();
    }
    
    public Position getSlower(int i)
    {
        return slowers.get(i);
    }
    
    public int getFasterCount()
    {
        return fasters.size();
    }
    
    public Position getFaster(int i)
    {
        return fasters.get(i);
    }
    
    public Player getPlayer1() {
        return player1;
    }
    
    public Player getPlayer2() {
        return player2;
    }
    
    public int getLevel() {
        return level;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }
    
}
