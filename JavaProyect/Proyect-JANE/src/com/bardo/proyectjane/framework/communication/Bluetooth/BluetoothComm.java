package com.bardo.proyectjane.framework.communication.Bluetooth;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothComm {

	public static final String UUID = "0000110100001000800000805F9B34FB";
	public static final String NAME = "JANE";

	private LocalDevice blueDevice;

	private StreamConnectionNotifier server;
	private ThreadedConnectionHandler connJANE;

	public BluetoothComm() {
		initDevice();
		createRFCOMMconnection();
		processJanesConnection();
	}

	public void initDevice() {
		// This function initialize the local bluetooth device and set it
		// discoverable for the client (JANE)
		try {
			blueDevice = LocalDevice.getLocalDevice();
			System.out.println("ModuleBluetooth: Connected to local device: "
					+ blueDevice.getFriendlyName()
					+ ". Setting it Discoverable");
			blueDevice.setDiscoverable(DiscoveryAgent.GIAC);
		} catch (BluetoothStateException e) {
			System.out
					.println("ModuleBluetooth: Error connecting to the local device");
		}
	}

	public void createRFCOMMconnection() {
		try {
			// The URL needed to create the connection between the server and
			// the client.
			String url = "btspp://localhost:" + UUID + ";name=" + NAME
					+ ";authenticate=false";
			System.out.println("ModuleBluetooth: Start advertising " + NAME + "...");

			// create the connection notifier.
			server = (StreamConnectionNotifier) Connector.open(url);
		} catch (IOException e) {
			System.out
					.println("ModuleBluettoh: Error creating RFCOMM connection");
		}
	}

	public void processJanesConnection() {
		try {
			System.out.println("ModuleBluetooth: Waiting for connection...");
			// The server wait for the incoming connection with JANE
			StreamConnection conn = server.acceptAndOpen();
			System.out.println("ModuleBluetooth: Connection requested...");
			// When the connection is requested a new thread is created to
			// manage his in/outcomming data.
			connJANE = new ThreadedConnectionHandler(conn);
			connJANE.start();
		} catch (IOException e) {
			System.out
					.println("ModuleBluetooth: Error managing the connection");
		}
	}

	public void closeDown() {
		System.out.println("ModuleBluetooth: closing down the server");
		try {
			// Closing the server.
			server.close();
		} catch (IOException e) {
			System.out.println("ModuleBluetooth: error closing the server");
		}
		// Closing the thread that manage the connection.
		connJANE.closeDown();
	}

}
