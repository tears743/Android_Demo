package info.ericyue.es.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class CargoInfoBaseActivity extends Activity {

	Button backHome;
	ListView cargoAbs ;
	String cargo_sd,cargo_dd,cargo_relation_company,cargo_name,cargo_weight,cargo_releasedate;
	String[] cargoItems;
	Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bundle = new Bundle();
	}
	public void queryCargoInfoItem(){}
	public void fillListItem(){}
	
	protected void setClickListener(){
	
	}
	
	

}
