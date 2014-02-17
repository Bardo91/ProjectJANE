package es.jane.projectjane_smartphone.homewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;
import android.widget.Toast;
import es.jane.projectjane_smartphone.Main;
import es.jane.projectjane_smartphone.R;
import es.jane.projectjane_smartphone.bluetooth.BluetoothService;

public class HomeScreenWidget extends AppWidgetProvider {

	public static String WIDGET_BUTTON_LIGHT = "es.bardo.changeLight";
	public static String WIDGET_BUTTON_FAN = "es.bardo.changeFan";
	public static String WIDGET_BUTTON_PC = "es.bardo.changePc";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.homescreenwidget_layout);
		remoteViews.setOnClickPendingIntent(R.id.lightwidgetbutton,
				buildLightButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.fanbuttonwidget,
				buildFanButtonPendingIntent(context));
		remoteViews.setOnClickPendingIntent(R.id.pcbuttonwidget,
				buildPcButtonPendingIntent(context));

		pushWidgetUpdate(context, remoteViews);

	}

	public static PendingIntent buildLightButtonPendingIntent(Context context) {
		Intent intent = new Intent();
		intent.setAction(WIDGET_BUTTON_LIGHT);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static PendingIntent buildFanButtonPendingIntent(Context context) {
		Intent intent = new Intent();
		intent.setAction(WIDGET_BUTTON_FAN);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static PendingIntent buildPcButtonPendingIntent(Context context) {
		Intent intent = new Intent();
		intent.setAction(WIDGET_BUTTON_PC);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context,
				HomeScreenWidget.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);

		//Toast.makeText(context, "Actualizando Widget", Toast.LENGTH_SHORT).show();
	}

}
