package info.ericyue.es.activity;

import info.ericyue.es.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ManagerHuoyuan extends Activity{

	private Button return2,edit2,btn_fabu_huoyuan,btn_manager_resend;
	Bundle bundle = new Bundle();
	
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, ManagerHuoyuan.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_huoyuan);
		
		return2 = (Button)findViewById(R.id.return2);
		//edit2 = (Button)findViewById(R.id.edit2);
		btn_fabu_huoyuan = (Button)findViewById(R.id.btn_fabu_huoyuan);
		btn_manager_resend = (Button)findViewById(R.id.btn_manager_resend);
		btn_fabu_huoyuan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CargoRelease.launch(ManagerHuoyuan.this, bundle);
			}
		});
		
		ReturnButton();
	}

	private void ReturnButton() {
		// TODO Auto-generated method stub
		return2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				ManagerHuoyuan.this.finish();

			}
		});

	}
}
