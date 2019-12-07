package ship;
import java.util.ArrayList;
import java.util.Collection;

import display.StdDraw;
import display.Vector2;
import module.Module;
import module.Motor;
import module.Reactor;
import module.Shield;
import module.WeaponControl;
import weapon.Projectile;
import weapon.Weapon;

/**
 * A ship is the composed of modules, tiles and crew members.
 * The ship has a hull that can be damaged by an opponent's ship.
 */
public abstract class Ship {
	
	
	protected Vector2<Double>			position;		// The position of the ship
	protected int						totalHull;		// The total hull integrity of the ship
	protected int						currentHull;	// The current hull integrity of the ship
	

	protected Reactor					reactor;		// The reactor of the ship
	protected Motor						motor;			// The motor of the ship
	protected Shield					shield;			// The shield of the shipmhip
	protected WeaponControl				weaponControl;	// The weapon control system
	
	protected Collection<CrewMember> 	crew;			// The crew members in the ship
	protected Collection<Tile>			layout;			// The layout of the ship
	protected boolean					isPlayer;		// Whether this ship is owned by the player
	protected Module[]					modules;		// The modules on the ship
	protected Collection<Projectile>	projectiles;	// The projectiles shot by the ship
	protected Tile						target;			// The targeted tile of the enemy ship
	protected Tile 						crewTile;       // The tile to teleport the crew member
	protected CrewMember				selectedMember; // The currently selected crew member
	
	protected IA ia; // the ia of the opponent ship
	
	public static final int AMMOUNT_REPA_MIN = 5;//the amount max of the reparation
	public static final int AMMOUNT_REPA_MAX = 8;//the amount max of the reparation
	
	//door
	protected Collection<Porte>         portes;        //the doors in the ship
	
	/**
	 * Creates a Ship for the player or the opponent at the provided position.
	 * @param isPlayer whether the ship is for the player
	 * @param position the location to create it
	 */
	public Ship(boolean isPlayer, Vector2<Double> position) {
		this.isPlayer = isPlayer;
		this.position = position;
		crew = new ArrayList<CrewMember>();
		projectiles = new ArrayList<Projectile>();
		layout = new ArrayList<Tile>();
		this.ia = null;
		
		this.portes = new ArrayList<Porte>();
	}
	
	// Main Methods
	
	/**
	 * Processes the action of the AI.
	 * @param player the enemy of the AI
	 */
	public void ai(Ship player) {
		if (isPlayer)
			return;
		else {
			if(this.ia == null)
				this.ia = new IAv1(this, player); 
			this.ia.step();
		}
	}
	
	/**
	 * Processes the time elapsed between the steps.
	 * @param elapsedTime the time elapsed since the last step
	 */
	public void step(double elapsedTime) {
		chargeWeapons(elapsedTime);
		processProjectiles(elapsedTime);
		this.repareModule(elapsedTime);
		for(Module m : this.modules) {
			m.stepDesactive(elapsedTime);
		}
		for(Porte p : this.portes)
			p.step(elapsedTime);
	}
	
	/**
	 * clean the variable of ship's step
	 */
	public void clean() {
		this.projectiles.clear();
		this.target = null;
	}
	
	// Drawing Methods
	
	/**
	 * Draws the ship and all its components.
	 */
	public void draw() {
		drawTiles();
		for (Projectile p : projectiles)
			p.draw();
	}

	/**
	 * Draw the tiles of the ship.
	 */
	private void drawTiles() {
		for (Tile t : layout)
			t.draw();
		for(Porte p : this.portes)
			p.draw();
	}

	/**
	 * Draws the HUD of the ship.
	 */
	public void drawHUD() {
		if (isPlayer)
			drawPlayerHUD();
		else
			drawOpponentHUD();
	}
	
	/**
	 * Draws the HUD of the player.
	 */
	private void drawPlayerHUD() {
		// Modules
		int i = 0;
		StdDraw.setPenColor(StdDraw.BLACK);
		for (Module m : modules) {
			m.drawHud(i);
			i++;
		}
		// Hull
		StdDraw.text(0.025, 0.97, "Hull");
		int j = currentHull;
		for (i = 1; i <= totalHull; i++)
			if (j > 0) {
				StdDraw.filledRectangle(0.05+(i*0.015), 0.97, 0.005, 0.015);
				j--;
			} else
				StdDraw.rectangle(0.05+(i*0.015), 0.97, 0.005, 0.015);
		
	}
	
