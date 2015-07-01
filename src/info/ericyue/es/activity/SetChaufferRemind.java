package info.ericyue.es.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.ericyue.es.R;
import info.ericyue.es.myView.SelectAddressView;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;



public class SetChaufferRemind extends Activity {
	
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c,SetChaufferRemind.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	info.ericyue.es.myView.SelectAddressView selectAddressView ;
	String chaufaddress,cartype;
	private Spinner cartypespinner;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> spinnercargoAdapter;
	private Button sijisubmit;
	private EditText datePickText;
	private String deaddate,release_date;
	private int myyear,mymonth,mydate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setchaufferremind);

		selectAddressView = (info.ericyue.es.myView.SelectAddressView)this.findViewById(R.id.chaufferaddress1);

		sijisubmit = (Button)findViewById(R.id.setchaufferremind);
		cartypespinner = (Spinner)findViewById(R.id.chaufferType);
		SetList();
		SetSpinnerAdapter();
		sijisubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					chaufaddress = selectAddressView.getFromAddress();

//					System.out.println( cartype+chaufaddress);

					if(chaufaddress.equals("")){
					Toast.makeText(SetChaufferRemind.this, "请选择司机所在地",Toast.LENGTH_SHORT).show();
				}
					else if(cartype.equals("请选择驾照类型")){
						Toast.makeText(SetChaufferRemind.this, "请选择驾照类型",Toast.LENGTH_SHORT).show();
					}
				else
				{
					SharedPreferences sp = getSharedPreferences("ChaufferRemind", MODE_MULTI_PROCESS);
					Editor ed = sp.edit();
					ed.putString("DateResearchString", deaddate);
					ed.putString("FromAddressSearchString",chaufaddress );
					ed.putString("chaufferType", cartype);
					if(ed.commit())
					{
						Toast.makeText(SetChaufferRemind.this, "设置成功",Toast.LENGTH_SHORT).show();
						SetChaufferRemind.this.finish();
					}
					else{
						Toast.makeText(SetChaufferRemind.this, "设置失败",Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		datePickText = (EditText)findViewById(R.id.chaufferdateselector);
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
				DatePickerDialog dpd = new DatePickerDialog(SetChaufferRemind.this,dpListener, myyear, mymonth-1, mydate);
				dpd.show();
			}
		});
		
		
		
	}
	private void UpDate(){
	 
	 datePickText.setText(myyear+"-"+mymonth+"-"+mydate);
	 deaddate =myyear+"-"+mymonth+"-"+mydate;
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
