package info.ericyue.es.activity;

import info.ericyue.es.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CargoDetail extends Activity {
	
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, CargoDetail.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	private Button backPage;
	private TextView txt_from_area,txt_to_area,txt_huo_freight_rates,
					txt_zhongliang,txt_tiji,txt_huo_contact,txt_huo_type,
					txt_trans_mode,txt_notice,txt_huo_phone,txt_huo_fixed_phone,
					txt_daoqi_time,txt_datatime;
	private String cargoDetails[];
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.v1_huo_details);
		
		super.onCreate(savedInstanceState);
		bundle = this.getIntent().getExtras();
		System.out.println(bundle.get("cargoDetail").toString().length()+"[]"+bundle.get("cargoDetail").toString());
		setView();
			
		txtContext();
		setclickListener();
	}
	private void setView(){
		
		backPage =(Button)findViewById(R.id.btn_title_left);
		txt_from_area =(TextView)findViewById(R.id.txt_from_area);
		txt_to_area =(TextView)findViewById(R.id.txt_to_area);
		txt_huo_freight_rates =(TextView)findViewById(R.id.txt_huo_freight_rates);
		txt_zhongliang =(TextView)findViewById(R.id.txt_zhongliang);
		txt_tiji =(TextView)findViewById(R.id.txt_tiji);
		txt_huo_contact= (TextView)findViewById(R.id.txt_huo_contact);
		txt_huo_type =(TextView)findViewById(R.id.txt_huo_type);
		
		txt_trans_mode =(TextView)findViewById(R.id.txt_trans_mode);
		txt_notice =(TextView)findViewById(R.id.txt_notice);
		txt_huo_phone =(TextView)findViewById(R.id.txt_huo_phone);
		txt_huo_fixed_phone =(TextView)findViewById(R.id.txt_huo_fixed_phone);
		txt_daoqi_time =(TextView)findViewById(R.id.txt_daoqi_time);
		txt_datatime =(TextView)findViewById(R.id.txt_datatime);

	}
	private void setclickListener(){
		backPage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CargoDetail.this.finish();
			}
		});
		txt_huo_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s=txt_huo_phone.getText().toString().trim();
				Intent myIntentDial=new Intent(Intent.ACTION_DIAL,
						Uri.parse("tel:"+s));
						myIntentDial.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				      	startActivity(myIntentDial);
			}
		});
		txt_huo_fixed_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s=txt_huo_fixed_phone.getText().toString().trim();
				Intent myIntentDial=new Intent(Intent.ACTION_DIAL,
						Uri.parse("tel:"+s));
						myIntentDial.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				      	startActivity(myIntentDial);
			}
		});
	}
	
	private void txtContext(){
		cargoDetails = bundle.get("cargoDetail").toString().split("/");
		
//		out.print(rs.getString("cargo_sd")+"/");								0
//		out.print(rs.getString("cargo_relation_company")+"/");	1
//		out.print(rs.getString("cargo_name")+"/");					2
//		out.print(rs.getString("cargo_weight")+"/");			3
//		out.print(rs.getString("introduce")+"/");				4
//		out.print(rs.getString("cargo_releasedate")+"/");		5
//		out.print(rs.getString("cargo_volume")+"/");			6

//		out.print(rs.getString("cargo_type"));					7
//		out.print(rs.getString("info_effective"));				8
//		out.print(rs.getString("transport_style"));				9
//		out.print(rs.getString("transport_fee")+"/");			10
//		out.print(rs.getString("link_name")+"/");				11
//		out.print(rs.getString("link_phone")+"/");				12
//		out.print(rs.getString("notice"));						13
//		out.print(rs.getString("fixed_phone"));					14
//		out.print(rs.getString("cargo_dd")+"%");				15
		txt_from_area.setText(cargoDetails[0]);
		txt_to_area.setText(cargoDetails[15]);
		txt_huo_freight_rates.setText(cargoDetails[10]);
		txt_zhongliang.setText(cargoDetails[3]);
		txt_tiji.setText(cargoDetails[6]);
		txt_huo_contact.setText(cargoDetails[11]);
		txt_huo_type .setText(cargoDetails[7]);
		
		txt_trans_mode.setText(cargoDetails[9]);
		txt_notice .setText(cargoDetails[13]);
		txt_huo_phone .setText(cargoDetails[12]);
		txt_huo_fixed_phone .setText(cargoDetails[14]);
		txt_daoqi_time .setText(cargoDetails[8]);
		txt_datatime .setText(cargoDetails[5]);
		
		
	}
	
}