	/**
	 * Draws the HUD of the opponent.
	 */
	private void drawOpponentHUD() {
		int j = currentHull;
		StdDraw.setPenColor(StdDraw.BLACK);
		for (int i = 1; i <= totalHull; i++)
			if (j > 0) {
				StdDraw.filledRectangle(0.67+(i*0.0075), 0.75, 0.0025, 0.0075);
				j--;
			} else
				StdDraw.rectangle(0.67+(i*0.0075), 0.75, 0.0025, 0.0075);
	}
	
	// Crew Methods
	
	/**
	 * Check if a crew member is currently selected.
	 * @return whether a crew member is selected
	 */
	public boolean isCrewMemberSelected() {
		return selectedMember != null;
	}

	/**
	 * Unselects the selected crew member.
	 */
	public void unselectCrewMember() {
		if (!isCrewMemberSelected()) {
			return;
		}
		selectedMember.unselect();
		selectedMember = null;
		//on deselectionne la case de target
		if(this.isPlayer) 
			this.crewTile.unmarkTarget();
		//this.crewTile = null;
	}

	/**
	 * Selects the crew member.
	 * @param i -th crew member
	 * @return if a member was selected
	 */
	public boolean selectMember(int i) {
		if(this.crew.size() <= i) {
			return false;
		}else {
			if(this.selectedMember != null)//on deselect le membre precedent si il existe
				this.selectedMember.unselect();
			this.selectedMember = ((ArrayList<CrewMember>)this.crew).get(i);
			this.selectedMember.select();
			if(this.crewTile == null) {
				this.crewTile = this.getTileFromMember(this.selectedMember);
			}
			this.crewTile.markTarget();
			return true;
		}
	}
	
	/**
	 * Adds a crew member to the ship.
	 * @param member the crew member to add
	 */
	public void addCrewMember(CrewMember member) {
		Tile t = this.getFirstTile();
		crew.add(member);
		t.addCrewMember(member);
	}
	
	/**
	 * teleport the selected crew member on the selected tile if they existe and swap if the tile is occuped
	 */
	public void teleportCrewMember() {
		//on ne peux avoir qu'un membre par tile, si deja occuper swap
		if(this.isCrewMemberSelected() && this.crewTile != null) {
			Tile t = this.getTileFromMember(this.selectedMember);
			Porte p = this.getPorteFromTile(t, this.crewTile);
			
			if(t != null && p != null) {
				if(p.isOuvert()) {
					t.removeCrewMember(this.selectedMember);
					this.crewTile.addCrewMember(this.selectedMember);
				}else {
					System.err.println("Porte non ouverte !");
				}
			}
		}
	}
	
	/**
	 * get the tile from a member
	 * @param member the member
	 * @return the tile of the member
	 */
	private Tile getTileFromMember(CrewMember member) {
		for(Tile t : this.layout) {
			if(t.isCrewMember(member))
				return t;
		}
		return null;
	}
	
	
	/**
	 * step the modules to be repared
	 * @param time the elapsed time
	 */
	public void repareModule(double time) {
		for(Module m : this.modules) {
			m.repare(time);
		}
	}
	// Layout Methods
	
	/**
	 * Adds a tile to the ship.
	 * @param t the tile to add
	 */
	protected void addTile(Tile t) {
		for(Tile tl : this.layout) {//si on a exactement une distance de 1 entre les deux tile
			if((Tile.getVerticalDistanceTile(tl, t) == 1 && Tile.getHorizontalDistanceTile(tl, t) == 0) ||
					(Tile.getVerticalDistanceTile(tl, t) == 0 && Tile.getHorizontalDistanceTile(tl, t) == 1)) {
				Porte p = new Porte(t, tl, this.isPlayer);
				this.portes.add(p);
				
			}
		}
		
		layout.add(t);
	}
	
	/**
	 * Gives the first tile of the ship.
	 * @return the first tile of the ship
	 */
	public Tile getFirstTile() {
		return layout.iterator().next();
	}
	
	/**
	 * return the tile hit by the projectile if it existe
	 * @param proj the projectile
	 * @return the tile hit by the projectile if it existe, null instead
	 */
	public Tile getTileHit (Projectile proj) {
		for(Tile tile : this.layout) {
			if(!proj.isOutOfRectangle(tile.getCenterPosition().getX(), tile.getCenterPosition().getY(), Tile.WIDTH/2, Tile.HEIGHT/2)) {
				return tile;
			}
		}
		return null;
	}
	// Energy Methods

	/**
	 * Decreased the energy allocated in the module. 
	 * @param module the module to remove energy from
	 */
	public void removeEnergy(int module) {
		if (module >= modules.length)
			return;
		if (modules[module].removeEnergy())
			reactor.addEnergy();
	}
	
