package info.ericyue.es.activity;


import java.util.ArrayList;
import java.util.HashMap;

import info.ericyue.es.R;
import info.ericyue.es.util.HttpUtil;
import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.view.View.*;

public class CargoInfo extends Activity {

	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, CargoInfo.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	
	Bundle bundle;
	Button backHome,btnleft;
	ListView cargoAbs ;
	String cargo_sd,cargo_dd,cargo_relation_company,cargo_name,cargo_weight,cargo_releasedate;
	String[] cargoItems;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v1_huo_list);
		
		bundle = this.getIntent().getExtras();
		setView();
		cargo_sd = bundle.getString("cargo_sd");
		cargo_dd = bundle.getString("cargo_dd");
		System.out.println("cargo_dd="+cargo_dd);
		if(!(cargo_sd==null || cargo_dd==null)){
		queryCargoInfoItem();
		}
		
		setClickListener();
		
	}
	
	public void setView(){
		backHome = (Button)findViewById(R.id.btn_h_zhuye);
		cargoAbs = (ListView)findViewById(R.id.list_condi_huo);
		btnleft = (Button)findViewById(R.id.btn_title_left);
	}
	protected void setClickListener(){
		
		btnleft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CargoInfo.this.finish();
				
			}
		});
		
		backHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
//				ExpressSensor.launch(CargoInfo.this, bundle);
				CargoInfo.this.finish();
			}
		});
		
	}
	
	public void fillListItem(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
		for(int i =0; i<cargoItems.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			String itemStrs[]=cargoItems[i].split("/");
			for(int j=0;j<itemStrs.length;j++){
			Log.i("info is ",itemStrs[j]);
			}
			map.put("cargo_address", itemStrs[0]+"----"+itemStrs[15]);
			map.put("company_Name", itemStrs[1]);
			map.put("cargo_type", itemStrs[7]);
			map.put("cargo_Name", itemStrs[2]);
			map.put("cargo_w",itemStrs[3]+"ถึ");
			map.put("introduce", itemStrs[4]);
			map.put("release_date", itemStrs[5]);

			listItem.add(map);
		}
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,
				listItem, R.layout.v1_huo_item,
				new String[]{"cargo_address","company_Name","cargo_type","cargo_Name"
				,"cargo_w","introduce","release_date"},
				new int[] {R.id.txt_arealine,R.id.txt_company_name,R.id.txt_company_type,R.id.txt_huo_name
				,R.id.txt_huo_dun,R.id.txt_shuoming,R.id.txt_time}); 
		cargoAbs.setAdapter(listItemAdapter);
		cargoAbs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				bundle.putString("cargoDetail", cargoItems[position]);
				
				CargoDetail.launch(CargoInfo.this, bundle);
				
			}
		});
	}
	public void queryCargoInfoItem(){
	 String str = HttpUtil.Query_cargo_info(cargo_sd, cargo_dd);
	 if(!str.equals("0")&&str !=null&&str !="")
	 {	
		 System.out.println(str);
			cargoItems = str.split("%");
		 	fillListItem();
	 }
	 else{
		 LinearLayout ll = (LinearLayout)findViewById(R.id.li_no_info);
		 ll.setVisibility(View.VISIBLE);
	 }
	}
}
