package es.jane.projectjane_smartphone.bluetooth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;
import es.jane.projectjane_smartphone.Main;
import es.jane.projectjane_smartphone.R;
import es.jane.projectjane_smartphone.jane.Librarian;

public class BluetoothService extends Service {

	public final static int ONGOING_ID = 1445;
	public final static int BIND_MESSAGER = 1;
	public final static int BIND_SERVICE = 2;

	public final static int COMMAND_MESSAGE = 0;
	public final static String COMMAND_RETRY_CONNECTION = "es.jane.RetryConn";
	public final static String COMMAND_SEND_TO_LITTLEBOSS = "es.jane.Send2LittleBoss";
	public final static String COMMAND_STOP_SERVICE = "es.jane.StopService";

	public final static int RETRY_CONNECTION_BUTTON = 3;

	private BluetoothManager blueManager;
	private NotificationManager mNotificationManager;
	private Builder mNotifyBuilder;

	private final IBinder mBinder = new BluetoothBinder();

	private BroadcastReceiver serviceBroadcast = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(COMMAND_RETRY_CONNECTION)) {
				retryConnection();
			} else if (action.equals(COMMAND_SEND_TO_LITTLEBOSS)) {
				sendToLittleBoss(intent.getStringExtra("string"));
				Log.d("LB", "Sent to little boss");
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();

		blueManager = new BluetoothManager(this);

		IntentFilter blueBroadcastFilter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		registerReceiver(blueManager.mReceiver, blueBroadcastFilter);

		IntentFilter serviceBroadcastFilter = new IntentFilter();
		serviceBroadcastFilter.addAction(COMMAND_RETRY_CONNECTION);
		serviceBroadcastFilter.addAction(COMMAND_SEND_TO_LITTLEBOSS);
		registerReceiver(serviceBroadcast, serviceBroadcastFilter);

		Toast.makeText(getApplicationContext(),
				"Bluetooth Connection Service created", Toast.LENGTH_SHORT)
				.show();

		retryConnection();
	}

	public void stopService() {
		Intent stopIntent = new Intent(Main.ACTION_STOP_SERVICE);
		sendBroadcast(stopIntent);
	}

	public void retryConnection() {
		if (!blueManager.getAdapter().isEnabled()) {
			enableBluetoothAdapter();
		} else {
			blueManager.discoverDevices();
		}
	}

	public void sendToLittleBoss(String str) {
		// Check that bluetooth connection with LittleBoss is Available
		if (blueManager.isAvailableLittleBoss()) {
			// Procces the string.
			String aux = Librarian.processRecon(str);

			// Create an intent that will be received in the main Activity by
			// the broadcastReceiver
			Intent changeTextIntent = new Intent(Main.ACTION_CHANGE_TEXT_IO);
			changeTextIntent.putExtra("text", aux);
			sendBroadcast(changeTextIntent);

			// Prepare the actions to be transmitted to LittleBoss
			String[] outStr = null;
			if (aux.contains(" y ")) {
				outStr = aux.split(" y ");
			} else {
				outStr = new String[1];
				outStr[0] = aux;
			}

			// Send info to LittleBoss
			for (int i = 0; i < outStr.length; i++) {
				aux = outStr[i];
				blueManager
						.writeLittleBoss((String.valueOf(aux.length()) + aux)
								.getBytes());

				// Create and send an intent to activate JANE's voice.
				Intent speakIntent = new Intent(Main.ACTION_SPEAK);
				speakIntent.putExtra("speak", Librarian.answer(aux));
				sendBroadcast(speakIntent);
			}
		} else {
			// If Bluetooth connection is not available, advise the user to
			// reconnect.
			Intent speakIntent = new Intent(Main.ACTION_SPEAK);
			Intent changeTextIntent = new Intent(Main.ACTION_CHANGE_TEXT_IO);
			speakIntent.putExtra("speak",
					"Por favor, activa el blutuz o reintenta conectarte");
			changeTextIntent.putExtra("text", "Bluetooth is not enabled");
			sendBroadcast(speakIntent);
			sendBroadcast(changeTextIntent);

		}
	}

	public void enableBluetoothAdapter() {
		if (blueManager.getAdapter() == null) {
			Log.d("BLUETOOTHMANAGER", "There's no bluetooth adapter");
		} else if (!blueManager.getAdapter().isEnabled()) {
			Intent enableBTIntent = new Intent(Main.ACTION_ENABLE_BT);
			sendBroadcast(enableBTIntent);
			Log.d("BLUETOOTH", "BLUETOOTH ENCENDIDO");
		}
	}

	public void startForeground() {
		// To prevent the system to kill the service, we start it in foreground
		Notification notification = new Notification(R.drawable.lighton,
				"Connected", System.currentTimeMillis());

		Intent main = new Intent(this, Main.class);
		main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, main,
				PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(this, "JANE - Smartphone",
				"Connected to LittleBoss", pendingIntent);
		notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;

		startForeground(ONGOING_ID, notification);
	}

	public void changeNotification(boolean on) {
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Sets an ID for the notification, so it can be updated
		if (on) {
			mNotifyBuilder = new NotificationCompat.Builder(
					BluetoothService.this).setContentTitle("JANE - Smartphone")
					.setContentText("Connected to LittleBoss")
					.setSmallIcon(R.drawable.lighton);
		} else {
			mNotifyBuilder = new NotificationCompat.Builder(
					BluetoothService.this).setContentTitle("JANE - Smartphone")
					.setContentText("Connected to LittleBoss")
					.setSmallIcon(R.drawable.lightoff);
		}
		mNotificationManager.notify(ONGOING_ID, mNotifyBuilder.build());
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(blueManager.mReceiver);
		unregisterReceiver(serviceBroadcast);
		blueManager.closeManager();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;

	}

	public class BluetoothBinder extends Binder {
		public BluetoothService getService() {
			return BluetoothService.this;
		}
	}

}
