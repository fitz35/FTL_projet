package weapon;

import display.StdDraw;
import display.Vector2;
import module.Module;

public class Ion extends Weapon {

	public class IonProjectile extends Projectile {

		protected IonProjectile(Vector2<Double> pos, Vector2<Double> dir) {
			super(0.01, 0.01);
			this.x = pos.getX();
			this.y = pos.getY();
			this.cSpeed = 0.6;
			this.xSpeed = dir.getX()*cSpeed;
			this.ySpeed = dir.getY()*cSpeed;
			this.color = StdDraw.BLUE;
			this.damage = shotDamage;

			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void applyEffect(Module tile) {
			tile.setDesactive(true);
		}
		
	}
	
	
	
	public Ion() {
		name = "Ion";
		requiredPower = 2;
		chargeTime = 7;
		shotDamage = 0;
		shotPerCharge = 5;
	}
	
	
	@Override
	public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
		return new IonProjectile(pos, dir);
	}

}
