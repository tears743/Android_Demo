package info.ericyue.es.activity;

import java.util.ArrayList;
import java.util.HashMap;

import info.ericyue.es.R;
import info.ericyue.es.util.HttpUtil;
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
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ChaufferRemindItems extends ChaufferInfoBasic {

	String chaufferDetail;
	SharedPreferences sp,chaufferSearchSp;
	Editor editor,chaufferSearchEditor;
	Button backleft,btnZhuYe;
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, ChaufferRemindItems.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v1_huo_list);
		setView();
		backleft = (Button)findViewById(R.id.btn_title_left);
		btnZhuYe = (Button)findViewById(R.id.btn_h_zhuye);
		sp = getSharedPreferences("config",MODE_PRIVATE);
		chaufferSearchSp = getSharedPreferences("ChaufferRemind", MODE_PRIVATE);
		editor = sp.edit();
		chaufferSearchEditor = chaufferSearchSp.edit();
		chaufferDetail = this.getIntent().getStringExtra("chaufferDetail");
		if(chaufferDetail.isEmpty()){
			chaufferDetail=chaufferSearchSp.getString("ChaufferDetail", "");
		}
		
		System.out.println("cargoDetail: "+chaufferDetail);
//		cargo_sd = this.getIntent().getStringExtra("cargo_sd");
//		cargo_dd = this.getIntent().getStringExtra("cargo_dd");
		Log.i("chaufferDetail:",chaufferDetail);
		if(!(chaufferDetail.isEmpty())){
			queryCargoInfoItem();
		}
		backleft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChaufferRemindItems.this.finish();
			}
		});
		btnZhuYe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChaufferRemindItems.this.finish();
			}
		});
		
//		setClickListener();
		
	}

	/* (non-Javadoc)
	 * @see info.ericyue.es.activity.ChauffeurInfo#fillListItem()
	 */
	public void fillListItem() {
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
		for(int i =0; i<chauffItems.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			String itemStrs[]=chauffItems[i].split("/");
			for(int j=0;j<itemStrs.length;j++){
			Log.i("info is ",itemStrs[j]);
			}
//			out.print(rs.getString("chauffeur_name")+"/");
//			out.print(rs.getString("health_status")+"/");
//			out.print(rs.getString("drive_age")+"/");
//			out.print(rs.getString("drive_tyle")+"/");
//			out.print(rs.getString("area_where")+"/");
//			out.print(rs.getString("tel")+"/");
//			out.print(rs.getString("effect_date")+"/");
//			out.print(rs.getString("remark")+"/");
//			out.print(rs.getString("publish_date")+"/");
//			out.print(rs.getString("publish_name")+"%");
			map.put("cargo_address", chaufferSearchSp.getString("FromAddressSearchString",""));
			map.put("company_Name", itemStrs[4]);
			map.put("cargo_type", itemStrs[8]);
			map.put("cargo_Name", itemStrs[1]);
			map.put("cargo_w","ÖÐ×ªµØ"+itemStrs[5]);
			map.put("introduce", "");
			map.put("release_date", itemStrs[9]);

			listItem.add(map);
		
		}
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,
				listItem, R.layout.v1_huo_item,
				new String[]{"cargo_address","company_Name"
				,"introduce","cargo_Name","cargo_w","release_date"},
				new int[] {R.id.txt_arealine,R.id.txt_company_type,R.id.txt_shuoming,R.id.txt_huo_name,R.id.txt_huo_dun,R.id.txt_time});
				
		cargoAbs.setAdapter(listItemAdapter);
		cargoAbs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				
				bundle.putString("chaufaddress",chaufferSearchSp.getString("FromAddressSearchString", "") );
				bundle.putString("chaufferDetail", chauffItems[position].substring(2));
				System.out.println(chauffItems[position]);
				ChauffeurDetail.launch(ChaufferRemindItems.this, bundle);
				
			}
		});
	}

	/* (non-Javadoc)
	 * @see info.ericyue.es.activity.ChauffeurInfo#queryCargoInfoItem()
	 */
	@Override
	public void queryCargoInfoItem() {
		String str = chaufferDetail;
		 if(!str.equals("0")&&!str.equals("")&&!str.equals(null))
		 {	
			 System.out.println(str);
				chauffItems = str.split("%");
			 	fillListItem();
		 }
		 else{
			 LinearLayout ll = (LinearLayout)findViewById(R.id.li_no_info);
			 ll.setVisibility(View.VISIBLE);
		 }
		}
	

	
}
