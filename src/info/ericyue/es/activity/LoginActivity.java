/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * Project Name:ExpressSensor
 * Create Date: 2011-7-17
 */
package info.ericyue.es.activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import info.ericyue.es.util.DownLoadManager;
import info.ericyue.es.util.HttpUtil;
import info.ericyue.es.R;
import info.ericyue.es.util.EncryptString;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
	
	
	public static void launch (Context c , Bundle bundle){
		Intent intent = new Intent(c, LoginActivity.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
		
	}
	
//	------------------------------version------------------------
	private final String TAG = this.getClass().getName();
	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int DOWN_ERROR = 4;
	private final int ERRORUSER = 5;
	private final int INPUTUSERNAME = 6;
	private String localVersion;
	private String serverVersion;
//	------------------------------version------------------------
	
    public ProgressDialog welcomeDialog=null;
    public AlertDialog dialogBuilder;
	public EditText userName,passWord;
	public LayoutInflater li, lii;
	private CheckBox isRmber;
	
	private long exitTime = 0;
	SharedPreferences userInfo ;
	public View myView, registtypeView;
	public ListView registType;
	public TextView typeName;
	private boolean cancel=false;
	private String name,pwd;
	
	Handler handler=new Handler();
	ProgressDialog pd ;
    Runnable runnable=new Runnable(){
       @Override
       public void run() {
    	   if(cancel)
    		   showDialog("远程服务器未就绪!",true);
       } 
    };
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
	    	if((System.currentTimeMillis()-exitTime) > 2000){  
	    		Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	    		exitTime = System.currentTimeMillis();  
	    	}  
	    	else{  	
//	          Intent location = new Intent( LoginActivity.this, LocationService.class );
//	          Intent record = new Intent( LoginActivity.this, TraceRecordService.class );
	          /* 以stopService方法关闭Intent */
//	          stopService(location);
//	          stopService(record);
//	    	  System.out.println(event);
	          finish();
	          System.exit(0);  
	    	}  
	    	return true;  
	    }  
	    return super.onKeyDown(keyCode, event); 
	}
    

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.welcome);  
        pd = new ProgressDialog(LoginActivity.this);
       
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    	li=LayoutInflater.from(LoginActivity.this);
    	myView=li.inflate(R.layout.login_dialog, null);
    	isRmber = (CheckBox)myView.findViewById(R.id.isRmber);
        isRmber.setChecked(false);
        isRmber.setOnCheckedChangeListener(new OnCheckedChangeListener() {
 			
 			@Override
 			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
 				// TODO Auto-generated method stub
 				isRmber.setChecked(isChecked);
 			}
 		});
    	
    	
        userName=(EditText) myView.findViewById(R.id.userEditText);
        passWord=(EditText) myView.findViewById(R.id.pwdEditText);
        
        
        userInfo = getSharedPreferences("UserName",MODE_MULTI_PROCESS);
        name=userInfo.getString("username", "");
        pwd = userInfo.getString("password", "");
         userName.setText(name);
        passWord.setText(pwd);
        
        try {
        	pd.setTitle("检查更新中请稍后");
        	
//        	pd.dispatchTouchEvent(new )
//        	pd.dispatchTouchEvent(ev)
			pd.show();
			localVersion = getVersionName();
			CheckVersionTask cv = new CheckVersionTask();
			new Thread(cv).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	}
	public class CheckVersionTask implements Runnable{
		
		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			try {
			queryDatabase();
			if(serverVersion.equals(localVersion)){
				
				Log.i(TAG, "版本号相同");
				Message msg = new Message();
				msg.what = UPDATA_NONEED;
				handler.sendMessage(msg);
				
			}else {
				
				Log.i(TAG, "版本号不相同 ");
				Message msg = new Message();
				msg.what = UPDATA_CLIENT;
				handler.sendMessage(msg);
			}
		}
			catch (Exception e) {
			Message msg = new Message();
			msg.what = GET_UNDATAINFO_ERROR;
			handler.sendMessage(msg);
			e.printStackTrace();
		}
		}
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case UPDATA_NONEED:
					pd.dismiss();
					Toast.makeText(getApplicationContext(), "不需要更新",
							Toast.LENGTH_SHORT).show();
					loginDialog();
					break;
				case UPDATA_CLIENT:
					pd.dismiss();
					 //对话框通知用户升级程序   
					showUpdataDialog();
					break;
				case GET_UNDATAINFO_ERROR:
					pd.dismiss();
					//服务器超时   
		            Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1).show(); 
					break;
				case DOWN_ERROR:
					pd.dismiss();
					//下载apk失败  
					loginDialog();
		            Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show(); 
					break;
				case ERRORUSER:
					Toast.makeText(getApplicationContext(), "用户名密码错误，请核对！", 1).show(); 
					break;
				case INPUTUSERNAME:
					Toast.makeText(getApplicationContext(), "请输入用户名密码！", 1).show(); 
					break;
				}
			}
		};
		/* 
		 *  
		 * 弹出对话框通知用户更新程序  
		 *  
		 * 弹出对话框的步骤： 
		 *  1.创建alertDialog的builder.   
		 *  2.要给builder设置属性, 对话框的内容,样式,按钮 
		 *  3.通过builder 创建一个对话框 
		 *  4.对话框show()出来   
		 */  
		protected void showUpdataDialog() {
			AlertDialog.Builder builer = new Builder(LoginActivity.this);
			builer.setTitle("版本升级");
			builer.setMessage("下载apk,更新");
			 //当点确定按钮时从服务器上下载 新的apk 然后安装   
			builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Log.i(TAG, "下载apk,更新");
					downLoadApk();
				}
			});
			builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//do sth
					loginDialog();
				}
			});
			AlertDialog dialog = builer.create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
		/* 
		 * 从服务器中下载APK 
		 */  
		protected void downLoadApk() {  
		    final ProgressDialog pd;    //进度条对话框  
		    pd = new  ProgressDialog(LoginActivity.this);  
		    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
		    pd.setMessage("正在下载更新");  
		    pd.show();  
		    new Thread(){  
		        @Override  
		        public void run() {  
		            try {  
		                File file = DownLoadManager.getFileFromServer("http://121.42.58.134:8080/app/LoginActivity.apk", pd);  
		                sleep(3000);  
		                installApk(file);  
		                pd.dismiss(); //结束掉进度条对话框  
		            } catch (Exception e) {  
		                Message msg = new Message();  
		                msg.what = DOWN_ERROR;  
		                handler.sendMessage(msg);  
		                e.printStackTrace();  
		            }  
		        }}.start();  
		}  
		  
		//安装apk   
		protected void installApk(File file) {  
		    Intent intent = new Intent();  
		    //执行动作  
		    intent.setAction(Intent.ACTION_VIEW);  
		    //执行的数据类型  
		    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
		    startActivity(intent);  
		}  
	}
	public void queryDatabase(){
		String stream=HttpUtil.QuerySystemInfo("0");
		String[] sys=stream.split(",");
		serverVersion=VersionStatus(sys[0]);
	}
	public String VersionStatus(String version){
		String[] tmp=version.split("\\.");
		
			return version;
		
	}
	private String getVersionName() throws Exception {
		//getPackageName()是你当前类的包名，0代表是获取版本信息  
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}
    
    class  ChooseRegist implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Bundle bundle = new Bundle();
