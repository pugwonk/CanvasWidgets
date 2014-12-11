package com.betaminus.canvaswidgets;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	final int REQUEST_PICK_APPWIDGET = 0;
	final int REQUEST_CREATE_APPWIDGET = 5;
	boolean startOnce = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		WidgetPlugin.startService(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	    ServicePart.appWidgetHost.stopListening();
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
	    int appWidgetId = ServicePart.appWidgetHost.allocateAppWidgetId();
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
	        	ServicePart.createWidget(data);
	        }
	    }
	    else if (resultCode == RESULT_CANCELED && data != null) {
	        int appWidgetId = 
	            data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	        if (appWidgetId != -1) {
	        	ServicePart.appWidgetHost.deleteAppWidgetId(appWidgetId);
	        }
	    }
	}

	// Show configuration activity of the widget picked by the user
	private void configureWidget(Intent data) {
	    Bundle extras = data.getExtras();
	    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	    AppWidgetProviderInfo appWidgetInfo = 
	    		ServicePart.appWidgetManager.getAppWidgetInfo(appWidgetId);
	    if (appWidgetInfo.configure != null) {
	        Intent intent = 
	            new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
	        intent.setComponent(appWidgetInfo.configure);
	        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	        startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
	    } else {
	    	ServicePart.createWidget(data);
	    }
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    }

	@Override
	protected void onStop() {
	    super.onStop();
	}
	
	public void btnPick_Click(View view) {
		selectWidget();
	}

	public void btnPoke_Click(View view) {
		ServicePart.PokeUpdate();
	}

}
