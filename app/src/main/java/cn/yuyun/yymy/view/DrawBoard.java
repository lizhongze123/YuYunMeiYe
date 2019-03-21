package cn.yuyun.yymy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.lzz.utils.LogUtils;
import cn.yuyun.yymy.R;

/**
 * @author
 * @desc
 * @date 2018/6/9
 */
public class DrawBoard extends View implements View.OnTouchListener {

    private Paint paint = new Paint();
    private Path path = new Path();
    //存储笔画
    private ArrayList<Path> paths = new ArrayList<>();


    /**
     * 保存上一次绘制的终点横坐标
     */
    private float pX;
    /**
     * 保存上一次绘制的终点纵坐标
     */
    private float pY;

    public DrawBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(12.0f);
        paint.setStyle(Paint.Style.STROKE);
        // 设置外边缘
        paint.setStrokeJoin(Paint.Join.ROUND);
        // 形状
        paint.setStrokeCap(Paint.Cap.SQUARE);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paint);
        }
    }

    //清除画板
    public void clear() {

        for (int i = 0; i < paths.size(); i++) {
            paths.get(i).reset();
        }
        paths.clear();
        invalidate();
    }

    //撤销，返回上一步
    public void goBack() {

        if (paths.size() >= 1) {
            path = paths.get(paths.size() - 1);
            path.reset();
            paths.remove(paths.size() - 1);
        }
        invalidate();
    }

    //创建签名文件
    public Bitmap save(String path) {
        this.setDrawingCacheEnabled(true);
        //截图
        Bitmap bitmap = this.getDrawingCache();
        ByteArrayOutputStream baos = null;
        FileOutputStream fos = null;
//        String path = null;
        File file = null;
        try {
//            path = Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + ".jpg";
            file = new File(path);
            fos = new FileOutputStream(file);
            baos = new ByteArrayOutputStream();
            //如果设置成Bitmap.compress(CompressFormat.JPEG, 100, fos) 图片的背景都是黑色的
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            if (b != null) {
                fos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    // 最好在子线程中调用
    /*public void save(String filePath) throws IOException {

        this.setDrawingCacheEnabled(true);
        //截图
        Bitmap bitmap = this.getDrawingCache();
        if (bitmap != null) {
            //保存
            saveBitmapForSdCard(bitmap,filePath);
//            Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getContext(), "截图失败", Toast.LENGTH_SHORT).show();
        }


    }*/

    private void saveBitmapForSdCard(Bitmap bitmap,String filePath) {
        if(TextUtils.isEmpty(filePath)){
            LogUtils.e("filePath为空");
        }
        //创建file对象
        File f = new File(filePath);
        //创建
        if(f.exists()){
            f.delete();
        }

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //原封不动的保存在内存卡上
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                path = new Path();
                paths.add(path);
                path.moveTo(event.getX(), event.getY());
                pX = event.getX();
                pY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                path.quadTo(pX,pY,event.getX(), event.getY());
                //quadTo比lineTo更润滑
                pX = event.getX();
                pY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
                default:
        }


        return true;
    }


}
