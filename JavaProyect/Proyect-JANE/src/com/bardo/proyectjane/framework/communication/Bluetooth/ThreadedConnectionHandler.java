package com.bardo.proyectjane.framework.communication.Bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

import com.bardo.proyectjane.framework.communication.Internal.InputQueue;
import com.bardo.proyectjane.framework.communication.Internal.SignalBox;

public class ThreadedConnectionHandler extends Thread {
	private volatile boolean isRunning = false;

	private StreamConnection conn;
	private String nameJANE;

	private InputStream in;
	private OutputStream out;

	public ThreadedConnectionHandler(StreamConnection conn) {
		this.conn = conn;
		this.nameJANE = reportDeviceName(conn);
	}

	private String reportDeviceName(StreamConnection conn) {
		// Return the friendly name of the device
		String devName;
		try {
			RemoteDevice rd = RemoteDevice.getRemoteDevice(conn);
			devName = rd.getFriendlyName(false);
		} catch (IOException e) {
			devName = "device ??";
		}
		return devName;
	}

	public void run() {
		try {
			// Obtaining the in/output streams from the connection.
			in = conn.openInputStream();
			out = conn.openOutputStream();

			// Manage the I/O data.
			processData();

			// Closing the connection.
			System.out.println("ModuleBluetooth: Closing connection with"
					+ nameJANE);
			if (conn != null) {
				in.close();
				out.close();
				conn.close();
			}
		} catch (IOException e) {

		}
	}

	private void processData() {
		isRunning = true;
		String line = null;
		while (isRunning) {
			// /////////////////////////////////
			try {
				line = String.valueOf(Character.toChars(in.read()));
			} catch (IOException e) {
			}
			// /////////////////////////////////
			if (line != null) {
				System.out.println(line);
				// SignalBox sBox = new SignalBox(null,
				// ExistedModules.MODULE_BLUETOOTH, line, 3);
				// inQueue.addSignalBox(sBox);
			}

		}

	}

	private String readData() {
		byte[] data = null;
		try {
			int len = in.read();
			if (len <= 0) {
				System.out.println("ModuleBlueTooth: Message Length Error");
				return "Error";
			}
			data = new byte[len];
			len = 0;
			while (len != data.length) {
				int ch = in.read(data, len, data.length - len);
				if (ch == -1) {
					System.out.println("ModuleBlueTooth: Message Length Error");
					return "Error";
				}
				len += ch;
			}
		} catch (IOException e) {
			System.out.println("ModuleBlueTooth: Message Length Error");
			return "Error";
		}
		return new String(data).trim();
	}

	private void sendData(SignalBox sBox) {

	}

	
	public void closeDown() {
		isRunning = false;
	}
}
