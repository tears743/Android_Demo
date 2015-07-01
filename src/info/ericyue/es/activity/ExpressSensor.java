/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * Project Name:ExpressSensor
 * Create Date: 2011-7-17
 */
package info.ericyue.es.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import viewbadger.demo.Cheeses;
//import viewbadger.demo.DemoActivity.BadgeAdapter.ViewHolder;

import info.ericyue.es.R;
import info.ericyue.es.util.HttpUtil;
import info.ericyue.es.util.TutorialDialog;
import info.ericyue.es.zxing.client.android.CaptureActivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.readystatesoftware.viewbadger.BadgeView;


public class ExpressSensor extends Activity{
	private GridView listView;
	private Bundle bundle;
	private TextView infoWall;
	private ProgressBar progressbar;
	private serviceReceiver receiver;
	private LocationManager locationManager;
	private int id;
	private String username,role;
	private boolean show_tutorial;
	private long exitTime = 0; //确定是短时间按下了两次返回键
	public Boolean[] badgerflages = new Boolean[19];
	BadgeView badge1;
	BadgeAdapter listItemAdapter;
	
	SharedPreferences sp,searchSp,chaufferSearchSp;
	Intent pushInten ;
	
	private static final String[] DATA = Cheeses.sCheeseStrings;
	
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, ExpressSensor.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
	    	if((System.currentTimeMillis()-exitTime) > 2000){  
	    		Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                exitTime = System.currentTimeMillis();  
	    	}  
	    	else{  	
//	          Intent location = new Intent( ExpressSensor.this, LocationService.class );
//	          Intent record = new Intent( ExpressSensor.this, TraceRecordService.class );
//	          /* 以stopService方法关闭Intent */
//	          stopService(location);
//	          stopService(record);
//	          stopService(pushInten);
	          finish();
	          System.exit(0);  
	    	}  
	    	return true;  
	    }  
	    return super.onKeyDown(keyCode, event);  
	}  
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
//		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
//		nm.cancel(1);
		
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//不显示标题栏
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
//		progressbar=(ProgressBar) findViewById(R.id.ProgressBar);
		infoWall=(TextView) findViewById(R.id.infoWall);
		show_tutorial = SettingsActivity.getShowTutorial(this);
//		headInfo(true,"欢迎进入Express Sensor(快递版)");
		listView = (GridView)findViewById(R.id.HomeListView);
		bundle=this.getIntent().getExtras();
		ImageView imView = (ImageView)findViewById(R.id.leftHead);
		BadgeView badge = new BadgeView(this, imView);
		badge.setText("1");
		badge.show();
		for(int i=0;i<19;i++){
		badgerflages[i] = false;
		}
		 if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	        }
		Runnable net = new Runnable() {
			
			@Override
			public void run() {
				username = bundle.getString("username");
				id=Integer.parseInt(getIdByUsername(bundle.getString("username")));
				role = getRole(id);
				// TODO Auto-generated method stub
				
			}
		};
		net.run();
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		openGPSSettings();
		bundle.putString("worker_id",bundle.getString("username"));
//		if(!isClient()){			
//			showDialog("账户类型与该版本客户端不匹配");
//		}
//		else{
//		1 货主2车主3司机
//		
		if(role.equals("1")){
			CargofillItemList();
			infoWall.setText("\n"+username+"   普通用户"+"\n\n 货源公司");
			infoWall.setTextColor(0xffdcdcdc);
		}
		else if(role.equals("2")){
			CarFillItemList();
			infoWall.setText("\n"+username+"   普通用户"+"\n\n 物流公司或车主");
			infoWall.setTextColor(0xffdcdcdc);
		}
		else{
			infoWall.setText("\n"+username+"   普通用户"+"\n\n 配货站");
			infoWall.setTextColor(0xffdcdcdc);
			ChauffeurFillItemList();
			
		}
			
			/**
			 * 开启系统服务监听。
			 */
			pushInten = new Intent(ExpressSensor.this,PushService.class);
			searchSp = getSharedPreferences("CargoRemind", MODE_MULTI_PROCESS);
			chaufferSearchSp= getSharedPreferences("ChaufferRemind", MODE_MULTI_PROCESS);
			sp = getSharedPreferences("config",MODE_MULTI_PROCESS);
			if(sp.getBoolean("RemindStatus",false)){
				
				startService(pushInten);
				
			}
			else{
				stopService(pushInten);
			}
			Intent i = new Intent(this, LocationService.class );
	        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
	        i.putExtras(bundle);
