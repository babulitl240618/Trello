package com.imaginebd.trellohayven;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
//import com.openatk.libtrello.TrelloSyncInfo;
import com.imaginebd.trellohayven.authenticator.AccountGeneral;
import com.imaginebd.trellohayven.internet.App;

public class AppsList extends Activity implements OnClickListener, OnItemClickListener {	
	
	private ListView appsListView = null;
	private TextView appsOrganizationName = null;
	private ArrayList<App> appsList = null;
	AppsArrayAdapter appsListAdapter = null;
	
	private boolean loading = false;
	private static SimpleDateFormat dateFormaterLocal = new SimpleDateFormat("LLL d, yyyy h:mm a", Locale.US);
	private static SimpleDateFormat dateFormaterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private static final String AUTHORITY = "com.imaginebd.trello";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apps_list);
		
		dateFormaterLocal.setTimeZone(TimeZone.getDefault());
		dateFormaterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

		this.setTitle(getString(R.string.AppsListTitle));
		appsListView = (ListView) findViewById(R.id.apps_list_view);
		appsOrganizationName = (TextView) findViewById(R.id.apps_farm);
		
		//Load organizations
		SharedPreferences prefs = getApplicationContext().getSharedPreferences(AUTHORITY, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
		String orgoName = prefs.getString("organizationName", "Unknown");
		appsOrganizationName.setText(orgoName);
		
		appsList = getAppList(this);
		appsListAdapter = new AppsArrayAdapter(this, R.layout.app, appsList);
		
		appsListView.setAdapter(appsListAdapter);
		appsListView.setOnItemClickListener(this);
	}
	
	@Override
	protected void onResume() {
		//Load organizations
		SharedPreferences prefs = getApplicationContext().getSharedPreferences(AUTHORITY, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
		String orgoName = prefs.getString("organizationName", "Unknown");
		appsOrganizationName.setText(orgoName);
		
		List<App> newApps = getAppList(this);
		
		appsList.clear();
		appsList.addAll(newApps);
		
		appsListAdapter.notifyDataSetChanged();
		refreshAppList(10);
		super.onResume();
	}
	
	
	
	@Override
	protected void onPause() {
		if(delayRefreshAppList != null) handler.removeCallbacks(delayRefreshAppList);
		delayRefreshAppList = null;
		super.onPause();
	}

	Runnable delayRefreshAppList = null;
	private Handler handler = new Handler();
	private void refreshAppList(final int interval){
		//Auto sync for presentations, allows intervals under 60 sec androids syncprovider minimum.
		if(delayRefreshAppList != null) handler.removeCallbacks(delayRefreshAppList);
		final Runnable r = new Runnable() {
		    public void run() {
		    	List<App> newApps = getAppList(getApplicationContext());
				appsList.clear();
				appsList.addAll(newApps);
				appsListAdapter.notifyDataSetChanged();
		    	
		        if(delayRefreshAppList != null) handler.postDelayed(delayRefreshAppList, interval*1000);
		    }
		};
		delayRefreshAppList = r;
        handler.postDelayed(delayRefreshAppList, interval*1000);
	}
	
	
	public static String dateToStringLocal(Date date) {
		if(date == null){
			return null;
		}
		return AppsList.dateFormaterLocal.format(date);
	} 
	public static Date stringToDateUTC(String date) {
		if(date == null){
			return null;
		}
		Date d;
		try {
			d = AppsList.dateFormaterUTC.parse(date);
		} catch (ParseException e) {
			d = new Date(0);
		}
		return d;
	}
	public static String dateToStringUTC(Date date) {
		if(date == null){
			return null;
		}
		return AppsList.dateFormaterUTC.format(date);
	}

	public static ArrayList<App> getAppList(Context context){
		ArrayList<App> newAppsList = new ArrayList<App>();
		
		//Find all supported apps
		Intent sendIntent = new Intent();
	public void doneLoadingList(){
		//Done loading apps update list
		if(loading){
			loading = false;
			//Remove loading screen
		}
		((BaseAdapter) appsListView.getAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		Log.d("List item:", "test");
	}
	
	private void removeAccountAndRemake(){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences(AUTHORITY, Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("FirstSetup", false);
		editor.commit();
		
		final Activity parent = this;
		AccountManager mAccountManager = AccountManager.get(this);
        mAccountManager.getAccountsByTypeAndFeatures(AccountGeneral.ACCOUNT_TYPE, null,
                new AccountManagerCallback<Account[]>() {
					@Override
					public void run(AccountManagerFuture<Account[]> future) {
						Account[] accounts = null;
                        try {
                        	accounts = future.getResult();
                        	for(int i=0; i<accounts.length; i++){
                        		Account acc = accounts[i];
                        		Log.d("AppsList - getAccountList", "Account Name:" + acc.name);
                        	}
                        	if(accounts.length > 1) Log.w("AppsList getAccountList", "More than 1 account.");
                        	if(accounts.length > 0) { 
                        		removeAccount(accounts[0]);
                        	} else {
                        		Intent go = new Intent(parent, MainActivity.class);
                    			parent.startActivity(go);
                        	}
                        } catch (Exception e) {
                        	Log.d("getAccountList", "error");
                            e.printStackTrace();
                        }
					}
                }
        , null);
	}
	
	private void removeAccount(Account account){
		final Activity parent = this;
		AccountManager mAccountManager = AccountManager.get(this);
		mAccountManager.removeAccount(account,
                new AccountManagerCallback<Boolean>() {
					@Override
					public void run(AccountManagerFuture<Boolean> future) {
						//Now we need to make a new account
						Intent go = new Intent(parent, MainActivity.class);
            			parent.startActivity(go);
					}
                }
        , null);
	}
}
