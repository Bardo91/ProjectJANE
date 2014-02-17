package com.bardo.proyectjane.modules;

import java.io.InputStream;
import java.io.OutputStream;

import com.bardo.proyectjane.framework.communication.Bluetooth.BluetoothComm;

public class ModuleBluetooth extends Thread {
	InputStream iStream;
	OutputStream oStream;

	BluetoothComm blueComm;

	public ModuleBluetooth() {
		// Instantiating the basic class that manage the bluetooth connection.
		blueComm = new BluetoothComm();

		// Added a hook to close the server when it's terminated.
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				blueComm.closeDown();
			}
		});

	}

	boolean running = false;

	@Override
	public void run() {
		while (running) {
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
