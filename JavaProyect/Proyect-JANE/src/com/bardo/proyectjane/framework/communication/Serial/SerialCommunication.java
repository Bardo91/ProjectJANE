package com.bardo.proyectjane.framework.communication.Serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import com.bardo.proyectjane.framework.communication.Internal.InputQueue;
import com.bardo.proyectjane.framework.communication.Internal.SignalBox;
import com.bardo.proyectjane.modules.ModuleSerialComm;

public class SerialCommunication {

	private SerialPort serialPort = null;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1", // MacOSX
			"/dev/ttyUSB0", // Linux
			"COM6", // Windows
	};

	private static BufferedReader input;
	private static OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public InputQueue queue = new InputQueue();
	public ModuleSerialComm parent;

	public SerialCommunication(ModuleSerialComm parent) {
		this.parent = parent;
		System.out.println("SerialCommunication: Initializing Serial Port.");
		initialize();
		System.out.println("SerialCommunication: Serial Port conected.");
	}

	public void initialize() {
		CommPortIdentifier portId = null;
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("SerialCommunication: Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(
					serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(new SerialPortEventListener() {

				@Override
				public void serialEvent(SerialPortEvent oEvent) {
					if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
						try {
							/*
							 * Event detected with some data available. We need
							 * to store it in a SignalBox variable, and then put
							 * it in to the queue
							 */
							String inputLine = input.readLine();

							String[] data = inputLine.split("-");
							SignalBox sBox = new SignalBox(null, Integer
									.valueOf(data[0]), data[2].toString(), Integer
									.valueOf(data[1]));
							queue.addSignalBox(sBox);

							System.out.println("SerialCommunication: "
									+ inputLine);
							synchronized (parent) {
								parent.notify();
							}

						} catch (Exception e) {
							System.err.println(e.toString());
						}
					}

				}
			});
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public synchronized void writeData(String data) {
		System.out.println("Sent: " + data);
		try {
			output.write(data.getBytes());
		} catch (Exception e) {
			System.out.println("SerialCommunication: could not write to port");
		}
	}
}
