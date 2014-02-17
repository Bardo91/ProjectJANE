package com.bardo.proyectjane.framework.communication.Internal;

import java.util.ArrayList;

/* This is a communication queue of signals, every thread in the program has an inputQueue */
/* The generic type T is the kind of data that the queue store */

/* The queue has 3 types of priorities (1 Low-p ; 2 Normal-p ; 3 High-p). 
 * Actually, there're three arrayList for every priority in order to accelerate the 
 * process of selecting the following signal according the priority access*/

public class InputQueue {

	private ArrayList<SignalBox> queueP1 = new ArrayList<SignalBox>();
	private ArrayList<SignalBox> queueP2 = new ArrayList<SignalBox>();
	private ArrayList<SignalBox> queueP3 = new ArrayList<SignalBox>();

	public InputQueue(){
		
	}
	
	public SignalBox getNextSignalBox() {
		if (queueP1.size() != 0) {
			return queueP1.remove(0);
		} else if (queueP2.size() != 0) {
			return queueP2.remove(0);
		} else if (queueP3.size() != 0) {
			return queueP3.remove(0);
		}
		return null;
	}

	public void addSignalBox(SignalBox signalBox) {
		int p = signalBox.getPriority();
		if (p == 1) {
			queueP1.add(signalBox);
		} else if (p == 2) {
			queueP2.add(signalBox);
		} else if (p == 3) {
			queueP3.add(signalBox);
		}
	}

	public int size() {
		return queueP1.size() + queueP2.size() + queueP3.size();
	}
}
