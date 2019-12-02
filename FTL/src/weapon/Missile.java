package weapon;

import display.StdDraw;
import display.Vector2;

public class Missile extends Weapon {

	public class MissileProjectile extends Projectile {

		protected MissileProjectile(Vector2<Double> pos, Vector2<Double> dir) {
			super(0.01, 0.01);
			this.x = pos.getX();
			this.y = pos.getY();
			this.cSpeed = 0.6;
			this.xSpeed = dir.getX()*cSpeed;
			this.ySpeed = dir.getY()*cSpeed;
			this.color = StdDraw.GREEN;
			this.damage = shotDamage;

			// TODO Auto-generated constructor stub
		}
		
	}
	
	public Missile() {
		name = "Missile";
		requiredPower = 4;
		chargeTime = 7;
		shotDamage = 4;
		shotPerCharge = 1;
	}
	

	
	private static int charge = 3;
	

	
	@Override
	public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
		if (charge > 0) { 
			charge--;
			return new MissileProjectile(pos, dir);
			
		}
		else {
			return null;
		}
	}
	
	
	public int getMissile() {
		return charge;
	}

}
