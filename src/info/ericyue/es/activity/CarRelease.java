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
import info.ericyue.es.activity.CargoRelease.MyHandler;
import info.ericyue.es.activity.CargoRelease.SubmitInfoThread;
import info.ericyue.es.myView.SelectAddressView;
import info.ericyue.es.util.HttpUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CarRelease extends Activity {

	private Button btReturn, btReset, btProvinces, btCitys, btCounty,
			ddProvinces, ddCitys, ddCounty, btRelease;
	private int myyear, mymonth, mydate;
	private EditText editMethods, datePickText, editExplain;
	private Spinner carNumSpinner, wordSpinner;
	private RadioGroup typeGroup;
	private RadioButton returnCar;
	private RadioButton localCar;
	private RadioGroup timeGroup;
	private RadioButton immediateCar;
	private RadioButton longtermCar;
	
	

	Bundle bundle;
	
	
	private String cfddress, ddddress, carMethods, setoutdate, cartype,
			timetype, words,explain,carNum;

	private ArrayAdapter<String> spinnercarAdapter, spinnerWordsAdapter;
	private List<String> list = new ArrayList<String>();
			//wordsList = new ArrayList<String>();
	info.ericyue.es.myView.SelectAddressView carReleaseFromAddress,carReleaseToAddress ;
	ProgressDialog pb;
	MyHandler mhandler;
	String userName;

	public static void launch(Context c, Bundle bundle) {
		Intent intent = new Intent(c, CarRelease.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carrelease);

		mhandler = new MyHandler();
		pb = new ProgressDialog(this);
		carReleaseFromAddress =(SelectAddressView)findViewById(R.id.carreleasefrom);
		carReleaseToAddress =(SelectAddressView)findViewById(R.id.carreleaseto);
		btReturn = (Button) findViewById(R.id.btReturn);
		btReset = (Button) findViewById(R.id.btReset);

		editMethods = (EditText) findViewById(R.id.editMethods);
		carNumSpinner = (Spinner) findViewById(R.id.carNumSpinner);
		typeGroup = (RadioGroup) findViewById(R.id.typeGroup);
		returnCar = (RadioButton) findViewById(R.id.returnCar);
		localCar = (RadioButton) findViewById(R.id.localCar);
		timeGroup = (RadioGroup) findViewById(R.id.timeGroup);
		immediateCar = (RadioButton) findViewById(R.id.immediateCar);
		longtermCar = (RadioButton) findViewById(R.id.longtermCar);
		datePickText = (EditText) findViewById(R.id.datepicker);
		wordSpinner = (Spinner) findViewById(R.id.wordSpinner);
		editExplain = (EditText) findViewById(R.id.editExplain);
		btRelease = (Button) findViewById(R.id.btRelease);
		cartype="";
		timetype="";
		SetList();
		SetSpinnerAdater();
		SetRadioGroup();
		SetDefaultDate();
		SetDateText();
//		SetView();
		SetReleaseButton();
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
				break;
			case 2:
				pb.dismiss();
				Toast.makeText(CarRelease.this, "发布成功！",Toast.LENGTH_SHORT).show();
				CarRelease.this.finish();
				Log.i("pbshow","dismiss");
				break;
			case 3:
				Toast.makeText(CarRelease.this, "发布不成功，请检查网络和输入后发布！",Toast.LENGTH_SHORT).show();
				break;
			}	
		}
		
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
			
			String rs= HttpUtil.insertVehicleInfo(userName, cfddress, ddddress, carMethods,
					carNum, cartype, timetype, 
					setoutdate, words, explain);
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
	
	private void SetReleaseButton() {

		btRelease.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int radioButtonId = typeGroup.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) CarRelease.this
						.findViewById(radioButtonId);
				cartype = rb.getText().toString();
				int timeradioButtonId = timeGroup.getCheckedRadioButtonId();
				RadioButton rb2 = (RadioButton) CarRelease.this
						.findViewById(timeradioButtonId);
				timetype = rb2.getText().toString();
				cfddress = carReleaseFromAddress.getFromAddress();
				ddddress = carReleaseToAddress.getFromAddress();
				carMethods = editMethods.getText().toString();

				explain = editExplain.getText().toString();
				userName = bundle.getString("username");
				System.out.println(cfddress + ddddress + carMethods);
				if(CheckInput()){
					SubmitInfoThread submitThread = new SubmitInfoThread();
					Thread myThread = new Thread(submitThread);
					myThread.start();
				

				}
			}
		});

	}
	private boolean CheckInput(){
		
		if(cfddress.equals("省份市区县区")||cfddress.isEmpty()){
			Toast.makeText(CarRelease.this, "请选择出发地", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(ddddress.equals("省份市区县区")||ddddress.isEmpty()){
			Toast.makeText(CarRelease.this, "请选择到达地", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(carNum.isEmpty()){
			Toast.makeText(CarRelease.this, "请输入车牌号", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(cartype.isEmpty()){
			Toast.makeText(CarRelease.this, "请选择行驶类型", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(timetype.isEmpty()){
			Toast.makeText(CarRelease.this, "请选择即时或长期", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}

	private void SetDefaultDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 10);
		cal.add(Calendar.MONTH, 1); // android bug
		myyear = cal.get(Calendar.YEAR);
		mymonth = cal.get(Calendar.MONTH);
		mydate = cal.get(Calendar.DAY_OF_MONTH);
		String myMonth = String.format("%02d", mymonth);
		String myDate =String.format("%02d", mydate);
		datePickText.setText(myyear + "-" + myMonth + "-" + myDate);
		setoutdate = myyear + "-" + myMonth + "-" + myDate;
	}

	private void SetDateText() {
		datePickText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog.OnDateSetListener dpListener = new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						myyear = year;
						mymonth = monthOfYear + 1;
						mydate = dayOfMonth;
						UpDate();
					}
				};
				DatePickerDialog dpd = new DatePickerDialog(CarRelease.this,
						dpListener, myyear, mymonth - 1, mydate);
				dpd.show();
			}
		});
	}

	private void UpDate() {
		
		String myMonth = String.format("%02d", mymonth);
		String myDate =String.format("%02d", mydate);
		datePickText.setText(myyear + "-" + myMonth + "-" + myDate);
		setoutdate = myyear + "-" + myMonth + "-" + myDate;
	}

	private void SetRadioGroup() {
		
		typeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton) CarRelease.this
						.findViewById(radioButtonId);
				cartype = rb.getText().toString();
			}
		});

		timeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				RadioButton rb2 = (RadioButton) CarRelease.this
						.findViewById(radioButtonId);
				timetype = rb2.getText().toString();
			}
		});
	}

	private void SetSpinnerAdater() {

		spinnercarAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return super.getCount() - 1;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View v = super.getView(position, convertView, parent);
				if (position == getCount()) {
					((TextView) v.findViewById(android.R.id.text1)).setText("");
					((TextView) v.findViewById(android.R.id.text1))
							.setHint(getItem(getCount()));// "Hint to be displayed"
				}

				return v;
			}

		};
		spinnercarAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		carNumSpinner.setAdapter(spinnercarAdapter);
		carNumSpinner.setSelection(spinnercarAdapter.getCount());
		carNumSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// cargoname.setText(spinnercarAdapter.getItem(position));
				carNum = spinnercarAdapter.getItem(position);
				parent.setVisibility(view.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				parent.setVisibility(View.INVISIBLE);
			}
		});
		carNumSpinner
				.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (hasFocus) {
							v.setVisibility(View.INVISIBLE);
						}
					}
				});
		spinnerWordsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		spinnerWordsAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		wordSpinner.setAdapter(spinnerWordsAdapter);
		wordSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// cargoname.setText(spinnercargoAdapter.getItem(position));
				words = spinnerWordsAdapter.getItem(position);
				parent.setVisibility(view.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				parent.setVisibility(View.INVISIBLE);
			}
		});

	}

	private void SetList() {
		String words[] = { "求货源", "有货速联系", "低价急走", "运价好商量", "装车付款", "司机在等",
				"回程车", "装车就走", "求零担", "晚上也可装车" };
		for (int i = 0; i < words.length; i++) {
			list.add(words[i]);

		}
	}
	

}
