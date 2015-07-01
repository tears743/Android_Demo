package info.ericyue.es.activity;

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
import info.ericyue.es.R;
import info.ericyue.es.myView.SelectAddressView;
import info.ericyue.es.util.HttpUtil;

public class CargoInfoSearch extends Activity {

	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, CargoInfoSearch.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}

	private Button backto;
	
	info.ericyue.es.myView.SelectAddressView cargoFromAddress,cargoToAddress ;

	
	Bundle bundle = new Bundle();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cargoinfosearch);
		
		backto = (Button)findViewById(R.id.btn_title_left);
		cargoFromAddress = (SelectAddressView)findViewById(R.id.from);
		cargoToAddress =(SelectAddressView)findViewById(R.id.to);

		Button back = (Button)findViewById(R.id.back);
		Button submit = (Button)findViewById(R.id.searchsubmit);
		backto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CargoInfoSearch.this.finish();
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String cargo_sd,cargo_dd;
				cargo_sd = cargoFromAddress.getFromAddress();
				cargo_dd = cargoToAddress.getFromAddress();
				if(cargo_sd.isEmpty()){
					Toast.makeText(CargoInfoSearch.this, "请选择出发地", Toast.LENGTH_SHORT).show();
				}
				else if(cargo_dd.isEmpty()){
					Toast.makeText(CargoInfoSearch.this, "请选择到达地", Toast.LENGTH_SHORT).show();
					
				}
				else{
					bundle.putString("cargo_sd", cargo_sd);
					bundle.putString("cargo_dd", cargo_dd);
					CargoInfo.launch(CargoInfoSearch.this, bundle);
				}
			}
		});

		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CargoInfoSearch.this.finish();
			}
		});
	}
	

	

}
