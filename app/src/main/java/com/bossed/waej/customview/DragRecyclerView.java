package com.bossed.waej.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/**
 * 可随意拖拽item RecyclerView
 */
public class DragRecyclerView extends RecyclerView {
    //拖拽响应的时间 默认为1s
    private long mDragResponseMs = 1000;
    //是否支持拖拽，默认不支持
    private boolean isDrag = false;
    //振动器，用于提示替换
    private final Vibrator mVibrator;
    //拖拽的item的position
    private int mDragPosition;
    //拖拽的item对应的View
    private View mDragView;

    //窗口管理器，用于为Activity上添加拖拽的View
    private final WindowManager mWindowManager;
    //item镜像的布局参数
    private WindowManager.LayoutParams mLayoutParams;

    //item镜像的 显示镜像，这里用ImageView显示
    private ImageView mDragMirrorView;
    //item镜像的bitmap
    private Bitmap mDragBitmap;

    //按下的点到所在item的左边缘距离
    private int mPoint2ItemLeft;
    private int mPoint2ItemTop;

    //DragView到上边缘的距离
    private int mOffset2Top;
    private int mOffset2Left;

    //按下时x,y
    private int mDownX;
    private int mDownY;
    //移动的时x.y
    private int mMoveX;
    private int mMoveY;

    //状态栏高度
    private final int mStatusHeight;

    //item发生变化的回调接口
    private OnItemMoveListener itemMoveListener;
    private final Handler mHandler;

    /**
     * 长按的Runnable
     */
    private final Runnable mLongClickRunnable = new Runnable() {
        @Override
        public void run() {

            isDrag = true;
            mVibrator.vibrate(200);
            //隐藏该item
            mDragView.setVisibility(INVISIBLE);
            //在点击的地方创建并显示item镜像
            createDragView(mDragBitmap, mDownX, mDownY);
        }
    };

    public DragRecyclerView(Context context) {
        this(context, null);
    }

    public DragRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mHandler = new Handler();
        mStatusHeight = getStatusHeight(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();

                mDragView = findChildViewUnder(mDownX, mDownY);
                if (mDragView == null) {
                    return super.dispatchTouchEvent(ev);
                }
                //获取按下的position
                mDragPosition = getChildAdapterPosition(mDragView);
                if (mDragPosition == NO_POSITION) {     //无效就返回
                    return super.dispatchTouchEvent(ev);
                }
                //延时长按执行mLongClickRunable
                mHandler.postDelayed(mLongClickRunnable, mDragResponseMs);
                //获取按下的item对应的View 由于存在复用机制，所以需要 处理FirstVisiblePosition
                //计算按下的点到所在item的left top 距离
                mPoint2ItemLeft = mDownX - mDragView.getLeft();
                mPoint2ItemTop = mDownY - mDragView.getTop();
                //计算RecyclerView的left top 偏移量：原始距离 - 相对距离就是偏移量
                mOffset2Left = (int) ev.getRawX() - mDownX;
                mOffset2Top = (int) ev.getRawY() - mDownY;
                //开启视图缓存
                mDragView.setDrawingCacheEnabled(true);
                //获取缓存的中的bitmap镜像 包含了item中的ImageView和TextView
                mDragBitmap = Bitmap.createBitmap(mDragView.getDrawingCache());
                //释放视图缓存 避免出现重复的镜像
                mDragView.destroyDrawingCache();

                break;
            case MotionEvent.ACTION_MOVE:

                mMoveX = (int) ev.getX();
                mMoveY = (int) ev.getY();
                //如果只在按下的item上移动，未超过边界，就不移除mLongClickRunable
                if (!isTouchInItem(mDragView, mMoveX, mMoveY)) {
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacks(mLongClickRunnable);
                //判断是否是点击
                if (!isDrag && mDragMirrorView == null && itemMoveListener != null && Math.abs(mMoveX - mDownX) < 20 && Math.abs(mMoveY - mDownY) < 20) {
                    itemMoveListener.onItemClick(mDragPosition);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isDrag && mDragMirrorView != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    mMoveX = (int) ev.getX();
                    mMoveY = (int) ev.getY();
                    onDragItem(mMoveX, mMoveY);
                    break;
                case MotionEvent.ACTION_UP:
                    onStopDrag();
                    isDrag = false;
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }


    /************************对外提供的接口***************************************/

    public boolean isDrag() {
        return isDrag;
    }

    public void setDrag(boolean drag) {
        isDrag = drag;
    }

    public long getDragResponseMs() {
        return mDragResponseMs;
    }

    public void setDragResponseMs(long mDragResponseMs) {
        this.mDragResponseMs = mDragResponseMs;
    }

    public void setOnItemMoveListener(OnItemMoveListener itemMoveListener) {
        this.itemMoveListener = itemMoveListener;
    }
    /******************************************************************************/


    /**
     * 点是否在该View上面
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchInItem(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        if (view.getLeft() < x && x < view.getRight()
                && view.getTop() < y && y < view.getBottom()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    @SuppressLint("PrivateApi")
    private static int getStatusHeight(Context context) {
        int statusHeight;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int height = Integer.parseInt(Objects.requireNonNull(localClass.getField("status_bar_height").get(localObject)).toString());
                statusHeight = context.getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 停止拖动
     */
    private void onStopDrag() {
        if (mDragView != null) {
            mDragView.setVisibility(VISIBLE);
        }
        removeDragImage();
        if (itemMoveListener != null) {
            itemMoveListener.onUp(mDragPosition);
        }
    }

    /**
     * WindowManager 移除镜像
     */
    private void removeDragImage() {
        if (mDragMirrorView != null) {
            mWindowManager.removeView(mDragMirrorView);
            mDragMirrorView = null;
        }
    }

    /**
     * 拖动item到指定位置
     *
     * @param x
     * @param y
     */
    private void onDragItem(int x, int y) {
        mLayoutParams.x = x - mPoint2ItemLeft + mOffset2Left;
        mLayoutParams.y = y - mPoint2ItemTop + mOffset2Top - mStatusHeight;
        //更新镜像位置
        mWindowManager.updateViewLayout(mDragMirrorView, mLayoutParams);
        int[] location = new int[2];
        getLocationOnScreen(location);
        int pY = location[1];
        if (itemMoveListener != null) {
            itemMoveListener.onMove(x, y + pY);
        }
    }

    /**
     * 创建拖动的镜像
     *
     * @param bitmap
     * @param downX
     * @param downY
     */
    @SuppressLint("RtlHardcoded")
    private void createDragView(Bitmap bitmap, int downX, int downY) {
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.TRANSLUCENT; //图片之外其他地方透明
        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT; //左 上
        //指定位置 其实就是 该 item 对应的 rawX rawY 因为Window 添加View是需要知道 raw x ,y的
        mLayoutParams.x = mOffset2Left + (downX - mPoint2ItemLeft);
        mLayoutParams.y = mOffset2Top + (downY - mPoint2ItemTop) + mStatusHeight;
        //指定布局大小
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //透明度
        mLayoutParams.alpha = 1f;
        //指定标志 不能获取焦点和触摸,允许拖动到窗口外
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

        mDragMirrorView = new ImageView(getContext());
        mDragMirrorView.setImageBitmap(bitmap);
        //添加View到窗口中
        mWindowManager.addView(mDragMirrorView, mLayoutParams);
    }

    /**
     * item 交换时的回调接口
     */
    public interface OnItemMoveListener {
        void onMove(int x, int y);

        void onUp(int position);

        void onItemClick(int position);
    }

}
