/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   TraceRecordService.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-8-20
 */
package info.ericyue.es.activity;

import info.ericyue.es.util.LocationUtil;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class TraceRecordService extends Service {
	private LocationManager locationManager;
	private Handler objHandler = new Handler();
	private String worker_id;
	private LocationUtil locationUtil; 
	private Runnable mTasks = new Runnable(){
		public void run(){	
			  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
			  locationUtil=new LocationUtil(locationManager,TraceRecordService.this,worker_id,TraceRecordService.this.getApplication());
			  Log.i("�켣��¼", "�켣��¼����������..."+worker_id);
			  objHandler.postDelayed(mTasks, 10000);
			  locationUtil.submitGPSToServer();
		  } 
	  };
	  @Override
	  public void onStart(Intent intent, int startId){
		  worker_id=intent.getExtras().getString("worker_id");
		  super.onStart(intent, startId);
	    
	  }
	  @Override
	  public void onCreate(){
	    /* ����ʼ������ÿ1��mTasks�߳� */
//		  android.os.Debug.waitForDebugger();
		  objHandler.postDelayed(mTasks, 10000);
	    super.onCreate();
	  }
	  @Override
	  public IBinder onBind(Intent intent){
	    /* IBinder����ΪService����������д�ķ��� */
	    return null;
	  }
	  @Override
	  public void onDestroy(){
	    /* ������������Ƴ�mTasks�߳� */
	    objHandler.removeCallbacks(mTasks);
	    super.onDestroy();
	  }  

}
