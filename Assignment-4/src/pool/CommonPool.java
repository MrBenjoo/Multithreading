package pool;

import java.util.LinkedList;
import java.util.Random;

/**
 * Common pool where the customers dont need a vip status. 
 * @author Benjamin Sejdic
 *
 */
public class CommonPool implements Runnable {
	private EntrancePoolQueue comPoolQueue;
	private IEnter iEnter;
	private LinkedList<Customer> guestList;
	private boolean poolOpen;
	private int maxGuests;

	/**
	 * onstructor
	 * 
	 * @param comPoolQueue
	 *            waiting queue for common pool
	 * @param maxGuests
	 *            max number of customers that can be at the same time in the pool
	 * @param iEnter
	 *            interface which is used as a callback for updating the GUI
	 */
	public CommonPool(EntrancePoolQueue comPoolQueue, int maxGuests, IEnter iEnter) {
		this.comPoolQueue = comPoolQueue;
		this.maxGuests = maxGuests;
		this.iEnter = iEnter;
		this.guestList = new LinkedList<>();
	}

	@Override
	public void run() {
		poolOpen = true;
		Random rand = new Random();
		while (poolOpen) {
			System.out.println("com pool running");
			try {
				// Get customer from waiting queue
				if (rand.nextBoolean()) {
					getEntranceCustomer();
				}

				// Get customer from adventure pool
				else {
					getAdvCustomer();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			goSleep((rand.nextInt(2) * 500) + 500); // sleep 1-1.5 seconds
		}
	}

	/**
	 * Get customer from waiting queue.
	 * 
	 * @throws InterruptedException
	 */
	private synchronized void getEntranceCustomer() throws InterruptedException {
		if (poolLimitExceded()) {
			wait();
		}
		Customer customer = comPoolQueue.getCustomer();
		if (customer != null) {
			guestList.add(customer);
			iEnter.enteringComPool();
		}
	}

	/**
	 * Get customer from adventure pool.
	 * 
	 * @throws InterruptedException
	 */
	private synchronized void getAdvCustomer() throws InterruptedException {
		if (poolLimitExceded()) {
			wait();
		}
		Customer customer = iEnter.getAdvPool().getCustomer();
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
			iEnter.updateComNr(guestList.size());
			notify();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is called from AdventurePool to simualate a customer leaving the
	 * common pool and enter the adventure pool.
	 * 
	 * @return customer from common pool.
	 */
	public synchronized Customer getCustomer() {
		Customer customer = null;
		if (guestList.size() > 0) {
			customer = guestList.pop();
			iEnter.enteringAdvPool();
			notify();
		}
		return customer;
	}

	/**
	 * Check if the pool limits has been exceeded
	 * 
	 * @return if numbers of customers is greater then the pool limit return true,
	 *         else return false
	 */
	private boolean poolLimitExceded() {
		return guestList.size() >= maxGuests;
	}

	/**
	 * Get the total amount of customers currently in the pool
	 * 
	 * @return customers in the pool
	 */
	public int getNbrCustomersPool() {
		return guestList.size();
	}

	/**
	 * Stop the thread
	 */
	public void close() {
		poolOpen = false;
	}

	/**
	 * Let the tread sleep x milliseconds
	 * 
	 * @param miliseconds
	 */
	public void goSleep(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
