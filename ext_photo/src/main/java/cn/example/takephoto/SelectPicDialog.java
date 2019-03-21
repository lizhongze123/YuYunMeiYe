package cn.example.takephoto;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;


public class SelectPicDialog extends Dialog implements View.OnClickListener {

	private Activity context;
	public static int CAMERA = 2;
	public static int ALBUM = 1;
	public static int CANCEL = 0;



	public SelectPicDialog(Activity context) {
		super(context);
		setContentView(R.layout.selectpic_dialog);
		this.context = context;
		init();
		initView();
	}

	public SelectPicDialog(Activity context, boolean cancelable,
						   OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		setContentView(R.layout.selectpic_dialog);
		this.context = context;
		init();
		initView();
	}

	public SelectPicDialog(Activity context, int theme) {
		super(context, theme);
		setContentView(R.layout.selectpic_dialog);
		this.context = context;
		init();
		initView();
	}

	private void initView() {
		TextView camera_tv = (TextView) findViewById(R.id.camera_tv);
		TextView album_tv = (TextView) findViewById(R.id.album_tv);
		TextView cancel_tv = (TextView) findViewById(R.id.cancel_tv);
		camera_tv.setOnClickListener(this);
		album_tv.setOnClickListener(this);
		cancel_tv.setOnClickListener(this);
	}

	private void init() {
		// 设置位置
		WindowManager manager = this.getWindow().getWindowManager();
		int width = manager.getDefaultDisplay().getWidth();
		Window window = this.getWindow();
		LayoutParams params = window.getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.width = width;
		// 弹出动画
		window.setWindowAnimations(R.style.select_anim_dialog);
		// 设置取消方式
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.camera_tv) {
			listener.oClick(CAMERA);

		} else if (i == R.id.album_tv) {
			listener.oClick(ALBUM);

		} else if (i == R.id.cancel_tv) {
			listener.oClick(CANCEL);

		}
	}

	public SelectImgListener listener;

	public interface SelectImgListener {
		void oClick(int item);
	}

	/**
	 * dialog点击监听 item 2 相机 1相册 0取消
	 */
	public void setSelectImgListener(SelectImgListener listener) {
		this.listener = listener;
	}

}
