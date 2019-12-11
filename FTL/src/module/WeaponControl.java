package module;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import display.Button;
import display.StdDraw;
import display.Vector2;
import main.Start;
import ship.Tile;
import weapon.Ion;
import weapon.Missile;
import weapon.Projectile;
import weapon.Weapon;

/**
 * A WeaponControl is a Module which handles weapons energy and activation.
 * This module has a specific HUD to display the weapons along with buttons
 * to interact with them.
 */
public class WeaponControl extends Module {
    
    private Collection<Weapon>                weapons;    // The weapon slots
    private Collection<WeaponButton>        weaponBtns;    // The button linked to the weapon slot
    
    /**
     * A WeaponButton is an implementation of a Button
     * which activates/deactivates the linked weapon when
     * left/right clicked.
     */
    private class WeaponButton extends Button {
        
        private int weaponIndex;
        
        public WeaponButton(Vector2<Double> pos, Vector2<Double> dim, int weaponIndex) {
            super(pos, dim);
            this.weaponIndex = weaponIndex;
        }

        @Override
        protected void onLeftClick() {
            activeWeapon(weaponIndex);
        }

        @Override
        protected void onRightClick() {
            deactiveWeapon(weaponIndex);
        }

        @Override
        protected void onMiddleClick() {}
        
    }
    
    /**
     * Construct a WeaponControl owned by the player or the opponent.
     * The WeaponControl's tile is drawn at the location given in tilePos.
     * The WeaponControl's HUD is drawn at the location given in hudPos.
     * The initialLevel of the WeaponControl is the amount of energy it
     * can allocated when created.
     * The amountWeapons defines the size of the weapon inventory.
     * @param tilePos position at which to draw the tile
     * @param isPlayer whether it belongs to the player
     * @param initialLevel initial amount of energy which can be allocatedy
     */
    public WeaponControl(Vector2<Double> tilePos, boolean isPlayer, int initialLevel) {
        super(tilePos, isPlayer);
        name = "Weapons";
        maxLevel = 8;
        currentLevel = initialLevel;
        allocatedEnergy = 0;
        amountDamage = 0;
        canBeManned = true;
        weapons = new ArrayList<Weapon>();
        weaponBtns = new ArrayList<WeaponButton>();
    }
    
    /**
     * Adds a weapon in the weapon inventory.
     * @param w the weapon to add 
     * @return whether the weapon has been added
     */
    public boolean addWeapon(Weapon w) {
        weapons.add(w);
        return true;
    }
    
    /**
     * Activates the weapon.
     * @param weapon the index in the inventory of the weapon
     * @return whether the weapon has been activated
     */
    public boolean activeWeapon(int weapon) {
        if (((ArrayList<Weapon>)weapons).get(weapon) == null)
            return false;
        int energy = 0;
        for (Weapon w : weapons)
            if (w != null)
                energy += w.isActivated() ? w.getRequiredPower() : 0;
        Weapon w = ((ArrayList<Weapon>)weapons).get(weapon);
        if (allocatedEnergy-amountDamage < energy + w.getRequiredPower())
            return false;
        w.activate();
        return true;
    }
    
    /**
     * Deactivates the weapon.
     * @param weapon the index in the inventory of the weapon
     */
    public void deactiveWeapon(int weapon) {
        ((ArrayList<Weapon>)weapons).get(weapon).deactive();
    }
    
    /**
     * Gives the weapon of the inventory
     * @param index location of the weapon in the inventory
     * @return the weapon at location index
     */
    public Weapon getWeapon(int index) {
        return ((ArrayList<Weapon>)weapons).get(index);
    }
    
    /**
     * return the size of the inventory of the weapons
     * @return the size of the inventory of the weapons
     */
    public int getNbWeapon() {
    	return this.weapons.size();
    }
    
    /**
	 * return if the WeaponControl has not all the weapon
	 * @return if the WeaponControl has not all the weapon
	 */
    public boolean canGainNewWeapon() {
    	ArrayList<Weapon> wPossible = Start.getWeaponPossible();
		
		for(Weapon w : wPossible) {
			if(!this.hasWeapon(w)) {
				return true;
			}
		}
		
		return false;
    }
    
