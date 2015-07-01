package info.ericyue.es.activity;

import info.ericyue.es.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ManagerCheyuan extends Activity {

	private Button return1,edit1,btn_fabu_cheyuan;
	Bundle bundle = new Bundle();
	
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, ManagerCheyuan.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_cheyuan);
		
		return1 = (Button)findViewById(R.id.return1);
		//btn_title_right = (Button)findViewById(R.id.btn_title_right);
		btn_fabu_cheyuan = (Button)findViewById(R.id.btn_fabu_cheyuan);
		//btn_manager_resend = (Button)findViewById(R.id.btn_manager_resend);
		btn_fabu_cheyuan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CarRelease.launch(ManagerCheyuan.this, bundle);
			}
		});
		
		ReturnButton();
	}

	private void ReturnButton() {
		// TODO Auto-generated method stub
		return1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				ManagerCheyuan.this.finish();

			}
		});

	}
}
