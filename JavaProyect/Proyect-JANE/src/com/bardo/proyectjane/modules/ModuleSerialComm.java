package com.bardo.proyectjane.modules;

import com.bardo.proyectjane.bigboss.BigBoss;
import com.bardo.proyectjane.framework.communication.Internal.SignalBox;
import com.bardo.proyectjane.framework.communication.Serial.SerialCommunication;

public class ModuleSerialComm extends Thread {

	private SerialCommunication SerialComm;
	private boolean running = false;
	private BigBoss bigBoss;

	public ModuleSerialComm(BigBoss bigBoss) {
		this.bigBoss = bigBoss;
		this.SerialComm = new SerialCommunication(this);
	}

	@Override
	public synchronized void run() {
		while (running) {
			try {
				wait();
				System.out.println("ModuleSerialComm: Revisando");
				SignalBox sBox = SerialComm.queue.getNextSignalBox();
				if (sBox != null) {
					bigBoss.manageSignalBox(sBox);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void writeToSerialPort(String data) {
		SerialComm.writeData(data);
	}

	public void closeSerialComm() {
		SerialComm.close();
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