//			System.out.println(position);
			switch (position) {
			case 0:
				
				CargoUserRegist.launch(LoginActivity.this,bundle);
				break;
			case 1:
				CarOwnerRegist.launch(LoginActivity.this, bundle);
				break;
			case 2:
				DriverRegist.launch(LoginActivity.this, bundle);
				break;
			default:
				
//				System.out.println(	parent);
				
				break;
			}
			
		}
    	
    	
    }
    
    private SimpleAdapter setlistAdpter(){
    	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
    	
    	String tname[]={"  货主","  车主","  司机"};
    	for(int i=0;i<3;i++){
    	HashMap<String,Object> map = new HashMap<String, Object>();
    	map.put("typename", tname[i]);
    	list.add(map);
    	}
    	SimpleAdapter adapter = new SimpleAdapter(LoginActivity.this,list,R.layout.rtypename,new String[] {"typename"}, new int[] {R.id.typename});
    	return adapter;
    }
    
    private void showDialog(String msg,final boolean close){
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("错误信息").setMessage(msg).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(close){
					finish();
				}
			}
		});
		AlertDialog alert=builder.create();
		alert.show();
    }
	private void showDialog(String msg){
		showDialog(msg,true);
	}
    private boolean login() throws Exception{
    	String username=userName.getText().toString();
    	String pwd=passWord.getText().toString();
//    	pwd=EncryptString.encryptString(pwd, "MD5");//To Upper Case
    	String result=query(username,pwd);  	
		handler.removeCallbacks(runnable);
		cancel=false;
    	if(result!=null&&result.equals("1")){
    		return true;
    	} 		
    	if(result!=null&&result.equals("0")){
    		return false;
    	}
    		return false;
    }
	private boolean validate(){
		String username=userName.getText().toString();
		String pwd=passWord.getText().toString();
		if(username.equals("")&&!pwd.equals("")){
			Toast.makeText(LoginActivity.this, "用户名必填！", Toast.LENGTH_SHORT).show();
//			showDialog("用户名必填！");
			return false;
		}
		if(pwd.equals("")&&!username.equals("")){
//			showDialog("密码必填！");
			Toast.makeText(LoginActivity.this, "密码必填！！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(username.equals("")&&pwd.equals("")){
//			showDialog("用户名和密码必填！");
			Toast.makeText(LoginActivity.this, "用户名和密码必填！", Toast.LENGTH_SHORT).show();
			return false;
		}	
		return true;
	}
	private void ShowRegistType(){
		lii=LayoutInflater.from(LoginActivity.this);
    	registtypeView = lii.inflate(R.layout.registtype, null,false);
    	typeName = (TextView)registtypeView.findViewById(R.id.typename);
        registType=(ListView)registtypeView.findViewById(R.id.registtype);
        registType.setOnItemClickListener(new ChooseRegist());
		AlertDialog.Builder alertB = new AlertDialog.Builder(LoginActivity.this);
		
    	alertB.setView(registtypeView);
    	final  AlertDialog alert = alertB.create();
    	registType.setAdapter(setlistAdpter());
    	alert.show();
    	
		
		
	}
	
	
    private String query(String username,String password){
    	String queryString="username="+username+"&password="+password;
    	String url=HttpUtil.BASE_URL+"servlet/LoginVerify?"+queryString;
    	Log.i("Login地址", url);
    	Log.i("xinxi", HttpUtil.queryStringForPost(url));
    	return HttpUtil.queryStringForPost(url);
    }
    private void loginDialog(){
    	dialogBuilder=new AlertDialog.Builder(this)
    	.setView(myView)
    	.setCancelable(false).setNeutralButton("注册",
    	new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
//				finish();
				ShowRegistType();
			}
		})
		.setNegativeButton("退出", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				dialogBuilder.dismiss();
				finish();
				
			}
		}) 
    	.setPositiveButton("登陆",
    	new DialogInterface.OnClickListener() {
    		
    		@Override
			public void onClick(DialogInterface arg0, int arg1) {
    			if(validate()){
//    				cancel=true;
    				handler.postDelayed(runnable, 5000);
    				LoginThread loginthread=new LoginThread();
    				loginthread.execute();
//    				dialogBuilder.dismiss();
    			}
			}
		}).create();
		
    	
