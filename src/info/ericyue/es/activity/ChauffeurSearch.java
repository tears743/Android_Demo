package info.ericyue.es.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.ericyue.es.R;
import info.ericyue.es.myView.SelectAddressView;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class ChauffeurSearch extends Activity {

	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, ChauffeurSearch.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	info.ericyue.es.myView.SelectAddressView chauffeurFromAddress ;
	private Spinner cartypespinner;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> spinnercargoAdapter;
	private String cartype,chaufaddress;
	private Button sijiback,sijisubmit;
	private Bundle bundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chauffeursearch);
		bundle = this.getIntent().getExtras();

		chauffeurFromAddress =(SelectAddressView)findViewById(R.id.chauffeurAddresssearch);
		cartypespinner =(Spinner)findViewById(R.id.chaufsearch);
		SetList();
		SetSpinnerAdapter();
		sijiback = (Button)findViewById(R.id.sijiback);
		sijisubmit = (Button)findViewById(R.id.sijisearchsubmit);
		sijiback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ChauffeurSearch.this.finish();
				
			}
		});
		sijisubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chaufaddress =chauffeurFromAddress.getFromAddress();
				bundle.putString("cartype", cartype);
				bundle.putString("chaufaddress", chaufaddress);
				System.out.println( cartype+chaufaddress);
				ChauffeurInfo.launch(ChauffeurSearch.this, bundle);
			}
		});
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
		
		String items[] = {"A1","A2","A3","B1","B2","C1","C2","C3","C4","«Î—°‘Òº›’’¿‡–Õ"};
		for(int i=0;i<10;i++){
			list.add(items[i]);
			
		}
	}
}
	
	
