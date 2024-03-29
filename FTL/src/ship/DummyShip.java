package ship;
import display.Vector2;
import module.Module;
import module.Reactor;
import module.Shield;
import module.Motor;
import module.WeaponControl;
import weapon.DummyGun;
//import weapon.Ion;
//import weapon.Laser;
//import weapon.Missile;
import weapon.Weapon;

public class DummyShip extends Ship {
	
	public DummyShip(boolean isPlayer, Vector2<Double> position) {
		// Creates the ship
		super(isPlayer, position);
		
		// Sets the characteristics of the ship.
		totalHull 		= 30;
		currentHull		= 30;
		modules = new Module[4];
		
		// Creates the tiles for the layout of the ship
		Tile front = new Tile(getNextTilePosition(), isPlayer);
		addTile(front);
		
		this.reactor = new Reactor(getNextTilePosition(),isPlayer, 8);
		addTile(reactor);
		this.motor = new Motor(getNextTilePosition(),isPlayer, 1);
		addTile(motor);
		
		
		
		
		this.shield = new Shield(getNextTilePosition(), new Vector2<Double>(0.25, 0.25),isPlayer, 1);
		addTile(shield);
		
		Tile mid = new Tile(getNextTilePosition(), isPlayer);
		addTile(mid);
		
		weaponControl = new WeaponControl(getNextTilePosition(), isPlayer, 1);
		addTile(weaponControl);
		
		Tile back = new Tile(getNextTilePosition(), isPlayer);
		addTile(back);
		
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
	
	private Vector2<Double> getNextTilePosition() {
		if (isPlayer)
			return new Vector2<Double>(position.getX()-(layout.size()*0.02), position.getY()); 
		else
			return new Vector2<Double>(position.getX(), position.getY()-(layout.size()*0.02));
	}

}