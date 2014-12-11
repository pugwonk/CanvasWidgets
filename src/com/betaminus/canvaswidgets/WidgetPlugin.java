package com.betaminus.canvaswidgets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Arrays;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import com.betaminus.canvaswidgets.R;
import com.pennas.pebblecanvas.plugin.PebbleCanvasPlugin;
import com.pennas.pebblecanvas.plugin.PebbleCanvasPlugin.ImagePluginDefinition;

public class WidgetPlugin extends PebbleCanvasPlugin {
	public static final String LOG_TAG = "CANV_WIDGET";
	private static Bitmap _current;

	public static final int WIDGET_ID = 1; // Needs to be unique only within this plugin package

	// send plugin metadata to Canvas when requested
	@Override
	protected ArrayList<PluginDefinition> get_plugin_definitions(Context context) {
		Log.i(LOG_TAG, "get_plugin_definitions");

		// create a list of plugins provided by this app
		ArrayList<PluginDefinition> plugins = new ArrayList<PluginDefinition>();

		// chart
		ImagePluginDefinition iplug = new ImagePluginDefinition();
		iplug.id = WIDGET_ID;
		iplug.name = context.getString(R.string.plugin_name) + " 120x120";
		iplug.params_description = "Widget";
		plugins.add(iplug);

		return plugins;
	}

	@Override
	protected String get_format_mask_value(int def_id, String format_mask,
			Context context, String param) {
		return "";
	}
	
	public static void startService(Context context) {
		// Service will only get started once, so no great problem
		// re-calling this
		Intent tickerService = new Intent(context, ServicePart.class);
		context.startService(tickerService);
	}

	// send bitmap value to canvas when requested
	@Override
	protected Bitmap get_bitmap_value(int def_id, Context context, String param) {
		startService(context);
		if (_current != null)
			return _current;
		else {
			Random rnd = new Random();
			Paint paint = new Paint();
			Bitmap b = Bitmap.createBitmap(120, 120, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b);
			for (int i = 0; i < 20; i++) {
				paint.setARGB(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
				c.drawRect(rnd.nextInt(120), rnd.nextInt(120), rnd.nextInt(120), rnd.nextInt(120), paint); 
			}
			return b;
		}
	}

	public static void stateChanged(Bitmap current, Context context) {
		_current = current;
		notify_canvas_updates_available(WIDGET_ID, context);
	}
}
