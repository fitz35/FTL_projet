package main;
import java.util.ArrayList;
import java.util.Collection;

import display.Button;
import display.StdDraw;
import display.Vector2;
import module.Module;
import ship.DummyShip;
import ship.Ship;
import weapon.Projectile;

/**
 * The world contains the ships and draws them to screen.
 */
public class World {
	
	private Bindings 	bind;	// The bindings of the game.
	private long 		time;	// The current time 
	
	private int 		level;  // The curent level of the opponent ship (= the difficulty)
	private Collection<ModuleButton> moduleButton ; // The button to display at the end of the round (upgrade module)
	
	Ship player;				// The ship of the player
	Ship opponent;				// The ship of the opponent
	
	/**
	 * Creates the world with the bindings, the player ship
	 * and the opponent ship.
	 */
	public World() {
		this.level = 0;
		bind = new Bindings(this);
		player = new DummyShip(true, new Vector2<Double>(0.3, 0.5));
		genNewOpponentShip();
		time = System.currentTimeMillis();
		this.moduleButton = new ArrayList<ModuleButton>();
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
		//on clean les bouttons
			for(ModuleButton button : this.moduleButton) {
				button.destroy();
			}
			this.moduleButton.clear();
			
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
			if(isPlayer) {
				if(this.opponent.getTileHit(proj) != null) {
					 this.opponent.applyDamage(proj);
					 toRemove.add(proj);
				}
			}else {
				if(this.player.getTileHit(proj) != null) {
					 this.player.applyDamage(proj);
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
	
	//fin du jeu
	/**
	 * if the player is dead
	 * @return if the player is dead
	 */
	public boolean isPlayerDead() {
		return this.player.getCurentHull() <=0;
	}
	
	/**
	 * if the opponent is dead
	 * @return if the opponent is dead
	 */
	public boolean isOpponentDead() {
		return this.opponent.getCurentHull() <= 0;
	}
	
	/**
	 * gen a new opponent ship of the level
	 */
	private void genNewOpponentShip() {
		this.opponent = new DummyShip(false, new Vector2<Double>(0.8, 0.5));
		this.level ++;
	}
	
	/**
	 * player's victory and resolution of the reward
	 */
	public void playerGagne() {
		//on genere un nouveau vaisseau
		this.genNewOpponentShip();
	}
	
	//drawing the end game hud
	/**
	 * drawing the hud of victory
	 */
	public void drawVictoryHud() {
		StdDraw.setPenColor(StdDraw.WHITE);//on efface lécran
		StdDraw.filledRectangle(0.5, 0.5, 0.23, 0.23);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(0.5, 0.70, "Gagné !");
		StdDraw.text(0.5, 0.70, "Gagné !");
		StdDraw.rectangle(0.5, 0.5, 0.25, 0.25);
		
		//module
		Module[] modules = this.player.getModule();
		
		for(int i = 0 ; i < modules.length ; i++ ) {
			double halfhigh = 0.075;
			double halfwidth = (0.5/modules.length)/2;
			double posX = 0.25 + halfwidth + i*halfwidth*2;
			double posY = 0.25 + halfhigh;
			
			//fond en rouge et pas de boutton si on peux pas l'améliorer
			if(modules[i].getCurrentLevel() >= modules[i].getMaxLevel()) {
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.filledRectangle(posX, posY, halfwidth, halfhigh);
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.rectangle(posX, posY, halfwidth, halfhigh);
			}else {//sinon on ajoute un boutton
				ModuleButton button = new ModuleButton(new Vector2<Double>(posX, posY),
						new Vector2<Double>(halfwidth, halfhigh), 
						modules[i],
						modules[i].getCurrentLevel() + 1) ;
				this.moduleButton.add(button);
				button.draw();
			}
			
			if(modules[i].getName() != null) {
				StdDraw.text(posX, posY, modules[i].getName());
			}else {
				StdDraw.text(posX, posY, "Reactor");
			}
		}
	}
	
	/**
	 * drawing the hud of defeat
	 */
	public void drawDefeatHud() {
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.rectangle(0.5, 0.5, 0.25, 0.25);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.filledRectangle(0.5, 0.5, 0.23, 0.23);
	}
	
	/**
	 * A WeaponButton is an implementation of a Button
	 * which activates/deactivates the linked weapon when
	 * left/right clicked.
	 */
	private class ModuleButton extends Button {
		
		private Module module;
		private int niveau;
		
		public ModuleButton(Vector2<Double> pos, Vector2<Double> dim, Module module, int niveau) {
			super(pos, dim, true);
			this.module = module;
			this.niveau = niveau;
		}

		@Override
		protected void onLeftClick() {
			if(this.module.getCurrentLevel() != this.module.getMaxLevel()) {
				this.module.setLevel(niveau);;
				playerGagne();
			}
		}

		@Override
		protected void onRightClick() {
			onLeftClick();
		}

		@Override
		protected void onMiddleClick() {}
		
	}
	
}
