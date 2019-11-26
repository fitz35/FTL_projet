package ship;
/**
 * IA of the opponent ship
 * @author clementL
 *
 */
public class IA {
	private Ship player;//the player ship
	private Ship opponent;//the ship who has this ia
	
	private int agressivity;// the ammount of agressivity beetween 0 and 10
	private long time;//the time beetween 2 step
	/**
	 * construct the IA process
	 * @param opponent the ship who use this IA
	 * @param player the player Ship
	 */
	public IA (Ship opponent, Ship player) {
		this.opponent = opponent;
		this.player = player;
		this.time = System.currentTimeMillis();
		this.agressivity = 10;
	}
	
	/**
	 * make the opponent ship step
	 */
	public void step() {
		evaluateAgressivity();
		long elapsedTime = System.currentTimeMillis();
		stepModule(elapsedTime - time);
		stepWeapon(elapsedTime - time);
		stepCrewMember(elapsedTime - time);
		this.time = elapsedTime;
	}
	
	//////////////////////////////////////////////////////////////////
	//usefull private function
	//////////////////////////////////////////////////////////////////
	//fonction de gestion des differentes capacitées
	/**
	 * manage the module
	 * @param long elapsedTime the time beetween the last call
	 * TODO
	 */
	private void stepModule(long elapsedTime) {
		removeEnergie();
		if(this.agressivity > 6) {
			//on met toute l'energie dans les weapon
			while(this.opponent.addEnergy(1)) {}
		}
	}
	
	/**
	 * manage the weapon and the shot
	 * @param long elapsedTime the time beetween the last call
	 * TODO
	 */
	private void stepWeapon(long elapsedTime) {
		this.opponent.activeWeapon(0);
		this.opponent.target = this.player.getFirstTile();
		this.opponent.shotWeapon(0);
	}
	
	/**
	 * manage the crew member
	 * @param long elapsedTime the time beetween the last call
	 * TODO
	 */
	private void stepCrewMember(long elapsedTime) {
		
	}
	//evaluation de l'agressivité
	/**
	 * evaluate agressivity of the ia
	 */
	private void evaluateAgressivity() {
		
	}
	
	//stepModule
	/**
	 * remove all the energie from the module
	 */
	private void removeEnergie() {
		
	}
	
	
}
