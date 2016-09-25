/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

/**
 *
 * @author Levente
 */
public class Snake {

    public static void main(String[] args) {
        new Snake();
    }
    
    public Snake()
    {
        new GamePanel().startGame();
    }
    
}
