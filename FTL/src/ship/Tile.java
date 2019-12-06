package ship;
import java.util.ArrayList;
import java.util.Collection;

import display.StdDraw;
import display.Vector2;
import weapon.Weapon;

/**
 * A tile is a cell of the ship's layout.
 * A weapon can be attached to the tile and a crew member 
 * can be on the tile.
 */
public class Tile {
	public static final double HEIGHT = 0.02;
	public static final double WIDTH = 0.02;
	
	private 		Weapon 			weapon;		// The weapon assigned to the tile
	protected 		Collection<CrewMember> 		member;		// The crew member on the tile
	private 		boolean 		isAimed;	// Whether the tile aimed at
	private 		boolean 		isPlayer;	// Whether the tile is owned by the player
	protected final Vector2<Double> tilePos;	// The position of the tile
	
	/**
	 * Creates a tile for the player of the opponent
	 * which is drawn at the given position.
	 * @param position location to draw the tile
	 * @param isPlayer whether it is owned by the player ship
	 */
	public Tile(Vector2<Double> position, boolean isPlayer) {
		this.tilePos = position;
		this.isPlayer = isPlayer;
		this.member = new ArrayList<CrewMember>();
	}
	
	/**
	 * Checks whether a crew member is inside the tile.
	 * @return whether the tile has a crew member
	 */
	public boolean hasCrewMember() {
		return !member.isEmpty();
	}
	
	/**
	 * Sets the given crew member has inside the tile.
	 * @param member the crew member to put inside the tile
	 */
	public void addCrewMember(CrewMember member) {
		this.member.add(member);
	}
	
	/**
	 * Sets the given crew member has inside the tile.
	 * @param member the crew member to put inside the tile
	 */
	public void removeCrewMember(CrewMember member) {
		this.member.remove(member);
	}
	
	/**
	 * Checks whether the given crew member is the on in the tile.
	 * @param member the crew member to compare it to
	 * @return whether the crew member is the one in the tile
	 */
	public boolean isCrewMember(CrewMember member) {
		return this.member.contains(member);
	}

	/**
	 * Removes the crew member of the tile.
	 * @param i the index of the member to remve
	 */
	public void removeCrewMember(int i) {
		((ArrayList<CrewMember>)member).remove(i);
	}
	
	/**
	 * get the member on the tile if he exist
	 * @param i the index of the crew member
	 * @return the member on the tile if he exist null instead
	 */
	public CrewMember getMember (int i) {
		return ((ArrayList<CrewMember>) this.member).get(i);
	}
	
	/**
	 * get the number of the members on the tile
	 * @return the number of the members on the tile
	 */
	public int getNbMember(){
		return this.member.size();
	}
	
	///////////////////////////////////////////////////////////////
	//draw
	///////////////////////////////////////////////////////////////
	
	/**
	 * Draws the tile, the member inside and the weapon.
	 */
	public void draw() {
		if (tilePos == null)
			return;
		
		double x = tilePos.getX();
		double y = tilePos.getY();
		if (isAimed) {
			StdDraw.setPenColor(this.isPlayer ? StdDraw.GREEN : StdDraw.RED);
			StdDraw.filledRectangle(x-0.01, y-0.01, 0.01, 0.01);
			StdDraw.setPenColor(StdDraw.BLACK);
		}
		if (weapon != null)
			if (isPlayer)
				drawWeaponHorizontal(x, y);
			else
				drawWeaponVertical(x, y);
		drawHorizontalWall(x,y);
		y-=0.02;
		drawVerticalWall(x,y);
		drawHorizontalWall(x,y);
		x-=0.02;
		drawVerticalWall(x,y);
		if(member != null) {
			int i = 1;
			int nb = this.getNbMember();
			for(CrewMember m : this.member) {
				double x1 = this.tilePos.getX()  - (i * WIDTH/nb - WIDTH/(2*nb));
				double y1 = this.getCenterPosition().getY();
				m.draw(new Vector2<Double>(x1, y1), nb);
				i++;
			}
			
		}
		StdDraw.setPenColor(StdDraw.BLACK);
	}
	
