package ship;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import display.StdDraw;
import display.Vector2;
import module.Module;
import module.Reactor;
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
	protected WeaponControl				weaponControl;	// The weapon control system
	
	protected Collection<CrewMember> 	crew;			// The crew members in the ship
	protected Collection<Tile>			layout;			// The layout of the ship
	protected boolean					isPlayer;		// Whether this ship is owned by the player
	protected Module[]					modules;		// The modules on the ship
	protected Collection<Projectile>	projectiles;	// The projectiles shot by the ship
	protected Tile						target;			// The targeted tile of the enemy ship
	protected Tile 						crewTile;       // The tile to teleport the crew member
	protected CrewMember				selectedMember; // The currently selected crew member
	
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
	}
	
	// Main Methods
	
	/**
	 * Processes the action of the AI.
	 * @param player the enemy of the AI
	 */
	public void ai(Ship player) {
		if (isPlayer)
			return;
	}
	
	/**
	 * Processes the time elapsed between the steps.
	 * @param elapsedTime the time elapsed since the last step
	 */
	public void step(double elapsedTime) {
		chargeWeapons(elapsedTime);
		processProjectiles(elapsedTime);
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
		for (Module m : modules)
			m.drawHud();
		// Hull
		StdDraw.text(0.025, 0.97, "Hull");
		int j = currentHull;
		for (int i = 1; i <= totalHull; i++)
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
		//on deselectionne la case
		this.crewTile.unmarkTarget();
	}

	/**
	 * Selects the crew member.
	 * @param i -th crew member
	 */
	public void selectMember(int i) {
		int j = 0;
		//on selectionne (ou reselectionne) la case
		if(this.crewTile == null)
			this.crewTile = this.getFirstTile();
		this.crewTile.markTarget();
		
		for (CrewMember m : crew) {
			if (j++ == i) {
				selectedMember = m;
				selectedMember.select();
				return;
			}
		}
	}
	
	/**
	 * Adds a crew member to the ship.
	 * @param member the crew member to add
	 */
	public void addCrewMember(CrewMember member) {
		Tile t = getEmptyTile();
		if (t == null) {
			System.err.println("The ship is full ! Sorry " + member.getName() + "...");
			return;
		}
		crew.add(member);
		t.setCrewMember(member);
	}
	
	// Layout Methods
	
	/**
	 * Adds a tile to the ship.
	 * @param t the tile to add
	 */
	protected void addTile(Tile t) {
		layout.add(t);
	}
	
	/**
	 * Gives an empty tile of the ship
	 * @return a tile empty of crew member
	 */
	private Tile getEmptyTile() {
		Iterator<Tile> it = layout.iterator();
		while(it.hasNext()) {
			Tile t = it.next();
			if (!t.hasCrewMember())
				return t;
		}
		return null;
	}
	
	/**
	 * Gives the first tile of the ship.
	 * @return the first tile of the ship
	 */
	private Tile getFirstTile() {
		return layout.iterator().next();
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
	 */
	public void addEnergy(int module) {
		if (module >= modules.length)
			return;
		if (reactor.getAllocatedEnergy() > 0 && modules[module].addEnergy()) 
			reactor.removeEnergy();
		
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
		Projectile p = weaponControl.shotWeapon(weapon, getWeaponTile(weaponControl.getWeapon(weapon)), new Vector2<Double>(xSpeed, ySpeed));
		if (p != null && this.target != null) {
			p.computeDirection(this.target.getCenterPosition());
			projectiles.add(p);
		}
	}
	
	/**
	 * Gives the tile on which the weapon is.
	 * @param w the weapon to get the tile from
	 * @return the tile on which the weapon is attached
	 */
	private Tile getWeaponTile(Weapon w) {
		for (Tile t : layout)
			if (t.getWeapon() == w)
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
		for (Projectile p : projectiles)
			if (p != null) {
				p.step(elapsedTime);
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
	 */
	public void applyDamage(Projectile p) {
		Tile touche = this.getTileHit(p);
		if(touche != null) {//si on n'est pas different de null on ne fait rien
			this.currentHull -= p.getDamage();
			if(touche instanceof Module) {//si on touche un module on applique l'effet
				p.applyEffect((Module)touche);
			}
		}
	}
	
	// Aiming Methods
	
	/**
	 * Aims the guns up.
	 * @param opponent the ship to aim at
	 */
	public void aimUp(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}else{
			this.target.unmarkTarget();
			int n = ((ArrayList<Tile>) opponent.layout).indexOf(this.target);
			if(n > 0) {
				this.target = ((ArrayList<Tile>) opponent.layout).get(n - 1);
			}else {
				this.target = ((ArrayList<Tile>) opponent.layout).get(opponent.layout.size() - 1);
			}
			this.target.markTarget();
			return;
		}
	}
	
	/**
	 * Aims the guns down.
	 * @param opponent the ship to aim at
	 */
	public void aimDown(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}else{
			this.target.unmarkTarget();
			int n = ((ArrayList<Tile>) opponent.layout).indexOf(this.target);
			if(n < opponent.layout.size() - 1) {
				this.target = ((ArrayList<Tile>) opponent.layout).get(n + 1);
			}else {
				this.target = opponent.getFirstTile();
			}
			this.target.markTarget();
			return;
		}
	}
	
	/**
	 * TODO
	 * Aims the guns left.
	 * @param opponent the ship to aim at
	 */
	public void aimLeft(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}
		System.err.println("Aiming System Critical Failure !");
	}

	/**
	 * TODO
	 * Aims the guns right.
	 * @param opponent the ship to aim at
	 */
	public void aimRight(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}
		System.err.println("Aiming System Critical Failure !");
	}
	
	/////////////////////////////////////////////////////////////////////////////
	//perso
	////////////////////////////////////////////////////////////////////////////
	//getter sp�cials
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
	//autre
	public void chooseTeleporteTileLeft() {
		if(this.isCrewMemberSelected()) {
			if (crewTile == null) {
				crewTile = this.getFirstTile();
				crewTile.markTarget();
				return;
			}else{
				this.crewTile.unmarkTarget();
				int n = ((ArrayList<Tile>) this.layout).indexOf(this.crewTile);
				if(n < this.layout.size() - 1) {
					this.crewTile = ((ArrayList<Tile>) this.layout).get(n + 1);
				}else {
					this.crewTile = this.getFirstTile();
				}
				this.crewTile.markTarget();
				return;
			}
		}
	}
	public void chooseTeleporteTileRight() {
		if(this.isCrewMemberSelected()) {
			if (crewTile == null) {
				crewTile = this.getFirstTile();
				crewTile.markTarget();
				return;
			}else{
				this.crewTile.unmarkTarget();
				int n = ((ArrayList<Tile>) this.layout).indexOf(this.crewTile);
				if(n > 0) {
					this.crewTile = ((ArrayList<Tile>) this.layout).get(n - 1);
				}else {
					this.crewTile = ((ArrayList<Tile>) this.layout).get(this.layout.size() - 1);
				}
				this.crewTile.markTarget();
				return;
			}
		}
	}
	
	public void teleportCrewMember() {
		//on ne peux avoir qu'un membre par tile
		if(this.isCrewMemberSelected() && this.crewTile != null && !this.crewTile.hasCrewMember()) {
			for(Tile tile : this.layout) {
				if(tile.isCrewMember(this.selectedMember)) {
					tile.removeCrewMember();
					break;
				}
			}
			this.crewTile.setCrewMember(this.selectedMember);
		}
	}
	
}
