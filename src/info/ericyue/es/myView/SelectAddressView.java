package info.ericyue.es.myView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import info.ericyue.es.R;
import info.ericyue.es.activity.ChauffeurSearch;
import info.ericyue.es.util.AddressSelectorData;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectAddressView extends LinearLayout {

	private Button province,city,district;
	View alterView;
	GridView gdView ;
	SimpleAdapter saGrid;
	ArrayList<HashMap<String, Object>> provinceGrid = new ArrayList<HashMap<String,Object>>();
	AlertDialog.Builder alterdia;
	private Boolean proFlag=false,cityFlag=false,disFlag=false;
	int proID,cityID,disID,ID;
	AddressSelectorData addressSelector;
	Button anyWhere;
	
	private static final String DATABASE_PATH ="/data/data/info.ericyue.es/database";
	private static final String DATABASE_NAME = "china.db3";
	private static String outFileName = DATABASE_PATH + "/" + DATABASE_NAME;
	
	public SelectAddressView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.addressselectbutton, this,true);
		province = (Button)findViewById(R.id.setFromProvince);
		city = (Button)findViewById(R.id.setFromCity);
		district = (Button)findViewById(R.id.setFromCounty);
		anyWhere =(Button)findViewById(R.id.AnyWhere);
		InputStream myInput = getResources().openRawResource(R.raw.china);
		
		try {
			Bundle bundle = new Bundle();
			addressSelector = new AddressSelectorData(myInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Province();
		SetView();
		
		
	}
	public SelectAddressView(Context context,AttributeSet attrs){
		super(context,attrs);
		LayoutInflater.from(context).inflate(R.layout.addressselectbutton, this,true);
		province = (Button)findViewById(R.id.setFromProvince);
		city = (Button)findViewById(R.id.setFromCity);
		district = (Button)findViewById(R.id.setFromCounty);
		
		InputStream myInput = getResources().openRawResource(R.raw.china);

		try {
			Bundle bundle = new Bundle();
			addressSelector = new AddressSelectorData(myInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		if(isEnabled()){
//			 return; 
//		}
		Province();
		SetView();
		
		
	}
	
	
	private void District(String cName){
		File f = new File(outFileName);
		SQLiteDatabase db =  SQLiteDatabase.openOrCreateDatabase(f,null);
		
		Cursor cur1 = db.rawQuery("Select CitySort from T_City where CityName ="+"\""+cName+"\"", null);
		cur1.moveToNext();
		String cs = cur1.getString(cur1.getColumnIndexOrThrow("CitySort"));
		cur1.close();
		cityID = Integer.parseInt(cs);
		
		Cursor cur = db.rawQuery("Select ZoneName from T_Zone where CityID ="+cityID, null);
		provinceGrid.clear();
		while(cur.moveToNext()){
			HashMap<String, Object> map = new HashMap<String, Object>();
			String province = cur.getString(cur.getColumnIndexOrThrow("ZoneName"));
			map.put("p0", province);
			provinceGrid.add(map);
		}
		db.close();
	}
	
	@SuppressLint("ShowToast")
	public String getFromAddress (){
		String Address;
		
		if(district.getText().toString().equals("不限区域")){
			 Address = province.getText().toString()+city.getText().toString();
		}
		else{
			 Address = province.getText().toString()+city.getText().toString()
							+district.getText().toString();
		}
		
		if(Address.equals("省份市区县区")){
//			Toast.makeText(SelectAddressView.this, "请选择省份，市区，县区",Toast.LENGTH_SHORT);
			return "";
		}
		else{
			return Address.replace(" ", "");
					
		}
	}
	
	private void City(String pName){
		File f = new File(outFileName);
		SQLiteDatabase db =  SQLiteDatabase.openOrCreateDatabase(f,null);
		
		Cursor cur1 = db.rawQuery("Select ProSort from T_Province where ProName ="+"\""+pName+"\"", null);
		cur1.moveToFirst();
		String cs = cur1.getString(cur1.getColumnIndexOrThrow("ProSort"));
		proID = Integer.parseInt(cs);
		Cursor cur = db.rawQuery("Select CityName,CitySort from T_City where ProID ="+proID, null);
		provinceGrid.clear();
	
		while(cur.moveToNext()){
			HashMap<String, Object> map = new HashMap<String, Object>();
			String province = cur.getString(cur.getColumnIndexOrThrow("CityName"));
			map.put("p0", province);
			provinceGrid.add(map);
		}
		db.close();
	}
	
	private void SetView(){
		
		
		
		
		saGrid = new SimpleAdapter(SelectAddressView.this.getContext(), provinceGrid,R.layout.griditem,new String[] {"p0"}, new int[]{R.id.p0});
//		province =(Button) findViewById(R.id.oprovince);
		LayoutInflater mInflater = LayoutInflater.from(SelectAddressView.this.getContext());
		
		alterView =mInflater.inflate(R.layout.gridview, null);
		anyWhere =(Button)alterView.findViewById(R.id.AnyWhere);
		anyWhere.setVisibility(View.INVISIBLE);
		alterdia = new AlertDialog.Builder(SelectAddressView.this.getContext());
		LinearLayout ll = (LinearLayout)alterView.findViewById(R.id.pro);
		alterdia.setView(alterView);
		final  AlertDialog alertdialog = alterdia.create();
		gdView = (GridView)alterView.findViewById(R.id.gdview);
		gdView.setAdapter(saGrid);
		gdView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					ID=position +1;
					
				  HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
				  String itemText=(String)item.get("p0");
				  if(province.getText().equals("省份")||!proFlag){
					  anyWhere.setVisibility(View.INVISIBLE);
					  proID = ID;
					  proFlag=true;
					  province.setText(itemText);
					  Toast.makeText(SelectAddressView.this.getContext(), itemText, Toast.LENGTH_SHORT).show();
					  City(itemText);
					  saGrid.notifyDataSetChanged();
				  }
				  else if(city.getText().equals("城市")||!cityFlag){
					  
					  cityID = ID;
					  System.out.println(ID);
					  cityFlag=true;
					  anyWhere.setVisibility(View.VISIBLE);
					  city.setText(itemText);
					  Toast.makeText(SelectAddressView.this.getContext(), itemText, Toast.LENGTH_SHORT).show();
					  District(itemText);
					  saGrid.notifyDataSetChanged();
//					  saGrid.notifyDataSetChanged();
//					  alertdialog.dismiss();
					  Toast.makeText(SelectAddressView.this.getContext(), itemText, Toast.LENGTH_SHORT).show();
				  }
				  else {
					  disID = ID;
					  district.setText(itemText);
					  anyWhere.setVisibility(View.VISIBLE);
					  Toast.makeText(SelectAddressView.this.getContext(), itemText, Toast.LENGTH_SHORT).show();
					  alertdialog.dismiss();
					  proFlag=false;
					  cityFlag=false;
				  }
				  
//				  System.out.println(position);
				  
			}
			
		});
		anyWhere.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						district.setText("不限区域");
						alertdialog.dismiss();
						
					}
				});
		city.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(province.getText().equals("省份")&&!proFlag){
					Toast.makeText(SelectAddressView.this.getContext(), "请先选择省份", Toast.LENGTH_SHORT).show();
				}
				else{
					anyWhere.setVisibility(View.INVISIBLE);
					City(province.getText().toString());
					cityFlag=false;
					proFlag=true;
					alertdialog.show();
				}
				
			}
		});
		district.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(city.getText().equals("市区")&&!cityFlag){
					Toast.makeText(SelectAddressView.this.getContext(), "请先选择城市", Toast.LENGTH_SHORT).show();
				}
				else{
					anyWhere.setVisibility(View.VISIBLE);
					proFlag=true;
					cityFlag=true;
					disFlag=true;
					District(city.getText().toString());
					alertdialog.show();
				}
			}
		});
		
		province.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				anyWhere.setVisibility(View.INVISIBLE);
				proFlag=false;
				cityFlag=false;
				disFlag=false;
				provinceGrid.clear();
				Province();
				saGrid.notifyDataSetChanged();
				alertdialog.show();
				
//				System.out.println(arg0);
				
			}
		});
	}
	private void Province(){
		
		File f = new File(outFileName);
		SQLiteDatabase db =  SQLiteDatabase.openOrCreateDatabase(f,null);
		Cursor cur = db.rawQuery("Select ProName,ProSort from T_Province", null);
//		String ps=cur.getString(cur.getColumnIndexOrThrow("ProSort"));
//		proID= Integer.parseInt(ps);
		provinceGrid.clear();
		while(cur.moveToNext()){
			HashMap<String, Object> map = new HashMap<String, Object>();
			String province = cur.getString(cur.getColumnIndexOrThrow("ProName"));
			
			map.put("p0", province);
			provinceGrid.add(map);
		}
		db.close();
	
	}

}
