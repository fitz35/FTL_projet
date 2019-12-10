package main;

import java.util.ArrayList;

import display.StdDraw;
import display.Vector2;
import map.CombatWorld;
import map.Sector;
import ship.DummyShip;
import weapon.Death;
import weapon.DummyGun;
import weapon.Ion;
import weapon.Laser;
import weapon.Missile;
import weapon.Weapon;

/**
 * This class starts the game by creating the canvas
 * in which the game will be drawn in and the world as
 * well as the main loop of the game.
 */
public class Start {
	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 650;
	/**
	 * return the weapon possible
	 * @return the weapon possible
	 */
	public static ArrayList<Weapon> getWeaponPossible(){
		ArrayList<Weapon> weaponPossible = new ArrayList<Weapon>();
		weaponPossible.add(new Death());
		weaponPossible.add(new DummyGun());
		weaponPossible.add(new Ion(null));
		weaponPossible.add(new Laser());
		weaponPossible.add(new Missile());
		return weaponPossible;
	}
	
	
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
						w.processKey();
						if(w.step()) {//si on a finis la manche
							p.setHasBeenMooved();
							p.addCoins(w.getCoinsEarn());
						}else {
							// Draws the world to the canvas
							w.draw();
						}
					}else if(p.getSector() == Sector.SECTOR_RESSOURCE){
						p.addCoins(Map.COINS_EARN_ON_DEPOSIT);
						p.setHasBeenMooved();
						m.draw();//evite les drop d'image
					}else if(p.getSector() == Sector.SECTOR_MARKET){
						m.lunchMarket();
					}
				}else {
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
	
	/**
	 * get a random int beetween min and max incluse
	 * @param min the min borne
	 * @param max the max borne
	 * @return random int beetween min and max incluse
	 */
	public static int getRandomInt(int min, int max) {
		return (int) Math.round( (min + Math.random() * (max - min)));
	}
	
}