	/**
	 * Increases the energy allocated in the module.
	 * @param module the module to add energy to
	 * @return if the energie was transfered into the module
	 */
	public boolean addEnergy(int module) {
		if (module < modules.length && reactor.getAllocatedEnergy() > 0 && modules[module].addEnergy()) {
			reactor.removeEnergy();
			return true;
		}else {
			return false;
		}
	}
	
	// Weapon Methods
	
	/**
	 * Deactivates a weapon. 
	 * @param weapon the weapon to deactivate
	 */
	public void deactiveWeapon(int weapon) {
		weaponControl.deactiveWeapon(weapon);
	}
	
	/**
	 * Activates a weapon. 
	 * @param weapon the weapon to activate
	 */
	public void activeWeapon(int weapon) {
		weaponControl.activeWeapon(weapon);
	}
	
	/**
	 * Charges the weapons by the time provided
	 * @param time the time to charge the weapons by
	 */
	public void chargeWeapons(double time) {
		weaponControl.chargeTime(time);
	}

	/**
	 * Shots a weapon.
	 * @param weapon the weapon to shot
	 */
	public void shotWeapon(int weapon) {
		double xSpeed = 1;
		double ySpeed = 1;
		Collection<Projectile> ps = weaponControl.shotWeapon(weapon, getWeaponTile(weaponControl.getWeapon(weapon)), new Vector2<Double>(xSpeed, ySpeed));
		if (ps != null && !ps.isEmpty() && this.target != null) {
			for(Projectile p : ps) {
				p.computeDirection(this.target.getCenterPosition());
				projectiles.add(p);
			}
		}else if(this.target == null) {
			System.err.println("Pas de cible !");
		}
	}
	
	/**
	 * Gives the tile on which the weapon is.
	 * @param w the weapon to get the tile from
	 * @return the tile on which the weapon is attached
	 */
	private Tile getWeaponTile(Weapon w) {
		for (Tile t : layout)
			if (t.getWeapon() != null)
				return t;
		return null;
	}
	
	// Projectile Methods
	
	/**
	 * Processes the projectiles with the time elapsed since
	 * the last processing.
	 * @param elapsedTime the time elapsed since the last call
	 */
	private void processProjectiles(double elapsedTime) {
		for (Projectile p : projectiles) {
			if (p != null) {
				p.step(elapsedTime);
			}
		}
	}
	
	/**
	 * Gives the projectiles shot by the ship.
	 * @return A collection of projectiles
	 */
	public Collection<Projectile> getProjectiles(){
		return projectiles;
	}

	/**
	 * Applies the damage a projectile did.
	 * @param p the projectile to process
	 * @note shield v2
	 */
	public void applyDamage(Projectile p) {
		Tile touche = this.getTileHit(p);
		if (!motor.esquive()) {
			if(touche != null) {//si on n'est pas different de null on ne fait rien
				this.currentHull= currentHull - p.getDamage();
				if(touche instanceof Module) {
					p.applyEffect((Module) touche);
				}
			}
		}
	}
	
	/*public void applyDamage(Projectile p) {//shield v1
		Tile touche = this.getTileHit(p);
		if (!motor.esquive()) {
			if(touche != null) {//si on n'est pas different de null on ne fait rien
				if (p instanceof Missile.MissileProjectile) {
					this.currentHull= currentHull - p.getDamage();
				}
				else {
					if (shield.cantProtect()) {
						this.currentHull = currentHull - p.getDamage(); 	
					}
					else {
						shield.reduceCharge();
					}
				}
				if(touche instanceof Module) {
					p.applyEffect((Module) touche);
				}
			}
			
		}
	}*/
	
	// Aiming Methods
	
	/**
	 * aim the crew tile left
	 */
	public void chooseTeleporteTileLeft() {
		this.crewTile.unmarkTarget();
		this.crewTile = aimLeft(this.layout, this.getTileFromMember(this.selectedMember));
	}
	
	/**
	 * aim the crew tile right
	 */
	public void chooseTeleporteTileRight() {
		this.crewTile.unmarkTarget();
		this.crewTile = aimRight(this.layout, this.getTileFromMember(this.selectedMember));
	}
	
	/**
	 * Aims the guns up.
	 * @param opponent the ship to aim at
	 */
	public void aimUp(Ship opponent) {
		this.target = aimUp(opponent.layout, this.target);
	}
	
