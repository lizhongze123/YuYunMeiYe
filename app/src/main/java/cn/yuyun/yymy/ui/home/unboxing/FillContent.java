package cn.yuyun.yymy.ui.home.unboxing;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yuyun.yymy.R;
import cn.yuyun.yymy.http.result.ResultUnboxingBean;
import cn.yuyun.yymy.ui.other.ViewBigImageActivity;
import cn.yuyun.yymy.utils.GlideHelper;

/**
 * @author lzz
 * @desc lzz
 * @date 2018/4/17
 */
public class FillContent {

    /**
     * 填充图片列表,包括原创和转发中的图片都可以使用
     */
    public static void fillWeiBoImgList(ResultUnboxingBean bean, final Context context, RecyclerView recyclerview, boolean isClick, boolean isSave) {
        for (int i = 0; i < bean.shareOrderVo.pictures.size(); i++) {
            String url = bean.shareOrderVo.pictures.get(i);
            if(!url.startsWith(context.getString(R.string.HTTP))){
                bean.shareOrderVo.pictures.set(i, context.getString(R.string.image_url_prefix) + url);
            }
        }
        if (bean.shareOrderVo.pictures == null || bean.shareOrderVo.pictures.size() == 0) {
            recyclerview.setVisibility(View.GONE);
            return;
        }
        if (recyclerview.getVisibility() == View.GONE) {
            recyclerview.setVisibility(View.VISIBLE);
        }
        GridLayoutManager gridLayoutManager = initGridLayoutManager(bean.shareOrderVo.pictures, context);
        ImageAdapter imageAdapter = new ImageAdapter(context, bean.shareOrderVo.pictures, isClick);
        imageAdapter.setSave(isSave);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(imageAdapter);
    }

    public static void fillWeiBoImgList(List<String> imageDatas, final Context context, RecyclerView recyclerview, boolean isClick) {
        if (imageDatas == null || imageDatas.size() == 0) {
            recyclerview.setVisibility(View.GONE);
            return;
        }
        if (recyclerview.getVisibility() == View.GONE) {
            recyclerview.setVisibility(View.VISIBLE);
        }
        GridLayoutManager gridLayoutManager = initGridLayoutManager(imageDatas, context);
        ImageAdapter imageAdapter = new ImageAdapter(context, imageDatas, isClick);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(imageAdapter);
    }

    public static void fillWeiBoImgList(List<String> imageDatas, final Context context, RecyclerView recyclerview, boolean isClick, boolean isSave) {
        if (imageDatas == null || imageDatas.size() == 0) {
            recyclerview.setVisibility(View.GONE);
            return;
        }
        if (recyclerview.getVisibility() == View.GONE) {
            recyclerview.setVisibility(View.VISIBLE);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        ImageAdapter imageAdapter = new ImageAdapter(context, imageDatas, isClick);
        imageAdapter.setSave(isSave);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(imageAdapter);
    }

    public static void fillWorkImgList(List<String> imageDatas, final Context context, RecyclerView recyclerview, boolean isClick, boolean isSave) {
        if (imageDatas == null || imageDatas.size() == 0) {
            recyclerview.setVisibility(View.GONE);
            return;
        }
        if (recyclerview.getVisibility() == View.GONE) {
            recyclerview.setVisibility(View.VISIBLE);
        }
        GridLayoutManager gridLayoutManager = initGridLayoutManager(imageDatas, context);
        ImageNineSizeAdapter imageAdapter = new ImageNineSizeAdapter(context, imageDatas, isClick);
        imageAdapter.setSave(isSave);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(imageAdapter);
    }

    public static void fillNineImgList(List<String> imageDatas, final Context context, RecyclerView recyclerview, boolean isClick, boolean isSave) {
        if (imageDatas == null || imageDatas.size() == 0) {
            recyclerview.setVisibility(View.GONE);
            return;
        }
        if (recyclerview.getVisibility() == View.GONE) {
            recyclerview.setVisibility(View.VISIBLE);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        ImageNineAdapter imageAdapter = new ImageNineAdapter(context, imageDatas, isClick);
        imageAdapter.setSave(isSave);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(imageAdapter);
    }

    public static void fillComunicationImgList(List<String> imageDatas, final Context context, RecyclerView recyclerview, boolean isClick, boolean isSave) {
        if (imageDatas == null || imageDatas.size() == 0) {
            recyclerview.setVisibility(View.GONE);
            return;
        }
        if (recyclerview.getVisibility() == View.GONE) {
            recyclerview.setVisibility(View.VISIBLE);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        ImageCommunicationAdapter imageAdapter = new ImageCommunicationAdapter(context, imageDatas, isClick);
        imageAdapter.setSave(isSave);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(imageAdapter);
    }

    /**
     * 根据图片数量，初始化GridLayoutManager，并且设置列数，
     * 当图片 = 1 的时候，显示1列
     * 当图片<=4张的时候，显示2列
     * 当图片>4 张的时候，显示3列
     *
     * @return
     */
    private static GridLayoutManager initGridLayoutManager(List<String> imageDatas, Context context) {
        GridLayoutManager gridLayoutManager;
        if (imageDatas != null) {
            switch (imageDatas.size()) {
                case 1:
                    gridLayoutManager = new GridLayoutManager(context, 1);
                    break;
                case 2:
                    gridLayoutManager = new GridLayoutManager(context, 2);
                    break;
                case 3:
                    gridLayoutManager = new GridLayoutManager(context, 3);
                    break;
                case 4:
                    gridLayoutManager = new GridLayoutManager(context, 2);
                    break;
                default:
                    gridLayoutManager = new GridLayoutManager(context, 3);
                    break;
            }
        } else {
            gridLayoutManager = new GridLayoutManager(context, 3);
        }
        return gridLayoutManager;
    }

    /**
     * 填充列表图片
     *
     * @param context
     * @param position
     * @param norImg
     * @param imageLabel
     */
    public static void fillImageList(final Context context, final List<String>urllist, final int position, final ImageView norImg, final ImageView imageLabel, boolean isClick, final boolean isSave) {
        norImg.setVisibility(View.VISIBLE);
        displayNorImg(context, urllist.get(position), norImg, imageLabel);
        if(isClick){
            norImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList list = new ArrayList();
                    list.addAll(urllist);
                    Bundle bundle = new Bundle();
                    bundle.putInt("code", position);
                    bundle.putBoolean("isSave", isSave);
                    bundle.putStringArrayList("imageList", list);
                    context.startActivity(ViewBigImageActivity.startViewBigImageActivity(context, bundle));
                }
            });
        }
    }

    public static void displayNorImg(Context context, String url, ImageView norImg, ImageView imageLable) {
        imageLable.setVisibility(View.GONE);
        norImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideHelper.displayImage(context,  url, norImg, R.color.loadding_img_bg);
    }
}
