package info.ericyue.es.util;

import info.ericyue.es.activity.CargoRelease;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MySubmitHandler extends Handler {
	Context context;
	ProgressDialog pb;
	
	
	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}


	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}


	/**
	 * @return the pb
	 */
	public ProgressDialog getPb() {
		return pb;
	}


	/**
	 * @param pb the pb to set
	 */
	public void setPb(ProgressDialog pb) {
		this.pb = pb;
	}


	@Override
	public void handleMessage(Message msg) {
		
		
		
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		switch (msg.what){
		case 1:
			pb.setTitle("�����ύ���Ժ�...");
			pb.setMessage("���Եȣ��ύ���Զ���ת");
			pb.show();
			Log.i("pbshow","show");
			break;
		case 2:
			pb.dismiss();
			Toast.makeText(context, "�����ɹ���",Toast.LENGTH_SHORT).show();
//			context.finish();
			Log.i("pbshow","dismiss");
			break;
		case 3:
			pb.dismiss();
			Toast.makeText(context, "�������ɹ����������������󷢲���",Toast.LENGTH_SHORT).show();
			break;
		}	
	}
}
	


