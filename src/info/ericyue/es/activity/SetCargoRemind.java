package info.ericyue.es.activity;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import info.ericyue.es.R;
import info.ericyue.es.util.AddressSelectorData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SetCargoRemind extends Activity {
	String release_date;
	Button setCargoRemind;
	
	private EditText datePickText;
	private String deaddate;
	private int myyear,mymonth,mydate;
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, SetCargoRemind.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}

	info.ericyue.es.myView.SelectAddressView selectAddressView2,selectAddressView3 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		InputStream myInput = getResources().openRawResource(R.raw.china);
		setContentView(R.layout.setcargoremind);
		try {
			Bundle bundle = new Bundle();
//			addressSelector = new AddressSelectorData(myInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		InitView();
		SetView();
		SetDateText();
		SetDefaultDate();
	}
	private void SetDefaultDate(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 10);
		cal.add(Calendar.MONTH, 1);  //android bug
		myyear = cal.get(Calendar.YEAR);
		mymonth = cal.get(Calendar.MONTH);
		mydate = cal.get(Calendar.DAY_OF_MONTH);
		datePickText.setText(myyear+"-"+mymonth+"-"+mydate);
		deaddate = myyear+"-"+mymonth+"-"+mydate;
		release_date = deaddate;
	}
	private void SetDateText(){
		
		
		
		datePickText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog.OnDateSetListener dpListener = new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						myyear = year;
						mymonth = monthOfYear+1;
						mydate = dayOfMonth;
						UpDate();
					}
				};
				DatePickerDialog dpd = new DatePickerDialog(SetCargoRemind.this,dpListener, myyear, mymonth-1, mydate);
				dpd.show();
			}
		});
		
		
		
	}
	private void UpDate(){
	 
	 datePickText.setText(myyear+"-"+mymonth+"-"+mydate);
	 deaddate =myyear+"-"+mymonth+"-"+mydate;
}
	
	private void InitView(){
//		setFromProvince = (Button)findViewById(R.id.setFromProvince);
//		setFromCity = (Button)findViewById(R.id.setFromCity);
//		setFromCounty = (Button)findViewById(R.id.setFromCounty);
//		setToProvince = (Button)findViewById(R.id.setToProvinces);
//		setToCity = (Button)findViewById(R.id.setToCitys);
//		setToCounty = (Button)findViewById(R.id.setToCounty);
		selectAddressView2 = (info.ericyue.es.myView.SelectAddressView)this.findViewById(R.id.chaufferaddress2);
		selectAddressView3 = (info.ericyue.es.myView.SelectAddressView)this.findViewById(R.id.chaufferaddress3);
		datePickText = (EditText)findViewById(R.id.dateselector);
		setCargoRemind = (Button)findViewById(R.id.setcargoremind);
//		SetView();
	}
	
	private void SetView(){
		
		setCargoRemind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String fromAdress = selectAddressView2.getFromAddress();
				String toAdress = selectAddressView3.getFromAddress();
//				
//				Log.i("from to :",fromAdress+" "+toAdress);
				if(!(fromAdress.equals("")&&toAdress.equals(""))){
					SharedPreferences sp = getSharedPreferences("CargoRemind", MODE_MULTI_PROCESS);
					Editor ed = sp.edit();
					ed.putString("DateResearchString", deaddate);
					ed.putString("FromAddressSearchString",fromAdress );
					ed.putString("ToAddressSearchString",toAdress );
					ed.apply();
					SetCargoRemind.this.finish();
				}
				else{
					Toast.makeText(SetCargoRemind.this, "«Î—°‘Òµÿ÷∑£°", Toast.LENGTH_SHORT).show();
					
				}
			}
		});
		

	}

}
