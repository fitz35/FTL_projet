package module;

import display.Vector2;

public class Motor extends Module{

	/**
	 * @param hudPos position at which to draw the HUD
	 * @param tilePos position at which to draw the tile
	 * @param isPlayer whether it belongs to the player
	 * @param initialLevel initial amount of energy which it can provide
	 */
	public Motor(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, int initialLevel) {
		super(hudPos, tilePos, isPlayer);
		// Indeed, this module cannot be destroyed and is 'really' placed in the ship
		name = "motor";
		maxLevel = 8;
		currentLevel = initialLevel;
		allocatedEnergy = 0;
		amountDamage = 0;
		canBeManned = false;
	}
	
	/**
	 * 
	 * @return si le ship esquive
	 */
	public boolean esquive () {
		return Math.random()<0.05*allocatedEnergy;
	}

}
