/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   TutorialDialog.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-8-14
 */
package info.ericyue.es.util;

 
import info.ericyue.es.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class TutorialDialog extends Dialog {
	public TutorialDialog(Context context) {
		super(context);
		initialize(context);
	}
	public TutorialDialog(Context context, int theme) {
		super(context, theme);
		initialize(context);
	}
	public TutorialDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		initialize(context);
	}
	private final void initialize(final Context context) {
		setContentView(R.layout.tutorial);
		setTitle("v0.96�汾���ݣ�");
		Button mCloseButton = (Button)findViewById(R.id.closeTutorial);
		if (mCloseButton != null) {
			mCloseButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}
}
