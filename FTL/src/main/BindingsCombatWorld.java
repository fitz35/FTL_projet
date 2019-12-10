package main;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;

import display.StdDraw;
import map.CombatWorld;
import weapon.DummyGun;
import weapon.Missile;

/**
 * The bindings class processes the key pressed by the player.
 */
public class BindingsCombatWorld {

	private CombatWorld w;		// The world on which the actions act
	private Collection<CodeCheat> cheatCode; //the cheatCode
	 
	/**
	 * Create the bindings
	 * @param w the world
	 */
	public BindingsCombatWorld(CombatWorld w) {
		this.w=w;
		
		this.cheatCode = new ArrayList<CodeCheat> ();
		int[] sequence = {KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_N};
		this.cheatCode.add(new CodeGetWeapon(sequence));
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
		if(key.getKeyCode() == KeyEvent.VK_1 && key.isShiftDown())
			w.player.removeEnergy(1);
		else if(key.getKeyCode() == KeyEvent.VK_1)
			w.player.addEnergy(1);
		
		else if(key.getKeyCode() == KeyEvent.VK_2 && key.isShiftDown())
			w.player.removeEnergy(2);
		else if(key.getKeyCode() == KeyEvent.VK_2)
			w.player.addEnergy(2);
		
		else if(key.getKeyCode() == KeyEvent.VK_3 && key.isShiftDown())
			w.player.removeEnergy(3);
		else if(key.getKeyCode() == KeyEvent.VK_3)
			w.player.addEnergy(3);
		
		else if(key.getKeyCode() == KeyEvent.VK_4 && key.isShiftDown())
			w.player.removeEnergy(4);
		else if(key.getKeyCode() == KeyEvent.VK_4)
			w.player.addEnergy(4);
		
		else if(key.getKeyCode() == KeyEvent.VK_5 && key.isShiftDown())
			w.player.removeEnergy(5);
		else if(key.getKeyCode() == KeyEvent.VK_5)
			w.player.addEnergy(5);
		
		
		// Weapon Management
		else if(key.getKeyCode() == KeyEvent.VK_A && key.isControlDown())
			w.player.shotWeapon(0);
		else if(key.getKeyCode() == KeyEvent.VK_A && key.isShiftDown())
			w.player.deactiveWeapon(0);
		else if(key.getKeyCode() == KeyEvent.VK_A)
			w.player.activeWeapon(0);
		
		else if(key.getKeyCode() == KeyEvent.VK_Z && key.isControlDown())
			w.player.shotWeapon(1);
		else if(key.getKeyCode() == KeyEvent.VK_Z && key.isShiftDown())
			w.player.deactiveWeapon(1);
		else if(key.getKeyCode() == KeyEvent.VK_Z)
			w.player.activeWeapon(1);
		
		else if(key.getKeyCode() == KeyEvent.VK_E && key.isControlDown())
			w.player.shotWeapon(2);
		else if(key.getKeyCode() == KeyEvent.VK_E && key.isShiftDown())
			w.player.deactiveWeapon(2);
		else if(key.getKeyCode() == KeyEvent.VK_E)
			w.player.activeWeapon(2);
		
		else if(key.getKeyCode() == KeyEvent.VK_R && key.isControlDown())
			w.player.shotWeapon(3);
		else if(key.getKeyCode() == KeyEvent.VK_R && key.isShiftDown())
			w.player.deactiveWeapon(3);
		else if(key.getKeyCode() == KeyEvent.VK_R)
			w.player.activeWeapon(3);
		
		else if(key.getKeyCode() == KeyEvent.VK_T && key.isControlDown())
			w.player.shotWeapon(4);
		else if(key.getKeyCode() == KeyEvent.VK_T && key.isShiftDown())
			w.player.deactiveWeapon(4);
		else if(key.getKeyCode() == KeyEvent.VK_T)
			w.player.activeWeapon(4);
		
		// Crew Management
		else if(key.getKeyCode() == KeyEvent.VK_Q && key.isShiftDown())
			w.player.unselectCrewMember();
		else if(key.getKeyCode() == KeyEvent.VK_Q)
			w.player.selectMember(0);
		
		else if(key.getKeyCode() == KeyEvent.VK_S && key.isShiftDown())
			w.player.unselectCrewMember();
		else if(key.getKeyCode() == KeyEvent.VK_S)
			w.player.selectMember(1);
		
		else if(key.getKeyCode() == KeyEvent.VK_D && key.isShiftDown())
			w.player.unselectCrewMember();
		else if(key.getKeyCode() == KeyEvent.VK_D)
			w.player.selectMember(2);
		
		else if(key.getKeyCode() == KeyEvent.VK_F && key.isShiftDown())
			w.player.unselectCrewMember();
		else if(key.getKeyCode() == KeyEvent.VK_F)
			w.player.selectMember(3);
		
		else if(key.getKeyCode() == KeyEvent.VK_W)
			w.player.chooseTeleporteTileLeft();
		else if(key.getKeyCode() == KeyEvent.VK_X)
			w.player.chooseTeleporteTileRight();
		else if(key.getKeyCode() == KeyEvent.VK_C)
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
	
	//cheat code
	/**
	 * Class representant un code cheat
	 * @author clementL
	 *
	 */
	public abstract class CodeCheat{
		
		protected int[] sequence;
		protected int i;
		
		protected CodeCheat (int[] sequence) {
			this.sequence = sequence;
			this.i = 0;
		}
		
		/**
		 * load a new key
		 * @param key the key to load
		 */
		protected final void newKey(int key) {
			if(key == this.sequence[this.i]) {
				this.i ++ ;
				if(this.i >= this.sequence.length) {
					this.toDo();
					this.i = 0;
				}
			}else {
				this.i = 0;
			}
		}
		
		/**
		 * the action to do if the code cheat is actif
		 */
		protected abstract void toDo();
	}
	
	
	private class CodeGetWeapon extends CodeCheat{

		protected CodeGetWeapon(int[] sequence) {
			super(sequence);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void toDo() {
			// TODO Auto-generated method stub
			CheatWeapon weapon = new CheatWeapon();
			w.player.addNewWeapon(weapon);
		}
		
		private class CheatWeapon extends Missile{
			public CheatWeapon() {
				name = "cheat";
				requiredPower = 0;
				chargeTime = 1;
				shotDamage = Integer.MAX_VALUE;
				shotPerCharge = 1;
				this.charge = Integer.MAX_VALUE;
			}
		}
		
	}
}
