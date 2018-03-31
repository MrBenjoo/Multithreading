package pool;

import java.util.Random;

/**
 * Removes customer from AdventurePool and CommonPool.
 * 
 * @author Benjamin Sejdic
 *
 */
public class ExitQueue implements Runnable {
	private CommonPool comPool;
	private AdventurePool advPool;
	private boolean stopThread;
	private int advExit;
	private int comExit;
	private IEnter iEnter;

	/**
	 * Constructor
	 * 
	 * @param advPool
	 *            adventurepool thread
	 * @param comPool
	 *            commonpool thread
	 * @param iEnter
	 *            interface which is used as a callback for updating the GUI
	 */
	public ExitQueue(AdventurePool advPool, CommonPool comPool, IEnter iEnter) {
		this.advPool = advPool;
		this.comPool = comPool;
		this.iEnter = iEnter;
		advExit = 0;
		comExit = 0;
	}

	@Override
	public void run() {
		stopThread = true;
		Random rand = new Random();
		while (stopThread) {

			// Customer leaves adventure pool
			if (rand.nextBoolean()) {
				if (advPool.exit()) {
					advExit++;
				}
			}

			// Customer leaves common pool
			else {
				if (comPool.exit()) {
					comExit++;
				}
			}
			iEnter.updateExit(advExit, comExit); // update GUI
			goSleep(rand.nextInt(rand.nextInt(3) * 1000 + 1000)); // sleep 1-3 seconds
		}
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
