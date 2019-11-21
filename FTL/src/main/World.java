package main;
import java.util.ArrayList;
import java.util.Collection;

import display.StdDraw;
import display.Vector2;
import ship.DummyShip;
import ship.Ship;
import ship.Tile;
import weapon.Projectile;

/**
 * The world contains the ships and draws them to screen.
 */
public class World {
	
	private Bindings 	bind;	// The bindings of the game.
	private long 		time;	// The current time 
	
	Ship player;				// The ship of the player
	Ship opponent;				// The ship of the opponent
	
	/**
	 * Creates the world with the bindings, the player ship
	 * and the opponent ship.
	 */
	public World() {
		bind = new Bindings(this);
		player = new DummyShip(true, new Vector2<Double>(0.3, 0.5));
		opponent = new DummyShip(false, new Vector2<Double>(0.8, 0.5));
		time = System.currentTimeMillis();
	}
	
	/**
	 * Processes the key pressed.
	 */
	public void processKey(){
		this.bind.processKey();
	}
	
	/**
	 * Makes a step in the world.
	 */
	public void step() {
		player.step(((double) (System.currentTimeMillis()-time))/1000);
		opponent.step(((double) (System.currentTimeMillis()-time))/1000);
		
		opponent.ai(player);
		
		processHit(player.getProjectiles(), true);
		processHit(opponent.getProjectiles(), false);
		
		time = System.currentTimeMillis();
	}
	
	/**
	 * Processes the projectiles hit
	 * @param projectiles collection of projectiles to check for hit
	 * @param isPlayer whether the own of the projectiles is the player
	 */
	private void processHit(Collection<Projectile> projectiles, boolean isPlayer) {
		Collection<Projectile> toRemove = new ArrayList<Projectile>();
		for(Projectile proj : projectiles) {
			//on test pour chaque projectiles les tile du vaisseau
			Collection<Vector2<Double>> list = null;
			if(isPlayer) {
				list = this.opponent.getPosLayout();
			}else {
				list = this.player.getPosLayout();
			}
			for(Vector2<Double> pos : list) {
				if(!proj.isOutOfRectangle(pos.getX(), pos.getY(), Tile.WIDTH/2, Tile.HEIGHT/2)) {//si on touche un module
					if(isPlayer) {
						this.opponent.applyDamage(proj);
					}else {
						this.player.applyDamage(proj);
					}
					toRemove.add(proj);
				}
			}
		}
		projectiles.removeAll(toRemove);
	}
	
	/**
	 * Draws the ships and HUDs.
	 */
	public void draw() {
		player.draw();
		player.drawHUD();
		
		opponent.draw();
		opponent.drawHUD();
		
	}
}
