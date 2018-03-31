package pool;

import java.util.LinkedList;
import java.util.Random;

/**
 * Representing an adventure pool where the guests needs to have a vip status
 * 
 * @author Benjamin Sejdic
 *
 */
public class AdventurePool implements Runnable {
	private int maxGuests;
	private LinkedList<Customer> guestList;
	private boolean poolOpen;
	private EntrancePoolQueue advPoolQueue;
	private IEnter iEnter;

	/**
	 * Constructor.
	 * 
	 * @param advPoolQueue
	 *            waiting queue for adventure pool.
	 * @param maxGuests
	 *            waiting queue for common pool.
	 * @param iEnter
	 *            callback to update GUI.
	 */
	public AdventurePool(EntrancePoolQueue advPoolQueue, int maxGuests, IEnter iEnter) {
		this.advPoolQueue = advPoolQueue;
		this.maxGuests = maxGuests;
		this.iEnter = iEnter;
		this.guestList = new LinkedList<>();
	}

	@Override
	public void run() {
		poolOpen = true;
		Random rand = new Random();
		while (poolOpen) {
			System.out.println("adv pool running");
			try {
				// Get customer from the waiting queue
				if (rand.nextBoolean()) {
					getEntranceCustomer();
				}

				// Get customer from the CommonPool
				else {
					getPoolCustomer();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			goSleep((rand.nextInt(2) * 500) + 500); // sleep 1-1.5 seconds
		}
	}

	/**
	 * Get customer from the waiting queue.
	 * 
	 * @throws InterruptedException
	 */
	private synchronized void getEntranceCustomer() throws InterruptedException {
		if (poolLimitExceeded()) {
			wait();
		}
		Customer customer = advPoolQueue.getCustomer();
		if (customer != null) {
			guestList.add(customer);
			iEnter.enteringAdvPool();
		}
	}

	/**
	 * Get customer from the CommonPool.
	 * 
	 * @throws InterruptedException
	 */
	private synchronized void getPoolCustomer() throws InterruptedException {
		if (poolLimitExceeded()) {
			wait();
		}
		Customer customer = iEnter.getComPool().getCustomer();
		if (customer != null) {
			guestList.add(customer);
			iEnter.enteringAdvPool();
		}
	}

	/**
	 * Called from ExitQueue to simulate a customer leaving the pool.
	 * 
	 * @return true if customer left the pool otherwise return false.
	 */
	public synchronized boolean exit() {
		if (guestList.size() > 0) {
			System.out.println("AdventurePool exit");
			guestList.pop();
			iEnter.updateAdvNr(guestList.size());
			notify();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is called from CommonPool to simualate a customer leaving
	 * adventure pool and entering the common pool.
	 * 
	 * @return customer from adventure pool.
	 */
	public synchronized Customer getCustomer() {
		Customer customer = null;
		if (guestList.size() > 0) {
			customer = guestList.pop();
			iEnter.enteringComPool();
			notify();
		}
		return customer;
	}

	/**
	 * Check if the pool limits has been exceeded.
	 * 
	 * @return if numbers of customers is greater then the pool limit return true,
	 *         else return false.
	 */
	private boolean poolLimitExceeded() {
		return guestList.size() >= maxGuests;
	}

	/**
	 * Get the total amount of customers currently in the pool
	 * 
	 * @return customers in the pool
	 */
	public int getNumberOfGuests() {
		return guestList.size();
	}

	/**
	 * Stop the thread.
	 */
	public void close() {
		poolOpen = false;
	}

	/**
	 * Let the tread sleep x milliseconds.
	 * 
	 * @param milliseconds
	 */
	public void goSleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
