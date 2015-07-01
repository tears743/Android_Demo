package info.ericyue.es.activity;

import java.util.ArrayList;
import java.util.HashMap;

import info.ericyue.es.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class CargoRemindItems extends CargoInfoBaseActivity {

	String cargoDetail;
	SharedPreferences sp,cargoSearchSp;
	Editor editor,cargoSearchEditor;
	Button backleft;
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, CargoRemindItems.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v1_huo_list);
		backleft = (Button)findViewById(R.id.btn_title_left);
		backleft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CargoRemindItems.this.finish();
			}
		});
		setView();
		sp = getSharedPreferences("config",MODE_PRIVATE);
		cargoSearchSp = getSharedPreferences("CargoRemind", MODE_PRIVATE);
		editor = sp.edit();
		cargoSearchEditor = cargoSearchSp.edit();
		cargoDetail = this.getIntent().getStringExtra("cargoDetail");
		if(cargoDetail.isEmpty()){
			cargoDetail=cargoSearchSp.getString("cargoDetail", "");
		}
		
		System.out.println("cargoDetail: "+cargoDetail);
		cargo_sd = this.getIntent().getStringExtra("cargo_sd");
		cargo_dd = this.getIntent().getStringExtra("cargo_dd");
		Log.i("cargoDetail:",cargoDetail);
		if(!(cargoDetail.isEmpty())){
			queryCargoInfoItem();
		}
//		 Intent i =new Intent();
//		 i.setAction("SENT");
//		 i.putExtra("MESSAGE","16");
//		 CargoRemindItems.this.sendBroadcast(i);
		
		setClickListener();
	}
	public void setView(){
		backHome = (Button)findViewById(R.id.btn_h_zhuye);
		cargoAbs = (ListView)findViewById(R.id.list_condi_huo);
		
	}

	/* (non-Javadoc)
	 * @see info.ericyue.es.activity.CargoInfoBaseActivity#setClickListener()
	 */
	@Override
	protected void setClickListener() {
		// TODO Auto-generated method stub
		backHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
//				ExpressSensor.launch(CargoInfo.this, bundle);
				CargoRemindItems.this.finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see info.ericyue.es.activity.CargoInfo#fillListItem()
	 */
	@Override
	public void fillListItem() {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
		for(int i =0; i<cargoItems.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			String itemStrs[]=cargoItems[i].split("/");
			System.out.println(Integer.parseInt(itemStrs[0]));
			editor.putInt("NewMessageCargoCounts", Integer.parseInt(itemStrs[0]));
			editor.apply();
			cargo_sd = itemStrs[1];
			cargo_dd = itemStrs[16];
			for(int j=0;j<itemStrs.length;j++){
			Log.i("info is ",itemStrs[j]);
			}
			map.put("cargo_address", cargo_sd+"----"+cargo_dd);
			map.put("company_Name", itemStrs[2]);
			map.put("cargo_type", itemStrs[8]);
			map.put("cargo_Name", itemStrs[3]);
			map.put("cargo_w",itemStrs[4]+"ถึ");
			map.put("introduce", itemStrs[5]);
			map.put("release_date", itemStrs[6]);

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
				String cargoItem = cargoItems[position];
				bundle.putString("cargoDetail", cargoItem.substring(2));
				
				CargoDetail.launch(CargoRemindItems.this, bundle);
				
			}
		});
	}

	/* (non-Javadoc)
	 * @see info.ericyue.es.activity.CargoInfo#queryCargoInfoItem()
	 */
	@Override
	public void queryCargoInfoItem( ) {
		// TODO Auto-generated method stub
		 String str = cargoDetail;
		 if(!str.equals("0")&&str !=null)
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
