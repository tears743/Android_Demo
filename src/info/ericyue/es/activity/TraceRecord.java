/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   TraceRecord.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-8-8
 */
package info.ericyue.es.activity;

import info.ericyue.es.util.HttpUtil;
import info.ericyue.es.util.MyOverLay;
//import info.ericyue.es.util.MyPositionItemizedOverlay;
import info.ericyue.es.R;
import java.util.List;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;
import android.location.Criteria;
import android.location.LocationListener;

//百度定位包
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
import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import  com.baidu.mapapi.MKGeneralListener;
//百度定位包
//import com.google.android.maps.MapController;
//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapView;
//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.Overlay;
//import com.google.android.maps.OverlayItem;

public class TraceRecord  extends Activity{
	
	//百度地图
	private BMapManager mBMapManager;
	private BDLocation location; 
	private LocationClient mLocationClient;
	private BDLocationListener myListener;
	public Vibrator mVibrator;
	public GeofenceClient mGeofenceClient;
	
	//百度地图
	private String locationPrivider="";
	private int zoomLevel=0;
	private GeoPoint gp1;
	private GeoPoint gp2;
	private double distance=0;
	private MapView mapView;
	private MapController mapController; 
	private LocationManager locationManager;
	private Drawable begin,end,user;
//	private MyPositionItemizedOverlay itemizedOverlay;
	private List<Overlay> mapOverlays;
	private Bundle bundle;
	private GeoPoint userPoint =new GeoPoint(0,0);
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, TraceRecord.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	@Override 
	public void onCreate(Bundle savedInstanceState){ 
		super.onCreate(savedInstanceState); 
		//百度地图
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		mBMapManager = new BMapManager(getApplication()); 
		mBMapManager.init(null); 
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		mLocationClient = new LocationClient(this.getApplicationContext());
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		setLocOption(mLocationClient);
		myListener = new MyLocationListenner();
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();
		//百度地图
		setContentView(R.layout.tracerecord); 
		bundle=this.getIntent().getExtras();
		mapView = (MapView)findViewById(R.id.mapView); 
		mapController = mapView.getController();
		zoomLevel = 10;//初始放大等级
		mapController.setZoom(zoomLevel); 
		locationManager = (LocationManager)
        getSystemService(Context.LOCATION_SERVICE);		
		mapView.setBuiltInZoomControls(true);//可以多点触摸放大  
		mapOverlays=mapView.getOverlays();
		begin=this.getResources().getDrawable(R.drawable.begin_markers);
		end=this.getResources().getDrawable(R.drawable.end_markers);
		user=this.getResources().getDrawable(R.drawable.user_markers);
		
		
		userPoint= GetUserGeoPointAndDraw();
		updateMapView();
	  }
	
	public class MyLocationListenner implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation mlocation) {
			if (mlocation==null) {
				System.out.println("location = null");
//				Toast.makeText(getApplicationContext(), "获取位置信息失败", Toast.LENGTH_LONG).show();
				return;
			}
//			System.out.println(mlocation.getLocType());
			if(mlocation.getLocType()== 161 || mlocation.getLocType() == 61){
				location = mlocation;
				
//				getGeoByLocation(location);
				showMyLocationOnMap(location.getLatitude(), location.getLongitude());
				
			}
//			Toast.makeText(getApplicationContext(), "定位成功："+location.getAddrStr(), Toast.LENGTH_LONG).show();
			
//			System.out.println(mlocation.getLatitude()+" "+mlocation.getLongitude());
		}
		

	}
	private void showMyLocationOnMap(double latitude,double longitude){
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mapView);
		MyLocationOverlay goodLocationOverlay = new MyLocationOverlay(mapView);
//		GeoPoint workerGeo= new GeoPoint();
		LocationData locData = new LocationData();
		locData.latitude = userPoint.getLatitudeE6()/1E6;
		locData.longitude = userPoint.getLongitudeE6()/1E6;
		locData.direction = 2.0f;
		myLocationOverlay.setData(locData);
		myLocationOverlay.setMarker(user);
		mapView.getOverlays().add(myLocationOverlay);
		
		
		LocationData wlocData = new LocationData();
		wlocData.latitude =latitude;
		wlocData.longitude = longitude;
		wlocData.direction = 2.0f;
		goodLocationOverlay.setData(wlocData);
		goodLocationOverlay.setMarker(end);
		mapView.getOverlays().add(goodLocationOverlay);
		
		
 
		mapView.refresh();
		mapController.animateTo(new GeoPoint((int)(wlocData.latitude*1e6),(int)(wlocData.longitude* 1e6)));
//		mapController.setCenter(new GeoPoint((int)(locData.latitude*1e6),(int)(locData.longitude* 1e6)));
		
//		Toast.makeText(this,"您距离您的包裹大约"+(int)GetDistance(new GeoPoint((int)(locData.latitude*1e6),(int)(locData.longitude* 1e6)) ,workerGeo)+"米", Toast.LENGTH_LONG).show();
		
	}
@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mLocationClient.stop();
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mLocationClient.stop();
		super.onPause();
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		mLocationClient.start();
		super.onRestart();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mLocationClient.start();
		super.onResume();
	}
