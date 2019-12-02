package module;

import display.Vector2;

public class Shield extends Module{
    
    private int cptNvCharge;                                    // compteur de nv de charge
    private static final double     TEMPS_DETRUIT_MAX = 2.0;      //temps de la destruction en seconde
    private static final double     TEMPS_DETRUIT_MIN = 1.5;      //temps de la destruction en seconde
    protected   double                timeDetruit;                // time elapsed during the last destruction

    /**
     * @param hudPos position at which to draw the HUD
     * @param tilePos position at which to draw the tile
     * @param isPlayer whether it belongs to the player
     * @param initialLevel initial amount of energy which it can provide
     */
    public Shield(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, int initialLevel) {
        super(hudPos, tilePos, isPlayer);
        // Indeed, this module cannot be destroyed and is 'really' placed in the ship
        name = "Shield";
        maxLevel = 8;
        currentLevel = initialLevel;
        allocatedEnergy = 0;
        amountDamage = 0;
        canBeManned = false;
        cptNvCharge = 0;
        
    }
    
    @Override
    public void stepDesactive (double elapsedTime) {
        super.stepDesactive(elapsedTime);
        System.out.println(cptNvCharge);
        if (getMaxNvCharge() != cptNvCharge) {
            
            if (getMaxNvCharge() == 1 +cptNvCharge  && allocatedEnergy%2 == 0) {
                if ((System.currentTimeMillis() - this.timeDetruit)/1000 > TEMPS_DETRUIT_MIN) {
                    cptNvCharge ++;
                }
            }
            else {
                if ((System.currentTimeMillis() - this.timeDetruit)/1000 > TEMPS_DETRUIT_MAX) {
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
    

}
