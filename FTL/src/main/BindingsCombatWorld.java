package main;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import display.StdDraw;
import display.Vector2;
import map.CombatWorld;
import module.Module;
import weapon.Death;
import weapon.Ion;
import weapon.Laser;
import weapon.Missile;
import weapon.Projectile;
import weapon.Weapon;

/**
 * The bindings class processes the key pressed by the player.
 */
public class BindingsCombatWorld {

	private CombatWorld w;		// The world on which the actions act
	private Collection<CodeCheat> cheatCode; //the cheatCode
	
	//selection des evenement
	private int moduleSelect;
	private int weaponSelect;
	private int memberSelect;
	
	/**
	 * Create the bindings
	 * @param w the world
	 */
	public BindingsCombatWorld(CombatWorld w) {
		this.w=w;
		
		this.cheatCode = new ArrayList<CodeCheat> ();
		int[] sequence1 = {KeyEvent.VK_Y, KeyEvent.VK_Y, KeyEvent.VK_Y};
		this.cheatCode.add(new CodeLevelUpModule(sequence1));
		
		
		int[] sequence2 = {KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_C};
		this.cheatCode.add(new CodeAddWeapon(sequence2, new CheatWeapon()));
		
		int[] sequence3 = {KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_D};
		this.cheatCode.add(new CodeAddWeapon(sequence3, new Death()));
		
		int[] sequence4 = {KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_I};
		this.cheatCode.add(new CodeAddWeapon(sequence4, new Ion(w.player.getWeaponControl())));
		
		int[] sequence5 = {KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_L};
		this.cheatCode.add(new CodeAddWeapon(sequence5, new Laser()));
		
		int[] sequence6 = {KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_M};
		this.cheatCode.add(new CodeAddWeapon(sequence6, new Missile()));
		
		this.setModuleSelect(1);
		this.setMemberSelect(0);
		this.setWeaponSelect(0);
	}
	
	/**
	 * Processes the key pressed by the player.
	 * Escape kills the game.
	 * The keys from 1 to 5 acts on the modules.
	 * The keys from A to R acts on the weapons.
	 * The keys from Q to F acts on the crew.
	 * The arrows keys acts on the aiming system.
	 * 
	 * It processes only one key at the time but the
	 * keys are popped out of a stack which prevents
	 * key shadowing and key loss.
	 */
	public void processKey() {
		if (!StdDraw.hasNextKeyTyped())
			return;
		KeyEvent key = StdDraw.nextKeyTyped();
		
		///////////////////////////////////////
		//code cheat
		for(CodeCheat code : this.cheatCode)
			code.newKey(key.getKeyCode());
		
		//////////////////////////////////////
		//key
		
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		
		// Module Energy Management
		
		if(key.getKeyCode() == KeyEvent.VK_1)
			this.setModuleSelect(this.moduleSelect - 1);
		else if(key.getKeyCode() == KeyEvent.VK_2)
			this.setModuleSelect(this.moduleSelect + 1);
		else if(key.getKeyCode() == KeyEvent.VK_3 && key.isShiftDown())
			w.player.removeEnergy(this.moduleSelect);
		else if(key.getKeyCode() == KeyEvent.VK_3)
			w.player.addEnergy(this.moduleSelect);
		
		// Weapon Management
		
		else if(key.getKeyCode() == KeyEvent.VK_A)
			this.setWeaponSelect(this.weaponSelect - 1);
		else if(key.getKeyCode() == KeyEvent.VK_Z)
			this.setWeaponSelect(this.weaponSelect + 1);
		else if(key.getKeyCode() == KeyEvent.VK_E && key.isControlDown())
			w.player.shotWeapon(this.weaponSelect);
		else if(key.getKeyCode() == KeyEvent.VK_E && key.isShiftDown())
			w.player.deactiveWeapon(this.weaponSelect);
		else if(key.getKeyCode() == KeyEvent.VK_E)
			w.player.activeWeapon(this.weaponSelect);
		
		// Crew Management
		
		else if(key.getKeyCode() == KeyEvent.VK_Q)
			this.setMemberSelect(this.memberSelect - 1);
		else if(key.getKeyCode() == KeyEvent.VK_S)
			this.setMemberSelect(this.memberSelect + 1);
		
		else if(key.getKeyCode() == KeyEvent.VK_X)
			w.player.chooseTeleporteTileLeft();
		else if(key.getKeyCode() == KeyEvent.VK_V)
			w.player.chooseTeleporteTileRight();
		else if(key.getKeyCode() == KeyEvent.VK_F)
			w.player.chooseTeleporteTileUp();
		else if(key.getKeyCode() == KeyEvent.VK_C)
			w.player.chooseTeleporteTileDown();
		else if(key.getKeyCode() == KeyEvent.VK_G)
			w.player.teleportCrewMember();
		
		// Aiming System
		else if(key.getKeyCode() == KeyEvent.VK_UP)
			processArrowKey(KeyEvent.VK_UP);
		else if(key.getKeyCode() == KeyEvent.VK_DOWN)
			processArrowKey(KeyEvent.VK_DOWN);
		else if(key.getKeyCode() == KeyEvent.VK_LEFT)
			processArrowKey(KeyEvent.VK_LEFT);
		else if(key.getKeyCode() == KeyEvent.VK_RIGHT)
			processArrowKey(KeyEvent.VK_RIGHT);
	}

