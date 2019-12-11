package weapon;

import java.util.Collection;
import java.util.LinkedList;

import display.StdDraw;
import display.Vector2;

public class Missile extends Weapon {

	public class MissileProjectile extends Projectile {

		public MissileProjectile(Vector2<Double> pos, Vector2<Double> dir) {
			super(0.01, 0.01);
			this.x = pos.getX();
			this.y = pos.getY();
			this.cSpeed = 0.5;
			this.xSpeed = dir.getX()*cSpeed;
			this.ySpeed = dir.getY()*cSpeed;
			this.color = StdDraw.GREEN;
			this.damage = shotDamage;

			// TODO Auto-generated constructor stub
		}
		
	}
	
	/**
	 * construct a missile
	 */
	public Missile() {
		name = "Missile";
		requiredPower = 4;
		chargeTime = 7;
		shotDamage = 4;
		shotPerCharge = 1;
	}
	

	
	protected int charge = 3;
	

	
	@Override
	public Collection<Projectile> shot(Vector2<Double> pos, Vector2<Double> dir) {
		if (charge > 0) { 
			charge--;
			Collection<Projectile> p = new LinkedList<Projectile>();
			p.add(new MissileProjectile(pos, dir));
			return p;
			
		}
		else {
			return null;
		}
	}
	
	/**
	 * get the number of the missile
	 * @return the number of missile
	 */
	public int getMissile() {
		return this.charge;
	}
	
	/**
	 * add a charge to the missile launch
	 */
	public void addMissile() {
		this.chargeTime ++;
	}

}
