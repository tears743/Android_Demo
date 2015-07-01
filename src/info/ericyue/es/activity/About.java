/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * Project Name:ExpressSensor
 * Create Date: 2011-7-17
 */
package info.ericyue.es.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import info.ericyue.es.R;
import info.ericyue.es.util.HttpUtil;

public class About extends Activity {
	private ListView listView;
	private String[] str=new String[8];
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, About.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.about);
        listView=(ListView) findViewById(R.id.aboutlist);
        listView.setCacheColorHint(0);
        fillItemList();
	}
	private void fillItemList(){
		queryDatabase();
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		String[] itemStr={"软件简介","软件作者","联系方式","项目主页","提交BUG","客户端当前版本","软件用户数量","最近更新"};
  		for(int i=0;i<8;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();  
		    map.put("ItemTitle", itemStr[i]);
		    map.put("ItemText", str[i]);
		    listItem.add(map); 
		}
	    SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
	    	R.layout.aboutlist,  
 	    	new String[] {"ItemTitle","ItemText"},   
 	    	new int[] {R.id.ItemTitle,R.id.ItemText}  
	    ); 
		listView.setAdapter(listItemAdapter);
		listView.setOnItemClickListener(itemListener);
	} 
	public void queryDatabase(){
		str[0]="本软件是一款基于Android平台的智能物流管理系统.特色在于让用户通过GPS来实时定位自己货物.并且采用\"类NFC\"的概念来实现包裹自动感应签收";
		str[1]="卓越科技";
		str[2]="E-mail:tangjian743@126.com";
		str[3]="http://216.170.125.124:8080/hy";
		str[4]="点击发送邮件提交BUG";
		String stream=HttpUtil.QuerySystemInfo("0");
		String[] sys=stream.split(",");
		str[5]="当前软件版本: "+VersionStatus(sys[0]);
		str[6]="当前用户数量: "+sys[1] +"人";
		str[7]="最近一次更新时间: "+sys[2];
	}
	public String VersionStatus(String version){
		String[] tmp=version.split("\\.");
		if(tmp[0].compareTo("3")>=0){
			return version+=" Release";
		}
		else if(tmp[0].compareTo("2")>=0){
			return version+=" Release Candidate";
		}
		else{
			return version+=" Alpha";
		}
	}
	public void openURL(String url){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}
	public void mailToEricyue(boolean bug){
		Uri uri=Uri.parse("mailto:tangjian743@126.com");
		Intent mail= new Intent(Intent.ACTION_SENDTO,uri);
		if(bug)
			mail.putExtra(Intent.EXTRA_SUBJECT, "[Express Sensor]BUG提交");
		startActivity(mail);
	}
	private OnItemClickListener itemListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			switch(position){
			case 0:
 				break;
			case 1:
				openURL("216.170.125.124:8080/hy/");
 				break;
			case 2:
				mailToEricyue(false);
 				break;	
			case 3:
				openURL("216.170.125.124:8080/hy/");
 				break;
			case 4:
				mailToEricyue(true);
 				break;
			case 5:
 				break;
				
			}	
		}
	};

 }