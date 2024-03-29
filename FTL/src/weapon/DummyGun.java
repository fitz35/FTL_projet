package weapon;

import java.util.Collection;
import java.util.LinkedList;

import display.StdDraw;
import display.Vector2;

/**
 * A dummy gun is an example of a gun which shots dummy pojectiles
 */
public class DummyGun extends Weapon {
	
	/**
	 * A dummy projectile is an example of a projectile
	 */
	private class DummyGunProjectile extends Projectile {

		public DummyGunProjectile(Vector2<Double> pos, Vector2<Double> dir) {
			super(0.01, 0.01);
			this.x = pos.getX();
			this.y = pos.getY();
			this.cSpeed = 0.2;
			this.xSpeed = dir.getX()*cSpeed;
			this.ySpeed = dir.getY()*cSpeed;
			this.color = StdDraw.LIGHT_GRAY;
			this.damage = shotDamage;
		}
	}
	
	/**
	 * Creates a dummy gun
	 */
	public DummyGun() {
		name = "DummyGun";
		requiredPower = 1;
		chargeTime = 11;
		shotDamage = 2;
		shotPerCharge = 1;
	}

	/**
	 * Shots a dummy projectile
	 * @see weapon.Weapon#shot(display.Vector2, display.Vector2)
	 */
	@Override
	public Collection<Projectile> shot(Vector2<Double> pos, Vector2<Double> dir) {
		Collection<Projectile> p = new LinkedList<Projectile>();
		p.add(new DummyGunProjectile(pos, dir));
		return p;
	}

}
