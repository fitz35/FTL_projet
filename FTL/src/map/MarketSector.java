package map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import display.Button;
import display.Vector2;
import main.Player;
import ship.CrewMember;
import weapon.DummyGun;
import weapon.Weapon;

public class MarketSector extends Sector {
	private Player player;//the player of the game
	
	//what the market has
	private int reparation;
	private Collection<CrewMember> member;
	private Collection<Weapon> weapon;
	
	//conbstante
	public static final int AMMOUNT_REPA_MIN = 5;//the amount max of the reparation
	public static final int AMMOUNT_REPA_MAX = 8;//the amount max of the reparation
	
	
	//boutton
	private Button[] bouttons;
	
	/**
	 * represente a market sector
	 */
	public MarketSector(Player p) {
		super("ressources/images/coin.png", Sector.SECTOR_MARKET);
		// TODO Auto-generated constructor stub
		
		this.player = p;
		
		this.reparation = 0;
		this.member = new LinkedList<CrewMember>();
		this.weapon = new LinkedList<Weapon>();
		this.bouttons = new Button[3];
		
		//gen the thing in the market
		for(int i = 0 ; i < 3 ; i++) {
			double rand = Math.random();
			if(rand < 1/3) {//member
				this.member.add(this.genMember());
			}else if(rand < 2/3) {//weapon
				this.weapon.add(this.genWeapon());
			}else {//reparation
				this.reparation ++;
				
			}
		}
		
	}
	
	/**
	 * lunche and step the market
	 */
	public void lunch() {
		//construct the buttons
		this.genBoutton();
		
		this.drawWorld();
	}
	
	/**
	 * draw the market's world
	 */
	private void drawWorld () {
		this.player.drawHud();
		
		for(Button b : this.bouttons) {
			b.draw();
		}
		
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
	private static final Vector2<Double> DIMBUTON = new Vector2<Double>(0.8/3 - 0.02, 0.5); 
	
	/**
	 * get the position of a button with the index
	 * @param i the index
	 * @return the position of a button with the index
	 */
	private static final Vector2<Double> getPosButon(int i) {return new Vector2<Double>(0.1 + (DIMBUTON.getX())*i + 0.01*(i*2 + 1), 0.2);}
	
	/**
	 * construct the boutton if they arent
	 */
	private void genBoutton() {
		if(this.bouttons[0] == null) {
			int indexBout = 0;
			for(int i = 0 ; i < this.reparation ; i++) {
				this.bouttons.add(new RepaButton(getPosButon(indexBout), DIMBUTON));
				indexBout++;
			}
			for(int i = 0 ; i < this.weapon.size() ; i++) {
				this.bouttons.add(new WeaponButton(getPosButon(indexBout), DIMBUTON, i));
				indexBout++;
			}
			for(int i = 0 ; i < this.member.size() ; i++) {
				this.bouttons.add(new MemberButton(getPosButon(indexBout), DIMBUTON, i));
				indexBout++;
			}
		}
	}
	
	
	/**
	 * A WeaponButton is an implementation of a Button
	 * which activate the amelioration buy
	 */
	private class WeaponButton extends Button {
		
		private int weaponIndex;
		
		public WeaponButton(Vector2<Double> pos, Vector2<Double> dim, int weaponIndex) {
			super(pos, dim);
			this.weaponIndex = weaponIndex;
		}

		@Override
		protected void onLeftClick() {
			System.out.println("test");
		}

		@Override
		protected void onRightClick() {
		}

		@Override
		protected void onMiddleClick() {}
		
	}
	
	/**
	 * A MemberButton is an implementation of a Button
	 * which activate the amelioration buy
	 */
	private class MemberButton extends Button {
		
		private int memberIndex;
		
		public MemberButton(Vector2<Double> pos, Vector2<Double> dim, int memberIndex) {
			super(pos, dim);
			this.memberIndex = memberIndex;
		}

		@Override
		protected void onLeftClick() {
			System.out.println("test");
		}

		@Override
		protected void onRightClick() {
		}

		@Override
		protected void onMiddleClick() {}
		
	}
	
	/**
	 * A RepaButton is an implementation of a Button
	 * which activate the amelioration buy
	 */
	private class RepaButton extends Button {
		
		public RepaButton(Vector2<Double> pos, Vector2<Double> dim) {
			super(pos, dim);
		}

		@Override
		protected void onLeftClick() {
			System.out.println("test");
		}

		@Override
		protected void onRightClick() {
		}

		@Override
		protected void onMiddleClick() {}
		
	}
}
