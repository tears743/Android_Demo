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

public class CarDetail extends Activity{

	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, CarDetail.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	private Button backPage;
	private TextView txt_from_area,txt_to_area,txt_pass_point,txt_datatime,txt_sendtime,txt_shuoming,
					 txt_car_length,txt_zaizhong,txt_car_location,txt_car_type,txt_car_condition,txt_chehao,
					 txt_phone,txt_daoqi_time;
	private String carDetails[];
	Bundle bundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.v1_che_details);
		
		super.onCreate(savedInstanceState);
		bundle = this.getIntent().getExtras();
		System.out.println(bundle.get("carDetail").toString().length()+"[]"+bundle.get("carDetail").toString());
		setView();
			
		txtContext();
		setclickListener();
	}

	private void setclickListener() {
		// TODO Auto-generated method stub
		backPage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CarDetail.this.finish();
			}
		});
		
	}

	private void txtContext() {
		// TODO Auto-generated method stub
		carDetails = bundle.get("carDetail").toString().split("/");
		txt_from_area.setText(carDetails[0]);
		txt_to_area.setText(carDetails[15]);
		txt_pass_point.setText(carDetails[10]);
		txt_datatime.setText(carDetails[3]);
		txt_sendtime.setText(carDetails[6]);
		txt_shuoming.setText(carDetails[11]);
		txt_car_length .setText(carDetails[7]);
		
		txt_zaizhong.setText(carDetails[9]);
		txt_car_location .setText(carDetails[13]);
		txt_car_type .setText(carDetails[12]);
		txt_car_condition .setText(carDetails[14]);
		txt_daoqi_time .setText(carDetails[8]);
		txt_datatime .setText(carDetails[5]);
	}

	private void setView() {
		// TODO Auto-generated method stub
		backPage =(Button)findViewById(R.id.btn_title_left);
		txt_from_area =(TextView)findViewById(R.id.txt_from_area);
		txt_to_area =(TextView)findViewById(R.id.txt_to_area);
		txt_pass_point =(TextView)findViewById(R.id.txt_pass_point);
		txt_datatime =(TextView)findViewById(R.id.txt_datatime);
		txt_sendtime= (TextView)findViewById(R.id.txt_sendtime);
		txt_shuoming =(TextView)findViewById(R.id.txt_shuoming);
		txt_car_length =(TextView)findViewById(R.id.txt_car_length);
		txt_zaizhong =(TextView)findViewById(R.id.txt_zaizhong);
		txt_car_location =(TextView)findViewById(R.id.txt_car_location);
		txt_car_type =(TextView)findViewById(R.id.txt_car_type);
		txt_daoqi_time =(TextView)findViewById(R.id.txt_daoqi_time);
		txt_datatime =(TextView)findViewById(R.id.txt_datatime);
		txt_car_condition =(TextView)findViewById(R.id.txt_car_condition);
		txt_chehao =(TextView)findViewById(R.id.txt_chehao);
		txt_phone =(TextView)findViewById(R.id.txt_phone);

	}
}