	/**
	 * Process the arrow keys.
	 * @param key the arrow key pressed
	 */
	private void processArrowKey(int key) {
		switch(key) {
			case(KeyEvent.VK_UP):
				w.player.aimUp(w.opponent);
				break;
			case(KeyEvent.VK_DOWN):
				w.player.aimDown(w.opponent);
				break;
			case(KeyEvent.VK_LEFT):
				w.player.aimLeft(w.opponent);
				break;
			case(KeyEvent.VK_RIGHT):
				w.player.aimRight(w.opponent);
				break;
		}
	}

	/**
	 * @param moduleSelect the moduleSelect to set
	 */
	private void setModuleSelect(int moduleSelect) {
		w.player.getModule()[this.moduleSelect].setSelected(false);
		this.moduleSelect = moduleSelect;
		if(this.moduleSelect < 1)
			this.moduleSelect = 1;
		else if(this.moduleSelect > w.player.getModule().length - 1)
			this.moduleSelect = w.player.getModule().length - 1;
		w.player.getModule()[this.moduleSelect].setSelected(true);
	}

	/**
	 * @param weaponSelect the weaponSelect to set
	 */
	private void setWeaponSelect(int weaponSelect) {
		this.weaponSelect = weaponSelect;
		if(this.weaponSelect < 0)
			this.weaponSelect = 0;
		else if(this.weaponSelect > w.player.getNbWeapon() - 1)
			this.weaponSelect = w.player.getNbWeapon() - 1;
		
		w.player.setSelectedWeapon(this.weaponSelect);
	}

	/**
	 * @param memberSelect the memberSelect to set
	 */
	private void setMemberSelect(int memberSelect) {
		w.player.unselectCrewMember();
		this.memberSelect = memberSelect;
		if(this.memberSelect < 0)
			this.memberSelect = 0;
		else if(this.memberSelect > w.player.getNbMember() - 1)
			this.memberSelect = w.player.getNbMember() - 1;
		w.player.selectMember(this.memberSelect);
	}

	
	/////////////////////////////////////////////////////////////////////////////
	//cheat code
	/////////////////////////////////////////////////////////////////////////////
	/**
	 * weapon one shot
	 * @author clementL
	 *
	 */
	private class CheatWeapon extends Missile{
		public CheatWeapon() {
			name = "cheat";
			requiredPower = 0;
			chargeTime = 1;
			shotDamage = Integer.MAX_VALUE;
			shotPerCharge = 1;
			this.charge = -1;
		}
		
		@Override
		public Collection<Projectile> shot(Vector2<Double> pos, Vector2<Double> dir) {
			Collection<Projectile> p = new LinkedList<Projectile>();
			p.add(new MissileProjectile(pos, dir));
			return p;
		}
		
	}
	
	/**
	 * level up all the module
	 * @author clementL
	 *
	 */
	private class CodeLevelUpModule extends CodeCheat{

		protected CodeLevelUpModule(int[] sequence) {
			super(sequence);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void toDo() {
			// TODO Auto-generated method stub
			Module[] modules = w.player.getModule();
			for(Module m : modules) {
				while(m.addLevel()) {}
			}
		}
	}
	
	/**
	 * add a weapon to the inventory of the player
	 * @author clementL
	 *
	 */
	private class CodeAddWeapon extends CodeCheat{
		private Weapon weapon;
		
		protected CodeAddWeapon(int[] sequence, Weapon w) {
			super(sequence);
			// TODO Auto-generated constructor stub
			this.weapon = w;
		}

		@Override
		protected void toDo() {
			// TODO Auto-generated method stub
			w.player.addNewWeapon(weapon);
		}
		
	}
}
