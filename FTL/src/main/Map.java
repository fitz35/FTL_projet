package main;

import display.StdDraw;
import display.Vector2;
import map.CombatWorld;
import map.MarketSector;
import map.Sector;

/**
 * The map which the player want to go
 * 
 * 
 * @author clementL
 *
 */
public class Map {
	//public constante
	public static final int NB_SECTOR = 10; // nb of sector in a row and a line.
	
	public static final int COINS_EARN_ON_DEPOSIT = 20;
	
	//////////////////////////////////////////////////////
	//private constante
	
	//taux de Sector
	private static final double TAUX_VAISSEAU = 0.7;
	private static final double TAUX_DEPOSIT = 0.1;
	private static final double TAUX_MAGASIN = 0.2;
	
	///////////////////////////////////////////////////////
	//attribut
	private Player player;
	
	private Sector[][] map; //the sector map
	
	private BindingsMap bind; // the bind for the map
	
	private boolean drawPlayerHud;//if the map draw the player hud
	
	/**
	 * construct a map
	 * @param player the player who play
	 */
	public Map(Player player, CombatWorld cbtWorld) {
		this.map = new Sector[NB_SECTOR][NB_SECTOR];
		this.player = player;
		
		//generation de la map
		for(int y = 0 ; y < this.map.length ; y++) {
			for(int x = 0 ; x < this.map[y].length ; x++){
				double rand = Math.random();
				if(rand < TAUX_VAISSEAU) {
					this.map[y][x] = new Sector("ressources/images/ship.png", Sector.SECTOR_SHIP);
				}else if(rand < TAUX_VAISSEAU + TAUX_MAGASIN) {
					this.map[y][x] = new MarketSector(this.player);
				}else if(rand <= TAUX_VAISSEAU + TAUX_MAGASIN + TAUX_DEPOSIT){
					this.map[y][x] = new Sector("ressources/images/ressource.png", Sector.SECTOR_RESSOURCE);
				}
			}
		}
		
		this.player.setPosOnMap(new Vector2<Integer>(0, 0), this.map[0][0].getType());
		//on le decouvre si c'est la premiere case pour l'afficher
		this.map[0][0].setVisible(); 
		//on decouvre celle autour
		this.decouvre();
		
		this.bind = new BindingsMap(this, cbtWorld);
		this.drawPlayerHud = false;
	}
	
	/**
	 * process the key
	 */
	public void processKey() {
		this.bind.processKey();
	}
	
	
	/**
	 * draw the map
	 */
	public void draw() {
		for(int y = 0 ; y < this.map.length ; y++) {
			for(int x = 0 ; x < this.map[y].length ; x++){
				this.map[y][x].draw(new Vector2<Double>((1.0/NB_SECTOR) * x + (1.0/NB_SECTOR)/2, 
						(1.0/NB_SECTOR) * y + (1.0/NB_SECTOR)/2));
			}
		}
		Vector2<Integer> pos = this.player.getPosOnMap();
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.filledRectangle(((1.0/NB_SECTOR) * pos.getX() + (1.0/NB_SECTOR)/2), 
								(1.0/NB_SECTOR) * pos.getY() + (1.0/NB_SECTOR)/2, 
								(1.0/NB_SECTOR)/6, 
								(1.0/NB_SECTOR)/6);
		
		if(this.drawPlayerHud) {
			this.player.drawHud();
		}
	}
	
	
	
	
	/**
	 * moove the player up if sens, down instead
	 * @param sens the sens of the moove
	 * @return if the player has mooved
	 */
	public boolean deplacePlayerVertical(boolean sens) {
		Vector2<Integer> pos = this.player.getPosOnMap();
		if(sens) {
			if(pos.getY() < NB_SECTOR - 1) {
				this.player.setPosOnMap(new Vector2<Integer>(pos.getX(), pos.getY() + 1), 
						this.map[pos.getY() + 1][pos.getX()].getType());
				this.map[pos.getY()][pos.getX()].setHasBeenVisited(true);//on mark le sector qu'on quite
				this.decouvre();
				return true;
			}
			else {
				return false;
			}
		}else {
			if(pos.getY() > 0) {
				this.player.setPosOnMap(new Vector2<Integer>(pos.getX(), pos.getY() - 1), 
						this.map[pos.getY() - 1][pos.getX()].getType());
				this.map[pos.getY()][pos.getX()].setHasBeenVisited(true);
				this.decouvre();
				return true;
			}else {
				return false;
			}
		}
	}
	
	/**
	 * moove the player right if sens, left instead
	 * @param sens the sens of the moove
	 * @return if the player has mooved
	 */
	public boolean deplacePlayerHorizontale(boolean sens) {
		Vector2<Integer> pos = this.player.getPosOnMap();
		if(sens) {
			if(pos.getX() < NB_SECTOR - 1) {
				this.player.setPosOnMap(new Vector2<Integer>(pos.getX() + 1, pos.getY()), 
						this.map[pos.getY()][pos.getX() + 1].getType());
				this.map[pos.getY()][pos.getX()].setHasBeenVisited(true);
				this.decouvre();
				return true;
			}
			else {
				return false;
			}
		}else {
			if(pos.getX() > 0) {
				this.player.setPosOnMap(new Vector2<Integer>(pos.getX() - 1, pos.getY()), 
						this.map[pos.getY()][pos.getX() - 1].getType());
				this.map[pos.getY()][pos.getX()].setHasBeenVisited(true);
				this.decouvre();
				return true;
			}else {
				return false;
			}
		}
	}
	
	/**
	 * set visible the sector arround the player
	 */
	private void decouvre() {
		Vector2<Integer> pos = this.player.getPosOnMap();
		if(pos.getY() + 1 < NB_SECTOR) {
			map[pos.getY() + 1][pos.getX()].setVisible();
		}
		if(pos.getY() - 1 >= 0) {
			map[pos.getY() - 1][pos.getX()].setVisible();
		}
		if(pos.getX() + 1 < NB_SECTOR) {
			map[pos.getY()][pos.getX() + 1].setVisible();
		}
		if(pos.getX() - 1 >= 0) {
			map[pos.getY()][pos.getX() - 1].setVisible();
		}
	}
	
	/**
	 * return if the Sector has Been already visited
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return if the Sector has Been already visited
	 * @note doesn't verify the array dimension
	 */
	public boolean isDecouvert(int x, int y) {
		return this.map[y][x].isHasBeenVisited();
	}
	
	/**
	 * if the player is on a market sector, lunch the market
	 */
	public void lunchMarket() {
		Vector2<Integer> pos = this.player.getPosOnMap();
		if(this.map[pos.getY()][pos.getX()] instanceof MarketSector) {
			((MarketSector) this.map[pos.getY()][pos.getX()]).lunch();
		}
	}
	
	/**
	 * change the state of the player hud
	 */
	public void setDrawPlayerHud() {
		this.drawPlayerHud = !this.drawPlayerHud;
	}
	
	/**
	 * return the player 
	 * @return the player
	 */
	public Player getPlayer() {
		return this.player;
	}
}
