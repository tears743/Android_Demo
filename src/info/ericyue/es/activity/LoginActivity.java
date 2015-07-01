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
    		   showDialog("Զ�̷�����δ����!",true);
       } 
    };
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
	    	if((System.currentTimeMillis()-exitTime) > 2000){  
	    		Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();                                
	    		exitTime = System.currentTimeMillis();  
	    	}  
	    	else{  	
//	          Intent location = new Intent( LoginActivity.this, LocationService.class );
//	          Intent record = new Intent( LoginActivity.this, TraceRecordService.class );
	          /* ��stopService�����ر�Intent */
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
        	pd.setTitle("�����������Ժ�");
        	
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
				
				Log.i(TAG, "�汾����ͬ");
				Message msg = new Message();
				msg.what = UPDATA_NONEED;
				handler.sendMessage(msg);
				
			}else {
				
				Log.i(TAG, "�汾�Ų���ͬ ");
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
					Toast.makeText(getApplicationContext(), "����Ҫ����",
							Toast.LENGTH_SHORT).show();
					loginDialog();
					break;
				case UPDATA_CLIENT:
					pd.dismiss();
					 //�Ի���֪ͨ�û���������   
					showUpdataDialog();
					break;
				case GET_UNDATAINFO_ERROR:
					pd.dismiss();
					//��������ʱ   
		            Toast.makeText(getApplicationContext(), "��ȡ������������Ϣʧ��", 1).show(); 
					break;
				case DOWN_ERROR:
					pd.dismiss();
					//����apkʧ��  
					loginDialog();
		            Toast.makeText(getApplicationContext(), "�����°汾ʧ��", 1).show(); 
					break;
				case ERRORUSER:
					Toast.makeText(getApplicationContext(), "�û������������˶ԣ�", 1).show(); 
					break;
				case INPUTUSERNAME:
					Toast.makeText(getApplicationContext(), "�������û������룡", 1).show(); 
					break;
				}
			}
		};
		/* 
		 *  
		 * �����Ի���֪ͨ�û����³���  
		 *  
		 * �����Ի���Ĳ��裺 
		 *  1.����alertDialog��builder.   
		 *  2.Ҫ��builder��������, �Ի��������,��ʽ,��ť 
		 *  3.ͨ��builder ����һ���Ի��� 
		 *  4.�Ի���show()����   
		 */  
		protected void showUpdataDialog() {
			AlertDialog.Builder builer = new Builder(LoginActivity.this);
			builer.setTitle("�汾����");
			builer.setMessage("����apk,����");
			 //����ȷ����ťʱ�ӷ����������� �µ�apk Ȼ��װ   
			builer.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Log.i(TAG, "����apk,����");
					downLoadApk();
				}
			});
			builer.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
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
		 * �ӷ�����������APK 
		 */  
		protected void downLoadApk() {  
		    final ProgressDialog pd;    //�������Ի���  
		    pd = new  ProgressDialog(LoginActivity.this);  
		    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
		    pd.setMessage("�������ظ���");  
		    pd.show();  
		    new Thread(){  
		        @Override  
		        public void run() {  
		            try {  
		                File file = DownLoadManager.getFileFromServer("http://121.42.58.134:8080/app/LoginActivity.apk", pd);  
		                sleep(3000);  
		                installApk(file);  
		                pd.dismiss(); //�������������Ի���  
		            } catch (Exception e) {  
		                Message msg = new Message();  
		                msg.what = DOWN_ERROR;  
		                handler.sendMessage(msg);  
		                e.printStackTrace();  
		            }  
		        }}.start();  
		}  
		  
		//��װapk   
		protected void installApk(File file) {  
		    Intent intent = new Intent();  
		    //ִ�ж���  
		    intent.setAction(Intent.ACTION_VIEW);  
		    //ִ�е���������  
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
		//getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ  
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
    	
    	String tname[]={"  ����","  ����","  ˾��"};
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
		builder.setTitle("������Ϣ").setMessage(msg).setCancelable(false).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
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
			Toast.makeText(LoginActivity.this, "�û������", Toast.LENGTH_SHORT).show();
//			showDialog("�û������");
			return false;
		}
		if(pwd.equals("")&&!username.equals("")){
//			showDialog("������");
			Toast.makeText(LoginActivity.this, "��������", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(username.equals("")&&pwd.equals("")){
//			showDialog("�û�����������");
			Toast.makeText(LoginActivity.this, "�û�����������", Toast.LENGTH_SHORT).show();
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
    	Log.i("Login��ַ", url);
    	Log.i("xinxi", HttpUtil.queryStringForPost(url));
    	return HttpUtil.queryStringForPost(url);
    }
    private void loginDialog(){
    	dialogBuilder=new AlertDialog.Builder(this)
    	.setView(myView)
    	.setCancelable(false).setNeutralButton("ע��",
    	new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
//				finish();
				ShowRegistType();
			}
		})
		.setNegativeButton("�˳�", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				dialogBuilder.dismiss();
				finish();
				
			}
		}) 
    	.setPositiveButton("��½",
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
     * ��½�߳�
     * @author Moonlight
     */
       class LoginThread extends AsyncTask<Void, Void, Void> {
        ProgressDialog d = new ProgressDialog(LoginActivity.this);
        boolean loginflag =false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            d.setMessage("������֤�û���Ϣ...");
            d.setTitle("���Ժ�...");
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
//					Toast.makeText(LoginActivity.this, "�û������������", Toast.LENGTH_SHORT).show();
					
					
				
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
            		Toast.makeText(LoginActivity.this, "��¼�ɹ���", Toast.LENGTH_SHORT).show();
            	}
            	else{
            		Toast.makeText(LoginActivity.this, "�û������������", Toast.LENGTH_SHORT).show();
            	}
            	d.dismiss();
            } catch (IllegalArgumentException e) {
            	e.printStackTrace();
            };
        }
    }
   
	
}


	
	
	