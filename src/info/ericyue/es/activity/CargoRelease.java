package info.ericyue.es.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import info.ericyue.es.R;
import info.ericyue.es.myView.SelectAddressView;
import info.ericyue.es.util.HttpUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.ArrayAdapter; 
import android.widget.AdapterView; 
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CargoRelease extends Activity {
	
	private Spinner mySpinner; 

	private int myyear,mymonth,mydate;
	

	String release_date;
	
	private List<String> list = new ArrayList<String>(),transList = new ArrayList<String>();    
    private EditText cargoname,datePickText,releationer,releationcompany,relationphone,fee,volume,weight,number,
    				 backup;    
    private Spinner cargospinner,transportModSpinner;    
    private ArrayAdapter<String> spinnercargoAdapter,spinnerTransAdapter; 
	
    private RadioGroup cargoType;
    
    private CheckBox upward,dampproof,fragile,dangerCargo;
    private Button submit;
    
    private String raddress,daddress,cargonamestr,cargotrans,cargotype,noticeitem,
    				deaddate,releationname,releationphone,feestr,volumestr,weightstr,
    				numberstr,backupstr,relcompany;
    String userName;
    private Bundle bundle;
    
    ProgressDialog pb;
   
    
    info.ericyue.es.myView.SelectAddressView cargoReleaseFromAddress,cargoReleaseToAddress ;
    MyHandler mhandler;
    
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, CargoRelease.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.cargorelease);
		pb =new ProgressDialog(CargoRelease.this);
		mhandler = new MyHandler();
		
		cargoReleaseFromAddress = (SelectAddressView)findViewById(R.id.cargoreleasefrom);
		cargoReleaseToAddress =(SelectAddressView)findViewById(R.id.cargoreleaseto);
		cargoname = (EditText)findViewById(R.id.cargoname);
		cargospinner = (Spinner)findViewById(R.id.cargospinner);
		transportModSpinner = (Spinner)findViewById(R.id.transportModSpinner);
		cargoType = (RadioGroup)findViewById(R.id.cargoType);
		upward = (CheckBox)findViewById(R.id.upward);
		dampproof = (CheckBox)findViewById(R.id.dampproof);
		fragile =(CheckBox)findViewById(R.id.fragile);
		dangerCargo =(CheckBox)findViewById(R.id.dangerCargo);
		datePickText = (EditText)findViewById(R.id.datepicker);
		releationer =  (EditText)findViewById(R.id.relationer);
		relationphone =(EditText)findViewById(R.id.rmphoneno);
//		releationcompany = (EditText)findViewById(R.id.relation_company);
		fee = (EditText)findViewById(R.id.fee);
		volume = (EditText)findViewById(R.id.volume);
		weight =  (EditText)findViewById(R.id.weight);
		number =  (EditText)findViewById(R.id.number);
		backup = (EditText)findViewById(R.id.explain);
		
		submit =(Button)findViewById(R.id.submit);
		noticeitem = "";
		cargonamestr= "";
		cargotrans="";
		SetList();
		SetSpinnerAdater();
		SetRadioGroup();
		SetCheckBox();
		SetDefaultDate();
		SetDateText();
