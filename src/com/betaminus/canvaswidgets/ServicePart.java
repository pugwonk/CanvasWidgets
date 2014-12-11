package com.betaminus.canvaswidgets;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class ServicePart extends Service {
	private static Timer timer = new Timer();
	private static Context ctx;
	final int APPWIDGET_HOST_ID = 2048;

	public static AppWidgetManager appWidgetManager;
	public static 	MyAppWidgetHost appWidgetHost;
	public static AppWidgetHostView hostView;

	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		Log.i(WidgetPlugin.LOG_TAG, "Creating service");
		ctx = this;
		appWidgetManager = AppWidgetManager.getInstance(this);
		appWidgetHost = new MyAppWidgetHost(this, APPWIDGET_HOST_ID);

		// Start listening to pending intents from the widgets
		appWidgetHost.startListening();
		addWidget(69); // add the clock, for testing. 6 in emulator, 69 on phone
		startService();
	}

	private static void addWidget(int appWidgetId) {
		AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);

		AppWidgetHostView oldHostView = appWidgetHost.createView(ctx, appWidgetId, appWidgetInfo);
		hostView = oldHostView;
		//hostView = new MyAppWidgetHostView(this, oldHostView);
		hostView.setAppWidget(appWidgetId, appWidgetInfo);
		// Add  it on the layout you want
		//LinearLayout myLayout = (LinearLayout)findViewById(R.id.mainLayout);
		//myLayout.addView(hostView);
	}

	// Get an instance of the selected widget as a AppWidgetHostView
	public static void createWidget(Intent data) {
		Bundle extras = data.getExtras();
		int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
		addWidget(appWidgetId);
	}

	// Call this when you want to remove one from your layout
	public void removeWidget(AppWidgetHostView hostView) {
		appWidgetHost.deleteAppWidgetId(hostView.getAppWidgetId());

		// Remove from your layout
		//LinearLayout myLayout = (LinearLayout)findViewById(R.id.mainLayout);
		//myLayout.removeView(hostView);
	}

	public void startService() {
		Log.i(WidgetPlugin.LOG_TAG, "Starting service ticks");
		timer.scheduleAtFixedRate(new mainTask(), 0, 10000);
	}

	private class mainTask extends TimerTask {
		public void run() {
			Log.i(WidgetPlugin.LOG_TAG, "Service tick");
			PokeUpdate();
		}
	}

	public void onDestroy() {
		super.onDestroy();
		Log.i(WidgetPlugin.LOG_TAG, "Stopping service");
	}

	private static Bitmap bitmapFromView(View v, int width, int height) {
		if (width * height == 0) {
			Log.i(WidgetPlugin.LOG_TAG, "Was going to return bitmap but no width/height yet");
			return null;
		}
		else {
			Log.i(WidgetPlugin.LOG_TAG, "Returning valid bitmap");
			Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b);
			v.layout(0, 0, width, height);
			v.draw(c);
			return b;
		}
	}

	public static void PokeUpdate() {
		//WidgetPlugin.stateChanged(bitmapFromView(hostView, hostView.getWidth(), hostView.getHeight()), ctx);
		WidgetPlugin.stateChanged(bitmapFromView(hostView, 300, 300), ctx);
	}
}