	/**
	 * Draws a wall of the tile horizontally.
	 * @param x X start position
	 * @param y Y start position
	 */
	private void drawHorizontalWall(double x, double y) {
			StdDraw.line(x-0.005, y, x-0.015, y);
			StdDraw.line(x, y, x-0.005, y);
			StdDraw.line(x-0.015, y, x-0.02, y);
	}
	
	/**
	 * Draws a wall of the tile vertically.
	 * @param x X start position
	 * @param y Y start position
	 */
	private void drawVerticalWall(double x, double y) {
			StdDraw.line(x, y+0.005, x, y+0.015);
			StdDraw.line(x, y, x, y+0.005);
			StdDraw.line(x, y+0.015, x, y+0.02);
	}
	
	/**
	 * Draws the weapon of the tile horizontally.
	 * @param x X start position
	 * @param y Y start position
	 */
	private void drawWeaponHorizontal(double x, double y) {
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.filledRectangle(x+0.01, y-0.01, 0.01, 0.005);
		StdDraw.setPenColor(StdDraw.BLACK);
	}
	
	/**
	 * Draws the weapon of the tile vertically
	 * @param x X start position
	 * @param y Y start position
	 */
	private void drawWeaponVertical(double x, double y) {
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.filledRectangle(x-0.01, y+0.01, 0.005, 0.01);
		StdDraw.setPenColor(StdDraw.BLACK);
	}

	/**
	 * Assigns a weapon to the tile.
	 * @param w the weapon to assign
	 */
	public void setWeapon(Weapon w) {
		weapon = w;
	}

	/**
	 * Gives the assigned weapon.
	 * @return the weapon
	 */
	public Weapon getWeapon() {
		return weapon;
	}

	/**
	 * Gives the horizontal position of the weapon.
	 * @return the position
	 */
	private Vector2<Double> getWeaponHorizontalPosition() {
		return new Vector2<Double>(tilePos.getX()+0.01, tilePos.getY()-0.01);
	}
	
	/**
	 * Gives the vertical position of the weapon.
	 * @return the position
	 */
	private Vector2<Double> getWeaponVerticalPosition() {
		return new Vector2<Double>(tilePos.getX()-0.01, tilePos.getY()+0.01);
	}
	
	/**
	 * Gives the position of the weapon.
	 * @return the position
	 */
	public Vector2<Double> getWeaponPosition() {
		if (isPlayer)
			return getWeaponHorizontalPosition();
		else	
			return getWeaponVerticalPosition();
	}

	/**
	 * Gives the position of the tile.
	 * @return the position
	 */
	public Vector2<Double> getPosition() {
		return tilePos;
	}
	
	/**
	 * Gives the center position of the tile.
	 * @return the position
	 */
	public Vector2<Double> getCenterPosition() {
		return new Vector2<Double>(tilePos.getX()-0.01, tilePos.getY()-0.01);
	}
	
	/**
	 * Marks the tile as targeted.
	 */
	public void markTarget() {
		isAimed = true;
	}
	
	/**
	 * Unmarks the tile as targeted.
	 */
	public void unmarkTarget() {
		isAimed = false;
	}
	
	//////////////////////////
	//comparaison and traitement
	///////////////////////////

	
	/**
	 * get the closest right tile from t in the colection, or himself if they isn't
	 * @param tiles the collection
	 * @param t the initiale tile
	 * @return the closest right tile from t in the colection, or himself if they isn't
	 */
	public static Tile getRightTile(Collection<Tile> tiles, Tile t) {
		Tile retour = t;
		
		for(Tile tile : tiles) {
			if(tile != t && tile.getCenterPosition().getX() > t.getCenterPosition().getX()) {
				if(t != retour) {
					if(getHorizontalDistanceTile(t, tile) < getHorizontalDistanceTile(t, retour) || (
							getHorizontalDistanceTile(t, tile) == getHorizontalDistanceTile(t, retour) &&
							getVerticalDistanceTile(t, tile) < getVerticalDistanceTile(t, retour))) {
							retour = tile;
						
					}
				}else {
					retour = tile;
				}
			}
		}
		
		return retour;
	}
	
