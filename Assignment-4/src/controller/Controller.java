package controller;

import pool.AdventurePool;
import pool.CommonPool;
import pool.EntrancePoolQueue;
import pool.ExitQueue;
import pool.IEnter;
import pool.Reception;
import view.GUISwimmingpool;

/**
 * 
 * @author Benjamin Sejdic
 *
 */
public class Controller implements IEnter {
	private GUISwimmingpool gui;
	private AdventurePool advPool;
	private CommonPool comPool;
	private Reception reception;
	private ExitQueue exitQueue;
	private EntrancePoolQueue advPoolQueue, comPoolQueue;

	/**
	 * Constructor
	 */
	public Controller() {
		showGUI();
		initQueues();
		initWorkers();
	}

	/**
	 * Display the user interface
	 */
	private void showGUI() {
		gui = new GUISwimmingpool(this);
		gui.Start();
	}

	/**
	 * Initialize the waiting queues
	 */
	private void initQueues() {
		advPoolQueue = new EntrancePoolQueue(this);
		comPoolQueue = new EntrancePoolQueue(this);
	}

	/**
	 * Initialize the threads
	 */
	private void initWorkers() {
		comPool = new CommonPool(comPoolQueue, gui.getComPoolLimit(), this);
		advPool = new AdventurePool(advPoolQueue, gui.getAdvPoolLimit(), this);
		reception = new Reception(advPoolQueue, comPoolQueue);
		exitQueue = new ExitQueue(advPool, comPool, this);
	}

	/**
	 * Simulate pool activity when the pools open by starting the threads
	 */
	public void onPoolOpen() {
		new Thread(reception).start();
		new Thread(advPool).start();
		new Thread(comPool).start();
		new Thread(exitQueue).start();
	}

	/**
	 * Stop the threads except ExitQueue and reset the waiting queues in the GUI.
	 */
	public void onPoolClosed() {
		reception.close();
		advPool.close();
		comPool.close();
		gui.reset();
		System.out.println("Pools closed.");
	}

	@Override
	public void enteringAdvPool() {
		gui.incAdvPoolCustomers(advPool.getNumberOfGuests());
		gui.incAdvQueue(advPoolQueue.getNbrCustomers());
	}

	@Override
	public void enteringComPool() {
		gui.incComPoolCustomers(comPool.getNbrCustomersPool());
		gui.incComQueue(comPoolQueue.getNbrCustomers());
	}

	@Override
	public void incAdvQueue(int nbrOfCustomers) {
		gui.incAdvQueue(nbrOfCustomers);
	}

	@Override
	public void incComQueue(int nbrOfCustomers) {
		gui.incComQueue(nbrOfCustomers);
	}

	@Override
	public AdventurePool getAdvPool() {
		return this.advPool;
	}

	@Override
	public CommonPool getComPool() {
		return this.comPool;
	}

	@Override
	public void updateExit(int advExit, int comExit) {
		gui.updateExit(advExit, comExit);

	}

	@Override
	public void updateComNr(int comNr) {
		gui.updateComNr(comNr);
	}

	@Override
	public void updateAdvNr(int advNr) {
		gui.updateAdvNr(advNr);
	}

}
