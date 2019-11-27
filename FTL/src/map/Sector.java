package map;

import display.StdDraw;
import display.Vector2;
import main.Map;

/**
 * represente a sector on the map
 * @author clementL
 *
 */
public class Sector {

	//Sector type
	public static final String SECTOR_SHIP = "ship";
	public static final String SECTOR_RESSOURCE = "ressource";
	public static final String SECTOR_MARKET = "market";
	
	private String image; //the path of the picture
	private boolean visible; //if the sector is visible by the player
	private String type; //the type of the sector
	private boolean hasBeenVisited; // if the sector has been visited
	
	/**
	 * 
	 * @param path of the picture
	 * @param type of the sector
	 */
	public Sector(String path, String type) {
		this.image = path;
		this.visible = false;
		this.type = type;
		this.setHasBeenVisited(false);
	}
	
	/**
	 * draw the sector at the location provided
	 * @param pos the position of the sector's middle
	 */
	public void draw(Vector2<Double> pos) {
		if(this.hasBeenVisited)
			StdDraw.setPenColor(StdDraw.GREEN);
		else
			StdDraw.setPenColor(StdDraw.RED);
		StdDraw.rectangle(pos.getX(), pos.getY(), (1.0/Map.NB_SECTOR)/2 - StdDraw.getPenRadius()/2, 
												(1.0/Map.NB_SECTOR)/2 - StdDraw.getPenRadius()/2);
		
		if(this.visible) {
			StdDraw.picture(pos.getX(), pos.getY(), this.image, 1.0/Map.NB_SECTOR - StdDraw.getPenRadius(), 
								1.0/Map.NB_SECTOR - StdDraw.getPenRadius());
		}else {
			StdDraw.setPenColor(StdDraw.GRAY);
			StdDraw.filledRectangle(pos.getX(), pos.getY(), (1.0/Map.NB_SECTOR - StdDraw.getPenRadius())/2,
					(1.0/Map.NB_SECTOR - StdDraw.getPenRadius())/2);
		}
		
		StdDraw.setPenColor();
	}



	//////////////////
	//getter
	//////////////////
	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}



	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}



	/**
	 * set the visibility to the sector to true
	 */
	public void setVisible() {
		this.visible = true;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the hasBeenVisited
	 */
	public boolean isHasBeenVisited() {
		return hasBeenVisited;
	}

	/**
	 * @param hasBeenVisited the hasBeenVisited to set
	 */
	public void setHasBeenVisited(boolean hasBeenVisited) {
		this.hasBeenVisited = hasBeenVisited;
	}
	
	
}
