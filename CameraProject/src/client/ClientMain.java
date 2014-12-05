package client;

import GUI.GUI;


public class ClientMain {

	/**
	 * @param args
	 * Starts the client side of the program.
	 */
	public static void main(String[] args) {
		new GUI(new ClientMonitor());
	}
}
