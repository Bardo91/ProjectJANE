package es.jane.projectjane_smartphone.homewidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;
import es.jane.projectjane_smartphone.R;
import es.jane.projectjane_smartphone.bluetooth.BluetoothService;

public class HomeScreenWidgetIntentReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(HomeScreenWidget.WIDGET_BUTTON_LIGHT)) {
			Send2LittleBossAndUpdateButtonListener(context, "cambiar luz");
			//Toast.makeText(context, "Received", Toast.LENGTH_SHORT).show();
		}
		if (intent.getAction().equals(HomeScreenWidget.WIDGET_BUTTON_FAN)) {
			Send2LittleBossAndUpdateButtonListener(context,
					"cambiar ventilador");
			//Toast.makeText(context, "Received", Toast.LENGTH_SHORT).show();
		}
		if (intent.getAction().equals(HomeScreenWidget.WIDGET_BUTTON_PC)) {
			Send2LittleBossAndUpdateButtonListener(context, "cambiar pc");
			//Toast.makeText(context, "Received", Toast.LENGTH_SHORT).show();
		}
	}

	private void Send2LittleBossAndUpdateButtonListener(Context context,
			String str) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.homescreenwidget_layout);

		Intent send2LBIntent = new Intent(
				BluetoothService.COMMAND_SEND_TO_LITTLEBOSS);
		send2LBIntent.putExtra("string", str);
		context.sendBroadcast(send2LBIntent);

		// REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
		remoteViews.setOnClickPendingIntent(R.id.lightwidgetbutton,
				HomeScreenWidget.buildLightButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.fanbuttonwidget,
				HomeScreenWidget.buildFanButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.pcbuttonwidget,
				HomeScreenWidget.buildPcButtonPendingIntent(context));

		HomeScreenWidget.pushWidgetUpdate(context.getApplicationContext(),
				remoteViews);
	}

}
