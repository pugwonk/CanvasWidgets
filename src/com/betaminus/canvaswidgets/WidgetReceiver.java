package com.betaminus.canvaswidgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class WidgetReceiver extends BroadcastReceiver {
	@Override
	public final void onReceive(Context context, Intent intent) {
        Log.d(WidgetPlugin.LOG_TAG, "Got power state");
		
        String action = intent.getAction();
        
        WidgetPlugin.stateChanged(action, context);
	}
}
