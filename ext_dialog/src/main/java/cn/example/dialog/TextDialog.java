package cn.example.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class TextDialog {
	private Dialog mDialog;
	private TextView mTextView;
	private int showTime = 5000;
	private boolean isNeedCancel = true;

	private Runnable cancelRunnable = new Runnable() {

		@Override
		public void run() {
			cancel();
		}
	};

	public TextDialog(Context context) {
		mDialog = new Dialog(context, R.style.loading_dialog);
		//下面两句话等价于设置style
//		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_text, null);
		mTextView = (TextView) view.findViewById(R.id.textview);
		mDialog.setCancelable(true);
		mDialog.setContentView(view);
	}

	public void setText(String text) {
		mTextView.setText(text);
	}

	public void setText(int textID) {
		mTextView.setText(textID);
	}

	public void show() {
		mDialog.show();
		if(isNeedCancel){
			mTextView.postDelayed(cancelRunnable, showTime);
		}
	}

	public void cancel() {
		if(mDialog.isShowing()){
			mDialog.cancel();
		}
	}

	public void setShowTime(int time){
		this.showTime = time;
	}

	public void isNeedCancel(boolean isNeedCancel){
		this.isNeedCancel = isNeedCancel;
	}
}

