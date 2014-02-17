package es.jane.projectjane_smartphone.jane;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

public class JANEvoice {

	final static public int VOICE_SPEAK_REQUEST_CODE = 200;

	private boolean active = false;

	private Activity main;
	private TextToSpeech TTS;

	public JANEvoice(Activity main) {
		this.main = main;
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		main.startActivityForResult(checkIntent, VOICE_SPEAK_REQUEST_CODE);
	}

	public void configureTTS() {
		TTS = new TextToSpeech(main, new OnInitListener() {

			@Override
			public void onInit(int status) {
				Toast.makeText(main, "TTS activated", Toast.LENGTH_SHORT)
						.show();
			}
		});

		TTS.setLanguage(new Locale("spa", "ESP"));

		active = true;

	}

	public void installTTS() {
		Intent installIntent = new Intent();
		installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
		main.startActivity(installIntent);
	}

	public void speak(String str) {
		if (active) {
			TTS.speak(str, TextToSpeech.QUEUE_ADD, null);
		}
	}

	public void closeTTS() {
		TTS.shutdown();
	}

}
