package info.ericyue.es.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import info.ericyue.es.R;
import info.ericyue.es.myView.SelectAddressView;
import info.ericyue.es.util.HttpUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class ChauffeurRelease extends Activity {

	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, ChauffeurRelease.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	

	
	private EditText chaufname,chaufhealth,driverexp ,effectdate,remark,chaufphone;
	private Spinner cartypespinner;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> spinnercargoAdapter;
	
	private String cartype,effectdatestr,release_date,chauffeurname,username,
				chauffeurhealth,driverexpstr,remarkstr,chaufaddress,chaufphonestr;
	private int myyear,mymonth,mydate;
	private Bundle bundle;
	private Button submit; 
	info.ericyue.es.myView.SelectAddressView chauffeurReleaseAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chauffeurrelease);
		chauffeurReleaseAddress = (SelectAddressView)findViewById(R.id.chauffeurrelease);
		chaufname = (EditText)findViewById(R.id.chaufname);
		chaufhealth= (EditText)findViewById(R.id.chaufhealth);
		driverexp = (EditText)findViewById(R.id.driverexp);
		effectdate = (EditText)findViewById(R.id.effectdate);
		remark = (EditText)findViewById(R.id.remark);
		cartypespinner = (Spinner)findViewById(R.id.cartypespinner);
		chaufphone = (EditText)findViewById(R.id.chaufphone);
		SetList();
		SetSpinnerAdapter();
		SetDefaultDate();
		SetDateText();
		bundle = this.getIntent().getExtras();
		submit = (Button)findViewById(R.id.chauffsubmit);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				chauffeurname = chaufname.getText().toString();
				username = bundle.getString("username");
				chauffeurhealth = chaufhealth.getText().toString();
				driverexpstr = driverexp.getText().toString();
				remarkstr = remark.getText().toString();
				chaufaddress =chauffeurReleaseAddress.getFromAddress();
				chaufphonestr = chaufphone.getText().toString();
				System.out.println(cartype+effectdatestr+release_date+chauffeurname+username+
					chauffeurhealth+driverexpstr+remarkstr+chaufaddress+chaufphonestr);
				if(CheckInput()){
				String rs =HttpUtil.insertChauffeurInfo(chauffeurname, chauffeurhealth,
						driverexpstr,cartype, chaufaddress, 
						chaufphonestr, effectdatestr, remarkstr,release_date, username);
					if(rs.equals("1")){
						Toast.makeText(getApplicationContext(),"发布成功！", Toast.LENGTH_SHORT);
						ChauffeurRelease.this.finish();
						
					}
					else{
						Toast.makeText(getApplicationContext(),"发布不成功，请检查网络和输入后发布！", Toast.LENGTH_SHORT);
					}
				}
				
			}
		});
	}
	
	private boolean CheckInput(){
		
		if(chaufaddress.equals("省份城市县区")||chaufaddress.isEmpty()){
			Toast.makeText(ChauffeurRelease.this, "请选择司机所在地", Toast.LENGTH_SHORT).show();
			return false;			
		}
		if(chauffeurname.isEmpty()){
			Toast.makeText(ChauffeurRelease.this, "请输入司机姓名", Toast.LENGTH_SHORT).show();
			return false;			
		}
		if(chauffeurhealth.isEmpty()){
			Toast.makeText(ChauffeurRelease.this, "请输入司机健康状况", Toast.LENGTH_SHORT).show();
			return false;			
		}
		if(driverexpstr.isEmpty()){
			Toast.makeText(ChauffeurRelease.this, "请输入司机驾龄", Toast.LENGTH_SHORT).show();
			return false;			
		}
		if(cartype.isEmpty()||cartype.equals("请选择驾照类型")){
			Toast.makeText(ChauffeurRelease.this, "请输入所开车型", Toast.LENGTH_SHORT).show();
			return false;			
		}
		if(chaufphonestr.isEmpty()){
			Toast.makeText(ChauffeurRelease.this, "请输入联系电话", Toast.LENGTH_SHORT).show();
			return false;			
		}
		
		
		return true;
	}
	
	
	private void SetDefaultDate(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 10);
		cal.add(Calendar.MONTH, 1);  //android bug
		myyear = cal.get(Calendar.YEAR);
		mymonth = cal.get(Calendar.MONTH);
		mydate = cal.get(Calendar.DAY_OF_MONTH);
		effectdate.setText(myyear+"-"+mymonth+"-"+mydate);
		effectdatestr = myyear+"-"+mymonth+"-"+mydate;
		release_date = effectdatestr;
	}
private void SetDateText(){
		
		
		
	effectdate.setOnClickListener(new OnClickListener() {
			
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
				DatePickerDialog dpd = new DatePickerDialog(ChauffeurRelease.this,dpListener, myyear, mymonth-1, mydate);
				dpd.show();
			}
		});
		
		
		
	}
	private void UpDate(){
		 
		effectdate.setText(myyear+"-"+mymonth+"-"+mydate);
		effectdatestr =myyear+"-"+mymonth+"-"+mydate;
	}
	private void SetSpinnerAdapter(){
		spinnercargoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				list){

					@Override
					public int getCount() {
						// TODO Auto-generated method stub
						return super.getCount()-1;
					}

					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {
						// TODO Auto-generated method stub
						View v= super.getView(position, convertView, parent);
						 if (position == getCount()) {
							 ((TextView)v.findViewById(android.R.id.text1)).setText("");
					         ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));//"Hint to be displayed"
					        }
						 	
					        return v;
					}
			
		};
		spinnercargoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cartypespinner.setAdapter(spinnercargoAdapter);
		cartypespinner.setSelection(spinnercargoAdapter.getCount());
		cartypespinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				cartype=spinnercargoAdapter.getItem(position).toString();
				parent.setVisibility(view.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				parent.setVisibility(View.INVISIBLE);
			}
		});
		cartypespinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					v.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		
	}
	
	private void SetList(){
		
		String items[] = {"A1","A2","A3","B1","B2","C1","C2","C3","C4","请选择驾照类型"};
		for(int i=0;i<10;i++){
			list.add(items[i]);
			
		}
	}
	
}
