package com.bardo.proyectjane.bigboss;

import com.bardo.proyectjane.framework.communication.Internal.SignalBox;
import com.bardo.proyectjane.modules.ModuleBluetooth;
import com.bardo.proyectjane.modules.ModuleInterface;
import com.bardo.proyectjane.modules.ModuleSerialComm;

/* This class is the main class that execute and manage the others threads */

public class BigBoss {

	/* Modules's declaration */

	public static ModuleSerialComm mSerialComm;
	public static ModuleInterface mInterface;
	public static ModuleBluetooth mBluetooth;

	public static void main(String[] args) {

	}

	/** Functions Related to initializing the program **/

	// This function initialize all the implemented modules of the program by
	// using another implemented functions
	public void initializeModules() {
		//initializeModuleInterface();
		//initializeModuleSerialComm();
		initializeModuleBluetooth();
	}

	public void initializeModuleSerialComm() {
		mSerialComm = new ModuleSerialComm(this);
		mSerialComm.setRunning(true);
		mSerialComm.start();
	}

	public void initializeModuleInterface() {
		mInterface = new ModuleInterface(this);
		mInterface.setRunning(true);
		mInterface.start();
	}

	public void initializeModuleBluetooth(){
		mBluetooth = new ModuleBluetooth();
		mBluetooth.setRunning(true);
		mBluetooth.start();
	}
	
	// This function close all the implemented modules of the program by using
	// another implemented functions
	public void closeModules() {
		//closeModuleSerialComm();
		//closeModuleInterface();
		closeModuleBluetooth();
	}

	public void closeModuleSerialComm() {
		System.out.println("BigBoss: Closing Serial Module");
		mSerialComm.closeSerialComm();
		System.out.println("BigBoss: SerialPort Closed");
		mSerialComm.setRunning(false);
		try {
			mSerialComm.join();
		} catch (InterruptedException e) {
		}
	}

	public void closeModuleInterface() {
		System.out.println("BigBoss: Closing Interface Module");
		mInterface.setRunning(false);
		try {
			mInterface.join();
		} catch (InterruptedException e) {
		}
	}
	public void closeModuleBluetooth() {
		System.out.println("BigBoss: Closing Bluetooth Module");
		mBluetooth.setRunning(false);
		try {
			mBluetooth.join();
		} catch (InterruptedException e) {
		}
	}
	

	/** Functions related to signaling **/

	// This function is used by the Comm Modules to send data to the others
	// modules
	public void manageSignalBox(SignalBox sBox) {
		int mod = sBox.getModule();
		switch (mod) {
		case ExistedModules.MODULE_IA:
			System.out.println("BigBoss: Send box to IA");
			break;
		case ExistedModules.MODULE_CAMERA:
			System.out.println("BigBoss: Send box to camera");
			break;
		case ExistedModules.MODULE_COMMAND_APPS:
			System.out.println("BigBoss: Send box to apps");
			break;
		case ExistedModules.MODULE_INTERFACE:
			try {
				mInterface.receiveData(sBox);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("BigBoss: Send box to interface");
			break;
		case ExistedModules.MODULE_BLUETOOTH:
			System.out.println("BigBoss: Send box to bluetooth");
			break;
		default:
			break;
		}
	}

	// This function is used to write data to the serial port
	public void writeToSerialPort(String data) {
		mSerialComm.writeToSerialPort(data);
	}

}
