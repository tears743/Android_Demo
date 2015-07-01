package info.ericyue.es.activity;

import info.ericyue.es.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ChauffeurManager extends Activity {

	
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, ChauffeurManager.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	
	private Button leftbtn;
	private LinearLayout llnodata,list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chauffeurmanager);
		leftbtn=(Button)findViewById(R.id.btn_title_left);
		leftbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ChauffeurManager.this.finish();
				
			}
		});
		llnodata = (LinearLayout)findViewById(R.id.manager_siji_nodata);
		llnodata.setVisibility(View.VISIBLE); 
	}
	
	

}
