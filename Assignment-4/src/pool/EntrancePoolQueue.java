package pool;

import java.util.LinkedList;

/**
 * Handles the waiting queue by placing/removing customers from it.
 * 
 * @author Benjamin Sejdic
 *
 */
public class EntrancePoolQueue {
	private LinkedList<Customer> queue;
	private int customersWaiting;
	private IEnter iEnter;

	/**
	 * Constructor
	 * 
	 * @param iEnter
	 *            callback to update GUI.
	 */
	public EntrancePoolQueue(IEnter iEnter) {
		this.queue = new LinkedList<>();
		this.iEnter = iEnter;
		this.customersWaiting = 0;
	}

	/**
	 * Places a new customer in the waiting queue and updates the waiting queue
	 * (text) in GUI.
	 * 
	 * @param customer
	 *            the customer that will be placed in the waiting queue.
	 * @return true if successfully added otherwise false
	 */
	public synchronized boolean enterQueue(Customer customer) {
		boolean added = queue.add(customer);
		if (added) {
			customersWaiting++;
			if (customer.isVip()) {
				iEnter.incAdvQueue(customersWaiting);
			} else {
				iEnter.incComQueue(customersWaiting);
			}
		}
		return added;
	}

	/**
	 * Simulates a customer leaving the waiting queue.
	 * 
	 * @return true if the customer has been successfully removed otherwise false
	 */
	public synchronized Customer getCustomer() {
		Customer customer = null;
		if (queue.size() > 0) {
			customer = queue.pop();
			customersWaiting--;
		}
		return customer;
	}

	/**
	 * Get the total number of customers waiting to enter the pool.
	 * 
	 * @return number of customer waiting.
	 */
	public int getNbrCustomers() {
		return this.customersWaiting;
	}

}