//    	AlertDialogtDialog dialog= dialogBuilder;
    	try{
    		Field field = dialogBuilder.getClass().getDeclaredField("mAlert");
            field.setAccessible(true);
            Object obj = field.get(dialogBuilder);
            field = obj.getClass().getDeclaredField("mHandler");
            field.setAccessible(true);
            field.set(obj,new ButtonHandler(dialogBuilder));
        }catch(Exception e){
        	e.printStackTrace();
        }
    	dialogBuilder.show();
    }
    /**
     * Login Dialog Defend Close
     * @author Ericyue@Moonlight
     *
     */
    class  ButtonHandler  extends  Handler{
    	private WeakReference <DialogInterface> mDialog;
        public ButtonHandler(DialogInterface dialog){
        	mDialog = new WeakReference <DialogInterface> (dialog);
        }
        @Override
        public void handleMessage(Message msg){
        	switch (msg.what){
        		case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                	((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                	break;
            }
        }
    }
    /**
     * 登陆线程
     * @author Moonlight
     */
       class LoginThread extends AsyncTask<Void, Void, Void> {
        ProgressDialog d = new ProgressDialog(LoginActivity.this);
        boolean loginflag =false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            d.setMessage("正在验证用户信息...");
            d.setTitle("请稍后...");
            d.show(); 
        }
        
        @Override
        protected Void doInBackground(Void... params) {
        	try {
        		loginflag=login();
				if(loginflag){
					if(isRmber.isChecked()){
						 name= userName.getText().toString();
						 pwd = passWord.getText().toString();
						 Editor ed ;
						 ed = userInfo.edit();
						 ed.putString("username", name);
						 ed.putString("password", pwd);
						 ed.commit();
					}
					Intent intent = new Intent(LoginActivity.this,ExpressSensor.class);
					Bundle bundle=new Bundle();
					bundle.putString("username",userName.getText().toString());
					intent.putExtras(bundle);	
					startActivityForResult(intent,0);
					dialogBuilder.dismiss();
		            finish();
				}
				else{
//					Message msg = new Message();
//					msg.what=5;
					
//					handler.sendMessage(msg);
//					Looper.loop();
//					Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
					
					
				
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        	return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
            	if(loginflag){
            		Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
            	}
            	else{
            		Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
            	}
            	d.dismiss();
            } catch (IllegalArgumentException e) {
            	e.printStackTrace();
            };
        }
    }
   
	
}


	
	
	