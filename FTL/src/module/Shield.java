package module;

import display.StdDraw;
import display.Vector2;
import weapon.Projectile;

public class Shield extends Module{
    
    private int cptNvCharge;                                    // compteur de nv de charge
    private static final double     TEMPS_DETRUIT_MAX = 2.0;      //temps de la destruction en seconde
    private static final double     TEMPS_DETRUIT_MIN = 1.5;      //temps de la destruction en seconde
    protected   double                timeDetruit;                // time elapsed during the last destruction
    protected Vector2<Double>       taille; //taille du bouclier

    /**
     * @param hudPos position at which to draw the HUD
     * @param tilePos position at which to draw the tile
     * @param taille taille of the shield arround the ship
     * @param isPlayer whether it belongs to the player
     * @param initialLevel initial amount of energy which it can provide
     */
    public Shield(Vector2<Double> hudPos, Vector2<Double> tilePos, Vector2<Double> taille, boolean isPlayer, int initialLevel) {
        super(hudPos, tilePos, isPlayer);
        // Indeed, this module cannot be destroyed and is 'really' placed in the ship
        name = "Shield";
        maxLevel = 8;
        currentLevel = initialLevel;
        allocatedEnergy = 0;
        amountDamage = 0;
        canBeManned = false;
        cptNvCharge = 0;
        this.taille = taille;
    }
    
    /**
     * draw the module tile and the shield
     * @note shield v2
     */
    @Override
    public void draw() {
    	super.draw();
    	
    	if(!( this.cantProtect ())) {
    		if(this.cptNvCharge > (this.getMaxNvCharge() * 75.0)/100.0)
    			StdDraw.setPenColor(StdDraw.BLUE);
    		else if(this.cptNvCharge > (this.getMaxNvCharge() * 50.0)/100.0)
    			StdDraw.setPenColor(StdDraw.BOOK_BLUE);
    		else if(this.cptNvCharge > (this.getMaxNvCharge() * 25.0)/100.0)
    			StdDraw.setPenColor(StdDraw.ORANGE);
    		else
    			StdDraw.setPenColor(StdDraw.RED);
    		StdDraw.rectangle(this.getPosition().getX(), this.getPosition().getY(), this.taille.getX()/2, this.taille.getY()/2);
    	}
    }
    
    @Override
    public void stepDesactive (double elapsedTime) {
        super.stepDesactive(elapsedTime);
        if (getMaxNvCharge() != cptNvCharge && !this.isDesactive()) {
            
            if (getMaxNvCharge() == 1 +cptNvCharge  && allocatedEnergy%2 == 0) {
                if ((System.currentTimeMillis() - this.timeDetruit)/1000 > TEMPS_DETRUIT_MIN * (1 - 0.05 * this.getNbMemberBonus())) {
                    cptNvCharge ++;
                }
            }
            else {
                if ((System.currentTimeMillis() - this.timeDetruit)/1000 > TEMPS_DETRUIT_MAX * (1 - 0.05 * this.getNbMemberBonus())) {
                    cptNvCharge ++;
                }
            }
        }
        
    }
    

    @Override
    public boolean removeEnergy() {
        boolean retour = super.removeEnergy();
        
        if (this.cptNvCharge > getMaxNvCharge()) {
            this.cptNvCharge = getMaxNvCharge();
        }
        
        return retour;
    }
    
    @Override
    public boolean addEnergy() {
    	boolean retour = super.addEnergy();
    	
    	 if (this.cptNvCharge < getMaxNvCharge()) {
             this.timeDetruit = System.currentTimeMillis();
         }
    	
    	
		return retour;
    }
    
    /**
     * 
     * @return le niveau max de la charge par rapport au nv d'energi eactuelle
     */
    private int getMaxNvCharge () {
        if (this.allocatedEnergy%2 == 0) {
             return this.allocatedEnergy/2;
        }
        else {
            return (this.allocatedEnergy/2)+1;
        }
    }
    
    /**
     * reduce the charge of the shield if its possible
     */
    public void reduceCharge () {
        if (cptNvCharge > 0) {
            this.timeDetruit = System.currentTimeMillis();
            cptNvCharge --;
        }
    }
    
    /**
     * 
     * @return true if can't protect
     */
    public boolean cantProtect () {
        return isDesactive() || cptNvCharge==0;
                
    }
    
    /**
     * test if a projectile is affected by the shield
     * @param p the projectile
     * @return  if a projectile is affected by the shield
     */
    public boolean isTouche(Projectile p) {
    	return !p.isOutOfRectangle(this.getPosition().getX(), this.getPosition().getY(), this.taille.getX()/2, this.taille.getY()/2);
    }
    
}