//	        startService(i); 
	        if(show_tutorial){
				showTutorial();
			}
	        if(SettingsActivity.getTraceRecord(this)){
	        	Intent tr = new Intent(this, TraceRecordService.class );
		        tr.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		        tr.putExtras(bundle);
//		        startService(tr); 
	        }
	        else{
	        	Toast.makeText(this, "轨迹记录服务关闭", Toast.LENGTH_LONG).show();
	        }
		}
//	}	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {}
	/**
	 * MenuInflater 用来解析定义在menu目录下的菜单布局文件
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home, menu);
		return super.onCreateOptionsMenu(menu);
	}
	/**
	 * 按下菜单后的响应
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.about_menu_item) {
			About.launch(ExpressSensor.this,bundle);
		} else if (itemId == R.id.about_menu_min) {
			Intent i= new Intent(Intent.ACTION_MAIN);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.addCategory(Intent.CATEGORY_HOME);
			startActivity(i);
		} else if (itemId == R.id.settings_menu_item) {
			SettingsActivity.launch(ExpressSensor.this,bundle);
		}
		return super.onOptionsItemSelected(item);
	}
	private void showDialog(String msg){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("错误").setMessage(msg).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
			}
		});
		AlertDialog alert=builder.create();
		alert.show();
	}
	private void showGPSWait(String msg){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("GPS未开启").setMessage(msg).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				 Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);  
			        startActivityForResult(intent,0); //此为设置完成后返回到获取界面   
			}
		});
		AlertDialog alert=builder.create();
		alert.show();
	}
	private  class BadgeAdapter extends SimpleAdapter {
		private List<HashMap<String, Object>> data;
		private LayoutInflater mInflater;
        private Context mContext;
        private int mResource;
        
		public BadgeAdapter(Context context,
				List<HashMap<String, Object>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			this.data=data;
			mInflater = LayoutInflater.from(context);
			mResource=resource;
			mContext=context;
		}

		
        private static final int droidRed = Color.RED;
//        public SimpleAdapter(Context context) {
//            mInflater = LayoutInflater.from(context);
//            mContext = context;
//        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(mResource, parent,false);
                holder = new ViewHolder();
                holder.imageView = (ImageView)convertView.findViewById(R.id.leftHead);
                holder.text = (TextView) convertView.findViewById(R.id.PurpleRowTextView);
                holder.badge = new BadgeView(mContext, holder.imageView);
                holder.badge.setBadgeBackgroundColor(droidRed);
                holder.badge.setTextColor(Color.BLACK);
                holder.badge.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
                holder.badge.setTextSize(8);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
//            holder.text.setText(data.get(position).get("ItemTitle"));
            setViewText(holder.text, data.get(position).get("ItemTitle").toString());
            setViewImage(holder.imageView, data.get(position).get("ItemImage").toString());
//            if (position % 3 == 0) {
            	holder.badge.setText("new!");
            	if(badgerflages[position]){
            	holder.badge.show();
            	}
            	else {
            	holder.badge.hide();
            }
//            
            
            return convertView;
        }

         class ViewHolder {
            ImageView imageView;
        	TextView text;
            BadgeView badge;
        }
    
		
		
	}
	public void openGPSSettings(){   
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS模块正常",Toast.LENGTH_LONG).show();
            return;
        }
        showGPSWait("按确定转入手机GPS设置!");
    }
	public String getRole(int id){
    	return HttpUtil.queryRole(id);
    }
	private String getIdByUsername(String username) {
		String queryString="username="+username;
    	String url=HttpUtil.BASE_URL+"servlet/QueryUser?"+queryString;
    	String id=HttpUtil.queryStringForPost(url);
    	return id;
	}
	final void showTutorial() {
		boolean showTutorial = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("show_tutorial", true);
		if (showTutorial) {
			final TutorialDialog dlg = new TutorialDialog(this);
			dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					CheckBox cb = (CheckBox) dlg.findViewById(R.id.show_tutorial);
					if (cb != null && cb.isChecked()) {
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ExpressSensor.this);
						prefs.edit().putBoolean("show_tutorial", false).commit();
					}
				}
			});
			dlg.show();
		} else {

		}
	}
	
	private void CargofillItemList(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		String[] itemStr={"GPS绑定","查看地图","扫描条码",
							"关于产品","货源发布",
							"车源查询","司机查询","货源管理","提醒","车源提醒",
							"司机提醒"};
		Integer[] iconBag={R.drawable.globalinfo,
				R.drawable.trace,
				R.drawable.qrcode,R.drawable.about,
				
				R.drawable.icon8,R.drawable.icon7,
				
				R.drawable.chauffeursearch,
				R.drawable.icon14,
				R.drawable.v1_about_user_set,R.drawable.icon11,
				R.drawable.icon10};
 		for(int i=0;i<11;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();  
		    map.put("ItemImage", iconBag[i]);//图像资源的ID  
		    map.put("ItemTitle", itemStr[i]);
		    listItem.add(map); 
		}
	    listItemAdapter = new BadgeAdapter(this,listItem,//数据源   
	    	R.layout.purple_row,  
	    	//动态数组与ImageItem对应的子项          
	    	new String[] {"ItemImage","ItemTitle"},   
	    	
	    	//ImageItem的XML文件里面的一个ImageView,两个TextView ID  
	    	new int[] {R.id.leftHead,R.id.PurpleRowTextView}  
	    ); 
//	    BadgeAdapter myAdapter = new BadgeAdapter(listItemAdapter);
		listView.setAdapter(listItemAdapter);
		listView.setOnItemClickListener(cargoItemListener);
	}
	
	private void ChauffeurFillItemList(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		String[] itemStr={"GPS绑定","查看地图","扫描条码",
							"关于产品","货源查询",
							"货源发布","车源发布","车源查询","货源管理","车源管理","提醒","货源提醒",
							"车源提醒"};
		Integer[] iconBag={R.drawable.globalinfo,
				R.drawable.trace,
				R.drawable.qrcode,R.drawable.about,
				R.drawable.icon2,
				R.drawable.icon1,
				R.drawable.chauffeurrelease,
				R.drawable.chauffeursearch,
				R.drawable.chauffeurmanager,
				R.drawable.icon14,
				R.drawable.v1_about_user_set,R.drawable.icon3,
				R.drawable.icon11};
 		for(int i=0;i<13;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();  
		    map.put("ItemImage", iconBag[i]);//图像资源的ID  
		    map.put("ItemTitle", itemStr[i]);
		    listItem.add(map); 
		}
	    listItemAdapter = new BadgeAdapter(this,listItem,//数据源   
	    	R.layout.purple_row,  
	    	//动态数组与ImageItem对应的子项          
	    	new String[] {"ItemImage","ItemTitle"},   
	    	
	    	//ImageItem的XML文件里面的一个ImageView,两个TextView ID  
	    	new int[] {R.id.leftHead,R.id.PurpleRowTextView}  
	    ); 
//	    BadgeAdapter myAdapter = new BadgeAdapter(listItemAdapter);
		listView.setAdapter(listItemAdapter);
		listView.setOnItemClickListener(chauffeurItemListener);
	}
	
	private void CarFillItemList(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		String[] itemStr={"GPS绑定","查看地图","扫描条码",
							"关于产品","货源查询","车源发布",
							"车源管理","司机查询","提醒","货源提醒",
							"司机提醒"
							};
		Integer[] iconBag={
				R.drawable.globalinfo,
				R.drawable.trace,
				R.drawable.qrcode,
				R.drawable.about,
				R.drawable.icon2,
				R.drawable.icon7,
				R.drawable.icon14,
				R.drawable.chauffeursearch,
				R.drawable.v1_about_user_set,
				R.drawable.icon3,
				R.drawable.icon10
				};
 		for(int i=0;i<11;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();  
		    map.put("ItemImage", iconBag[i]);//图像资源的ID  
		    map.put("ItemTitle", itemStr[i]);
		    listItem.add(map); 
		}
	    listItemAdapter = new BadgeAdapter(this,listItem,//数据源   
	    	R.layout.purple_row,  
	    	//动态数组与ImageItem对应的子项          
	    	new String[] {"ItemImage","ItemTitle"},   
	    	
	    	//ImageItem的XML文件里面的一个ImageView,两个TextView ID  
	    	new int[] {R.id.leftHead,R.id.PurpleRowTextView}  
	    ); 
//	    BadgeAdapter myAdapter = new BadgeAdapter(listItemAdapter);
		listView.setAdapter(listItemAdapter);
		listView.setOnItemClickListener(carItemListener);
	}
	
	public void headInfo(boolean run,String msg){
//		if(!run)
//			progressbar.setVisibility(View.GONE);
//		else 
//			progressbar.setVisibility(View.VISIBLE);
		infoWall.setText(msg);
	}
	
//	"GPS绑定","查看地图","扫描条码",
//	"关于产品","货源查询",
//	"货源发布","车源发布","车源查询","货源管理","车源管理","提醒","货源提醒",
//	"车源提醒"
	
	private OnItemClickListener chauffeurItemListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			switch(position){
			case 0:
//				badgerflages[position]=true;
				GlobalInfo.launch(ExpressSensor.this,bundle);
				break;
			case 1:
//				badgerflages[position]=true;
//				listItemAdapter.notifyDataSetChanged();
				TraceRecord.launch(ExpressSensor.this,bundle);
				break;
			case 2:
				
//				badgerflages[position]=true;
				CaptureActivity.launch(ExpressSensor.this,bundle);
				break;	
			case 3:
//				badgerflages[position]=true;
//				synchronized(listView){
////				listView.notifyAll();
//				}
				About.launch(ExpressSensor.this,bundle);
				break;
			case 4:
//				badgerflages[position]=true;
//				listView.notifyAll();
				CargoInfoSearch.launch(ExpressSensor.this,bundle);
				break;
			case 5:
//				badgerflages[position]=true;
//				listView.notifyAll();
				CargoRelease.launch(ExpressSensor.this, bundle);
				break;
			case 6:
//				badgerflages[position]=true;
//				listView.notifyAll();
				CarRelease.launch(ExpressSensor.this, bundle);
				break;
			case 7:
//				badgerflages[position]=true;
//				listView.notifyAll();
				CarInfoSearch.launch(ExpressSensor.this, bundle);
				break;
			case 8:
//				badgerflages[position]=true;
//				listView.notifyAll();
				ManagerHuoyuan.launch(ExpressSensor.this, bundle);
				break;
			case 9:
				ManagerCheyuan.launch(ExpressSensor.this, bundle);
				break;
			case 10:
				PushActivity.launch(ExpressSensor.this, bundle);
				break;
				
			case 11:
//				badgerflages[position]=false;
				listItemAdapter.notifyDataSetChanged();
//				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
				Intent intent = new Intent(ExpressSensor.this,CargoRemindItems.class);
//				CargoRemindItems.launch(ExpressSensor.this, bundle);
				intent.putExtra("cargoDetail", searchSp.getString("cargoDetail",""));
				ExpressSensor.this.startActivity(intent);
				break;
			case 12:
//				badgerflages[position]=false;
//				listItemAdapter.notifyDataSetChanged();
//////			bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
//				Intent chauffeurintent = new Intent(ExpressSensor.this,Car.class);
//////			CargoRemindItems.launch(ExpressSensor.this, bundle);
//				chauffeurintent.putExtra("chaufferDetail", chaufferSearchSp.getString("chaufferDetail",""));
//				ExpressSensor.this.startActivity(chauffeurintent);
				break;
			case 13:
//				badgerflages[position]=true;
//				listView.notifyAll();
				ManagerHuoyuan.launch(ExpressSensor.this, bundle);
				break;
			case 14:
//				badgerflages[position]=true;
//				synchronized(listView){
//					listView.notify;
//					}
				ManagerCheyuan.launch(ExpressSensor.this, bundle);
				break;
			case 15:
				
				PushActivity.launch(ExpressSensor.this, bundle);
				break;
			case 16:
//				badgerflages[position]=false;
//				listItemAdapter.notifyDataSetChanged();
////				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
//				Intent intent = new Intent(ExpressSensor.this,CargoRemindItems.class);
////				CargoRemindItems.launch(ExpressSensor.this, bundle);
//				intent.putExtra("cargoDetail", searchSp.getString("cargoDetail",""));
//				ExpressSensor.this.startActivity(intent);
//				break;
			case 18:
//				badgerflages[position]=false;
//				listItemAdapter.notifyDataSetChanged();
////				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
//				Intent chauffeurintent = new Intent(ExpressSensor.this,ChaufferRemindItems.class);
////				CargoRemindItems.launch(ExpressSensor.this, bundle);
//				chauffeurintent.putExtra("chaufferDetail", chaufferSearchSp.getString("chaufferDetail",""));
//				ExpressSensor.this.startActivity(chauffeurintent);
//				break;
//				SetChaufferRemind.launch(ExpressSensor.this, bundle);
				
				
			}	
		}
	};
	
//	"GPS绑定","查看地图","扫描条码",
//	"关于产品","货源查询","车源发布",
//	"司机发布","车源管理","提醒","货源提醒",
//	"司机提醒"
	
	private OnItemClickListener carItemListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			switch(position){
			case 0:
//				badgerflages[position]=true;
				GlobalInfo.launch(ExpressSensor.this,bundle);
				break;
			case 1:
//				badgerflages[position]=true;
//				listItemAdapter.notifyDataSetChanged();
				TraceRecord.launch(ExpressSensor.this,bundle);
				break;
			case 2:
				
//				badgerflages[position]=true;
				CaptureActivity.launch(ExpressSensor.this,bundle);
				break;	
			case 3:
//				badgerflages[position]=true;
//				synchronized(listView){
////				listView.notifyAll();
//				}
				About.launch(ExpressSensor.this,bundle);
				break;
			case 4:
//				badgerflages[position]=true;
//				listView.notifyAll();
				CargoInfoSearch.launch(ExpressSensor.this,bundle);
				break;
			case 5:
//				badgerflages[position]=true;
//				listView.notifyAll();
				CarRelease.launch(ExpressSensor.this, bundle);
				
				break;
			case 6:
				ManagerCheyuan.launch(ExpressSensor.this, bundle);
				break;
			case 7:
//				badgerflages[position]=true;
//				listView.notifyAll();
				ChauffeurSearch.launch(ExpressSensor.this, bundle);
				
				break;
			case 8:
//				badgerflages[position]=true;
//				listView.notifyAll();
				
				PushActivity.launch(ExpressSensor.this, bundle);
				break;
			case 10:
				badgerflages[position]=false;
				listItemAdapter.notifyDataSetChanged();
////				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
				Intent chauffeurintent = new Intent(ExpressSensor.this,ChaufferRemindItems.class);
////				CargoRemindItems.launch(ExpressSensor.this, bundle);
				chauffeurintent.putExtra("chaufferDetail", chaufferSearchSp.getString("chaufferDetail",""));
				ExpressSensor.this.startActivity(chauffeurintent);
				
				break;
			case 9:
				badgerflages[position]=false;
				listItemAdapter.notifyDataSetChanged();
//				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
				Intent intent = new Intent(ExpressSensor.this,CargoRemindItems.class);
//				CargoRemindItems.launch(ExpressSensor.this, bundle);
				intent.putExtra("cargoDetail", searchSp.getString("cargoDetail",""));
				ExpressSensor.this.startActivity(intent);
				break;
			case 11:
//				badgerflages[position]=false;
//				listItemAdapter.notifyDataSetChanged();
//////				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
//				Intent chauffeurintent = new Intent(ExpressSensor.this,ChaufferRemindItems.class);
//////				CargoRemindItems.launch(ExpressSensor.this, bundle);
//				chauffeurintent.putExtra("chaufferDetail", chaufferSearchSp.getString("chaufferDetail",""));
//				ExpressSensor.this.startActivity(chauffeurintent);
				break;
				
//			case 11:
//				badgerflages[position]=true;
//				listView.notifyAll();
//				ChauffeurSearch.launch(ExpressSensor.this, bundle);
//				break;
			case 12:
//				badgerflages[position]=true;
//				listView.notifyAll();
//				ChauffeurManager.launch(ExpressSensor.this, bundle);
				break;
			case 13:
//				badgerflages[position]=true;
//				listView.notifyAll();
//				ManagerHuoyuan.launch(ExpressSensor.this, bundle);
				break;
			case 14:
//				badgerflages[position]=true;
//				synchronized(listView){
//					listView.notify;
//					}
//				ManagerCheyuan.launch(ExpressSensor.this, bundle);
				break;
			case 15:
				
//				PushActivity.launch(ExpressSensor.this, bundle);
				break;
			case 16:
//				badgerflages[position]=false;
//				listItemAdapter.notifyDataSetChanged();
////				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
//				Intent intent = new Intent(ExpressSensor.this,CargoRemindItems.class);
////				CargoRemindItems.launch(ExpressSensor.this, bundle);
//				intent.putExtra("cargoDetail", searchSp.getString("cargoDetail",""));
//				ExpressSensor.this.startActivity(intent);
//				break;
			case 18:
//				badgerflages[position]=false;
//				listItemAdapter.notifyDataSetChanged();
////				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
//				Intent chauffeurintent = new Intent(ExpressSensor.this,ChaufferRemindItems.class);
////				CargoRemindItems.launch(ExpressSensor.this, bundle);
//				chauffeurintent.putExtra("chaufferDetail", chaufferSearchSp.getString("chaufferDetail",""));
//				ExpressSensor.this.startActivity(chauffeurintent);
//				break;
//				SetChaufferRemind.launch(ExpressSensor.this, bundle);
				
				
			}	
		}
	};
	
	
	private OnItemClickListener cargoItemListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			switch(position){
			case 0:
//				badgerflages[position]=true;
				GlobalInfo.launch(ExpressSensor.this,bundle);
				break;
			case 1:
//				badgerflages[position]=true;
//				listItemAdapter.notifyDataSetChanged();
				TraceRecord.launch(ExpressSensor.this,bundle);
				break;
			case 2:
				
//				badgerflages[position]=true;
				CaptureActivity.launch(ExpressSensor.this,bundle);
				break;	
			case 3:
//				badgerflages[position]=true;
//				synchronized(listView){
////				listView.notifyAll();
//				}
				About.launch(ExpressSensor.this,bundle);
				break;
			case 4:
//				badgerflages[position]=true;
//				listView.notifyAll();
				CargoRelease.launch(ExpressSensor.this,bundle);
				break;
			case 5:
//				badgerflages[position]=true;
//				listView.notifyAll();
				CarInfoSearch.launch(ExpressSensor.this, bundle);
				break;
			case 6:
//				badgerflages[position]=true;
//				listView.notifyAll();
				ChauffeurSearch.launch(ExpressSensor.this, bundle);
				break;
			case 7:
//				badgerflages[position]=true;
//				listView.notifyAll();
				ManagerHuoyuan.launch(ExpressSensor.this, bundle);
				break;
			case 8:
//				badgerflages[position]=true;
//				listView.notifyAll();
				PushActivity.launch(ExpressSensor.this, bundle);
				break;
			case 9:
//				badgerflages[position]=false;
//				listItemAdapter.notifyDataSetChanged();
////				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
//				Intent intent = new Intent(ExpressSensor.this,CargoRemindItems.class);
////				CargoRemindItems.launch(ExpressSensor.this, bundle);
//				intent.putExtra("cargoDetail", searchSp.getString("cargoDetail",""));
//				ExpressSensor.this.startActivity(intent);
				break;
			case 10:
				badgerflages[position]=false;
				listItemAdapter.notifyDataSetChanged();
//				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
				Intent chauffeurintent = new Intent(ExpressSensor.this,ChaufferRemindItems.class);
//				CargoRemindItems.launch(ExpressSensor.this, bundle);
				chauffeurintent.putExtra("chaufferDetail", chaufferSearchSp.getString("chaufferDetail",""));
				ExpressSensor.this.startActivity(chauffeurintent);
				break;
				
			case 11:
//				badgerflages[position]=true;
//				listView.notifyAll();
				ChauffeurSearch.launch(ExpressSensor.this, bundle);
				break;
			case 12:
//				badgerflages[position]=true;
//				listView.notifyAll();
				ChauffeurManager.launch(ExpressSensor.this, bundle);
				break;
			case 13:
//				badgerflages[position]=true;
//				listView.notifyAll();
				ManagerHuoyuan.launch(ExpressSensor.this, bundle);
				break;
			case 14:
//				badgerflages[position]=true;
//				synchronized(listView){
//					listView.notify;
//					}
				ManagerCheyuan.launch(ExpressSensor.this, bundle);
				break;
			case 15:
				
				PushActivity.launch(ExpressSensor.this, bundle);
				break;
			case 16:
//				badgerflages[position]=false;
//				listItemAdapter.notifyDataSetChanged();
////				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
//				Intent intent = new Intent(ExpressSensor.this,CargoRemindItems.class);
////				CargoRemindItems.launch(ExpressSensor.this, bundle);
//				intent.putExtra("cargoDetail", searchSp.getString("cargoDetail",""));
//				ExpressSensor.this.startActivity(intent);
//				break;
			case 18:
//				badgerflages[position]=false;
//				listItemAdapter.notifyDataSetChanged();
////				bundle.putString("cargoDetail", searchSp.getString("cargoDetail",""));
//				Intent chauffeurintent = new Intent(ExpressSensor.this,ChaufferRemindItems.class);
////				CargoRemindItems.launch(ExpressSensor.this, bundle);
//				chauffeurintent.putExtra("chaufferDetail", chaufferSearchSp.getString("chaufferDetail",""));
//				ExpressSensor.this.startActivity(chauffeurintent);
//				break;
//				SetChaufferRemind.launch(ExpressSensor.this, bundle);
				
				
			}	
		}
	};
	
	
	
	public class serviceReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			try{
		        /* 取并来自后台服务所Broadcast的参数 */
		        Bundle b = intent.getExtras();
		        String message = b.getString("MESSAGE");
		       
