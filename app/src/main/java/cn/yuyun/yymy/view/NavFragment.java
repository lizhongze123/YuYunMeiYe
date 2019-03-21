package cn.yuyun.yymy.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.main.PubActivity;
import cn.yuyun.yymy.ui.chat.ChatFragment;
import cn.yuyun.yymy.ui.help.DiscoveryFragment;
import cn.yuyun.yymy.ui.home.HomeFragment;
import cn.yuyun.yymy.ui.me.MeFragment;
import cn.yuyun.yymy.ui.store.StoreFragment;
import cn.yuyun.yymy.utils.BlurBuilder;

public class NavFragment extends Fragment implements View.OnClickListener ,View.OnLongClickListener {

    NavigationButton mNavDiscovery;
    NavigationButton mNavMoment;
    NavigationButton mNavFriend;
    NavigationButton mNavMe;
    NavigationButton mNavChat;
    ImageView mNavPub;

    private Context mContext;
    private int mContainerId;
    private FragmentManager mFragmentManager;
    private NavigationButton mCurrentNavButton;
    private OnNavigationReselectListener mOnNavigationReselectListener;

    public NavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nav, container, false);

        /*ShapeDrawable lineDrawable = new ShapeDrawable();
        lineDrawable.getPaint().setColor(getResources().getColor(R.color.white));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                new ColorDrawable(getResources().getColor(R.color.white)),
                lineDrawable
        });
        rootView.setBackgroundDrawable(layerDrawable);*/

        mNavDiscovery = (NavigationButton) rootView.findViewById(R.id.nav_item_news);
        mNavMoment = (NavigationButton) rootView.findViewById(R.id.nav_item_tweet);
        mNavFriend = (NavigationButton) rootView.findViewById(R.id.nav_item_explore);
        mNavMe = (NavigationButton) rootView.findViewById(R.id.nav_item_me);
        mNavChat = (NavigationButton) rootView.findViewById(R.id.nav_item_chat);
        mNavPub = (ImageView) rootView.findViewById(R.id.nav_item_tweet_pub);

        mNavDiscovery.setOnClickListener(this);
        mNavMoment.setOnClickListener(this);
        mNavFriend.setOnClickListener(this);
        mNavMe.setOnClickListener(this);
        mNavChat.setOnClickListener(this);
        mNavPub.setOnClickListener(this);

        mNavDiscovery.init(R.drawable.index_home_selector,
                "首页",
                HomeFragment.class);

        mNavMoment.init(R.drawable.index_store_selector,
                "门店",
                StoreFragment.class);

        mNavFriend.init(R.drawable.index_help_selector,
                "发现",
                DiscoveryFragment.class);

        mNavMe.init(R.drawable.index_me_selector,
                "我的",
                MeFragment.class);

        mNavChat.init(R.drawable.index_me_selector,
                "消息",
                ChatFragment.class);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof NavigationButton) {
            NavigationButton nav = (NavigationButton) v;
            mNavPub.setSelected(false);
            doSelect(nav);
        } else if (v.getId() == R.id.nav_item_tweet_pub) {
//            PopupMenuUtil.getInstance()._show(mContext, v);
            BlurBuilder.snapShotWithoutStatusBar(getActivity());
            PubActivity.show(getContext());
        }
    }

    public void setup(Context context, FragmentManager fragmentManager, int contentId, OnNavigationReselectListener listener) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mContainerId = contentId;
        mOnNavigationReselectListener = listener;

        // do clear
        clearOldFragment();
        // do select first
        doSelect(mNavDiscovery);
//        doSelect(mNavFriend);
    }

    public void select() {
        if (mNavFriend != null){
            doSelect(mNavFriend);
        }
    }

    @SuppressWarnings("RestrictedApi")
    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0){
            return;
        }
        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            if (fragment != this && fragment != null) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit){
            transaction.commitNow();
        }
    }

    private void doSelect(NavigationButton newNavButton) {
        // If the new navigation is me info fragment, we intercept it
        /*
        if (newNavButton == mNavMe) {
            if (interceptMessageSkip())
                return;
        }
        */

        NavigationButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {
                onReselect(oldNavButton);
                return;
            }
            oldNavButton.setSelected2(false);
        }
        if(newNavButton != null){
            newNavButton.setSelected2(true);
            doTabChanged(oldNavButton, newNavButton);
            mCurrentNavButton = newNavButton;
        }

    }

    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(mContext,
                        newNavButton.getClx().getName(), null);
                ft.add(mContainerId, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commit();
    }

    private void onReselect(NavigationButton navigationButton) {
        OnNavigationReselectListener listener = mOnNavigationReselectListener;
        if (listener != null) {
            listener.onReselect(navigationButton);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public interface OnNavigationReselectListener {
        void onReselect(NavigationButton navigationButton);
    }


}
