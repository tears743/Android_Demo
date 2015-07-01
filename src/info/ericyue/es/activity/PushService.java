package info.ericyue.es.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Debug;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import info.ericyue.es.R;
import info.ericyue.es.util.HttpUtil;

public class PushService extends Service {
	SharedPreferences sp,cargoSearchSp,chaufferSearchSp;
	Editor editor,editorSearch,chaufferEd;
	PushThread pushThread = new PushThread();
	NotificationManager mNotificationManager ; 
	Notification mnotification;
	Intent mintent;
	PendingIntent pintent;
	private SharedPreferences.OnSharedPreferenceChangeListener mlistener;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		
		mlistener = new OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				// TODO Auto-generated method stub
				Log.i("sharedPreferencesandkey",sharedPreferences.toString()+key);
				
			}
		};
		
		sp = getSharedPreferences("config", MODE_MULTI_PROCESS);
//		sp.registerOnSharedPreferenceChangeListener(mlistener);
		editor = sp.edit();
		cargoSearchSp = getSharedPreferences("CargoRemind", MODE_MULTI_PROCESS);
		chaufferSearchSp = getSharedPreferences("ChaufferRemind",MODE_MULTI_PROCESS);
//		chaufferSearchSp.registerOnSharedPreferenceChangeListener(mlistener);
		chaufferEd =chaufferSearchSp.edit();
		editorSearch = cargoSearchSp.edit();
		 mintent=new Intent(PushService.this,CarInfo.class);
		 
		
		 mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		 mnotification = new Notification();
		 mnotification.icon=R.drawable.icon;
         mnotification.tickerText="测试通知";
         mnotification.flags = Notification.FLAG_AUTO_CANCEL;
        // notification.defaults=Notification.DEFAULT_SOUND;   //同时发出系统声音
		
		super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		System.out.println("服务Start");
		super.onStart(intent, startId);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		pushThread = new PushThread();
		pushThread.start();
		
		System.out.println("服务StartCommand");
		
		return super.onStartCommand(intent, flags, startId);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		pushThread.interrupt();
		
		System.out.println("服务onDestroy");
		
		mNotificationManager.cancel(1);
		mNotificationManager.cancel(2);
		super.onDestroy();
	}
	
	
	
	
	public void ChaufferRemindService(){
		
		chaufferSearchSp = getSharedPreferences("ChaufferRemind",MODE_MULTI_PROCESS);
//		chaufferSearchSp.registerOnSharedPreferenceChangeListener(mlistener);
		chaufferEd =chaufferSearchSp.edit();
		 
		String datestr = chaufferSearchSp.getString("DateResearchString", "");
		String areawhere = chaufferSearchSp.getString("FromAddressSearchString", "");
		String chaufferType = chaufferSearchSp.getString("chaufferType", "");
		Log.i("chaufferType",chaufferType+","+areawhere+","+datestr);
//		android.os.Debug.waitForDebugger();
		String result= HttpUtil.queryChaufferRemind(datestr, areawhere, chaufferType);
		Log.i("result",result);
		chaufferEd.putString("ChaufferDetail", result);
		chaufferEd.commit();
		
		int count=sp.getInt("NewChaufferMessageCounts", 0);
		Character c = result.charAt(0);
		Log.i("c",c.toString());
		int messagecount = Integer.parseInt(c.toString());
//		System.out.println(messagecount+" "+count);
		if(messagecount>count){
			editor.putInt("NewChaufferMessageCounts", messagecount);
			editor.apply();
//			System.out.println(result);
			 mintent=new Intent(PushService.this,ChauffeurDetail.class);
			 mintent.putExtra("chaufferDetail", result);
			 
			 mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 pintent=PendingIntent.getActivity(PushService.this,0,mintent,0);
			 mnotification.setLatestEventInfo(PushService.this,"新消息", "您的司机信息提醒有"+messagecount+"条新通知！", pintent);
			 mNotificationManager.notify(2, mnotification);
			 Intent i =new Intent();
			 i.setAction("SENT");
			 i.putExtra("MESSAGE","18");
			 PushService.this.sendBroadcast(i);
		}
	}
	
	public void  CargoRemindService (){
		
		cargoSearchSp = getSharedPreferences("CargoRemind", MODE_MULTI_PROCESS);

		editorSearch = cargoSearchSp.edit();
		
		String date=cargoSearchSp.getString("DateResearchString", "");
		
		 
		String fromAdd = cargoSearchSp.getString("FromAddressSearchString", "");
		String toAdd = cargoSearchSp.getString("ToAddressSearchString", "");
		String result = HttpUtil.queryCargoRemind(date, fromAdd, toAdd);
		Log.i("date, fromAdd, toAdd",date+","+ fromAdd+","+toAdd);
		editorSearch.putString("cargoDetail", result);
		editorSearch.commit();
		
		int count=sp.getInt("NewCargoMessageCounts", 0);
		Character c = result.charAt(0);
		
		int messagecount = Integer.parseInt(c.toString());
		System.out.println(messagecount+" "+count);
		if(messagecount>count){
			editor.putInt("NewCargoMessageCounts", messagecount);
			editor.apply();
			System.out.println(result);
			 mintent=new Intent(PushService.this,CargoRemindItems.class);
			 mintent.putExtra("cargoDetail", result);
			 mintent.putExtra("cargo_sd", fromAdd);
			 mintent.putExtra("cargo_dd", toAdd);
			 mintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 pintent=PendingIntent.getActivity(PushService.this,0,mintent,0);
			 mnotification.setLatestEventInfo(PushService.this,"新消息", "您的货源提醒有"+messagecount+"条新通知！", pintent);
			 mNotificationManager.notify(1, mnotification);
			 Intent i =new Intent();
			 i.setAction("SENT");
			 i.putExtra("MESSAGE","16");
			 PushService.this.sendBroadcast(i);
		}
	}
	
	public class PushThread  extends Thread {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
			while(!this.isInterrupted()){
				
				Thread.sleep(3000);
				Log.i("cargoSearchSp", cargoSearchSp.getString("DateResearchString", ""));
				if(!( cargoSearchSp.getString("DateResearchString", "")=="")){ // 这句话判断逻辑有问题，在提醒查看后还会有通知
					
					CargoRemindService ();
				}
				if(!(chaufferSearchSp.getString("DateResearchString", "")=="")){
					ChaufferRemindService();
				}
				
			}
			System.out.println("stop thread");
			
		}
			catch(Exception e){
				System.out.println(e);
				
			}
			super.run();
		}
		
		
	}

}