	/**
	 * get the closest left tile from t in the colection, or himself if they isn't
	 * @param tiles the collection
	 * @param t the initiale tile
	 * @return the closest left tile from t in the colection, or himself if they isn't
	 */
	public static Tile getLeftTile(Collection<Tile> tiles, Tile t) {
		Tile retour = t;
		
		for(Tile tile : tiles) {
			if(tile != t && tile.getCenterPosition().getX() < t.getCenterPosition().getX()) {
				if(t != retour) {
					if(getHorizontalDistanceTile(t, tile) < getHorizontalDistanceTile(t, retour) || (
							getHorizontalDistanceTile(t, tile) == getHorizontalDistanceTile(t, retour) &&
							getVerticalDistanceTile(t, tile) < getVerticalDistanceTile(t, retour))) {
							retour = tile;
						
					}
				}else {
					retour = tile;
				}
			}
		}
		
		return retour;
	}
	
	/**
	 * get the closest up tile from t in the colection, or himself if they isn't
	 * @param tiles the collection
	 * @param t the initiale tile
	 * @return the up right tile from t in the colection, or himself if they isn't
	 */
	public static Tile getUpTile(Collection<Tile> tiles, Tile t) {
		Tile retour = t;
		
		for(Tile tile : tiles) {
			if(tile != t && tile.getCenterPosition().getY() > t.getCenterPosition().getY()) {
				if(t != retour) {
					if(getVerticalDistanceTile(t, tile) < getVerticalDistanceTile(t, retour) || (
							getVerticalDistanceTile(t, tile) == getVerticalDistanceTile(t, retour) && 
							getHorizontalDistanceTile(t, tile) < getHorizontalDistanceTile(t, retour))) {
							retour = tile;
						
					}
				}else {
					retour = tile;
				}
			}
		}
		
		return retour;
	}
	
	/**
	 * get the closest down tile from t in the colection, or himself if they isn't
	 * @param tiles the collection
	 * @param t the initiale tile
	 * @return the closest down tile from t in the colection, or himself if they isn't
	 */
	public static Tile getDownTile(Collection<Tile> tiles, Tile t) {
		Tile retour = t;
		
		for(Tile tile : tiles) {
			if(tile != t && tile.getCenterPosition().getY() < t.getCenterPosition().getY()) {
				if(t != retour) {
					if(getVerticalDistanceTile(t, tile) < getVerticalDistanceTile(t, retour) || (
							getVerticalDistanceTile(t, tile) == getVerticalDistanceTile(t, retour) && 
							getHorizontalDistanceTile(t, tile) < getHorizontalDistanceTile(t, retour))) {
							retour = tile;
						
					}
				}else {
					retour = tile;
				}
			}
		}
		
		return retour;
	}
	
	
	/**
	 * Return the horizontale distance beetween two tile in tile
	 * @param tile1 the first tile
	 * @param tile2 the second tile
	 * @return the horizontale distance beetween two tile in tile
	 */
	private static int getHorizontalDistanceTile(Tile tile1, Tile tile2) {
		return (int) (Math.round(Math.abs(tile1.getCenterPosition().getX() - tile2.getCenterPosition().getX())/Tile.WIDTH));
	}
	/**
	 * Return the Vertical distance beetween two tile in tile
	 * @param tile1 the first tile
	 * @param tile2 the second tile
	 * @return the Vertical distance beetween two tile in tile
	 */
	private static int getVerticalDistanceTile(Tile tile1, Tile tile2) {
		return (int) (Math.round(Math.abs(tile1.getCenterPosition().getY() - tile2.getCenterPosition().getY())/Tile.HEIGHT));
	}
	@Override
	public String toString() {
		return "Tile : player : " + this.isPlayer +". membres : " + this.member;
	}
	
}
