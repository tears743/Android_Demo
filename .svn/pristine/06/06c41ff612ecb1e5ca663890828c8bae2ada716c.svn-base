/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   GetCurrentLocation.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-8-18
 */
package info.ericyue.es.util;


import info.ericyue.es.activity.ExpressDelivery;
import info.ericyue.es.activity.ExpressSensor;
import info.ericyue.es.activity.SettingsActivity;
import info.ericyue.es.activity.WiFiAP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

//import com.google.android.maps.GeoPoint;
//百度地图
import com.baidu.platform.comapi.basestruct.GeoPoint;
import  com.baidu.mapapi.MKGeneralListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.location.BDLocationListener;

//百度地图


import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class LocationUtil{
	private LocationManager locationManager;
	private String id;//存放用户id
	private String worker_id;
	private Context context;
	private WiFiAP wifiap;
	private Bundle bundle;
	private long[] pattern = {1800,300}; 
	private boolean wifiopen=false;
	private NotificationManager notificationManager; 
	private LocationClient mLocationClient;
	private BDLocationListener myListener;
	public Vibrator mVibrator;
	public GeofenceClient mGeofenceClient;
	public BDLocation mylocation;
	private SensorManager sm ;
	private int sensorType;
	public LocationUtil(Bundle bundle,LocationManager locationManager,Context context,String worker_id,WiFiAP wifiap){
		
		this.locationManager=locationManager;
		this.context=context;
		this.worker_id=worker_id;
		this.id = getIdByUsername(worker_id);
		Log.i("id in service is",id);
		this.wifiap=wifiap;
		this.bundle=bundle;
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
	}
	public LocationUtil(LocationManager locationManager,Context context,String worker_id,Application application){
		this.locationManager=locationManager;
		this.context=context;
		this.worker_id=worker_id;
		this.id = getIdByUsername(worker_id);
		Log.i("id in service is",id);
		sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorType = android.hardware.Sensor.TYPE_ACCELEROMETER;
        mVibrator =(Vibrator)application.getSystemService(Service.VIBRATOR_SERVICE);
        mGeofenceClient = new GeofenceClient(application.getApplicationContext());
		mLocationClient = new LocationClient(application.getApplicationContext());
		setLocOption(mLocationClient);
		myListener = new MyLocationListenner();
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();
		
	}
	private void setLocOption(LocationClient mLocClient){
		
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度,默认值gcj02

		//当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation( )后，每隔设定的时间，定位SDK就会进行一次定位。
		//当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。
		option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms

//		option.disableCache(true);//true表示禁用缓存定位，false表示启用缓存定位。
//		option.setPoiNumber(5);	//最多返回POI个数	
//		option.setPoiDistance(1000); //poi查询距离		
//		option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息		

		mLocClient.setLocOption(option);
		
	}
	private String getIdByUsername(String username) {
		String queryString="username="+worker_id;
    	String url=HttpUtil.BASE_URL+"servlet/QueryUser?"+queryString;
    	String id=HttpUtil.queryStringForPost(url);
    	return id;
	}
	 public void updateWithNewLocation(BDLocation location) {   
         String latLongString;   
         if (location != null) {   
        	 double lat = location.getLatitude();   
        	 double lng = location.getLongitude();   
        	 latLongString = "纬度:" + lat + "   经度:" + lng;
        	 Log.i("loc:",latLongString);
         } else {   
        	 latLongString = "无法获取地理信息";   
         }   
         Log.i("快递员坐标", "您当前的位置是:" + latLongString);   
	 }   
	 public class MyLocationListenner implements BDLocationListener{

			@Override
			public void onReceiveLocation(BDLocation mlocation) {
				if (mlocation==null) {
					System.out.println("location = null");
//					Toast.makeText(getApplicationContext(), "获取位置信息失败", Toast.LENGTH_LONG).show();
					return;
				}
//				System.out.println(mlocation.getLocType());
				if(mlocation.getLocType()== 161 || mlocation.getLocType() == 61){
					mylocation = mlocation;
//					getGeoByLocation(location);
					updateWithNewLocation(mlocation); 
//					GetUserLocation();
					
				}
//				Toast.makeText(getApplicationContext(), "定位成功："+location.getAddrStr(), Toast.LENGTH_LONG).show();
//				showMyLocationOnMap(location.getLatitude(), location.getLongitude());
//				System.out.println(mlocation.getLatitude()+" "+mlocation.getLongitude());
			}
	 }
//	public final BDLocationListener locationListener = new BDLocationListener() {
//
//		@Override
//		public void onReceiveLocation(BDLocation arg0) {
//			// TODO Auto-generated method stub
//			
//		}   
//       
//	};
	public GeoPoint GetWorkerLocation(){	
//		 	Criteria criteria = new Criteria();   
//	        criteria.setAccuracy(Criteria.ACCURACY_FINE);   
//	        criteria.setAltitudeRequired(false);   
//	        criteria.setBearingRequired(false);   
//	        criteria.setCostAllowed(true);   
//	        criteria.setPowerRequirement(Criteria.POWER_LOW);   
//	        String provider = locationManager.getBestProvider(criteria, true);   
//	        BDLocation location = locationManager.getLastKnownLocation(provider);   
//	        updateWithNewLocation(location);   
//	        locationManager.requestLocationUpdates(provider, 2000, 10,   
//	                        locationListener);  
		if(mylocation!=null){
			double geoLatitude = mylocation.getLatitude()*1E6; 
			double geoLongitude = mylocation.getLongitude()*1E6; 
			HttpUtil.UpdateUserCurrentLocation(worker_id,""+mylocation.getLatitude(),""+mylocation.getLongitude());
			return new GeoPoint((int) geoLatitude, (int) geoLongitude);
			}
		else{
			return new GeoPoint(0,0);
		}
	}

	public double GetDistance(GeoPoint gp1,GeoPoint gp2){
		double Lat1r = ConvertDegreeToRadians(gp1.getLatitudeE6()/1E6);
		double Lat2r = ConvertDegreeToRadians(gp2.getLatitudeE6()/1E6);
		double Long1r= ConvertDegreeToRadians(gp1.getLongitudeE6()/1E6);
		double Long2r= ConvertDegreeToRadians(gp2.getLongitudeE6()/1E6);
		/* 地球半径(KM) */
		double R = 6371;
		double d = 
				Math.acos(Math.sin(Lat1r)*Math.sin(Lat2r)+
				Math.cos(Lat1r)*Math.cos(Lat2r)*
                Math.cos(Long2r-Long1r))*R;
		return d*1000;
	}
	public double ConvertDegreeToRadians(double degrees){
		return (Math.PI/180)*degrees;
	}
	public boolean CloseToEachOther(GeoPoint gp1,GeoPoint gp2,int miles){
	/**
	 * 按汽车每小时60KM/h,大约快递员到达20分钟前，返回true。
	 */
		Log.i("Moonlight","目前距离"+GetDistance(gp1,gp2));
		if(GetDistance(gp1,gp2)<miles)
			return true;
		else 
			return false;
	}
	public boolean verifyUser(String msg) throws Exception{
//		worker_id+user_id+username+user_pwd+user_truename;
		String tmp=HttpUtil.QueryBaseInfo(id);
		String[] tmp2=tmp.split("/");
		Log.i("tmp is",tmp);
		/**
		 * id/username/password/truename
		 */
		String st=worker_id+tmp2[0]+tmp2[1]+tmp2[2]+tmp2[3];
		return true;
//				EncryptString.encryptString(st,"MD5").equals(msg);
	}
	public void executeListen() throws IOException, Exception{
		String loc=HttpUtil.queryCurrentLocation();
		if(loc==null||loc.length()==0||loc.equals("")||loc.equals(" ")){
			Log.i("find null", "find null...........................");
			wifiap.closeWiFiAP();
			return;
		}
		else{
			Log.i("用户当前位置:" ,HttpUtil.queryCurrentLocation());
			String user_location[]=loc.split("#");			
			for(int i=0;i<user_location.length;++i){
				String tmp[]=user_location[i].split(",");
				id=tmp[0];//用户ID
				int lat=(int) (Double.parseDouble(tmp[1])*1E6);
				int lng=(int) (Double.parseDouble(tmp[2])*1E6);
				if(CloseToEachOther(new GeoPoint(lat,lng),GetWorkerLocation(),2000)){
				/**
				 * 距离接近,发送提醒短信。	
				 */
					String name=HttpUtil.queryTradeInfo("receiver_id", id, "receiver_name");
					String tradenumber=HttpUtil.queryTradeInfo("receiver_id", id, "trade_number");
					String smsMessage="尊敬的顾客["+name+"]您好,您的包裹即将到达,请到收发室领取,一小时内不到默认退回。[配送方:Express Sensor]";
					String phone=HttpUtil.queryTradeInfo("receiver_id", id, "receiver_phone");
					Log.i("Moonlight","id:"+id+" 发现近距离客户端 2000m "+phone);
					if(HttpUtil.SentMsgDone(id).equals("0")){
						Log.i("UTIL", HttpUtil.SentMsgDone(id));
						sendMsg(phone,smsMessage,name);
					}
					if(HttpUtil.QueryTradeProcessed(id).equals("1")){
						return;
					}
					if(HttpUtil.queryTradeInfo("receiver_id", id, "goods_status").equals("2")){
	        			/**
		        		 *向主Activity发送广播
		        		 */
	        			Intent fine = new Intent("SENT");
		                fine.putExtra("MESSAGE","客户["+name+"]投递完毕");
		                context.sendBroadcast(fine);
		                int icon = android.R.drawable.stat_notify_more;
		                long when = System.currentTimeMillis();
		                // 第一个参数为图标,第二个参数为标题,第三个为通知时间
		                Notification notification = new Notification(icon, null, when);
		                Intent openintent = new Intent(context, ExpressDelivery.class);
		                openintent.putExtras(bundle);
		                // 当点击消息时就会向系统发送openintent意图
		                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,openintent, 0);
		                notification.setLatestEventInfo(context,"有新的投递完成", "客户["+name+"]投递完毕", contentIntent);
		                notification.defaults=Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;  
		                notification.vibrate=pattern;
		                notificationManager.notify(0, notification);
		                HttpUtil.UpdateTradeProcessed(id);
	        		}
					if(HttpUtil.queryTradeInfo("receiver_id", id, "goods_status").equals("3")){
	        			/**
		        		 *向主Activity发送广播
		        		 */
						Intent fine = new Intent("SENT");
		                fine.putExtra("MESSAGE","客户["+name+"]拒绝签收");
		                context.sendBroadcast(fine);
		                int icon = android.R.drawable.stat_notify_more;
		                long when = System.currentTimeMillis();
		                // 第一个参数为图标,第二个参数为标题,第三个为通知时间
		                Notification notification = new Notification(icon, null, when);
		                Intent openintent = new Intent(context, ExpressDelivery.class);
		                openintent.putExtras(bundle);
		                // 当点击消息时就会向系统发送openintent意图
		                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,openintent, 0);
		                notification.setLatestEventInfo(context,"有投递拒签", "客户["+name+"]投递拒签", contentIntent);
		                notification.defaults=Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;  
		                notification.vibrate=pattern;
		                notificationManager.notify(0, notification);      
		                HttpUtil.UpdateTradeProcessed(id);
	        		}
					if(SettingsActivity.getWIFIReceive(context)){
						if(CloseToEachOther(new GeoPoint(lat,lng),GetWorkerLocation(),200)){
						    Log.i("快递端", "距离为200m时,打开WIFI");
							/**
							 * 距离为200m时，开始启动NFC近场收货功能；
							 */
						    	if(!wifiopen){
							        /**
							         * wifiopen，确保wifiAP只启动一次。
							         */
								    	Log.i("快递端", "WiFi热点接收模式");
							        	wifiap=new WiFiAP(context);
							        	wifiap.StartWiFiAP();
										wifiopen=true;
										/**
										 * 如果客户端进入快递员WIFI范围内，表明其来取快递。
										 */
						    	}      
						}//endif
					}
				}
			}
		}
	}	
	//
	public void sendMsg(String phone,String smsMessage,String name){
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent mPI = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
		smsManager.sendTextMessage(phone, null,smsMessage, mPI, null); 
		Toast.makeText(context, "信息已经发送到 "+name,Toast.LENGTH_SHORT).show();
		HttpUtil.UpdateMsgDone(id);
	}
	public UrlEncodedFormEntity makeEntity(){
		Date curDate = new Date();
		String date=new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(curDate);
		GeoPoint gp=GetWorkerLocation();
		if(gp==null){
			gp=new GeoPoint(0,0);
		}
		double lng=gp.getLongitudeE6()/1E6;
		double lat=gp.getLatitudeE6()/1E6;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", worker_id));
		params.add(new BasicNameValuePair("date", date));
		params.add(new BasicNameValuePair("lng", lng+""));
		params.add(new BasicNameValuePair("lat", lat+""));
		try {
			return new UrlEncodedFormEntity(params,HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean submitGPSToServer(){
		String url = HttpUtil.BASE_URL+"servlet/GPSPost";
		HttpPost request = HttpUtil.getHttpPost(url);
		request.setEntity(makeEntity());
		String result= HttpUtil.queryStringForPost(request);
		if(result!=null&&result.equals("1"))
			return true;
		return false;
		}
	 }

