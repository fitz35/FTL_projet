package module;
import display.StdDraw;
import display.Vector2;
import ship.CrewMember;
import ship.Tile;

/**
 * A module is an implementation of Tile which handles energy.
 * This tile has a HUD to display its current energy level.
 */
public abstract class Module extends Tile {
	private static final double     TEMPS_REPARE = 2.0; //temps de la reparation en seconde
	protected static final double     BASE_BONUS   = 0.05;//base bonus from a member on the module 
	
	protected   double              desactivationTime;  //temps de la desactivation en seconde

	protected	String				name;				// Name of the module
	protected	int 				maxLevel;			// Maximum level of the module
	protected 	int 				currentLevel;		// Current level of the module
	protected 	int 				allocatedEnergy;	// Amount of energy allocated to the module
	protected 	int					amountDamage;		// Amount of damage done to the module
	protected   double				timeElapsed;        // time elapsed during the last repare
	protected   double				timeDesactive;      // time elapsed during the last desactivation
	protected  	boolean 			canBeManned; 		// Can a crew member man this module
	protected 	boolean 			desactive;          // Desactivation is true if desactive is at true
	
	protected   boolean             isSelected;         // If the module is selected by the player
	
	/**
	 * Construct a module owned by the player or the opponent.
	 * The module's tile is drawn at the location given in tilePos.
	 * The module's HUD is drawn at the location given in hudPos.
	 * @param tilePos position at which to draw the tile
	 * @param isPlayer whether it belongs to the player
	 */
	public Module(Vector2<Double> tilePos, boolean isPlayer) {
		super(tilePos, isPlayer);
		this.desactivationTime = 0.0;
	}
	
	/**
	 * Increments energy allocated to the module.
	 * @return whether the energy has been added or not
	 */
	public boolean addEnergy() {
		if (allocatedEnergy < currentLevel - amountDamage) {
			++allocatedEnergy;
			return true;
		}
		return false;
	}
	
	/**
	 * Decrements energy allocated to the module.
	 * @return whether the energy has been added or not
	 */
	public boolean removeEnergy() {
		if (allocatedEnergy > 0) {
			--allocatedEnergy;
			return true;
		}
		return false;
	}
	
	//crew methode and repare
	/**
	 * step the module to be repared and repare it if needed
	 * @param elapsedTime the time elapsed
	 */
	public void repare (double elapsedTime) {
		double nb = this.getNbMemberBonus();
		if(nb != 0) {
			//on avance le temps
			if(this.amountDamage > 0) {
				this.timeElapsed += elapsedTime;
			}else {
				this.timeElapsed = 0;
			}
			//on repare si jamais
			if(this.timeElapsed >= Module.TEMPS_REPARE/nb) {
				this.amountDamage --;
				this.timeElapsed = 0;
			}
		}
	}
	
	// Draw Methods
	
	/**
	 * Draw the module's tile. 
	 */
	@Override
	public void draw() {
		super.draw();	
		if (name != null && name.length() > 0)
			StdDraw.text(tilePos.getX()-0.01, tilePos.getY()-0.01, ""+name.charAt(0));
	}
	
	/**
	 * Draw the module's HUD.
	 * @param index the index of the module
	 */
	public void drawHud(int index) {
		double x = this instanceof Reactor ? 0.02 : 0.02 + 0.08 + (index - 1) * 0.08 ;
		double y = this instanceof Reactor ? 0.005 : 0.115;
		if(this.isSelected)
			StdDraw.setPenColor(StdDraw.GREEN);
		else
			StdDraw.setPenColor(StdDraw.BLACK);
		if (getName() != null)
			StdDraw.text(x, y, getName());
		
		StdDraw.setPenColor(StdDraw.BLACK);
		int j = allocatedEnergy;
		int k = currentLevel - amountDamage;
		for (int i = 1; i <= currentLevel; i++)
			if(k-- <= 0) {
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.filledRectangle(x, (y+0.01)+(i*0.015), 0.015, 0.005);
				StdDraw.setPenColor(StdDraw.BLACK);
			} else if (j-- > 0) {
				StdDraw.filledRectangle(x, (y+0.01)+(i*0.015), 0.015, 0.005);
			} else
				StdDraw.rectangle(x, (y+0.01)+(i*0.015), 0.015, 0.005);
	}

	/**
	 * Gives the name of the module.
	 * @return name of the module
	 */
	public String getName() {
		return name;
	}

	/////////////
	// Getters //
	/////////////
	
	public int 		getMaxLevel() 			{ return maxLevel;			}
	public int 		getCurrentLevel()		{ return currentLevel; 		}
	public int		getAllocatedEnergy()	{ return allocatedEnergy;	}
	public int		getAmountDamage()		{ return amountDamage;		}
	public boolean 	getCanBeManned() 		{ return canBeManned; 		}
	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean 	isDesactive()			{ return desactive; 		}
	
	////////////
	// setter //
	////////////
	/**
	 * add 1 to the level of the module if it possible
	 * @return if the level has been added
	 */
	public boolean addLevel() {
		if(this.currentLevel < this.maxLevel) {
			this.currentLevel = this.currentLevel + 1;
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * apply the dmg on the module
	 * @param dmg the the damage to apply
	 */
	public void appliqueDmg(int dmg) {
		if(this.amountDamage != -1) {//pas le reacteur
			this.amountDamage += dmg;
		}
	}
	
	/**
	 * Set desactive
	 * @param desactive si le module est desactive
	 * @param time the time of desactivation
	 */
	public void setDesactive(boolean desactive, double time) {
		this.desactive = desactive;
		this.desactivationTime = time;
	}
	
	/**
	 * process time of desactivation
	 * @param elapsedTime temps depuis la derniere desactivation
	 */
	public void stepDesactive (double elapsedTime) {
		//on avance le temps
		if(this.desactive) {
			this.timeDesactive += elapsedTime;
		}else {
			this.timeDesactive = 0;
		}
		
		//on repare si jamais
		if(this.timeDesactive >= this.desactivationTime) {
			this.desactive = false;
			this.timeDesactive = 0;
		}
	}
	
	/**
	 * get the number of member on the tile with ponderation
	 * @return the number of member on the tile with ponderation
	 */
	protected double getNbMemberBonus () {
		double retour = 0.0;
		if(this.getName() != null) {//on ne s'occupe pas du reacteur
			for(CrewMember m : this.member) {
				if(m.getType() == CrewMember.TYPE_NONE)
					retour += 1.0;
				else if(m.getType().compareTo(this.getName()) == 0)
					retour += 2.0;
				else
					retour += 0.5;
			}
		}
		return retour;
	}
	
	@Override
	public String toString() {
		return "Module :\n" + this.name + "  energie : " + this.allocatedEnergy + " max energie : " + 
					this.currentLevel + "\n" + super.toString() + "\n";
	}

}
