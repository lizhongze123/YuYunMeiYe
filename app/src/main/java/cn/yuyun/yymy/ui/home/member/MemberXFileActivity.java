package cn.yuyun.yymy.ui.home.member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.http.ApiException;
import com.example.http.DataBean;
import com.example.http.ProgressSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.yuyun.yymy.R;
import cn.yuyun.yymy.base.BaseActivity;
import cn.yuyun.yymy.base.BaseNoTitleActivity;
import cn.yuyun.yymy.http.AppModelFactory;
import cn.yuyun.yymy.http.result.ResultMemberBean;
import cn.yuyun.yymy.http.result.SecretBean;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;

import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA;
import static cn.yuyun.yymy.constan.Constans.EXTRA_DATA2;

/**
 * @author
 * @desc
 * @date
 */

public class MemberXFileActivity extends BaseNoTitleActivity {

    @BindView(R.id.iv_salon)
    ImageView ivSalon;
    @BindView(R.id.iv_live)
    ImageView ivLive;
    @BindView(R.id.iv_emotion)
    ImageView ivEmotion;
    @BindView(R.id.iv_family)
    ImageView ivFamily;

    EditText etOneOne;
    EditText etOneTwo;
    EditText etOneThree;
    EditText etOneFour;
    EditText etOneFive;
    EditText etTwoOne;
    EditText etTwoTwo;
    EditText etTwoThree;
    EditText etTwoFour;
    EditText etTwoFive;
    EditText etTwoSix;
    EditText etTwoSeven;
    EditText etTwoEight;
    EditText etTwoNine;
    EditText etTwoTen;
    EditText etTwoEleven;
    EditText etTwoTwelve;
    EditText etThreeOne;
    EditText etThreeTwo;
    EditText etThreeThree;
    EditText etThreeFour;
    EditText etThreeFive;
    EditText etThreeSix;
    EditText etThreeSeven;
    EditText etThreeEight;
    EditText etThreeNine;
    EditText etFourOne;
    EditText etFourTwo;
    EditText etFourThree;
    EditText etFourFour;
    EditText etFourFive;
    EditText etFourSix;
    EditText etFourSeven;
    EditText etFourEight;
    EditText etFourNine;

    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private EditText[] etAry;
    private ResultMemberBean memberBean;
    private StoreBean storeBean;
    private AnimationSet animationSet = new AnimationSet(true);
    private AnimationSet animationSet2 = new AnimationSet(true);

