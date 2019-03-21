package cn.yuyun.yymy.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

import cn.lzz.utils.LogUtils;

/**
 * @author
 * @desc
 * @date
 */
public class MediaManager {

    private static MediaPlayer mMediaPlayer;

    public static void play(String path){
        try {
            LogUtils.e("music--"+path);
            if(mMediaPlayer == null){
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            mMediaPlayer.reset();
            //从网路加载音乐
            mMediaPlayer.setDataSource(path) ;
            //需使用异步缓冲
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    LogUtils.e("开始播放");
                    mMediaPlayer.start();
                }


            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void release() {
        if (null != mMediaPlayer) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}
