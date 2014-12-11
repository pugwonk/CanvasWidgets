package com.betaminus.canvaswidgets;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;

public class MyAppWidgetHost extends AppWidgetHost {

	public MyAppWidgetHost(Context context, int hostId) {
		super(context, hostId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected AppWidgetHostView onCreateView(Context context, int appWidgetId,
			AppWidgetProviderInfo appWidget) {
		// TODO Auto-generated method stub
		return super.onCreateView(context, appWidgetId, appWidget);
	}
}
