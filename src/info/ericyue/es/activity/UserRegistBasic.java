package info.ericyue.es.activity;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.ericyue.es.R;
import info.ericyue.es.util.AddressSelectorData;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class UserRegistBasic extends Activity {

	
	AddressSelectorData asData;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		asData = 
		InputStream myInput = getResources().openRawResource(R.raw.china);
		try {
			Bundle bundle = new Bundle();
			asData = new AddressSelectorData(myInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
