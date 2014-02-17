package es.jane.projectjane_smartphone.jane;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

public class JANErecognition {

	public static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

	private Context context;
	private SpeechRecognizer speechRecon = null;

	public JANErecognition(Context context) {
		this.context = context;
		if (SpeechRecognizer.isRecognitionAvailable(context)) {
			speechRecon = SpeechRecognizer.createSpeechRecognizer(context);
		}
	}

	public boolean checkVoiceRecognition() {
		// Check if voice recognition is present
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			Toast.makeText(context, "Voice recognizer not present",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public void startRecon() {
		if (checkVoiceRecognition()) {
			Intent recognizerIntent = new Intent(
					RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

			recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
					getClass().getPackage().getName());
			recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
			recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

			((Activity) context).startActivityForResult(recognizerIntent,
					VOICE_RECOGNITION_REQUEST_CODE);
		}
	}

	public void destroyRecon() {
		speechRecon.destroy();
	}
}
