package main;

import display.StdDraw;
import display.Vector2;
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
	
	
	public Map(Player player) {
		this.map = new Sector[NB_SECTOR][NB_SECTOR];
		//generation de la map
		for(int y = 0 ; y < this.map.length ; y++) {
			for(int x = 0 ; x < this.map[y].length ; x++){
				double rand = Math.random();
				
				if(rand < TAUX_VAISSEAU) {
					this.map[y][x] = new Sector("ressources/images/ship.png", Sector.SECTOR_SHIP);
				}else if(rand < TAUX_VAISSEAU + TAUX_MAGASIN) {
					this.map[y][x] = new Sector("ressources/images/coin.png", Sector.SECTOR_MARKET);
				}else {
					this.map[y][x] = new Sector("ressources/images/ressource.png", Sector.SECTOR_RESSOURCE);
				}
			}
		}
		this.player = player;
		this.player.setPosOnMap(new Vector2<Integer>(0, 0), this.map[0][0].getType());
		this.decouvre();
		
		this.bind = new BindingsMap(this);
	}
	
	/**
	 * process the key
	 */
	public void processKey() {
		this.bind.processKey();
	}
	
	
	/**
	 * draw the map or the evenement
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
		StdDraw.filledRectangle((1.0/NB_SECTOR) * pos.getX() + (1.0/NB_SECTOR)/2, 
								(1.0/NB_SECTOR) * pos.getY() + (1.0/NB_SECTOR)/2, 
								(1.0/NB_SECTOR)/2, 
								(1.0/NB_SECTOR)/2);
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
}
