package es.jane.projectjane_smartphone;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import es.jane.projectjane_smartphone.bluetooth.BluetoothManager;
import es.jane.projectjane_smartphone.bluetooth.BluetoothService;
import es.jane.projectjane_smartphone.jane.JANErecognition;
import es.jane.projectjane_smartphone.jane.JANEvoice;

public class Main extends Activity {
	public final static String ACTION_CHANGE_TEXT_IO = "es.jane.ChangeTextIOAction";
	public final static String ACTION_SPEAK = "es.jane.SpeakAction";
	public final static String ACTION_CHANGE_LIGHT = "es.jane.ChangeLight";
	public final static String ACTION_ENABLE_BT = "es.jane.EnableBT";
	public final static String ACTION_STOP_SERVICE = "es.jane.StopService";

	private boolean isServiceRunning = false;

	private Button speakButton, retryConButton, stopServiceButton;
	private TextView textIO;
	private JANErecognition JANErecon;
	private JANEvoice JANEvoi;

	// public IncomingBroadcaster inBroadcast;

	private BroadcastReceiver inBroadcast = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ACTION_CHANGE_TEXT_IO)) {
				textIO.setText("OUTUT: " + intent.getStringExtra("text"));
			} else if (action.equals(ACTION_SPEAK)) {
				JANEvoi.speak(intent.getStringExtra("speak"));
			} else if (action.equals(ACTION_ENABLE_BT)) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent,
						BluetoothManager.REQUEST_ENABLE_BT);
			} else if (action.equals(ACTION_STOP_SERVICE)) {
				stopService();
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		JANErecon = new JANErecognition(this);
		JANEvoi = new JANEvoice(this);

		textIO = (TextView) findViewById(R.id.text_in_out);

		// inBroadcast = new IncomingBroadcaster();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_SPEAK);
		filter.addAction(ACTION_CHANGE_TEXT_IO);
		filter.addAction(ACTION_CHANGE_LIGHT);
		filter.addAction(ACTION_ENABLE_BT);
		filter.addAction(ACTION_STOP_SERVICE);
		registerReceiver(inBroadcast, filter);

		speakButton = (Button) findViewById(R.id.speak_button);
		speakButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View button) {
				try {
					JANErecon.startRecon();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		retryConButton = (Button) findViewById(R.id.retry_con_button);
		retryConButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View button) {
				if (!isServiceRunning) {
					Toast.makeText(getApplicationContext(),
							"Re-opening the bluetooth service",
							Toast.LENGTH_SHORT).show();
					Intent serviceIntent = new Intent(getApplicationContext(),
							BluetoothService.class);
					startService(serviceIntent);
					isServiceRunning = true;
				} else {
					Toast.makeText(getApplicationContext(), "Connecting",
							Toast.LENGTH_SHORT).show();
					retryConnection();
				}
			}
		});

		stopServiceButton = (Button) findViewById(R.id.stop_con_button);
		stopServiceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View button) {
				if (isServiceRunning) {
					stopService();
				}
			}
		});

	}

	public void stopService() {
		Toast.makeText(getApplicationContext(), "Stoping Service",
				Toast.LENGTH_SHORT).show();
		Intent serviceIntent = new Intent(getApplicationContext(),
				BluetoothService.class);
		stopService(serviceIntent);
		isServiceRunning = false;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent serviceIntent = new Intent(this, BluetoothService.class);
		startService(serviceIntent);
		isServiceRunning = true;
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(inBroadcast);
		JANEvoi.closeTTS();
		super.onDestroy();
	}

	public void retryConnection() {
		Intent retryConIntent = new Intent(
				BluetoothService.COMMAND_RETRY_CONNECTION);
		sendBroadcast(retryConIntent);
	}

	private void sendToLittleBoss(String aux) {
		Intent send2LBIntent = new Intent(
				BluetoothService.COMMAND_SEND_TO_LITTLEBOSS);
		send2LBIntent.putExtra("string", aux);
		sendBroadcast(send2LBIntent);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == JANErecognition.VOICE_RECOGNITION_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				ArrayList<String> textMatchList = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				// Get the first recon.
				String aux = textMatchList.remove(0);
				// Send to the service
				sendToLittleBoss(aux);
			}
		} else if (requestCode == BluetoothManager.REQUEST_ENABLE_BT) {
			retryConnection();
		} else if (requestCode == JANEvoice.VOICE_SPEAK_REQUEST_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				JANEvoi.configureTTS();
			} else {
				JANEvoi.installTTS();
			}
		}
	}

}
