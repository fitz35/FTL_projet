package main;

/**
 * Class representant un code cheat
 * @author clementL
 *
 */
public abstract class CodeCheat {
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
