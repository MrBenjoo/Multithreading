package pool;

/**
 * 
 * @author Benjamin Sejdic
 *
 */
public interface IEnter {

	/**
	 * Increase amount of customers in the waiting queue for the adventure pool
	 * 
	 * @param nbrOfCustomers
	 *            number of customers waiting
	 */
	public void incAdvQueue(int nbrOfCustomers);

	/**
	 * Increase amount of customers in the waiting queue for the comoon pool
	 * 
	 * @param nbrOfCustomers
	 *            number of customers waiting
	 */
	public void incComQueue(int nbrOfCustomers);

	/**
	 * Update text for number of customers that left both pools
	 * 
	 * @param advExit
	 *            number of customers who exited adventure pool
	 * @param comExit
	 *            number of customers who exited common pool
	 */
	public void updateExit(int advExit, int comExit);

	/**
	 * Increase amount of customers in the common pool
	 * 
	 * @param comNr
	 *            number of customers currently in the pool
	 */
	public void updateComNr(int comNr);

	/**
	 * Increase amount of customers in the adventure pool
	 * 
	 * @param advNr
	 *            number of customers currently in the pool
	 */
	public void updateAdvNr(int advNr);

	/**
	 * Update the labels for waiting queue for the adventure pool and for numbers of
	 * customers currently in the pool.
	 */
	public void enteringAdvPool();

	/**
	 * Update the labels for waiting queue for the common pool and for numbers of
	 * customers currently in the pool.
	 */
	public void enteringComPool();

	/**
	 * Get a refernce to AdventurePool.
	 * 
	 * @return AdventurePool.
	 */
	public AdventurePool getAdvPool();

	/**
	 * Get a refernce to CommonPool.
	 * 
	 * @return CommonPool.
	 */
	public CommonPool getComPool();

}
