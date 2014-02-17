package es.jane.projectjane_smartphone.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BluetoothManager {
	public static final int REQUEST_ENABLE_BT = 1002;
	public static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private BluetoothService service;

	private boolean running = true;
	Thread connectionLittleBoss;

	private BluetoothAdapter blueAdapter;
	private static InputStream iStreamJANE = null;
	private static OutputStream oStreamJANE = null;
	private static InputStream iStreamLittleBoss = null;
	private static OutputStream oStreamLittleBoss = null;

	public BluetoothManager(BluetoothService service) {
		this.service = service;
		blueAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	public void discoverDevices() {
		blueAdapter.startDiscovery();
	}

	public BluetoothAdapter getAdapter() {
		return this.blueAdapter;
	}

	public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				Log.d("BLUETOOTHMANAGER", "Found device");
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a
				// ListView
				if (device.getName().equals("JANE")) {
					Log.d("BLUETOOTHMANAGER", "Found JANE");
					blueAdapter.cancelDiscovery();
					connectAsClient(device);
				}
				if (device.getName().equals("HC-06")) {
					Log.d("BLUETOOTHMANAGER", "Found LittleBoss");
					blueAdapter.cancelDiscovery();
					connectAsServer(device);
				}
			}

		}
	};

	public void connectAsClient(BluetoothDevice device) {
		final BluetoothSocket mmSocket;
		final BluetoothDevice mmDevice = device;

		BluetoothSocket tmp = null;

		Log.d("BLUETOOTHMANAGER", "Trying connection with JANE");
		try {
			tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {

		}

		Log.d("BLUETOOTHMANAGER", "Waiting connection with JANE");
		mmSocket = tmp;
		Thread connection = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mmSocket.connect();
				} catch (IOException ConnectEx) {
					Log.d("BLUETOOTHMANAGER", "Error creating the connection");
					try {
						mmSocket.close();
					} catch (IOException closeEx) {
						return;
					}
				}
				try {
					iStreamLittleBoss = mmSocket.getInputStream();
					oStreamLittleBoss = mmSocket.getOutputStream();

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		connection.start();

	}

	public void connectAsServer(BluetoothDevice device) {
		final BluetoothSocket mmSocket;
		final BluetoothDevice mmDevice = device;

		BluetoothSocket tmp = null;

		Log.d("BLUETOOTHMANAGER", "Trying connection with LittleBoss");
		try {
			tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
		}
		mmSocket = tmp;

		Log.d("BLUETOOTHMANAGER", "Waiting connection with LittleBoss");
		connectionLittleBoss = new Thread(new Runnable() {

			@Override
			public void run() {

				byte[] buffer = new byte[1024];

				try {
					mmSocket.connect();
				} catch (IOException ConnectEx) {
					Log.d("BLUETOOTHMANAGER", "Error connecting to LittleBoss");
					try {
						mmSocket.close();
					} catch (IOException closeEx) {
					}
					return;
				}
				try {
					iStreamLittleBoss = mmSocket.getInputStream();
					oStreamLittleBoss = mmSocket.getOutputStream();

					Log.d("BLUETOOTHMANAGER", "Connected to LittleBoss");
					service.startForeground();

					while (running) {

						try {
							iStreamLittleBoss.read(buffer);
							String command = String.valueOf(buffer);
							Log.d("ISTREAM", command);

						} catch (IOException e) {
							iStreamLittleBoss = null;
							service.stopService();
							break;
						}

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		connectionLittleBoss.start();

	}

	public void writeJANE(byte[] bytes) {
		try {
			oStreamJANE.write(bytes);
		} catch (IOException e) {
		}
	}

	public void writeLittleBoss(byte[] bytes) {
		try {
			oStreamLittleBoss.write(bytes);
		} catch (IOException e) {
		}
	}

	public boolean isAvailableJANE() {
		if (iStreamJANE != null) {
			return true;
		}
		return false;
	}

	public boolean isAvailableLittleBoss() {
		if (iStreamLittleBoss != null) {
			return true;
		}
		return false;
	}

	public void closeManager() {
		running = false;
		try {
			if (iStreamLittleBoss != null)
				iStreamLittleBoss.close();
			if (iStreamJANE != null)
				iStreamJANE.close();
		} catch (IOException e) {
		}
		if (connectionLittleBoss != null)
			connectionLittleBoss.stop();
	}

}
