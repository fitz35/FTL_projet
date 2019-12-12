package map;
import java.util.ArrayList;
import java.util.Collection;

import display.Button;
import display.StdDraw;
import display.Vector2;
import main.BindingsCombatWorld;
import main.Start;
import module.Module;
import module.Shield;
import ship.CrewMember;
import ship.DummyShip;
import ship.Ship;
import weapon.Missile;
import weapon.Projectile;

/**
 * The world contains the ships and draws them to screen.
 */
public class CombatWorld {
	
	private BindingsCombatWorld 	bind;	// The bindings of the game.
	private long 		time;	// The current time 
	
	private int 		level;  // The curent level of the opponent ship (= the difficulty)
	private Collection<Button> moduleButton ; // The button to display at the end of the round (upgrade module)
	private Button SkipRecompenseButton;      // button to skip the recompense'
	private int amelioration;   // The number of the amélioration to give to the player
	private Module moduleToUpgrade; //the module to upgrade
	
	public Ship player;				// The ship of the player
	public Ship opponent;				// The ship of the opponent
	
	private boolean skipRecompense;  
	//constante
	public static final double MULTIPLICATEUR_COINS = 0.5;//the multiplicater apply to the level for the money earn after the victory
	
	/**
	 * Creates the world with the bindings, the player ship
	 * and the opponent ship.
	 * @param playerShip the player's ship
	 */
	public CombatWorld(Ship playerShip) {
		this.level = 1;
		player = playerShip;
		genNewOpponentShip();
		time = System.currentTimeMillis();
		this.moduleButton = new ArrayList<Button>();
		this.amelioration = -1;
		this.moduleToUpgrade = null;
		this.SkipRecompenseButton = null;
		
		bind = new BindingsCombatWorld(this);
		this.skipRecompense = false;
	}
	
	
	/**
	 * Processes the key pressed.
	 */
	public void processKey(){
		this.bind.processKey();
	}
	
	/**
	 * Makes a step in the world.
	 * @return true si on a finis la manche
	 */
	public boolean step() {
		//on gere les upgrades du player et la regeneration du ship adverse
		if((this.amelioration != -1 && this.moduleToUpgrade != null) || this.skipRecompense) {
			this.upgrade(this.player, true);
			this.amelioration = -1;
			if(!this.skipRecompense)
				this.moduleToUpgrade.addLevel();
			
			
			//on clean les bouttons
			for(Button button : this.moduleButton) {
				button.destroy();
			}
			this.SkipRecompenseButton.destroy();
			this.SkipRecompenseButton = null;
			
			this.moduleButton.clear();
			
			this.genNewOpponentShip();
			this.player.clean();
			
			this.skipRecompense = false;
			this.moduleToUpgrade = null; // apres la destruction des bouttons pour eviter les effets de bords (POURQUOI LES THREAD ?????)
			return true;
		}else{
			//on avance le monde si on n'a pas finis
			if(!this.isPlayerDead() && !this.isPlayerWin()) {
				player.step(((double) (System.currentTimeMillis()-time))/1000);
				opponent.step(((double) (System.currentTimeMillis()-time))/1000);
					
				opponent.ai(player);
					
				processHit(player.getProjectiles(), true);
				processHit(opponent.getProjectiles(), false);
					
				time = System.currentTimeMillis();
			}
			return false;
		}
	}
	
