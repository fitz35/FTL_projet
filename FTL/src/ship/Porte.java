package ship;


import display.Button;
import display.StdDraw;
import display.Vector2;

/**
 * construct and manage the door beetween two tile
 * @author clementL
 *
 */
public class Porte {
	
	private Tile tile1;
	private Tile tile2;
	
	private boolean ouvert;  // if the door is open
	private double    timeOuvert; //when the door was oppened
	private Vector2<Double> pos;//the pos of the door
	private boolean isPlayer;   // weather the door is a players door
	
	//button
	private boolean valide; 
	private OuvertButton btn;
	
	//dessin
	private boolean orientation; // true : horizontal, false : vertical
	private static final double EPAISSEUR = 0.001; // the epaisseur of the door
	
	//time ouvert
	private static final double TEMPS_OUVERT = 5.0;// en seconde
	
	/**
	 * construct a door beetween two tile
	 * @param tile1 the first tile
	 * @param tile2 the second tile
	 */
	public Porte(Tile tile1, Tile tile2, boolean player) {
		this.tile1 = tile1;
		this.tile2 = tile2;
		
		this.ouvert = false;
		this.isPlayer = player;
		this.orientation = Tile.getHorizontalDistanceTile(tile1, tile2) == 0;
		this.pos = new Vector2<Double>(tile1.getCenterPosition().getX() + (tile2.getCenterPosition().getX() - tile1.getCenterPosition().getX())/2,
				tile1.getCenterPosition().getY() + (tile2.getCenterPosition().getY() - tile1.getCenterPosition().getY())/2);
		this.timeOuvert = 0;
		
		//boutton
		this.valide = false;
		if(this.isPlayer) {
			if(this.orientation)
				this.btn = new OuvertButton(pos, new Vector2<Double>(Tile.WIDTH/2, Tile.HEIGHT/2));
			else
				this.btn = new OuvertButton(pos, new Vector2<Double>(Tile.WIDTH/2 , Tile.HEIGHT/2));
		}else
			this.btn = null;
	}

	/**
	 * draw the door
	 */
	public void draw() {
		StdDraw.setPenColor(StdDraw.RED);
		if(ouvert) {
			
		}else {
			if(this.orientation)
				StdDraw.filledRectangle(pos.getX(), pos.getY(), Tile.WIDTH/2, EPAISSEUR);
			else
				StdDraw.filledRectangle(pos.getX(), pos.getY(), EPAISSEUR, Tile.HEIGHT/2);
		}
	}
	
	/**
	 * step the door
	 * @param time the time beetween the last call
	 */
	public void step(double time) {
		if(this.ouvert) {
			if(this.timeOuvert > TEMPS_OUVERT) {
				this.ouvert = false;
			}else {
				this.timeOuvert += time;
			}
		}else {
			if(this.valide) {
				this.ouvert = true;
				this.timeOuvert = 0;
			}
		}
		
		this.valide = false;
	}

	/**
	 * @return the ouvert
	 */
	public boolean isOuvert() {
		return ouvert;
	}
	/**
	 * @return the tile1
	 */
	public Tile getTile1() {
		return tile1;
	}

	/**
	 * @return the tile2
	 */
	public Tile getTile2() {
		return tile2;
	}
	/////////////////////////////////////////////////////////////
	/**
	 * this an implementation of a button to oppen the door
	 * @author clementL
	 *
	 */
	private class OuvertButton extends Button{
		/**
		 * construct the button
		 * @param pos the middle position of the button
		 * @param dim the halfDimension of the button
		 */
		public OuvertButton(Vector2<Double> pos, Vector2<Double> dim) {
			super(pos, dim);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onLeftClick() {
			// TODO Auto-generated method stub
			valide = true;
		}

		@Override
		protected void onRightClick() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void onMiddleClick() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
