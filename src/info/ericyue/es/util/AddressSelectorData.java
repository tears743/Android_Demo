package info.ericyue.es.util;

import info.ericyue.es.R;
import info.ericyue.es.activity.CarInfoSearch;
import info.ericyue.es.activity.SetCargoRemind;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressSelectorData extends SetCargoRemind{
	private static final String DATABASE_PATH ="/data/data/info.ericyue.es/database";
	private static final String DATABASE_NAME = "china.db3";
	private static String outFileName = DATABASE_PATH + "/" + DATABASE_NAME;
	
	
	
	public AddressSelectorData(InputStream myInput) throws Exception{
		
		
		buildDatabase(myInput);
		
	}
	
	private void buildDatabase(InputStream mInput) throws Exception{
		InputStream myInput= mInput;
		  File file = new File(outFileName);  
		  File dir = new File(DATABASE_PATH);
		  if (!dir.exists()) {
		   if (!dir.mkdir()) {
			   
		    throw new Exception("ÎÞsd");
		   }
		  }
	  
	  if (!file.exists()) {   
	   try {
	    OutputStream myOutput = new FileOutputStream(outFileName);
	    
	    byte[] buffer = new byte[1024];
	       int length;
	       while ((length = myInput.read(buffer))>0){
	        myOutput.write(buffer, 0, length);
	       }
	       myOutput.close();
	       myInput.close();
	   } catch (Exception e) {
	    e.printStackTrace();
	   }
	  
	  }
	}

}
