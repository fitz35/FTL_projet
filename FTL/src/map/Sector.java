package map;

import display.StdDraw;
import display.Vector2;
import main.Map;

/**
 * represente a sector on the map
 * @author clementL
 *
 */
public abstract class Sector {
	private String image; //the path of the picture
	private boolean visible; //if the sector is visible by the player
	
	
	
	
	
	public Sector(String path) {
		this.image = path;
		this.visible = false;
	}
	
	/**
	 * draw the sector at the location provided
	 * @param pos the position of the sector's middle
	 */
	public void draw(Vector2<Double> pos) {
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.rectangle(pos.getX(), pos.getY(), 1.0/Map.NB_SECTOR, 1.0/Map.NB_SECTOR);
		
		if(this.visible) {
			StdDraw.picture(pos.getX(), pos.getY(), this.image, 1.0/Map.NB_SECTOR - StdDraw.getPenRadius(), 
								1.0/Map.NB_SECTOR - StdDraw.getPenRadius());
		}else {
			StdDraw.setPenColor(StdDraw.GRAY);
			StdDraw.filledRectangle(pos.getX(), pos.getY(), 1.0/Map.NB_SECTOR - StdDraw.getPenRadius(), 1.0/Map.NB_SECTOR - StdDraw.getPenRadius());
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
	
	
	
	
	
	
	
	
}
