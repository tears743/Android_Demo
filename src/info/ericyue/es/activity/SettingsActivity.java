/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   SettingsActivity.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-8-13
 */
package info.ericyue.es.activity;

import info.ericyue.es.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Window;

public class SettingsActivity extends PreferenceActivity {
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, SettingsActivity.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.settings);
	}
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
 
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
	public static boolean getShowTutorial(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("show_tutorial", true);
	}
	
	public static String getUpdateTime(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("auto_update","10000");
	}
	public static String getTraceTime(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getString("gps_record","10000");
	}
	public static boolean getAutoUpdate(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("switch_autorefresh",false);
	}
	public static boolean setAutoUpdate(Context context,boolean status){
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("switch_autorefresh", status).commit();
	}
	public static boolean getTraceRecord(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("trace_record",false);
	}
	public static boolean setTraceRecord(Context context,boolean status){
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("trace_record", status).commit();
	}
	public static boolean getWIFIReceive(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("wifi_receive",false);
	}
}