//		        Log.i("message,stop",message + stopMessage);
		        if(message.equals("16")||message.equals("18")){
			        int notifyNum = Integer.parseInt(message);
			        listItemAdapter.notifyDataSetChanged();
			        badgerflages[notifyNum]=true;
		        }
//		        System.out.println(stopMessage.equals("stopPush"));
//		        String stopMessage = b.getString("stopPush");
		        if(message.equals("stopPush")){
		        	System.out.println(stopService(pushInten));
		        }
		        
//		        Toast.makeText(ExpressSensor.this, message, Toast.LENGTH_LONG).show();
		     }catch(Exception e){
		        e.getStackTrace();
		      }
		}
	  
	}
	
	@Override
	protected void onResume(){
	    // TODO Auto-generated method stub
	    try{
	      /* 前景程序生命周期开始 */
	      IntentFilter filter;
	      /* 自定义要过滤的广播讯息(DavidLanz) */
	      filter = new IntentFilter("SENT");
	      receiver = new serviceReceiver();
	      registerReceiver(receiver, filter);
	    }catch(Exception e){
	      e.getStackTrace();
	    }
	    super.onResume();
	  }
	@Override
	protected void onPause(){
	    // TODO Auto-generated method stub
	    /* 前景程序生命周期结束，解除刚守系统注册的Receiver */
//	    unregisterReceiver(receiver);
	    super.onPause();
	  }
}





