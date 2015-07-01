package info.ericyue.es.activity;

import info.ericyue.es.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ChauffeurDetail extends Activity {

	
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, ChauffeurDetail.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	
	TextView txt_chauffer_area,txt_chauffer_name,txt_health_status
	,txt_driver_age,txt_driver_type,txt_areawhere,txt_tel,
	txt_effect_date,txt_remark,txt_datatime;
	Button btn_title_left;
	Bundle bundle;
	String chauffAddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v1_chauffer_details);
		InitView();
		bundle = this.getIntent().getExtras();
		String str = bundle.getString("chaufferDetail");
		chauffAddress = bundle.getString("chaufaddress");
		SetText(str);
		btn_title_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ChauffeurDetail.this.finish();
				
			}
		});
	}
	
	private void SetText(String str){
		String[] items = str.split("/");
		txt_chauffer_area.setText(chauffAddress);
		txt_chauffer_name.setText(items[0]);
		txt_health_status .setText(items[1]);
		txt_driver_age.setText(items[2]);
		txt_driver_type.setText(items[3]);
		txt_areawhere.setText(items[4]);
		txt_tel .setText(items[5]);
		txt_effect_date.setText(items[6]);
		txt_remark.setText(items[7]);
		txt_datatime.setText(items[8]);
		
	}
	
	public void InitView(){
		btn_title_left = (Button)findViewById(R.id.btn_title_left);
		txt_chauffer_area =(TextView)findViewById(R.id.txt_chauffer_area);
		txt_chauffer_name = (TextView)findViewById(R.id.txt_chauffer_name);
		txt_health_status =(TextView)findViewById(R.id.txt_health_status);
		txt_driver_age=(TextView)findViewById(R.id.txt_driver_age);
		txt_driver_type=(TextView)findViewById(R.id.txt_driver_type);
		txt_areawhere=(TextView)findViewById(R.id.txt_areawhere);
		txt_tel = (TextView)findViewById(R.id.txt_tel);
		txt_effect_date=(TextView)findViewById(R.id.txt_effect_date);
		txt_remark=(TextView)findViewById(R.id.txt_remark);
		txt_datatime=(TextView)findViewById(R.id.txt_datatime);
	}
	

}
