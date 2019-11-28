package map;

import main.Player;

public class MarketSector extends Sector {
	private Player player;//the player of the game
	
	/**
	 * represente a market sector
	 */
	public MarketSector(Player p) {
		super("ressources/images/coin.png", Sector.SECTOR_MARKET);
		// TODO Auto-generated constructor stub
		
		this.player = p;
	}
	
	/**
	 * lunche and step the market
	 */
	public void lunch() {
		
	}

}
