package ship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import module.Module;

public class IAv1 extends IA {

	public IAv1(Ship opponent, Ship player) {
		super(opponent, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
		evaluateAgressivity();
		long elapsedTime = System.currentTimeMillis();
		stepModule(elapsedTime - time);
		stepWeapon(elapsedTime - time);
		stepCrewMember(elapsedTime - time);
		this.time = elapsedTime;
	}

//////////////////////////////////////////////////////////////////
//usefull private function
//////////////////////////////////////////////////////////////////
//fonction de gestion des differentes capacit�es
	/**
	 * manage the module
	 * 
	 * @param long elapsedTime the time beetween the last call TODO
	 */
	private void stepModule(long elapsedTime) {
		removeEnergie();
		if (this.agressivity > 6) {
//on met toute l'energie dans les weapon
			while (this.opponent.addEnergy(1)) {
			}
		}
	}

	/**
	 * manage the weapon and the shot
	 * 
	 * @param long elapsedTime the time beetween the last call TODO
	 */
	private void stepWeapon(long elapsedTime) {
		this.opponent.activeWeapon(0);
		this.opponent.target = this.player.getFirstTile();
		this.opponent.shotWeapon(0);
	}

	/**
	 * manage the crew member
	 * 
	 * @param long elapsedTime the time beetween the last call TODO
	 */
	private void stepCrewMember(long elapsedTime) {
		ArrayList<Module> endomagedModule = (ArrayList<Module>) getEndomagedModule(this.opponent);
		
		Iterator<Module> it = endomagedModule.iterator();
		int i = 0;
		while(it.hasNext() && this.opponent.selectMember(i)) {
			Module m = it.next();
			this.opponent.crewTile = m;
			this.opponent.teleportCrewMember();
			
			this.opponent.unselectCrewMember();
			i++;
		}
		
		
	}

//evaluation de l'agressivit�
	/**
	 * evaluate agressivity of the ia
	 */
	private void evaluateAgressivity() {

	}

//stepModule
	/**
	 * remove all the energie from the module
	 */
	private void removeEnergie() {

	}
//stepMember
	/**
	 * get the damaged module from a ship
	 * @param Ship ship the ship
	 * @return the damaged module
	 */
	private static Collection<Module> getEndomagedModule(Ship ship) {
		Module[] m = ship.getModule();
		Collection<Module> retour = new ArrayList<Module>();
		for(Module module : m) {
			if(module.getAmountDamage() > 0) {
				retour.add(module);
			}
		}
		
		return retour;
	}
}