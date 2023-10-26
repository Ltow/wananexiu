package com.bossed.waej.util;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.bossed.waej.R;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountDownTimeUtil {
    private WeakReference<TextView> tvCodeWr;//控件软引用，防止内存泄漏
    private CountDownTimer timer;
    private final int times = 60 * 1000;
    private long startTime = 0;
    private long endTime = 0;

    public CountDownTimeUtil(TextView textView) {
        super();
        this.tvCodeWr = new WeakReference(textView);
    }

    public CountDownTimeUtil(TextView textView, Long startTime, Long endTime) {
        super();
        this.tvCodeWr = new WeakReference(textView);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public CountDownTimeUtil() {
        super();
    }

    public void setContent(TextView textView, Long startTime, Long endTime) {
        this.tvCodeWr = new WeakReference(textView);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void runTimer() {
        //倒计时的总时长   每次的间隔时间   单位都是毫秒
        timer = new CountDownTimer(times, 1000) {

            /**
             * 这个是倒计时结束的回调
             */
            @Override
            public void onFinish() {
                if (tvCodeWr.get() != null) {
                    tvCodeWr.get().setClickable(true);
                    tvCodeWr.get().setText("重新获取");
                    tvCodeWr.get().setTextColor(Color.parseColor("#999999"));
                    tvCodeWr.get().setBackgroundResource(R.drawable.shape_stroke_cccccc_corners_dp0);
                }
                cancel();
            }

            /**
             * 这个是每次间隔指定时间的回调
             *
             * @param millisUntilFinished 剩余的时间，单位毫秒
             */
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                if (tvCodeWr.get() != null) {
                    tvCodeWr.get().setClickable(false);
                    tvCodeWr.get().setText(millisUntilFinished / 1000 + "s后重新获取");
                    tvCodeWr.get().setTextColor(Color.parseColor("#256AFC"));
                    tvCodeWr.get().setBackgroundResource(R.drawable.shape_stroke_256afc_corners_dp0);
                }
            }
        }.start();
    }

    /**
     * 两个时间戳的时间差倒计时
     */
    public void runTimer2() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format.format(startTime);
        String date2 = format.format(endTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = 0;
        try {
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            diff = d1.getTime() + 1000 * 60 * 60 * 72 - d2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (diff > 0)
            timer = new CountDownTimer(diff, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvCodeWr.get().setText(formatLongToTimeStr(millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    if (tvCodeWr.get() != null)
                        tvCodeWr.get().setText("");
                }
            }.start();
        else
            tvCodeWr.get().setText("已超时");
    }

    private String formatLongToTimeStr(Long date) {
        long day = date / (60 * 60 * 24);
        long hour = (date / (60 * 60) - day * 24);
        long min = ((date / 60) - day * 24 * 60 - hour * 60);
        long s = (date - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
    }

    /**
     * 这个方法可以在activity或者fragment销毁的时候调用，防止内存泄漏
     * 如果在activity或者fragment关闭销毁的时候没有调用cancle方法，它的onTick方法还是会继续执行，这个时候UI控件都为空，不注意判断的话很容易空指针
     */
    public void cancel() {
        if (timer != null) {
            timer.onFinish();
            timer.cancel();
            timer = null;
        }
    }
}