    @BindView(R.id.viewPager)
    ViewPager mTabPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_xfile);
    }

    @Override
    protected void setUpViewAndData() {
        super.setUpViewAndData();
        initViews();
        initDatas();
    }

    private void initDatas() {
        getMemberSecret();
    }

    public static Intent startMemberXFileActivity(Context context, ResultMemberBean bean, StoreBean storeBean) {
        return new Intent(context, MemberXFileActivity.class).putExtra(EXTRA_DATA, bean).putExtra(EXTRA_DATA2, storeBean);
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivEdit.setVisibility(View.GONE);
                tvSave.setVisibility(View.VISIBLE);
                setHint("请输入内容", etAry);
                setEnable(true, etAry);
            }
        });
        memberBean = (ResultMemberBean) getIntent().getSerializableExtra(EXTRA_DATA);
        storeBean = (StoreBean) getIntent().getSerializableExtra(EXTRA_DATA2);

        LayoutInflater inflater = getLayoutInflater();
        final List<View> views = new ArrayList<>();
        View view1 = inflater.inflate(R.layout.item_member_xfile_one, null);
        View view2 = inflater.inflate(R.layout.item_member_xfile_two, null);
        View view3 = inflater.inflate(R.layout.item_member_xfile_three, null);
        View view4 = inflater.inflate(R.layout.item_member_xfile_four, null);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        etOneOne = view1.findViewById(R.id.et_oneOne);
        etOneTwo = view1.findViewById(R.id.et_oneTwo);
        etOneThree = view1.findViewById(R.id.et_oneThree);
        etOneFour = view1.findViewById(R.id.et_oneFour);
        etOneFive = view1.findViewById(R.id.et_oneFive);
        etTwoOne = view2.findViewById(R.id.et_twoOne);
        etTwoTwo = view2.findViewById(R.id.et_twoTwo);
        etTwoThree = view2.findViewById(R.id.et_twoThree);
        etTwoFour = view2.findViewById(R.id.et_twoFour);
        etTwoFive = view2.findViewById(R.id.et_twoFive);
        etTwoSix = view2.findViewById(R.id.et_twoSix);
        etTwoSeven = view2.findViewById(R.id.et_twoSeven);
        etTwoEight = view2.findViewById(R.id.et_twoEight);
        etTwoNine = view2.findViewById(R.id.et_twoNine);
        etTwoTen = view2.findViewById(R.id.et_twoTen);
        etTwoEleven = view2.findViewById(R.id.et_twoEleven);
        etTwoTwelve= view2.findViewById(R.id.et_twoTwelve);
        etThreeOne = view3.findViewById(R.id.et_threeOne);
        etThreeTwo = view3.findViewById(R.id.et_threeTwo);
        etThreeThree = view3.findViewById(R.id.et_threeThree);
        etThreeFour = view3.findViewById(R.id.et_threeFour);
        etThreeFive = view3.findViewById(R.id.et_threeFive);
        etThreeSix = view3.findViewById(R.id.et_threeSix);
        etThreeSeven = view3.findViewById(R.id.et_threeSeven);
        etThreeEight = view3.findViewById(R.id.et_threeEight);
        etThreeNine = view3.findViewById(R.id.et_threeNine);
        etFourOne = view4.findViewById(R.id.et_fourOne);
        etFourTwo = view4.findViewById(R.id.et_fourTwo);
        etFourThree = view4.findViewById(R.id.et_fourThree);
        etFourFour = view4.findViewById(R.id.et_fourFour);
        etFourFive = view4.findViewById(R.id.et_fourFive);
        etFourSix = view4.findViewById(R.id.et_fourSix);
        etFourSeven = view4.findViewById(R.id.et_fourSeven);
        etFourEight = view4.findViewById(R.id.et_fourEight);
        etFourNine = view4.findViewById(R.id.et_fourNine);

        etAry = new EditText[]{etOneOne, etOneTwo, etOneThree, etOneFour, etOneFive, etTwoOne, etTwoTwo, etTwoThree, etTwoFour, etTwoFive, etTwoSix, etTwoSeven, etTwoEight,etTwoNine,etTwoTen,etTwoEleven,etTwoTwelve,etThreeOne, etThreeTwo,
                etThreeThree, etThreeFour, etThreeFive, etThreeSix, etThreeSeven, etThreeEight, etThreeNine, etFourOne, etFourTwo, etFourThree, etFourFour, etFourFive, etFourSix,
                etFourSeven, etFourEight, etFourNine};

        PagerAdapter mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager)container).removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager)container).addView(views.get(position));
                //return super.instantiateItem(container, position);
                return views.get(position);
            }
        };
        mTabPager.setAdapter(mPagerAdapter);
        mTabPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    ivSalon.startAnimation(animationSet);
                    ivLive.startAnimation(animationSet2);
                    ivEmotion.startAnimation(animationSet2);
                    ivFamily.startAnimation(animationSet2);
                }else if(position == 1){
                    ivSalon.startAnimation(animationSet2);
                    ivLive.startAnimation(animationSet);
                    ivEmotion.startAnimation(animationSet2);
                    ivFamily.startAnimation(animationSet2);
                }else if(position == 2){
                    ivSalon.startAnimation(animationSet2);
                    ivLive.startAnimation(animationSet2);
                    ivEmotion.startAnimation(animationSet);
                    ivFamily.startAnimation(animationSet2);
                }else if(position == 3){
                    ivSalon.startAnimation(animationSet2);
                    ivLive.startAnimation(animationSet2);
                    ivEmotion.startAnimation(animationSet2);
                    ivFamily.startAnimation(animationSet);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.15f, 1.0f, 1.15f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        //动画持续时间
        scaleAnimation.setDuration(300);
        //保存动画效果到。。
        animationSet.addAnimation(scaleAnimation);
        animationSet.setFillAfter(true);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(
                1.0f, 1.0f, 1.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        //动画持续时间
        scaleAnimation2.setDuration(300);
        //保存动画效果到。。
        animationSet2.addAnimation(scaleAnimation2);
        animationSet2.setFillAfter(true);

        ivSalon.startAnimation(animationSet);
    }

    private void addMemberSecret(SecretBean secretBean) {
        AppModelFactory.model().addMemberSecret(secretBean, new ProgressSubscriber<DataBean<Object>>(mContext) {
            @Override
            public void onNext(DataBean<Object> result) {
                showToast("保存成功");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    private void getMemberSecret() {
        AppModelFactory.model().getMemberSecret(storeBean.group_id, memberBean.memberId, new ProgressSubscriber<DataBean<SecretBean>>(mContext) {
            @Override
            public void onNext(DataBean<SecretBean> result) {
                if (null != result.data) {

                    etOneOne.setText(result.data.beauty_salon_ever);
                    etOneTwo.setText(result.data.project_ever);
                    etOneThree.setText(result.data.wanna_promote);
                    etOneFour.setText(result.data.body_cares);
                    etOneFive.setText(result.data.other_secret);

                    etTwoOne.setText(result.data.interest_topic);
                    etTwoTwo.setText(result.data.time_table);
                    etTwoThree.setText(result.data.favorite_drink);
                    etTwoFour.setText(result.data.shopping_mall);
                    etTwoFive.setText(result.data.children_school);
                    etTwoSix.setText(result.data.hospital);
                    etTwoSeven.setText(result.data.hotel);
                    etTwoEight.setText(result.data.travel);
                    etTwoNine.setText(result.data.anaphylaxis);
                    etTwoTen.setText(result.data.jewelry);
                    etTwoEleven.setText(result.data.body_cares);
                    etTwoTwelve.setText(result.data.daliy);

                    etThreeOne.setText(result.data.the_third_person);
                    etThreeTwo.setText(result.data.emotional_experience);
                    etThreeThree.setText(result.data.person_care_about);
                    etThreeFour.setText(result.data.sex);
                    etThreeFive.setText(result.data.boss_relationship);
                    etThreeSix.setText(result.data.friends);
                    etThreeSeven.setText(result.data.favorite_character);
                    etThreeEight.setText(result.data.spouse_relationship);
                    etThreeNine.setText(result.data.remembrance_day);

                    etFourOne.setText(result.data.job_revenue_herself);
                    etFourTwo.setText(result.data.job_revenue_lover);
                    etFourThree.setText(result.data.family_property);
                    etFourFour.setText(result.data.house_property);
                    etFourFive.setText(result.data.car_property);
                    etFourSix.setText(result.data.addr);
                    etFourSeven.setText(result.data.religious);
                    etFourEight.setText(result.data.favorite_movies_books);
                    etFourNine.setText(result.data.most_valueable_shopping);

                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNormalError(ApiException ex) {
                super.onNormalError(ex);
                showToast("请求失败，请稍后重试");
            }

            @Override
            public void onNoNetWorkError() {
                super.onNoNetWorkError();
                showToast(mContext.getString(R.string.error_no_network));
            }
        });
    }

    @OnClick({R.id.tv_save, R.id.iv_salon, R.id.iv_live, R.id.iv_emotion, R.id.iv_family})
    public void onClickItem(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                ivEdit.setVisibility(View.VISIBLE);
                tvSave.setVisibility(View.GONE);
                setHint("", etAry);
                setEnable(false, etAry);
                SecretBean secretBean = new SecretBean();
                secretBean.member_id = memberBean.memberId;

                secretBean.beauty_salon_ever = etOneOne.getText().toString();
                secretBean.project_ever = etOneTwo.getText().toString();
                secretBean.wanna_promote = etOneThree.getText().toString();
                secretBean.body_cares = etOneFour.getText().toString();
                secretBean.other_secret = etOneFive.getText().toString();

                secretBean.interest_topic = etTwoOne.getText().toString();
                secretBean.time_table = etTwoTwo.getText().toString();
                secretBean.favorite_drink = etTwoThree.getText().toString();
                secretBean.shopping_mall = etTwoFour.getText().toString();
                secretBean.children_school = etTwoFive.getText().toString();
                secretBean.hospital = etTwoSix.getText().toString();
                secretBean.hotel = etTwoSeven.getText().toString();
                secretBean.travel = etTwoEight.getText().toString();
                secretBean.anaphylaxis = etTwoNine.getText().toString();
                secretBean.jewelry = etTwoTen.getText().toString();
                secretBean.body_cares = etTwoEleven.getText().toString();
                secretBean.daliy = etTwoTwelve.getText().toString();

                secretBean.the_third_person = etThreeOne.getText().toString();
                secretBean.emotional_experience = etThreeTwo.getText().toString();
                secretBean.person_care_about = etThreeThree.getText().toString();
                secretBean.sex = etThreeFour.getText().toString();
                secretBean.boss_relationship = etThreeFive.getText().toString();
                secretBean.friends = etThreeSix.getText().toString();
                secretBean.favorite_character = etThreeSeven.getText().toString();
                secretBean.spouse_relationship = etThreeEight.getText().toString();
                secretBean.remembrance_day = etThreeNine.getText().toString();

                secretBean.job_revenue_herself = etFourOne.getText().toString();
                secretBean.job_revenue_lover = etFourTwo.getText().toString();
                secretBean.family_property = etFourThree.getText().toString();
                secretBean.house_property = etFourFour.getText().toString();
                secretBean.car_property = etFourFive.getText().toString();
                secretBean.addr = etFourSix.getText().toString();
                secretBean.religious = etFourSeven.getText().toString();
                secretBean.favorite_movies_books = etFourEight.getText().toString();
                secretBean.most_valueable_shopping = etFourNine.getText().toString();

                addMemberSecret(secretBean);
                break;
            case R.id.iv_salon:
               mTabPager.setCurrentItem(0);
                break;
            case R.id.iv_live:
                mTabPager.setCurrentItem(1);
                break;
            case R.id.iv_emotion:
                mTabPager.setCurrentItem(2);
                break;
            case R.id.iv_family:
                mTabPager.setCurrentItem(3);
                break;
            default:
        }
    }

    private void setHint(String hint, EditText... arg) {
        for (EditText et : arg) {
            et.setHint(hint);
        }
    }

    private void setEnable(boolean isEnable, EditText... arg) {
        for (EditText et : arg) {
            et.setEnabled(isEnable);
        }
    }



}
