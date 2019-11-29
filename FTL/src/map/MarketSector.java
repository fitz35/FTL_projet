package map;

import java.awt.Font;
import java.util.Collection;
import java.util.LinkedList;

import display.Button;
import display.StdDraw;
import display.Vector2;
import main.Player;
import ship.CrewMember;
import ship.Ship;
import weapon.DummyGun;
import weapon.Weapon;

public class MarketSector extends Sector {
	private Player player;//the player of the game
	
	//what the market has
	private int reparation;
	private Collection<Vendable> toSell;
	
	//constante
	public static final int COST_REPA = 2;//the amount max of the reparation
	
	//boutton
	private Button[] bouttons;
	
	//what the player has choosen
	private boolean[] choose;
	private boolean valide;
	private Button valideButton;
	
	/**
	 * represente a market sector
	 */
	public MarketSector(Player p) {
		super("ressources/images/coin.png", Sector.SECTOR_MARKET);
		// TODO Auto-generated constructor stub
		
		this.player = p;
		
		this.reparation = 0;
		this.toSell = new LinkedList<Vendable>();
		this.bouttons = new Button[3];
		this.choose = new boolean[3];
		this.valide = false;
		
		//gen the thing in the market and the choose tab
		for(int i = 0 ; i < 3 ; i++) {
			double rand = Math.random();
			if(rand < 1.0/3) {//member
				this.toSell.add(this.genMember());
			}else if(rand < 2.0/3) {//weapon
				this.toSell.add(this.genWeapon());
			}else {//reparation
				this.reparation ++;
			}
			
			this.choose[i] = false;
		}
		
	}
	
	/**
	 * lunche and step the market
	 */
	public void lunch() {
		//construct the buttons
		if(this.valide)
			this.validate();
		else {
			this.genBoutton();
			this.drawWorld();
		}
	}
	
	/**
	 * draw the market's world
	 */
	private void drawWorld () {
		this.player.drawHud();
		
		//StdDraw.setFont(new Font("sans serif", Font.BOLD, 20)); -> fait laguer
		StdDraw.setPenColor(StdDraw.BLACK);
		
		StdDraw.text(0.5, 0.90, "MARKET !");
		
		//StdDraw.setFont();
		//draw the buttons
		for(int i = 0 ; i < this.choose.length ; i++) {
			this.bouttons[i].draw();
			if(this.choose[i])
				StdDraw.setPenColor(StdDraw.GREEN);
			else
				StdDraw.setPenColor(StdDraw.RED);
			StdDraw.rectangle(getHalfPosButon(i).getX(), 
							  getHalfPosButon(i).getY(), 
							  HALFDIMBUTON.getX(), 
							  HALFDIMBUTON.getY());
			
			//text du boutton
			String textButon = "";
			String textPrix = "";
			if(i < this.toSell.size()) {//dans l'ordre de lecture apres validation
				Vendable v = ((LinkedList<Vendable>) this.toSell).get(i);
				textButon = v.getDisplayName();
				textPrix = "Prix : " + v.getPrice() + " coins.";
			}else {
				textButon = "Une reparation entre " + Ship.AMMOUNT_REPA_MIN + " et " + Ship.AMMOUNT_REPA_MAX + "!";
				textPrix = "Prix : " + COST_REPA + " coins.";
			}
			
			StdDraw.text(getHalfPosButon(i).getX(), getHalfPosButon(i).getY(), textButon);
			
			//text du prix
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.text(getHalfPosButon(i).getX(), getHalfPosButon(i).getY() + HALFDIMBUTON.getY() + 0.025,
					textPrix);
		}
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.filledRectangle(0.1, 0.1, 0.05, 0.05);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(0.1, 0.1, "valider !");
	}
	
	/**
	 * process the player validation in the market
	 */
	private void validate() {
		//destroy the button
		for(Button b : this.bouttons) {
			b.destroy();
		}
		this.bouttons = new Button[3];
		this.valideButton.destroy();
		this.valideButton = null;
		
		for(int i = 0 ; i < this.choose.length ; i++) {
			if(this.choose[i]) {
				if(i<this.toSell.size()) {
					Vendable v = ((LinkedList<Vendable>)this.toSell).get(i);
					if(v instanceof CrewMember) {
						this.player.getShip().addCrewMember((CrewMember)v); 
					}else if(v instanceof Weapon) {
						this.player.getShip().addNewWeapon((Weapon) v);
					}
					this.player.removeCoins(v.getPrice());
				}else {
					this.player.getShip().repareHull();
					this.player.removeCoins(COST_REPA);
				}
			}
		}
		this.player.setHasBeenMooved();
	}
	
	///////////////////////////////////////////////////////////////////////////
	//gen of the think
	/**
	 * gen a random new crewMember
	 * @return a new random crewmember
	 */
	private CrewMember genMember() {
		return new CrewMember("Market");
	}
	/**
	 * gen a random new crewMember
	 * @return a new random crewmember
	 */
	private Weapon genWeapon() {
		return new DummyGun();
	}
	
	/////////////////////////////////////////////////////////////////////////
	//boutton
	private static final Vector2<Double> HALFDIMBUTON = new Vector2<Double>((0.8/3 - 0.02)/2, 0.2/2); 
	
	/**
	 * get the position of a button with the index
	 * @param i the index
	 * @return the position of a button with the index
	 */
	private static final Vector2<Double> getHalfPosButon(int i) {
		return new Vector2<Double>(0.1 + (HALFDIMBUTON.getX() * 2)*((double)i + 0.5) + 0.01*(i*2 + 1), 
									0.2 + HALFDIMBUTON.getY());
		}
	
	/**
	 * construct the boutton if they arent
	 */
	private void genBoutton() {
		if(this.bouttons[0] == null) {
			for(int i = 0 ; i < 3 ; i++)
				this.bouttons[i] = new ChoosenButton(getHalfPosButon(i), HALFDIMBUTON, i);
		}
		if(this.valideButton == null) {
			this.valideButton = new ValidateButton(new Vector2<Double>(0.1, 0.1), 
					new Vector2<Double>(0.05, 0.05));
		}
	}
	
	
	/**
	 * A chosenButton is an implementation of a Button
	 * which activate the amelioration's buy of the given index
	 */
	private class ChoosenButton extends Button {
		
		private int choosenIndex;
		
		public ChoosenButton(Vector2<Double> pos, Vector2<Double> dim, int choosenIndex) {
			super(pos, dim);
			this.choosenIndex = choosenIndex;
		}

		@Override
		protected void onLeftClick() {
			choose[this.choosenIndex] = true;
		}

		@Override
		protected void onRightClick() {
			choose[this.choosenIndex] = false;
		}

		@Override
		protected void onMiddleClick() {}
		
	}
	
	/**
	 * A chosenButton is an implementation of a Button which validate the choice of the player
	 * @author clementL
	 *
	 */
	private class ValidateButton extends Button{

		public ValidateButton(Vector2<Double> pos, Vector2<Double> dim) {
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
