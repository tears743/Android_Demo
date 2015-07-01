package info.ericyue.es.activity;

import info.ericyue.es.R;
import info.ericyue.es.myView.SelectAddressView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CarInfoSearch extends Activity {
	
	public static void launch(Context c, Bundle bundle) {
		Intent intent = new Intent(c, CarInfoSearch.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	private Button back1,btsearchsubmit;
	info.ericyue.es.myView.SelectAddressView carFromAddress,cartoAddress ;
	
	Bundle bundle = new Bundle();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carinfosearch);
	
		carFromAddress =(SelectAddressView)findViewById(R.id.carfrom);
		cartoAddress =(SelectAddressView)findViewById(R.id.carto);
		back1 = (Button)findViewById(R.id.back1);
		btsearchsubmit = (Button)findViewById(R.id.btsearchsubmit);
		btsearchsubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String car_sd,car_dd;
				
				car_sd = carFromAddress.getFromAddress();
				car_dd = cartoAddress.getFromAddress();
				if(car_sd.isEmpty()){
					Toast.makeText(CarInfoSearch.this, "请选择出发地", Toast.LENGTH_SHORT).show();
				}
				else if(car_dd.isEmpty()){
					Toast.makeText(CarInfoSearch.this, "请选择到达地", Toast.LENGTH_SHORT).show();
					
				}
				else{				
					bundle.putString("car_sd", car_sd);
					bundle.putString("car_dd", car_dd);
					CarInfo.launch(CarInfoSearch.this, bundle);
				}
			}
		});

		back1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CarInfoSearch.this.finish();
			}
		});
	}
	

}
