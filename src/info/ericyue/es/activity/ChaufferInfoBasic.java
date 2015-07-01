package info.ericyue.es.activity;

import info.ericyue.es.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class ChaufferInfoBasic extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public Bundle bundle;
	
	public ListView cargoAbs ;
	private Button backHome;
	public String cartype,chauffaddress,chauffItems[];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bundle = new Bundle();
	}
	
	protected void setView(){
		backHome = (Button)findViewById(R.id.btn_h_zhuye);
		cargoAbs = (ListView)findViewById(R.id.list_condi_huo);
	}
	public void fillListItem(){}
	public void queryCargoInfoItem(){}
	

}
