package ship;

import display.StdDraw;
import display.Vector2;
import map.Vendable;

/**
 * A CrewMember is a character inside the ship.
 */
public class CrewMember implements Vendable{
	public static final String TYPE_NONE = "None";
	public static final String TYPE_MOTOR = "Motor";
	public static final String TYPE_SHIELD = "Shield";
	public static final String TYPE_WEAPONCONTROL = "WeaponControl";
	
	public static final String[] TYPE_POSSIBLE = {TYPE_MOTOR, TYPE_SHIELD, TYPE_WEAPONCONTROL,
									TYPE_NONE};
	
	private String 	name;		// The name of the crew member
	private boolean isSelected; // Whether he/she is selected
	private String type;        // the type of the crew member
	
	/**
	 * Creates a CrewMember.
	 * @param name the name the crew member
	 */
	public CrewMember(String name) {
		this(name, TYPE_POSSIBLE[(int) Math.round(Math.random() * (TYPE_POSSIBLE.length - 1))]);
	}
	
	/**
	 * Creates a CrewMember.
	 * @param name the name the crew member
	 */
	public CrewMember(String name, String type) {
		this.name = name;
		this.type = type;
		
		
	}

	/**
	 * Draws the CrewMember at the location provided.
	 * @param location where to draw the crew member
	 * @param nb the nb of member on the cell
	 */
	public void draw(Vector2<Double> location, int nb) {
		if (isSelected) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.circle(location.getX(), location.getY(), 0.006/nb);
		}
		setColorWithType();
		StdDraw.filledRectangle(location.getX(), location.getY(), 0.005/nb, 0.0025);
		if(this.isSelected)
			StdDraw.textLeft(0, 0.7, "Membre selectionné, type : " + this.type);
		
		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		StdDraw.filledRectangle(location.getX(), location.getY(), 0.0025/nb, 0.0025);
		StdDraw.setPenColor(StdDraw.BLACK);
	}
	
	/**
	 * set penColor with a specific color of the type
	 */
	private void setColorWithType() {
		switch(this.type) {
			case TYPE_NONE :
				StdDraw.setPenColor(StdDraw.BOOK_BLUE);
				break;
			case TYPE_MOTOR :
				StdDraw.setPenColor(StdDraw.GREEN);
				break;
			case TYPE_SHIELD :
				StdDraw.setPenColor(StdDraw.ORANGE);
				break;
			case TYPE_WEAPONCONTROL :
				StdDraw.setPenColor(StdDraw.PINK);
				break;
		}
	}
	
	
	/**
	 * Selects the crew member.
	 */
	public void select() {
		isSelected = true;
	}
	
	/**
	 * Unselects the crew member.
	 */
	public void unselect() {
		isSelected = false;
	}
	
	/**
	 * Gives the name of the crew member.
	 * @return the name of the crew member.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	@Override
	public int getPrice() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "Membre : '" + this.getName() + "' de type : " + this.getType();
	}
}
