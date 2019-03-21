package cn.yuyun.yymy.ui.home.attendance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lzz.utils.ResoureUtils;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.map.LocationBean;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;

/**
 * @author lzz
 * @desc
 * @date
 */

public class SearchLocationActivity2 extends BaseNoTitleActivity implements Inputtips.InputtipsListener, AdapterView.OnItemClickListener {


    @BindView(R.id.keyWord)
    EditText etInput;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.inputtip_list)
    ListView mInputListView;

    private List<Tip> mCurrentTipList;
    private InputTipsAdapter mIntipAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location2);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
    }

    private void initViews() {
        city = sendCityInfo();
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etInput.getText().toString())) {
                        if (!IsEmptyOrNullString(etInput.getText().toString())) {
                            InputtipsQuery inputquery = new InputtipsQuery(etInput.getText().toString(), city);
                            Inputtips inputTips = new Inputtips(mContext.getApplicationContext(), inputquery);
                            inputTips.setInputtipsListener(SearchLocationActivity2.this);
                            inputTips.requestInputtipsAsyn();
                        } else {
                            if (mIntipAdapter != null && mCurrentTipList != null) {
                                mCurrentTipList.clear();
                                mIntipAdapter.notifyDataSetChanged();
                            }
                        }
                    }else{
                        showToast("请输入关键字");
                    }

                }
                return false;
            }
        });

        mInputListView.setOnItemClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    /**
     * 输入提示回调
     *
     * @param tipList
     * @param rCode
     */
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            // 正确返回
            List<Tip> listTip = new ArrayList<>();
            for (int i = 0; i < tipList.size(); i++) {
                if(null != tipList.get(i).getPoint()){
                    listTip.add(tipList.get(i));
                }
            }
            mCurrentTipList = listTip;
            mIntipAdapter = new InputTipsAdapter(
                    getApplicationContext(),
                    mCurrentTipList);
            mInputListView.setAdapter(mIntipAdapter);
            mIntipAdapter.notifyDataSetChanged();
        } else {
            showToast(rCode + "");
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mCurrentTipList != null) {
            Tip tip = (Tip) adapterView.getItemAtPosition(i);
            if (null != tip) {
                LocationBean locationBean = new LocationBean(tip.getPoint().getLongitude(), tip.getPoint().getLatitude(), tip.getName(), tip.getAddress());
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DATA, locationBean);
                setResult(RESULT_OK, intent);
                this.finish();
            } else {
                showToast("请重新选择");
            }
        }
    }

    private String city;

    public String sendCityInfo() {
        //将前面定位数据中的city数据传过来
        //前面定位所在城市信息
        String info;
        Intent intent = this.getIntent();
        info = intent.getStringExtra("city");
        return info;
    }

    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    private TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
            return keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN;
        }
    };
}
