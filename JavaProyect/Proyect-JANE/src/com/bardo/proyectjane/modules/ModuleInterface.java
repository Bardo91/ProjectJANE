package com.bardo.proyectjane.modules;

import com.bardo.proyectjane.bigboss.BigBoss;
import com.bardo.proyectjane.framework.Interface.InterfaceFrame;
import com.bardo.proyectjane.framework.communication.Internal.InputQueue;
import com.bardo.proyectjane.framework.communication.Internal.SignalBox;

public class ModuleInterface extends Thread {

	private boolean running = false;
	private BigBoss bigBoss;
	private InputQueue queue;
	private InterfaceFrame interfaceFrame;

	public ModuleInterface(BigBoss bigBoss) {
		queue = new InputQueue();
		this.bigBoss = bigBoss;
		interfaceFrame = new InterfaceFrame();
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				interfaceFrame.setVisible(true);
			}
		});
	}

	@Override
	public void run() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						while (running) {
							String texto = "Hay "
									+ String.valueOf(queue.size())
									+ " elementos en la cola";
							interfaceFrame.editText(texto);
						}

					}
				});
				t.start();
			}
		});
	}

	public void receiveData(SignalBox sBox) {
		System.out.println("ModuleInterface: saving received data");
		queue.addSignalBox(sBox);
		System.out.println("ModuleInterface: data received");
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
