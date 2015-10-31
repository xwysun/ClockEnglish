package com.xwysun.AlarmClock;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.xwysun.R;
import com.xwysun.wordmanage.model.clock.Ring;

import java.io.IOException;

/**
 * Created by tornado on 2015/10/27.
 */
public class RIngVUtils  {
    private static MediaPlayer mediaPlayer=null;
    private static Vibrator vibrator=null;
    public static void play(Context context,Ring ring){
        if(ring == Ring.GOODMORNING)
            mediaPlayer = MediaPlayer.create(context,R.raw.nature);
        else if(ring == Ring.GETUP){
            mediaPlayer = MediaPlayer.create(context,R.raw.cryshoulder);
        }else if(ring == Ring.GOODLUCK){
            mediaPlayer = MediaPlayer.create(context,R.raw.deda);
        }
        mediaPlayer.stop();
     //   mediaPlayer.set
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        try {
            //在播放音频资源之前，必须调用Prepare方法完成些准备工作
            mediaPlayer.prepare();
            //开始播放音频
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void stop(){
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();//停止播放
                mediaPlayer.release();//释放资源
                mediaPlayer = null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void vibrate(Context context){
        /**
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         */
        if(vibrator==null)
            vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = {100,500,100,500};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern,10);           //重复10次上面的pattern 如果只想震动一次，index设为-1
    }
    public static void cancelVibrate(){
        if(vibrator!=null)
            vibrator.cancel();
    }
}
