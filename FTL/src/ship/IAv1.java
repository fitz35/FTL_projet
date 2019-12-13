package ship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import main.Start;
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
//fonction de gestion des differentes capacitées
	/**
	 * manage the module
	 * 
	 * @param long elapsedTime the time beetween the last call TODO
	 */
	private void stepModule(long elapsedTime) {
		if (this.agressivity > 6) {
			//on met toute l'energie dans les weapon
			while (this.opponent.addEnergy(1)) {
			}
			//puis dans le bouclier
			while (this.opponent.addEnergy(3)) {
			}
			//puis dans le moteur
			while (this.opponent.addEnergy(2)) {
			}
		}
	}

	/**
	 * manage the weapon and the shot
	 * 
	 * @param long elapsedTime the time beetween the last call TODO
	 */
	private void stepWeapon(long elapsedTime) {
		if(this.opponent.weaponControl.canActiveWeapon()) {//active les weapon au hasard
			ArrayList<Integer> iAlea = new ArrayList<Integer>();
			for(int i = 0 ; i < this.opponent.getNbWeapon() ; i++) {
				iAlea.add(i);
			}
			int i = Start.getRandomInt(0, iAlea.size() - 1);
			while(!iAlea.isEmpty() && this.opponent.activeWeapon(iAlea.get(i))) {
				iAlea.remove(i);
				i = Start.getRandomInt(0, iAlea.size() - 1);
			}
		}
		this.opponent.target = ((ArrayList<Tile>)this.player.layout).get(Start.getRandomInt(0, this.player.layout.size() - 1));
		for(int i = 0 ; i < this.opponent.getNbWeapon() ; i++)//tire avec tous les weapon
			this.opponent.shotWeapon(i);
	}

	/**
	 * manage the crew member
	 * 
	 * @param long elapsedTime the time beetween the last call TODO
	 */
	private void stepCrewMember(long elapsedTime) {
		//on se focus sur les modules endomager
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

//evaluation de l'agressivité
	/**
	 * evaluate agressivity of the ia
	 */
	private void evaluateAgressivity() {

	}

//stepModule
	
	
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
	
	/**
	 * get the member from the collections who has the type tye 
	 * @param members the members to test
	 * @param type the type search
	 * @return he member from the collections who has the type tye 
	 */
	private static Collection<CrewMember> getMemberFromModule(ArrayList<CrewMember> members, String type) {
		ArrayList<CrewMember> retour = new ArrayList<CrewMember>();
		for(CrewMember m : members) {
			if(m.getType().compareTo(type) == 0)
				retour.add(m);
		}
		
		return retour;
	}
}
