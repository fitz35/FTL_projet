package weapon;

import java.util.Collection;
import java.util.LinkedList;

import display.StdDraw;
import display.Vector2;
import module.Module;
import module.WeaponControl;

public class Ion extends Weapon {

	public class IonProjectile extends Projectile {
		private double timeDesactivation; // the time the module is desactivate
		
		/**
		 * construct a ion projectiles
		 * @param pos the initiale position of the projectile
		 * @param dir the direction of the projectile
		 * @param timeDesactivation the time the module is desactivate
		 */
		protected IonProjectile(Vector2<Double> pos, Vector2<Double> dir, double timeDesactivation) {
			super(0.01, 0.01);
			this.x = pos.getX();
			this.y = pos.getY();
			this.cSpeed = 0.6;
			this.xSpeed = dir.getX()*cSpeed;
			this.ySpeed = dir.getY()*cSpeed;
			this.color = StdDraw.BLUE;
			this.damage = shotDamage;
			
			this.timeDesactivation = timeDesactivation;

			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void applyEffect(Module tile) {
			tile.setDesactive(true, this.timeDesactivation);
		}
		
	}
	
	private WeaponControl arsenal;
	
	/**
	 * construct a ion weapon
	 * @param arsenal the arsenal who provide the ion weapon
	 */
	public Ion(WeaponControl arsenal) {
		name = "Ion";
		requiredPower = 2;
		chargeTime = 7;
		shotDamage = 0;
		shotPerCharge = 5;
		this.arsenal = arsenal;
	}
	
	/**
	 * set the arsenal
	 * @param arsenal the arsenal to set
	 */
	public void setArsenal(WeaponControl arsenal) {
		this.arsenal = arsenal;
	}
	
	
	@Override
	public Collection<Projectile> shot(Vector2<Double> pos, Vector2<Double> dir) {
		Collection<Projectile> p = new LinkedList<Projectile>();
		p.add(new IonProjectile(pos, dir, arsenal.getAllocatedEnergy()));
		return p;
	}

}
