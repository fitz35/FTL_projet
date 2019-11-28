package main;
import display.StdDraw;
import display.Vector2;
import map.CombatWorld;
import map.Sector;
import ship.DummyShip;

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
		
		//player
		Player p = new Player(new DummyShip(true, new Vector2<Double>(0.3, 0.5)));
		// Creates the world
		CombatWorld w = new CombatWorld(p.getShip());
		//map
		Map m = new Map(p);
		
		// Game infinite loop -> j'usqu'a ce que le joueur meure
		while(!w.isPlayerDead()) {
				// Clears the canvas of the previous frame
				StdDraw.clear();
				
				if(p.isHasBeenMooved() && !m.isDecouvert(p.getPosOnMap().getX(), p.getPosOnMap().getY())) {
					if(p.getSector() == Sector.SECTOR_SHIP) {
						// Processes the key pressed during the last frame
						w.processKey();
						if(w.step())//si on a finis la manche
							p.setHasBeenMooved();
						// Draws the world to the canvas
						w.draw();
					}else if(p.getSector() == Sector.SECTOR_RESSOURCE){
						p.addCoins(50);
						p.setHasBeenMooved();
					}else if(p.getSector() == Sector.SECTOR_MARKET){
						m.lunchMarket();
						p.setHasBeenMooved();
					}
				}else {
					if(w.isPlayerWin())//si le joueur a deja gagner une fois, on regen le ship adverse
						w.prepareForNewCombat();
					m.processKey();
					m.draw();
				}
				
				// Shows the canvas to screen
				StdDraw.show();
				
				// Waits for 20 milliseconds before drawing next frame.
				StdDraw.pause(20);
		}
		
		w.drawDefeatHud();
		
	}

}
