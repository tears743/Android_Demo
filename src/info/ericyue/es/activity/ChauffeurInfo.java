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
import android.widget.TextView;


public class ChauffeurInfo extends Activity {

	
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, ChauffeurInfo.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	
	public Bundle bundle;
	
	public ListView cargoAbs ;
	private Button backHome,btnleft ;
	public String cartype,chauffaddress,chauffItems[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v1_huo_list);
		bundle =this.getIntent().getExtras();
		setView();
		cartype = bundle.getString("cartype");
		chauffaddress = bundle.getString("chaufaddress");
		queryCargoInfoItem();
		setClickListener();
		View myView = LayoutInflater.from(this).inflate(R.layout.v1_huo_item, null);
		TextView company_type,huo_name,huo_dun;
		company_type = (TextView)myView.findViewById(R.id.txt_company_type);
		huo_name = (TextView)myView.findViewById(R.id.txt_huo_name);
		
		huo_dun = (TextView)myView.findViewById(R.id.txt_huo_dun);

	}
	protected void setClickListener(){
		
		backHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
//				ExpressSensor.launch(CargoInfo.this, bundle);
				ChauffeurInfo.this.finish();
			}
		});
		
		btnleft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ChauffeurInfo.this.finish();
				
			}
		});
		
	}
	protected void setView(){
		backHome = (Button)findViewById(R.id.btn_h_zhuye);
		cargoAbs = (ListView)findViewById(R.id.list_condi_huo);
		btnleft= (Button)findViewById(R.id.btn_title_left);
		
	}
	public void fillListItem(){
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
			map.put("cargo_address", chauffaddress);
			map.put("company_Name", itemStrs[3]);
			map.put("cargo_type", itemStrs[7]);
			map.put("cargo_Name", itemStrs[0]);
			map.put("cargo_w","ÖÐ×ªµØ"+itemStrs[4]);
			map.put("introduce", "");
			map.put("release_date", itemStrs[8]);

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
				bundle.putString("chaufferDetail", chauffItems[position]);
				System.out.println(chauffItems[position]);
				ChauffeurDetail.launch(ChauffeurInfo.this, bundle);
				
			}
		});
	}
	public void queryCargoInfoItem(){
		 String str = HttpUtil.Query_chauff_info(cartype, chauffaddress);
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
