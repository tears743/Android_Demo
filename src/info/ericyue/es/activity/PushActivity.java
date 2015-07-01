package info.ericyue.es.activity;

import info.ericyue.es.R;
import info.ericyue.es.activity.ExpressSensor.serviceReceiver;

import java.security.PublicKey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class PushActivity extends Activity {

	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, PushActivity.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	
	ImageButton setRemindButton ;
	boolean remindFlag=false;
	SharedPreferences sp;
	Editor editor;
	Button setCargoRemind,setCarRemind,setChaufferRemind;
	
	
	Intent pushIntent;
	
	Bundle bundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pushmain);
		bundle = new Bundle();
		pushIntent = new Intent(this,PushService.class);
		InitView ();
		
	}
	
	public void InitView (){
		
		setCargoRemind = (Button)findViewById(R.id.setCargoRemind);
		setChaufferRemind =(Button)findViewById(R.id.setChauffeurRemind);
		setChaufferRemind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetChaufferRemind.launch(PushActivity.this, bundle);
			}
		});
		setCargoRemind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetCargoRemind.launch(PushActivity.this, bundle);
			}
		});
		
		setRemindButton = (ImageButton)findViewById(R.id.setStatus);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		editor = sp.edit();
		if(!sp.getBoolean("RemindStatus", false)){
		editor.putBoolean("RemindStatus", remindFlag);
		editor.commit();
		}
		else{
			
			remindFlag = sp.getBoolean("RemindStatus", false);
			System.out.println(remindFlag);
			if(remindFlag){
				setRemindButton.setImageDrawable(getResources().getDrawable(R.drawable.v1_bid_satrt));
				startService(pushIntent);
			}
			
			
		}
		
		setRemindButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!remindFlag){
					remindFlag=true;
					setRemindButton.setImageDrawable(getResources().getDrawable(R.drawable.v1_bid_satrt));
					editor.putBoolean("RemindStatus", remindFlag);
					editor.putInt("NewCargoMessageCounts", 0);
					editor.putInt("NewChaufferMessageCounts", 0);
					editor.commit();
					startService(pushIntent);
				}
				else{
						setRemindButton.setImageDrawable(getResources().getDrawable(R.drawable.v1_bid_stop));
						remindFlag=false;
						editor.putBoolean("RemindStatus", remindFlag);
						editor.commit();
						Intent i =new Intent();
						 i.setAction("SENT");
						 i.putExtra("MESSAGE","stopPush");
						 PushActivity.this.sendBroadcast(i);
						stopService(pushIntent);
					 
					}
				}
		});
	}
	

	
}
