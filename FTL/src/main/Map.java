package main;

import display.Vector2;
import map.MagasinSector;
import map.RessourceSector;
import map.Sector;
import map.ShipSector;

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
	
	
	private Sector[][] map; //the sector map
	
	
	public Map() {
		this.map = new Sector[NB_SECTOR][NB_SECTOR];
		//generation de la map
		for(int y = 0 ; y < this.map.length ; y++) {
			for(int x = 0 ; x < this.map[y].length ; x++){
				double rand = Math.random();
				
				if(rand < TAUX_VAISSEAU) {
					this.map[y][x] = new ShipSector();
				}else if(rand < TAUX_VAISSEAU + TAUX_MAGASIN) {
					this.map[y][x] = new MagasinSector();
				}else {
					this.map[y][x] = new RessourceSector();
				}
			}
		}
	}
	
	/**
	 * draw the map
	 */
	public void draw() {
		for(int y = 0 ; y < this.map.length ; y++) {
			for(int x = 0 ; x < this.map[y].length ; x++){
				this.map[y][x].draw(new Vector2<Double>((1.0/NB_SECTOR) * x + (1.0/NB_SECTOR)/2, (1.0/NB_SECTOR) * y + (1.0/NB_SECTOR)/2));
			}
		}
	}
	
	
	
	
	
	
	
	
}