    /**
     * return if the weaponcontrol contain a weapon of the same instance
     * @param w the weapon to test
     * @return if the weaponcontrol contain a weapon of the same instance
     */
    public boolean hasWeapon(Weapon w) {
    	for(Weapon wPos : this.weapons){
    		if(w.getName().compareTo(wPos.getName()) == 0) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * add a random weapon no own
     */
    public void addWeaponAleatoire() {
		LinkedList<Integer> indexWeaponPossible = new LinkedList<Integer>();
		int i = 0;
		ArrayList<Weapon> weaponPossible = Start.getWeaponPossible();
		for(Weapon w : weaponPossible) {
			if(!this.hasWeapon(w)) {
				indexWeaponPossible.add(i);
			}
			i++;
		}
		int aleaInt = indexWeaponPossible.get(Start.getRandomInt(0, indexWeaponPossible.size() - 1));
		
		this.weapons.add(weaponPossible.get(aleaInt));
		if(weaponPossible.get(aleaInt) instanceof Ion) {
			((Ion) weaponPossible.get(aleaInt)).setArsenal(this);
		}
	}
    
    /**
     * Charges the weapon.
     * @param time the charging time to increase the weapon's charge by
     */
    public void chargeTime(double time) {
        for (Weapon w : weapons)
            if (w != null) {
                if (w.isActivated())
                    w.charge(time);
                else
                    w.charge(-3*time);
            }
    }
    
    /**
     * Draws the weapon inventory and the weapon in it as well
     * as their charging time.
     */
    @Override
    public void drawHud(int index) {
    	super.drawHud(index);
        double x = 0.01;
        double y = 0.02;
        double y_offset = 0.03;
        StdDraw.rectangle(x+0.05+(0.05*weapons.size()), y + y_offset, (0.05*weapons.size()), 0.04);
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = ((ArrayList<Weapon>)weapons).get(i);
            if (w == null)
                continue;
            
            StdDraw.setPenColor(StdDraw.GRAY);
            if (w.getCurrentCharge() == w.getChargeTime())
                StdDraw.setPenColor(StdDraw.YELLOW);
            StdDraw.filledRectangle(x+0.1+(0.09*i), y + y_offset, 0.045, (w.getCurrentCharge()/w.getChargeTime())*0.035);
            if (w.isActivated())
                StdDraw.setPenColor(StdDraw.CYAN);
            if (weaponBtns.isEmpty() || weaponBtns.size()<=i)
                weaponBtns.add( new WeaponButton(new Vector2<Double>(x+0.1+(0.09*i), y + y_offset), new Vector2<Double>(0.045, 0.035), i));
            else
                ((ArrayList<WeaponButton>)weaponBtns).get(i).draw();
            StdDraw.rectangle(x+0.1+(0.09*i), y + y_offset, 0.045, 0.035);
            StdDraw.setPenColor(StdDraw.BLACK);
            if(w instanceof Missile) {
                StdDraw.text(x+0.1+(0.09*i), y + y_offset + 0.01, w.getName());
                StdDraw.text(x+0.1+(0.09*i), y + y_offset - 0.01, "(" + 
                		(((Missile)w).getMissile() == -1 ? "infinis" : ((Missile)w).getMissile())
                		+ " missiles)");
            }else {
                StdDraw.text(x+0.1+(0.09*i), y + y_offset, w.getName());
            }
        }
    }
    
    /**
     * Shots the weapon from the tile in the direction provided.
     * @param weapon the weapon to shot
     * @param tile the tile to shot it from
     * @param dir the direction in which to shot it
     * @return the projectiles created by the weapon
     */
    public Collection<Projectile> shotWeapon(int weapon, Tile tile, Vector2<Double> dir) {
        if (((ArrayList<Weapon>)weapons).get(weapon) == null || !((ArrayList<Weapon>)weapons).get(weapon).isCharged())
            return null;
        Vector2<Double> v = tile.getPosition();
        ((ArrayList<Weapon>)weapons).get(weapon).resetCharge();
        Collection<Projectile> ps = ((ArrayList<Weapon>)weapons).get(weapon).shot(new Vector2<Double>(v.getX(), v.getY()), dir);
        for(Projectile p : ps)
        	p.addBonus(1 + 0.05 * this.getNbMemberBonus()); 
        return ps;
    }
    
    /**
     * Removes energy for the WeaponControl and deactivates
     * weapons if energy is not sufficient anymore. 
     */
    @Override
    public boolean removeEnergy() {
        if (allocatedEnergy > 0) {
            --allocatedEnergy;
            int energy = 0;
            for (Weapon w : weapons)
                if (w != null)
                    energy += w.isActivated() ? w.getRequiredPower() : 0;
            Weapon last = null;
            if (energy > allocatedEnergy)
                for (Weapon w : weapons)
                    if (w != null && w.isActivated())
                        last = w;
            if (last != null)
                last.deactive();
            return true;
        }
        return false;
    }
    
    /**
     * damage the module for the WeaponControl and deactivates
     * weapons if energy is not sufficient anymore.
     */
    @Override
    public void appliqueDmg(int dmg) {
        super.appliqueDmg(dmg);
        for(Weapon w : this.weapons) {
            if(w != null) {
                if(this.currentLevel - this.amountDamage < w.getRequiredPower())//on desactive si on n'a pas assez d'energie apres les damage
                    w.deactive();
            }
        }
    }
    
    /**
     * add a missile to the missile weapon
     */
    public void addMissile() {
        for(Weapon w : this.weapons) {
            if(w.getName().compareTo("Missile") == 0) {
                ((Missile) w ).addMissile();
                return;
            }
                
        }
        return;
    }
    
    /**
     * get the nb of missile 
     * @return the nb of missile 
     */
    public int getNbMissile() {
        for(Weapon w : this.weapons) {
            if(w.getName().compareTo("Missile") == 0) {
                return ((Missile) w ).getMissile();
            }
                
        }
        return 0;
    }
    
}

