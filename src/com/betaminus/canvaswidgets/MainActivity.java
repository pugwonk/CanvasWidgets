package com.betaminus.canvaswidgets;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	final int APPWIDGET_HOST_ID = 2048;
	final int REQUEST_PICK_APPWIDGET = 0;
	final int REQUEST_CREATE_APPWIDGET = 5;

	AppWidgetManager appWidgetManager;
	AppWidgetHost appWidgetHost;
	AppWidgetHostView hostView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	// Let user pick a widget from the list of intalled AppWidgets
	public void selectWidget()
	{
	    int appWidgetId = this.appWidgetHost.allocateAppWidgetId();
	    Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
	    pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	    addEmptyData(pickIntent);
	    startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
	}

	// For some reason you have to add this empty data, else it won't work
	public void addEmptyData(Intent pickIntent)
	{
	    ArrayList<AppWidgetProviderInfo> customInfo = 
	        new ArrayList<AppWidgetProviderInfo>();
	    pickIntent.putParcelableArrayListExtra(
	        AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
	    ArrayList<Bundle> customExtras = new ArrayList<Bundle>();
	    pickIntent.putParcelableArrayListExtra(
	        AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, 
	                                Intent data) {
	    if (resultCode == RESULT_OK ) {
	        if (requestCode == REQUEST_PICK_APPWIDGET) {
	            configureWidget(data);
	        }
	        else if (requestCode == REQUEST_CREATE_APPWIDGET) {
	            createWidget(data);
	        }
	    }
	    else if (resultCode == RESULT_CANCELED && data != null) {
	        int appWidgetId = 
	            data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	        if (appWidgetId != -1) {
	            appWidgetHost.deleteAppWidgetId(appWidgetId);
	        }
	    }
	}

	// Show configuration activity of the widget picked by the user
	private void configureWidget(Intent data) {
	    Bundle extras = data.getExtras();
	    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	    AppWidgetProviderInfo appWidgetInfo = 
	            appWidgetManager.getAppWidgetInfo(appWidgetId);
	    if (appWidgetInfo.configure != null) {
	        Intent intent = 
	            new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
	        intent.setComponent(appWidgetInfo.configure);
	        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	        startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
	    } else {
	        createWidget(data);
	    }
	}

	// Get an instance of the selected widget as a AppWidgetHostView
	public void createWidget(Intent data) {
	    Bundle extras = data.getExtras();
	    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	    addWidget(appWidgetId);
	}

	private void addWidget(int appWidgetId) {
		AppWidgetProviderInfo appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId);

	    hostView = appWidgetHost.createView(this, appWidgetId, appWidgetInfo);
	    hostView.setAppWidget(appWidgetId, appWidgetInfo);
	    // Add  it on the layout you want
	    LinearLayout myLayout = (LinearLayout)findViewById(R.id.mainLayout);
	    myLayout.addView(hostView);
	}

	// Call this when you want to remove one from your layout
	public void removeWidget(AppWidgetHostView hostView) {
	    appWidgetHost.deleteAppWidgetId(hostView.getAppWidgetId());

	    // Remove from your layout
	    LinearLayout myLayout = (LinearLayout)findViewById(R.id.mainLayout);
	    myLayout.removeView(hostView);
	}

	@Override
	protected void onStart() {
	    super.onStart();
	    appWidgetManager = AppWidgetManager.getInstance(this);
	    appWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);

	    // Start listening to pending intents from the widgets
	    appWidgetHost.startListening();
	    addWidget(69); // add the clock, for testing
	    }

	@Override
	protected void onStop() {
	    super.onStop();
	    appWidgetHost.stopListening();
	}
	
	public void btnPick_Click(View view) {
		selectWidget();
	}

	public void btnPoke_Click(View view) {
	    WidgetPlugin.stateChanged(bitmapFromView(hostView, 120, 120), this);
	}

	private Bitmap bitmapFromView(View v, int width, int height) {
		Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(0, 0, width, height);
		v.draw(c);
		return b;
	}
}