//		SetView();
		SetSubmitButton();
		bundle = this.getIntent().getExtras();
		
	}
	
	class MyHandler extends Handler{

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what){
			case 1:
				pb.setTitle("正在提交请稍后...");
				pb.setMessage("请稍等，提交后自动跳转");
				pb.show();
				Log.i("pbshow","show");
				mhandler.removeMessages(1);
//				msg.recycle();
				break;
			case 2:
				pb.dismiss();
				Toast.makeText(CargoRelease.this, "发布成功！",Toast.LENGTH_SHORT).show();
				CargoRelease.this.finish();
				Log.i("pbshow","dismiss");
				mhandler.removeMessages(2);
//				msg.recycle();
				break;
			case 3:
				Toast.makeText(CargoRelease.this, "发布不成功，请检查网络和输入后发布！",Toast.LENGTH_SHORT).show();
				mhandler.removeMessages(1);
//				msg.recycle();
				break;
			}	
		}
		
	}
	
	private void SetSubmitButton(){
		
		submit.setOnClickListener(new OnClickListener() {
			
			
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				cargotype = "货";
				raddress = cargoReleaseFromAddress.getFromAddress();
				daddress = cargoReleaseToAddress.getFromAddress();
				cargonamestr = cargoname.getText().toString();

				releationname = releationer.getText().toString();

				releationphone = relationphone.getText().toString();
				feestr = fee.getText().toString();
				volumestr = volume.getText().toString();
				weightstr = weight.getText().toString(); 
				backupstr = backup.getText().toString();
				
				userName = bundle.getString("username");
				
				if(CheckInput()){
				
					SubmitInfoThread submitThread = new SubmitInfoThread();
					Thread myThread = new Thread(submitThread);
					myThread.start();
				
						
						
				}
				
			}
		});
		
		
	}
	
	class SubmitInfoThread implements Runnable{

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			Message msg1 = new Message();
			msg1.what = 1;
			mhandler.sendMessage(msg1);
			
			String rs=HttpUtil.insertCargoInfo(userName, raddress, daddress, cargonamestr, weightstr, backupstr,
					release_date, volumestr, cargotype, deaddate, cargotrans,feestr, 
					releationname, releationphone, noticeitem);
			if(rs.equals("1")){
				Message msg2 = new Message();
				msg2.what = 2;
				mhandler.sendMessage(msg2);
			
		}
			else{
				Message msg3 = new Message();
				msg3.what = 3;
				mhandler.sendMessage(msg3);
				
			}
		}
		
	}
	 
	
	public boolean  CheckInput(){
		if(raddress.equals("省份城市县区")||raddress.isEmpty()||raddress.equals("nullnullnull")){
			Toast.makeText(CargoRelease.this, "请选择出发地",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(daddress.equals("省份城市县区")||daddress.isEmpty()||daddress.equals("nullnullnull")){
			Toast.makeText(CargoRelease.this, "请选择到达地",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(cargonamestr.isEmpty()){
			Toast.makeText(CargoRelease.this, "请输入/选择货物名称",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(weightstr.isEmpty()){
			Toast.makeText(CargoRelease.this, "请输入重量",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(cargotype.isEmpty()){
			Toast.makeText(CargoRelease.this, "请选择货物类型",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(releationname.isEmpty()){
			Toast.makeText(CargoRelease.this, "请输入联系人",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(cargotrans.isEmpty()){
			Toast.makeText(CargoRelease.this, "请选择运输方式",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(releationphone.isEmpty()){
			Toast.makeText(CargoRelease.this, "请输入联系电话",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(noticeitem.isEmpty()){
			Toast.makeText(CargoRelease.this, "请选择注意事项",Toast.LENGTH_SHORT).show();
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
		String myMonth = String.format("%02d", mymonth);
		String myDate =String.format("%02d", mydate);
		datePickText.setText(myyear+"-"+myMonth+"-"+myDate);
		deaddate = myyear+"-"+myMonth+"-"+myDate;
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
				DatePickerDialog dpd = new DatePickerDialog(CargoRelease.this,dpListener, myyear, mymonth-1, mydate);
				dpd.show();
			}
		});
		
		
		
	}
	
	 private void UpDate(){
		 String myMonth = String.format("%02d", mymonth);
		 String myDate =String.format("%02d", mydate);
		 datePickText.setText(myyear+"-"+myMonth+"-"+myDate);
		 deaddate =myyear+"-"+myMonth+"-"+myDate;
	 }
	
	private void SetCheckBox(){
		upward.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

            	if(arg1){
            		Toast.makeText(CargoRelease.this, upward.getText(), Toast.LENGTH_SHORT).show();
            		noticeitem = "向上";
            	}
                
            }

			
        });
		dampproof.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

            	if(arg1){
            		Toast.makeText(CargoRelease.this, dampproof.getText(), Toast.LENGTH_SHORT).show();
            		noticeitem = "防潮";
            	}
                
            }

			
        });
		fragile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
   
            	if(arg1){
            		noticeitem = "易碎";
            		Toast.makeText(CargoRelease.this,fragile.getText(), Toast.LENGTH_SHORT).show();
            	}
                
            }

			
        });		
		dangerCargo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
            	if(arg1){
            		noticeitem = "危险品";
            		Toast.makeText(CargoRelease.this, dangerCargo.getText(), Toast.LENGTH_SHORT).show();
            	}
                
            }

			
        });		
		
	}
	
	private void SetRadioGroup(){
		cargoType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton)CargoRelease.this.findViewById(radioButtonId);
				cargotype = rb.getText().toString();
			}
		});
	}
	
	private void SetSpinnerAdater(){
		
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
		cargospinner.setAdapter(spinnercargoAdapter);
		cargospinner.setSelection(spinnercargoAdapter.getCount());
		cargospinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				cargoname.setText(spinnercargoAdapter.getItem(position));
				parent.setVisibility(view.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				parent.setVisibility(View.INVISIBLE);
			}
		});
		cargospinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					v.setVisibility(View.INVISIBLE);
				}
			}
		});
		spinnerTransAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,transList);
		spinnerTransAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		transportModSpinner.setAdapter(spinnerTransAdapter);
		transportModSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				cargoname.setText(spinnercargoAdapter.getItem(position));
				cargotrans = spinnerTransAdapter.getItem(position);
				parent.setVisibility(view.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				parent.setVisibility(View.INVISIBLE);
			}
		});
		
	}
	
	private void SetList(){
		String items[] = {"设备","煤炭","矿产","钢材","饲料","建材","木材","粮食"
				,"食品","饮料","蔬菜","电器","化工产品","畜产品","可自行输入或是选择名称"};
		String transportMod[] = {"不限","物流公司","整车配货","零担配货"};
		for(int i =0;i<items.length;i++){
			list.add(items[i]);
			
		}
		for(int i =0;i<transportMod.length;i++){
			transList.add(transportMod[i]);
			
		}
	}

}
