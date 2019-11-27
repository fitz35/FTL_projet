package main;

import display.Vector2;
import ship.Ship;

public class Player {
	private Ship ship; //ship of the player
	private Vector2<Integer> posOnMap; // position of the player on the map (with integer coordinate). It begin at 0/0
	private boolean hasBeenMooved; //if the player has been mooved
	private String sector; //type of the sector who is the player
	
	public Player(Ship ship) {
		// TODO Auto-generated constructor stub
		this.setShip(ship);
		this.posOnMap = null;
		this.setHasBeenMooved();
		this.sector = null;
	}

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

}
