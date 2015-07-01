/**
 * 
 */
package info.ericyue.es.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.ericyue.es.R;
import info.ericyue.es.myView.SelectAddressView;
import info.ericyue.es.util.HttpUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author tears
 *
 */
public class CarOwnerRegist extends Activity {

	public static void launch (Context c , Bundle bundle){
		Intent intent = new Intent(c, CarOwnerRegist.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
		}
	
	private EditText userName,passWord1,passWord2,
	registerName,registerNum,registerEmail,
	registerCompanyName;
	private String registName,passWord,trueName,cellphone,email,companyName,address,role;
	private Button submit;
	
	
	info.ericyue.es.myView.SelectAddressView carOwnerRegistAddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.carownerregist);


		userName = (EditText)findViewById(R.id.ousername);
		passWord1 =(EditText)findViewById(R.id.opassword1);
		passWord2 =(EditText)findViewById(R.id.opassword2);
		registerName =(EditText)findViewById(R.id.oregistername);
		registerNum =(EditText)findViewById(R.id.oregisternum);
		registerEmail =(EditText)findViewById(R.id.oregisteremail);
		registerCompanyName =(EditText)findViewById(R.id.oregistercompanyname);
		carOwnerRegistAddress = (SelectAddressView)findViewById(R.id.cargoregistaddress);

		submit = (Button)findViewById(R.id.osubmit);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				CheckInput();
			}
		});

	}
	
	public void CheckInput(){
		if(!CheckUserName(userName.getText().toString())){
			Toast.makeText(CarOwnerRegist.this, "用户名只能由4-18个字母或数字组成（不区分大小写），不能用中文，注册成功后不可修改",Toast.LENGTH_LONG).show();
		}
			else if(!CheckPassword(passWord1.getText().toString())){
			Toast.makeText(CarOwnerRegist.this, "密码只能由6-16个字母或数字组成（区分大小写），不能用中文",Toast.LENGTH_LONG).show();
		}
				else if(!passWord1.getText().toString().equals(passWord2.getText().toString())){
				Toast.makeText(CarOwnerRegist.this, "两次密码不一致！",Toast.LENGTH_LONG).show();
			}
					else{
						String state="";
						try{
							GetRegistInfo();
						
							state=HttpUtil.insertRegisterInfo(registName, trueName, passWord, companyName, role, cellphone, address, email);
						}
						catch(Exception e){
							
							System.out.println(e);
							
						}
							if(state.contains("0"))
							{
								Toast.makeText(CarOwnerRegist.this, "用户名有重复，请更改。",Toast.LENGTH_LONG).show();
							}
							else
							{	
								Toast.makeText(CarOwnerRegist.this, "注册成功"+state,Toast.LENGTH_LONG).show();
								Bundle bundle = new Bundle();
								LoginActivity.launch(CarOwnerRegist.this, bundle);
								CarOwnerRegist.this.finish();
							}
					}
	}
	

	private void GetRegistInfo() {
		registName = userName.getText().toString();
		passWord= passWord1.getText().toString();
		trueName= registerName.getText().toString();
		cellphone=registerNum.getText().toString();
		email=registerEmail.getText().toString();
		companyName=registerCompanyName.getText().toString();
		address=carOwnerRegistAddress.getFromAddress();
//		address = URLEncoder.encode(URLEncoder.encode(address,"UTF-8"),"UTF-8");
		role="2";
	}
	private boolean CheckPassword(String str){
		
		if(str==null){
			return false;
		}
		if(str.length()<6||str.length()>16){
			return false;
		}
		String line = str;
		  Pattern pattern = Pattern.compile("[\\u4E00-\\u9FFF]+$");
		  Matcher matcher = pattern.matcher(line);
		  while (matcher.find()) {
		   System.out.println(matcher.group()); // 结果
		   return false;
		  }
		
		return true;
	}
private boolean CheckUserName(String str){
		
		if(str==null){
			return false;
		}
		if(str.length()<4||str.length()>18){
			return false;
		}
		String line = str;
		  Pattern pattern = Pattern.compile(".*[a-zA-Z].*[0-9]|.*[0-9]|.*[a-zA-Z]");
		  Matcher matcher = pattern.matcher(line);
		  int count=0;
		  Boolean b= matcher.find();
		  System.out.println(b);
		  return b;
		 
		 
	}


}
