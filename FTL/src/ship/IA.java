package ship;

/**
 * IA of the opponent ship
 * @author clementL
 *
 */
public abstract class IA {
	protected Ship player;//the player ship
	protected Ship opponent;//the ship who has this ia
	
	protected int agressivity;// the ammount of agressivity beetween 0 and 10
	protected long time;//the time beetween 2 step
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
	public abstract void step();
}
