package ship;

import display.Vector2;
import module.Module;
import module.Motor;
import module.Reactor;
import module.Shield;
import module.WeaponControl;
import weapon.DummyGun;
import weapon.Weapon;

/**
 * construct a tri fighter
 * @author clementL
 *
 */
public class TriFighter extends Ship {
	//pour le layout
	private int iGauche;
	private int iDroite;
	private int iMiddle;
	

	public TriFighter(boolean isPlayer, Vector2<Double> position) {
		super(isPlayer, position);
		// TODO Auto-generated constructor stub
		totalHull 		= 30;
		currentHull		= 30;
		modules = new Module[4];
		
		this.iGauche = 0;
		this.iDroite = 0;
		this.iMiddle = 0;
		
		//tile et module
		this.motor = new Motor(new Vector2<Double>(this.position.getX(), this.position.getY() + Tile.HEIGHT),isPlayer, 1);
		addTile(motor);
		
		this.shield = new Shield(new Vector2<Double>(this.position.getX(), this.position.getY() - Tile.HEIGHT), 
							new Vector2<Double>(0.25, 0.25),isPlayer, 1);
		addTile(shield);
		
		weaponControl = new WeaponControl(this.constructNextMiddle(), isPlayer, 1);
		addTile(weaponControl);
		
		this.reactor = new Reactor(this.constructNextMiddle(),isPlayer, 8);
		addTile(reactor);
		
		Tile front = null;
		int taille = 4;
		for( ; this.iMiddle <= taille ; ) {
			front = new Tile(this.constructNextMiddle(), this.isPlayer);
			addTile(front);
		}
		
		for( ; this.iDroite <= taille ; ) {
			addTile(new Tile(this.constructNextRight(), this.isPlayer));
		}
		
		for( ; this.iGauche <= taille ; ) {
			addTile(new Tile(this.constructNextGauche(), this.isPlayer));
		}
		
		
		// Assigns the modules
		modules[0] = reactor;
		modules[1] = weaponControl;
		modules[2] = motor;
		modules[3] = shield;
		
		// Creates the gun of the ship
		Weapon w = new DummyGun();
		/*Weapon x = new Laser();
		Weapon z = new Ion(weaponControl);
		Weapon y = new Missile();*/
		// Assigns the gun to the weapon control
		weaponControl.addWeapon(w);
		/*weaponControl.addWeapon(x);
		weaponControl.addWeapon(z);
		weaponControl.addWeapon(y);*/
		
		// Places the weapon at the front
		front.setWeapon(w);
		// Adds a crew member to the ship
		addCrewMember(new CrewMember("Jose"));
	}

	
	/**
	 * construct the left tiles
	 * @return the position of the next left tile
	 */
	private Vector2<Double> constructNextGauche() {
		iGauche++;
		if(this.isPlayer)
			return new Vector2<Double>(this.position.getX() + (iGauche - 1) * Tile.WIDTH, this.position.getY() + 2 * Tile.HEIGHT);
		else
			return new Vector2<Double>(this.position.getX() - (iGauche - 1) * Tile.WIDTH, this.position.getY() - 2 * Tile.HEIGHT);
	}
	
	/**
	 * construct the right tiles
	 * @return the position of the next right tile
	 */
	private Vector2<Double> constructNextRight() {
		iDroite++;
		if(this.isPlayer)
			return new Vector2<Double>(this.position.getX() + (iDroite - 1) * Tile.WIDTH, this.position.getY() - 2 * Tile.HEIGHT);
		else
			return new Vector2<Double>(this.position.getX() - (iDroite - 1) * Tile.WIDTH, this.position.getY() + 2 * Tile.HEIGHT);
	}
	
	/**
	 * construct the middle tiles
	 * @return the position of the next middle tile
	 */
	private Vector2<Double> constructNextMiddle() {
		iMiddle++;
		if(this.isPlayer)
			return new Vector2<Double>(this.position.getX() + (iMiddle - 1) * Tile.WIDTH, this.position.getY());
		else
			return new Vector2<Double>(this.position.getX() - (iMiddle - 1) * Tile.WIDTH, this.position.getY());
	}
}
