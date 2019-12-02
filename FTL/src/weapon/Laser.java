package weapon;

import display.StdDraw;
import display.Vector2;

public class Laser extends Weapon {

	public class LaserProjectile extends Projectile {

		protected LaserProjectile(Vector2<Double> pos, Vector2<Double> dir) {
			super(0.01, 0.01);
			this.x = pos.getX();
			this.y = pos.getY();
			this.cSpeed = 0.7;
			this.xSpeed = dir.getX()*cSpeed;
			this.ySpeed = dir.getY()*cSpeed;
			this.color = StdDraw.RED;
			this.damage = (int) (1 + Math.random() * (shotDamage - 1)) ;

			// TODO Auto-generated constructor stub
		}
		
	}
	
	public Laser() {
		name = "Laser";
		requiredPower = 2;
		chargeTime = 7;
		shotDamage = 4;
		shotPerCharge = 5;
	}
	
	
	@Override
	public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
		return new LaserProjectile(pos, dir);
	}

}
