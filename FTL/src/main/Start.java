package main;
import display.StdDraw;

/**
 * This class starts the game by creating the canvas
 * in which the game will be drawn in and the world as
 * well as the main loop of the game.
 */
public class Start {
	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 650;
	
	
	public static void main(String[] args) {
		// Creates the canvas of the game
		StdDraw.setCanvasSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		// Enables double buffering to allow animation
		StdDraw.enableDoubleBuffering();
		
		// Creates the world
		CombatWorld w = new CombatWorld();
		
		// Game infinite loop -> j'usqu'a ce que le joueur meure
		while(!w.isPlayerDead()) {
				// Clears the canvas of the previous frame
				StdDraw.clear();
				
				// Processes the key pressed during the last frame
				w.processKey();
				
				// Makes a step of the world if the ship of adversire is alive
				if(!w.isPlayerWin()) {
					w.step();
					// Draws the world to the canvas
					w.draw();
				}else {
					w.drawVictoryHud();
				}
				
				// Shows the canvas to screen
				StdDraw.show();
				
				// Waits for 20 milliseconds before drawing next frame.
				StdDraw.pause(20);
		}
		
		w.drawDefeatHud();
		
	}

}
