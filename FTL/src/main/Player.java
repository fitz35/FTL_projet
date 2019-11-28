package main;

import display.Vector2;
import ship.Ship;

public class Player {
	
	
	
	
	private Ship ship; //ship of the player
	private Vector2<Integer> posOnMap; // position of the player on the map (with integer coordinate). It begin at 0/0
	private boolean hasBeenMooved; //if the player has been mooved
	private String sector; //type of the sector who is the player
	private int coins; //the coin of the player
	
	/**
	 * construct the player
	 * @param ship the ship of the player
	 */
	public Player(Ship ship) {
		// TODO Auto-generated constructor stub
		this.setShip(ship);
		this.posOnMap = null;
		this.setHasBeenMooved();
		this.sector = null;
		this.coins = 10;
	}
	
	/**
	 * draw information on the player
	 */
	public void drawHud() {
		
	}
	
	
	//////////////////////////////////////////
	//getter/setter
	//////////////////////////////////////////
	

	/**
	 * @return the ship
	 */
	public Ship getShip() {
		return ship;
	}

	/**
	 * @param ship the ship to set
	 */
	public void setShip(Ship ship) {
		this.ship = ship;
	}

	/**
	 * @return the posOnMap
	 */
	public Vector2<Integer> getPosOnMap() {
		return posOnMap;
	}

	/**
	 * @param posOnMap the posOnMap to set
	 * @param sector the type of the sector
	 */
	public void setPosOnMap(Vector2<Integer> posOnMap, String sector) {
		this.hasBeenMooved = true;
		this.sector = sector;
		this.posOnMap = posOnMap;
	}

	/**
	 * @return the hasBeenMooved
	 */
	public boolean isHasBeenMooved() {
		return hasBeenMooved;
	}

	/**
	 * set has been mooved to false
	 */
	public void setHasBeenMooved() {
		this.hasBeenMooved = false;
	}

	/**
	 * @return the sector
	 */
	public String getSector() {
		return sector;
	}

	/**
	 * @return the coins
	 */
	public int getCoins() {
		return coins;
	}

	/**
	 * add coin to the player
	 * @param coins the coins to add
	 */
	public void addCoins(int coins) {
		this.coins += coins;
	}

	/**
	 * remove coin to the player
	 * @param coins the coins to remove
	 */
	public void removeCoins(int coins) {
		this.coins -= coins;
	}
}
