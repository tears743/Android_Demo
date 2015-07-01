package info.ericyue.es.activity;

import java.util.ArrayList;
import java.util.HashMap;

import info.ericyue.es.R;
import info.ericyue.es.util.HttpUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class CarInfo extends Activity{

	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, CarInfo.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	
	Bundle bundle;
	Button backHome;
	ListView carAbs ;
	String car_sd,car_dd,car_type,car_releasedate;
	String[] carItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v1_che_list);
		bundle = this.getIntent().getExtras();
		setView();
		car_sd = bundle.getString("car_sd");
		car_dd = bundle.getString("car_dd");
		queryCarInfoItem();
//		fillListItem();
		
		setClickListener();
	}
	private void setView() {
			// TODO Auto-generated method stub
			backHome = (Button)findViewById(R.id.btn_c_zhuye);
			carAbs = (ListView)findViewById(R.id.list_condi_che);
		}


	private void setClickListener() {
//		View myView = LayoutInflater.from(CarInfo.this).inflate(R.layout.v1_che_item, null);
//		backHome = (Button)myView.findViewById(R.id.btn_c_zhuye);
		// TODO Auto-generated method stub
			backHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
//				ExpressSensor.launch(CargoInfo.this, bundle);
				CarInfo.this.finish();
			}
		});
	}

	private void fillListItem() {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
		for(int i =0; i<carItems.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			String itemStrs[]=carItems[i].split("/");
			for(int j=0;j<itemStrs.length;j++){
			Log.i("info is ",itemStrs[j]);
			}
			map.put("car_address", car_sd+"----"+car_dd);
			map.put("car_type", itemStrs[7]);
			map.put("car_releasedate", itemStrs[5]);

			listItem.add(map);
		}
		
	
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,
				listItem, R.layout.v1_che_item,
				new String[]{"car_address","car_type","car_releasedate"},
				new int[] {R.id.txt_arealine,R.id.txt_company_name,R.id.txt_company_type,R.id.txt_huo_name
				,R.id.txt_huo_dun,R.id.txt_shuoming,R.id.txt_time}); 
				carAbs.setAdapter(listItemAdapter);
				carAbs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				bundle.putString("carDetail", carItems[position]);
				
				CarDetail.launch(CarInfo.this, bundle);
				
			}
		});
	}

	private void queryCarInfoItem() {
		// TODO Auto-generated method stub
//		String str = HttpUtil.Query_cargo_info(car_sd, car_dd);
//		carItems = str.split("%");
		 LinearLayout ll = (LinearLayout)findViewById(R.id.li_no_info);
		 ll.setVisibility(View.VISIBLE);
	}

	
}
