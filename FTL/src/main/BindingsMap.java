package main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;

import display.StdDraw;
import map.CombatWorld;

public class BindingsMap {
	private Map w;		// The map on which the actions act
	private CombatWorld cbtW; //the world of the combat
	private Collection<CodeCheat> cheatCode; //the cheatCode
	 
	
	/**
	 * Create the bindings
	 * @param w the world
	 */
	public BindingsMap(Map w, CombatWorld cbtWorld) {
		this.w=w;
		this.cbtW = cbtWorld;
		
		this.cheatCode = new ArrayList<CodeCheat> ();
		int[] sequence1 = {KeyEvent.VK_M, KeyEvent.VK_M, KeyEvent.VK_M};
		this.cheatCode.add(new CodeEarnInfinitMoney(sequence1));
		
		int[] sequence2 = {KeyEvent.VK_A, KeyEvent.VK_L, KeyEvent.VK_V};
		this.cheatCode.add(new CodeLevelUpEnemy(sequence2));
	}
	
	/**
	 * Processes the key pressed by the player.
	 * Escape kills the game.
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
		
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		else if (key.getKeyCode() == KeyEvent.VK_F1)
			w.setDrawPlayerHud();
		else if(key.getKeyCode() == KeyEvent.VK_UP)
			this.w.deplacePlayerVertical(true);
		else if(key.getKeyCode() == KeyEvent.VK_DOWN)
			this.w.deplacePlayerVertical(false);
		else if(key.getKeyCode() == KeyEvent.VK_LEFT)
			this.w.deplacePlayerHorizontale(false);
		else if(key.getKeyCode() == KeyEvent.VK_RIGHT)
			this.w.deplacePlayerHorizontale(true);
	}
	
	///////////////////////////////////////////////////////////
	//code cheat
	
	private class CodeEarnInfinitMoney extends CodeCheat{

		protected CodeEarnInfinitMoney(int[] sequence) {
			super(sequence);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void toDo() {
			// TODO Auto-generated method stub
			w.getPlayer().addCoins(5000);
		}
		
	}
	
	
	private class CodeLevelUpEnemy extends CodeCheat{

		protected CodeLevelUpEnemy(int[] sequence) {
			super(sequence);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void toDo() {
			// TODO Auto-generated method stub
			cbtW.addLevel(5);
		}
		
	}
	

}
