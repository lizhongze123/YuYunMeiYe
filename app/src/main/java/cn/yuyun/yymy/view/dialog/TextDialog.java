package cn.yuyun.yymy.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.yuyun.yymy.R;

public class TextDialog {
	private Dialog mDialog;
	private TextView mTextView;
	private int showTime = 4000;
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
		try {
			if ((this.mDialog != null) && this.mDialog.isShowing()) {
				mDialog.cancel();
			}
		} catch (final IllegalArgumentException e) {
			// Handle or log or ignore
		} catch (final Exception e) {
			// Handle or log or ignore
		} finally {
//			this.mDialog = null;
		}
	}

	public void setShowTime(int time){
		this.showTime = time;
	}

	public void isNeedCancel(boolean isNeedCancel){
		this.isNeedCancel = isNeedCancel;
	}
}

