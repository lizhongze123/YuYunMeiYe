package cn.yuyun.yymy.ui.home.bill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import java.io.File;

import cn.lzz.utils.ToastUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.app.MyApplication;
import cn.yuyun.yymy.event.NotifyEvent;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.request.RequestCertificate;
import cn.yuyun.yymy.http.result.ResultBillManage;
import cn.yuyun.yymy.utils.EvenManager;
import cn.yuyun.yymy.utils.GlideHelper;
import cn.yuyun.yymy.view.DrawBoard;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;
import static cn.yuyun.yymy.constan.Constans.EXTRA_TYPE;

/**
 * @author
 * @desc 签名
 * @date
 */
public class SignPaintActivity extends AppCompatActivity {

    private DrawBoard drawBoard;
    public static int TYPE_NORMAL = 0;
    public static int TYPE_CASHIER = 1;
    private int type;
    private String recordId;
    private int recordType;

    public static Intent startSignPaintActivity(Context context) {
        return new Intent(context, SignPaintActivity.class).putExtra(EXTRA_TYPE, TYPE_NORMAL);
    }

    public static Intent startSignPaintActivityForCashier(Context context, String recordId, int recordType) {
        return new Intent(context, SignPaintActivity.class).putExtra(EXTRA_TYPE, TYPE_CASHIER).putExtra(EXTRA_DATA, recordId).putExtra(EXTRA_DATA2, recordType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_paint);
        initView();
    }

    private void initView() {
        type = getIntent().getIntExtra(EXTRA_TYPE, 0);
        if(type == TYPE_CASHIER){
            recordId = getIntent().getStringExtra(EXTRA_DATA);
            recordType = getIntent().getIntExtra(EXTRA_DATA2, 2);
        }
        drawBoard = (DrawBoard) findViewById(R.id.drawBoard);
        findViewById(R.id.iv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == TYPE_CASHIER){
                    EvenManager.sendEvent(new NotifyEvent(NotifyEvent.FINISH_CASHIER));
                    startActivity(new Intent(SignPaintActivity.this, BillManageActivity.class));
                    finish();
                }else{
                    finish();
                }
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawBoard.goBack();
            }
        });
        findViewById(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawBoard.clear();
            }
        });
        findViewById(R.id.tv_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path =  Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + ".jpg";
                drawBoard.save(path);
//                finish();
                upload(path);
            }
        });
    }

    private void upload(final String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            AppModelFactory.model().uploadPic(file, 5, new ProgressSubscriber<DataBean<String>>(this, false) {
                @Override
                public void onNext(DataBean<String> o) {
                    if(null != o.data){
                        if(type == TYPE_CASHIER){
                            submit(o.data);
                        }else{
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_DATA, o.data);
                            setResult(RESULT_OK, intent);
                            ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "上传成功");
                            finish();
                        }
                    }
                }

                @Override
                public void onError(ApiException ex) {
                    super.onError(ex);
                    ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
                }
            });
        }

    }

    private void submit(String path) {
        RequestCertificate body = new RequestCertificate();
        body.autograph = path;
        body.record_id = recordId;
        body.record_type = recordType;
        AppModelFactory.model().postCertificate(body, new ProgressSubscriber<Object>(SignPaintActivity.this, false) {

            @Override
            public void onNext(Object result) {
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), "提交成功");
                EvenManager.sendEvent(new NotifyEvent(NotifyEvent.FINISH_CASHIER));
                if (recordType == 1) {
                    startActivity(BillManageStorevalueDetailActivity.startBillManageDetailActivity(SignPaintActivity.this, recordId));
                } else {
                    startActivity(BillManageDetailActivity.startBillManageDetailActivity(SignPaintActivity.this, recordId));
                }
                finish();

            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                ToastUtils.toastInBottom(MyApplication.getInstance().getApplicationContext(), ex.message);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

}