private void setLocOption(LocationClient mLocClient){
		
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("gcj02");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(2000);//设置发起定位请求的间隔时间为10000ms
		mLocClient.setLocOption(option);
	}
	public GeoPoint GetUserGeoPointAndDraw(){
		GeoPoint workGeoPoint =new GeoPoint(0,0) ;
		String loc=HttpUtil.queryCurrentLocation();
		Log.i("TRacerecord", loc);
		if(loc==null||loc.length()==0||loc.equals(" "))
			return workGeoPoint;
		String[] locations=loc.split("#");
		for(int i=0;i!=locations.length;++i){
			String[] tmp=locations[i].split(",");
			workGeoPoint= new GeoPoint((int)(Double.parseDouble(tmp[1])*1E6),(int)(Double.parseDouble(tmp[2])*1E6));
		}
			return workGeoPoint;
	}
//	public void addMark(GeoPoint point,Drawable icon){
//		OverlayItem overlayItem = new OverlayItem(point,"","");
//		itemizedOverlay = new MyPositionItemizedOverlay(icon);
//		itemizedOverlay.addOverlay(overlayItem);
//		mapOverlays.add(itemizedOverlay);
//	}
	/* MapView的Listener */
//	public final LocationListener mLocationListener = 
//		new LocationListener(){ 
//		@Override 
//		public void onLocationChanged(Location location){} 
//		@Override 
//		public void onProviderDisabled(String provider){} 
//		@Override 
//		public void onProviderEnabled(String provider){} 
//		@Override 
//		public void onStatusChanged(String provider,int status,Bundle extras){} 
//	}; 

	/* 取得GeoPoint的method */ 
//	private GeoPoint getGeoByLocation(Location location){ 
//		GeoPoint gp = null; 
//		try{ 
//			if(location != null){ 
//				double geoLatitude = location.getLatitude()*1E6; 
//				double geoLongitude = location.getLongitude()*1E6; 
//				gp = new GeoPoint((int) geoLatitude, (int) geoLongitude);
//			} 
//		} 
//		catch(Exception e){ 
//			e.printStackTrace(); 
//		}
//		return gp;
//	} 
	/* 取得LocationProvider */
//	public void getLocationPrivider(){ 
//		Criteria criteria = new Criteria();
////		criteria.setAccuracy(Criteria.ACCURACY_FINE); 
////	    criteria.setAltitudeRequired(false); 
////	    criteria.setBearingRequired(false); 
////	    criteria.setCostAllowed(true); 
////	    criteria.setPowerRequirement(Criteria.POWER_LOW); 
//	    locationPrivider = locationManager.getBestProvider(criteria, true);
//	    if(locationPrivider != null){
//	    
//	    	locationManager.getLastKnownLocation(locationPrivider); 
//	    }
//	}
	private void setStartPoint(){  
//		addMark(gp1,begin);
	}
//	private void setRoute(){  
//		int mode=2;
//		MyOverLay mOverlay = new MyOverLay(gp1,gp2,mode); 
//		List<Overlay> overlays = mapView.getOverlays(); 
//		overlays.add(mOverlay);
//	}
//	private void setEndPoint(){  
//		addMark(gp2,end);
//	}
	/* 重设Overlay的method */
	private void resetOverlay(){
		List<Overlay> overlays = mapView.getOverlays(); 
		overlays.clear();
	} 
	/* 更新MapView的method */
	public void refreshMapView(){ 
		mapView.displayZoomControls(true); 
		MapController myMC = mapView.getController(); 
		myMC.animateTo(gp2); 
		myMC.setZoom(zoomLevel); 
		mapView.setSatellite(false); 
	} 
	/* 取得两点间的距离的method */
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
	private double ConvertDegreeToRadians(double degrees){
		return (Math.PI/180)*degrees;
	}
	/* format移动距离的method */
	public String format(double num){
		NumberFormat formatter = new DecimalFormat("###");
		String s=formatter.format(num);
		return s;
	}
//	@Override
//	protected boolean isRouteDisplayed(){
//		return false;
//	}
	public void updateMapView(){
		int mode=2;
//		resetOverlay();
		distance=0;
		String un=HttpUtil.GetWorkerTrace(bundle.getString("worker_id"));
		if(un==null){
			return;
		}
		String[] gpsline=un.split("#");
		/**
		 * 画起点
		 */
		String []tmp=gpsline[0].split(",");
		gp2=new GeoPoint((int)(Double.parseDouble(tmp[1])*1E6),(int)(Double.parseDouble(tmp[2])*1E6));
		gp1=gp2;
//		addMark(gp2,begin);
		refreshMapView();
		/**
		 * 画路线
		 */
		for(int i=1;i<gpsline.length-1;++i){
			
			tmp=gpsline[i].split(",");
			if(gpsline[i].equals("")||gpsline[i].equals(" ")||gpsline.length==0){
				break;
			}
			gp2=new GeoPoint((int)(Double.parseDouble(tmp[1])*1E6),(int)(Double.parseDouble(tmp[2])*1E6));
//			MyOverLay mOverlay = new MyOverLay(gp1,gp2,mode); 
//			List<Overlay> overlays = mapView.getOverlays(); 
//			overlays.add(mOverlay);
			refreshMapView();
			distance+=GetDistance(gp1,gp2);
			gp1=gp2;
			
		}	
		/**
		 * 画终点
		 */
		tmp=gpsline[gpsline.length-1].split(",");
		gp2=new GeoPoint((int)(Double.parseDouble(tmp[1])*1E6),(int)(Double.parseDouble(tmp[2])*1E6));
//		addMark(gp2,end);
		refreshMapView();
		Toast.makeText(this, "您目前移动了大约:"+(int)distance+"米", Toast.LENGTH_LONG).show();
	}
} 