	/**
	 * Aims the guns down.
	 * @param opponent the ship to aim at
	 */
	public void aimDown(Ship opponent) {
		this.target = aimDown(opponent.layout, this.target);
	}
	
	/**
	 * Aims the guns right.
	 * @param opponent the ship to aim at
	 */
	public void aimRight(Ship opponent) {
		this.target = aimRight(opponent.layout, this.target);
	}
	
	/**
	 * Aims the guns left.
	 * @param opponent the ship to aim at
	 */
	public void aimLeft(Ship opponent) {
		this.target = aimLeft(opponent.layout, this.target);
	}
	
	
	/**
	 * Aims in the layout up from the tile
	 * @param layout the layout
	 * @param t the tile
	 * @return the tile up if it exists
	 * @note update t with the new information
	 */
	public static Tile aimUp(Collection<Tile> layout, Tile t) {
		if (t == null) {
			t = layout.iterator().next();
			t.markTarget();
			return t;
		}else{
			t.unmarkTarget();
			t = Tile.getUpTile(layout, t);
			t.markTarget();
			return t;
		}
	}
	
	/**
	 * Aims in the layout down from the tile
	 * @param layout the layout
	 * @param t the tile
	 * @return the tile down if it exists
	 * @note update t with the new information
	 */
	public static Tile aimDown(Collection<Tile> layout, Tile t) {
		if (t == null) {
			t = layout.iterator().next();
			t.markTarget();
			return t;
		}else{
			t.unmarkTarget();
			t = Tile.getDownTile(layout, t);
			t.markTarget();
			return t;
		}
	}
	
	/**
	 * Aims in the layout left from the tile
	 * @param layout the layout
	 * @param t the tile
	 * @return the tile left if it exists
	 * @note update t with the new information
	 */
	public static Tile aimLeft(Collection<Tile> layout, Tile t) {
		if (t == null) {
			t = layout.iterator().next();
			t.markTarget();
			return t;
		}else{
			t.unmarkTarget();
			t = Tile.getLeftTile(layout, t);
			t.markTarget();
			return t;
		}
	}
	
	/**
	 * Aims in the layout right from the tile
	 * @param layout the layout
	 * @param t the tile
	 * @return the tile right if it exists
	 * @note update t with the new information
	 */
	public static Tile aimRight(Collection<Tile> layout, Tile t) {
		if (t == null) {
			t = layout.iterator().next();
			t.markTarget();
			return t;
		}else{
			t.unmarkTarget();
			t = Tile.getRightTile(layout, t);
			t.markTarget();
			return t;
		}
	}
	
	
	///////////////
	//getter and setter//
	/////////////////////
	
	/**
	 * get the curent hull of the ship
	 * @return the curent hull of the ship
	 */
	public int getCurentHull () {
		return this.currentHull;
	}
	
	/**
	 * get the total hull of the ship
	 * @return the total hull of the ship
	 */
	public int getTotalHull() {
		return this.totalHull;
	}
	
	/**
	 * repare the hull with an aleatoir value beetween AMMOUNT_REPA_MIN and AMMOUNT_REPA_MAX
	 */
	public void repareHull () {
		this.currentHull = (int) Math.min((this.currentHull + AMMOUNT_REPA_MIN + 
				Math.round(Math.random() * (AMMOUNT_REPA_MAX - AMMOUNT_REPA_MIN))), this.totalHull);
	}
	
	/**
	 * return the modules of the ship
	 * @return the modules of the ship
	 */ 
	public Module[] getModule () {
		return this.modules;
	}
	
	/**
	 * set a new weapon
	 * @param weapon to set
	 */
	public void addNewWeapon(Weapon weapon) {
		this.weaponControl.addWeapon(weapon);
	}
	
	/**
	 * add a missile to the missile weapon
	 */
	public void addMissile() {
		this.weaponControl.addMissile();
	}
	
	/**
	 * get the shield of the ship
	 * @return the shield of the ship
	 */
	public Shield getShield() {
		return this.shield;
	}
	
	/**
	 * get the nb of missile
	 * @return the nb of missile
	 */
	public int getNbMissile() {
		return this.weaponControl.getNbMissile();
	}
	
	/**
	 * get the porte from 2 tile
	 * @param tile1 the first tile
	 * @param tile2 the second tile
	 * @return the porte
	 */
	private Porte getPorteFromTile(Tile tile1, Tile tile2) {
		for(Porte p : this.portes) {
			if((tile1 == p.getTile1() && tile2 == p.getTile2()) || (tile1 == p.getTile2() && tile1 == p.getTile2())) {
				return p;
			}
		}
		return null;
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////
	//class
	
}
