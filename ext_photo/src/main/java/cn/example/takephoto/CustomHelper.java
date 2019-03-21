package cn.example.takephoto;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;


import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

/**
 * @author
 * @desc
 * @date
 */

public class CustomHelper {

    private Activity mContext;
    private SelectPicDialog dialog;

    private TakePhoto takePhoto;
    private final int cropSize = 70;
    private Uri imageUri;

    /**是否裁剪*/
    private boolean isCrop;
    /**
     * 最大宽度，默认为720
     */
    private int maxWidth = 1024;
    /**
     * 最大高度,默认为960
     */
    private int maxHeight = 1365;

    private int limit;

    public CustomHelper(Activity context) {
        mContext = context;
        initPitureDialog();
    }

    public CustomHelper setCrop(boolean isCrop) {
        this.isCrop = isCrop;
        return this;
    }

    public CustomHelper setLimit(int limit) {
        this.limit = limit;
        return this;
    }


    public void onClick(TakePhoto takePhoto) {
        this.takePhoto = takePhoto;

        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/YUYUN/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        showPicDialog();
    }



    /**
     * 初始化图片选择对话框
     */
    public void initPitureDialog() {
        dialog = new SelectPicDialog(mContext, R.style.select_MyDialogStyle);
        dialog.setSelectImgListener(new SelectPicDialog.SelectImgListener() {

            @Override
            public void oClick(int item) {
                switch (item) {
                    case 0:
                        dialog.dismiss();
                        break;
                    case 1: {
                        //album
                        if(isCrop){
                            takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions(cropSize, cropSize));
                        }else{
                            if(limit > 1){
                                takePhoto.onPickMultiple(limit);
                            }else{
                                takePhoto.onPickFromGallery();
                            }
                        }
                        dialog.dismiss();
                    }
                    break;
                    case 2: {
                        //拍照
                        if(isCrop){
                            takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions(cropSize, cropSize));
                        }else{
                            takePhoto.onPickFromCapture(imageUri);
                        }
                        dialog.dismiss();
                    }
                    break;
                    default:
                }
            }
        });
    }

    /**
     * 显示拍照对话框
     */
    public void showPicDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    private void configCompress(TakePhoto takePhoto){
        CompressConfig config;
        config = new CompressConfig.Builder()
                .setMaxSize(1024000)
                .setMaxPixel(maxWidth>=maxHeight? maxWidth:maxHeight)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config,false);
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        //使用takephoto自带相册
        if(limit > 0){
            builder.setWithOwnGallery(true);
        }else{
            builder.setWithOwnGallery(false);
        }
        takePhoto.setTakePhotoOptions(builder.create());
    }

    private CropOptions getCropOptions(int width, int height) {
        //此处的宽高代表裁切框你给的宽高比例
        boolean withWonCrop = false;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

}
