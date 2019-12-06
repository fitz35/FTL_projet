package weapon;

import java.util.Collection;
import java.util.LinkedList;

import display.StdDraw;
import display.Vector2;

public class Death extends Weapon {

    public class DeathProjectile extends Projectile {
        
        private int trilean;
        private boolean stock;

        protected DeathProjectile(Vector2<Double> pos, Vector2<Double> dir, int position) {
            super(0.01, 0.01);
            this.x = pos.getX();
            this.y = pos.getY();
            this.cSpeed = 0.25;
            this.xSpeed = dir.getX()*cSpeed;
            this.ySpeed = dir.getY()*cSpeed;
            this.color = StdDraw.RED;
            this.damage = (int) (1 + Math.random() * (shotDamage - 1)) ;
            this.trilean = position;
            this.stock = false;

            // TODO Auto-generated constructor stub
        }
            
        @Override
        public void step(double time) {
            super.step(time);
            double val = 5.20;
            if (!this.stock && this.x > 0.5) {
                switch(this.trilean) {
                    case 1: this.ySpeed = (this.xSpeed + val* this.ySpeed) * this.cSpeed;                //rotation de 45°
                            this.xSpeed = (val * this.xSpeed) * this.cSpeed;
                            break;
                            
                    case 3: this.ySpeed = (- this.xSpeed + val* this.ySpeed) * this.cSpeed;                //rotation de 45°
                            this.xSpeed = (val * this.xSpeed) * this.cSpeed;
                            break;
                }
                this.stock = true;
            }
        }
        
    }
    
    public Death() {
        name = "Death";
        requiredPower = 1;
        chargeTime = 2;
        shotDamage = 8;
        shotPerCharge = 1;
    }
    
    
    @Override
    public Collection<Projectile> shot(Vector2<Double> pos, Vector2<Double> dir) {
        Collection<Projectile> p = new LinkedList<Projectile>();
        p.add(new DeathProjectile(pos, dir,1));
        p.add(new DeathProjectile(pos, dir,2));
        p.add(new DeathProjectile(pos, dir,3));
        return p;
    }

}
