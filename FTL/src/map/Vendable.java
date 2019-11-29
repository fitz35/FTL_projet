package map;

/**
 * set needed function to be sell in the market
 * @author clementL
 *
 */
public interface Vendable {
	/**
	 * get the price of the object
	 * @return the price of the object
	 */
	public int getPrice();
	
	/**
	 * get the display name of the object
	 * @return the display name of the object
	 */
	public String getDisplayName();
	
}
