package pool;

import java.util.Random;

/**
 * Responsible for placing customers in the waiting queue for both pools.
 * 
 * @author Benjamin Sejdic
 *
 */
public class Reception implements Runnable {
	private EntrancePoolQueue advPoolQueue, comPoolQueue;
	private boolean poolOpen;

	/**
	 * Constructor
	 * 
	 * @param advPoolQueue
	 *            waiting queue for adventure pool
	 * @param comPoolQueue
	 *            waiting queue for common pool
	 */
	public Reception(EntrancePoolQueue advPoolQueue, EntrancePoolQueue comPoolQueue) {
		this.advPoolQueue = advPoolQueue;
		this.comPoolQueue = comPoolQueue;
	}

	@Override
	public void run() {
		poolOpen = true;
		Random rand = new Random();
		while (poolOpen) {

			// Enter waiting queue for CommonPool
			if (rand.nextBoolean()) {
				boolean entered = comPoolQueue.enterQueue(new Customer(false));
				if (entered) {
					System.out.println("A new customer was places in the waiting queue for the common pool.");
				}
			}

			// Enter waiting queue for AdventurePool
			else {
				boolean entered = advPoolQueue.enterQueue(new Customer(true));
				if (entered) {
					System.out.println("A new customer was places in the waiting queue for the adventure pool.");
				}
			}

			goSleep((rand.nextInt(2) * 500) + 500); // sleep 1-1.5 seconds
		}
	}

	/**
	 * Let the tread sleep x milliseconds
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

	/**
	 * Stop the thread
	 */
	public void close() {
		poolOpen = false;
	}

}