	/**
	 * Processes the projectiles hit
	 * @param projectiles collection of projectiles to check for hit
	 * @param isPlayer whether the own of the projectiles is the player
	 * 
	 * @note shield v2
	 */
	private void processHit(Collection<Projectile> projectiles, boolean isPlayer) {
		Collection<Projectile> toRemove = new ArrayList<Projectile>();
		for(Projectile proj : projectiles) {
			//on test si il est capter par le shield
			Shield shield = isPlayer ? this.opponent.getShield() : this.player.getShield();
			
			if (shield.isTouche(proj) && !(proj instanceof Missile.MissileProjectile) && !shield.cantProtect()) {
				proj.applyEffect(shield);
				if(!shield.isDesactive()) {//on verifie qu'il n'a pas ete désactiver par le projectile
					shield.reduceCharge();
				}
				toRemove.add(proj);
				
			}else {
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
		}
		projectiles.removeAll(toRemove);
	}
	
	/**
	 * Draws the ships and HUDs.
	 */
	public void draw() {
		if(this.isPlayerWin()) {
			this.drawVictoryHud();
		}else {
			player.draw();
			player.drawHUD();
			
			opponent.draw();
			opponent.drawHUD();
		}
		
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
	public boolean isPlayerWin() {
		return this.opponent.getCurentHull() <= 0 ;
	}
	
	/**
	 * gen a new opponent ship of the level
	 */
	private void genNewOpponentShip() {
		this.opponent = new DummyShip(false, new Vector2<Double>(0.8, 0.5));
		
		for(int i = 0 ; i < this.level ; i++) {
			this.upgrade(this.opponent, false);
		}
		
		this.level ++;
	}
	
	/**
	 * add a reward to the the player ship according with this.amelioration if the ship is a player
	 * @param Ship ship the ship to upgrade
	 * @param boolean isPlayer if the ship is a player
	 */
	private void upgrade(Ship ship, boolean isPlayer) {
		int tmp = isPlayer ? this.amelioration : Start.getRandomInt(ship.canGainNewWeapon() ? 0 : 1, 3);
		switch(tmp) {
			case 0:
				//"Une arme !";
				ship.addRandomWeapon();
				break;
			case 1:
				//"Une réparation entre 1 et 5 de l’intégrité de la coque !";
				ship.repareHull();
				break;
			case 2:
				//"Un membre d’équipage !";
				ship.addCrewMember(new CrewMember("bill" + this.level));
				break;
			case 3:
				//"Un missile supplémentaire !";
				ship.addMissile();
				break;
		}
	}
	
	//drawing the end game hud
	/**
	 * drawing the hud of victory
	 */
	public void drawVictoryHud() {
		StdDraw.setPenColor(StdDraw.WHITE);//on efface lécran
		StdDraw.filledRectangle(0.5, 0.5, 0.23, 0.23);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.rectangle(0.5, 0.5, 0.25, 0.25);
		StdDraw.text(0.5, 0.70, "Gagné !");
		
		//amélioration
		if(this.amelioration == -1) {
			this.amelioration = Start.getRandomInt(this.player.canGainNewWeapon() ? 0 : 1, 3);
		}
		String amelio_string = "";
		switch(this.amelioration) {
			case 0:
				amelio_string += "Une arme !";
				break;
			case 1:
				amelio_string += "Une réparation entre " + Ship.AMMOUNT_REPA_MIN + " et " + 
									Ship.AMMOUNT_REPA_MAX + " de l’intégrité de la coque !";
				break;
			case 2:
				amelio_string += "Un membre d’équipage !";
				break;
			case 3:
				amelio_string += "Un missile supplémentaire !";
				break;
		}
		
		StdDraw.textLeft(0.25, 0.65, "Coins gagnés : " + ((int)(this.level*MULTIPLICATEUR_COINS)));
		StdDraw.textLeft(0.25, 0.60, "Upgrade aleatoire : ");
		StdDraw.text(0.5, 0.55, amelio_string);
		
		//module choice
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
						modules[i]);
				this.moduleButton.add(button);
				button.draw();
			}
			
			if(modules[i].getName() != null) {
				StdDraw.text(posX, posY, modules[i].getName());
			}else {
				StdDraw.text(posX, posY, "Reactor");
			}
		}
		
		
		//skip la recompense
		Vector2<Double> posSkip = new Vector2<Double>(0.2, 0.2);
		Vector2<Double> dimSkip = new Vector2<Double>(0.05, 0.05);
		if(this.SkipRecompenseButton == null)
			this.SkipRecompenseButton = new SkipButton(posSkip, dimSkip);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.rectangle(posSkip.getX(), posSkip.getY(), dimSkip.getX(), dimSkip.getY());
		StdDraw.text(posSkip.getX(), posSkip.getY(), "skip");
	}
	
	/**
	 * drawing the hud of defeat
	 */
	public void drawDefeatHud() {
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.filledRectangle(0.5, 0.5, 0.25, 0.25);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.rectangle(0.5, 0.5, 0.25, 0.25);
		StdDraw.text(0.5, 0.5, "perdu !!!");
	}
	
	//////////////////////////////////////////////////////////////
	/**
	 * get the coin earn by the player if he kill the opponent ship
	 * @return the coin earn by the player if he kill the opponent ship
	 */
	public int getCoinsEarn() {
		return (int) (this.level * MULTIPLICATEUR_COINS);
	}
	
	
	
	//////////////////////////////////////////////////////////////
	
	/**
	 * A WeaponButton is an implementation of a Button
	 * which activates/deactivates the linked weapon when
	 * left/right clicked.
	 */
	private class ModuleButton extends Button {
		
		private Module module;
		
		public ModuleButton(Vector2<Double> pos, Vector2<Double> dim, Module module) {
			super(pos, dim, true);
			this.module = module;
		}

		@Override
		protected void onLeftClick() {
			if(this.module.getCurrentLevel() < this.module.getMaxLevel() && moduleToUpgrade == null) {
				moduleToUpgrade = this.module;
			}
		}

		@Override
		protected void onRightClick() {
			onLeftClick();
		}

		@Override
		protected void onMiddleClick() {}
		
	}
	
	private class SkipButton extends Button{

		public SkipButton(Vector2<Double> pos, Vector2<Double> dim) {
			super(pos, dim);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onLeftClick() {
			// TODO Auto-generated method stub
			skipRecompense = true;
		}

		@Override
		protected void onRightClick() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void onMiddleClick() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
