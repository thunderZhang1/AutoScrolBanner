package com.zhangjingjing.autoscrolbanner;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by zhangjingjing on 16/9/5.
 */
public class CycleViewpager extends ViewPager{
    private CyclePagerAdapterWrapper mAdapter;
    private ArrayList<OnPageChangeListener> mPageChangeListeners;

    private final int AUTO_SCROLL = 0x01;
    private boolean                stopScrollWhenTouch         = true;
    public static final int        DEFAULT_INTERVAL            = 3000;
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_SCROLL:
                    scrollOnce();
                    sendEmptyMessageDelayed(AUTO_SCROLL,DEFAULT_INTERVAL);
                    break;
                default:
                    break;
            }
        }
    };
    public void stopAutoScroll() {
        mHandler.removeMessages(AUTO_SCROLL);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if ((action == MotionEvent.ACTION_DOWN)) {
            stopAutoScroll();
        } else if (ev.getAction() == MotionEvent.ACTION_UP ) {
            startAutoScroll();
        }
        return super.dispatchTouchEvent(ev);
    }

    public CycleViewpager(Context context) {
        super(context);
        init(context);
    }

    public CycleViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * @param context
     * 初始化viewpager相关
     * 绑定listerner
     */
    private void init(Context context){
        if(mPageChangeListener!=null){
            super.removeOnPageChangeListener(mPageChangeListener);
        }
        super.addOnPageChangeListener(mPageChangeListener);
    }

    public void startAutoScroll() {
        mHandler.sendEmptyMessageDelayed(AUTO_SCROLL,DEFAULT_INTERVAL);
    }
    private int prePagerIndex = -1;//记录上一次viewpager的页码
    private float mPreviousOffset = -1; //记录上一页的左边偏移百分比
    /**
     * viewpager页面切换监听器
     */
    OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);
                if (positionOffset == 0 && mPreviousOffset ==0&& (position == 0
                        || position == mAdapter.getCount() - 1)) { //滑动动画结束 && 在边界位置
                    setCurrentItem(realPosition, false);
                }
                mPreviousOffset = positionOffset;
                if(mPageChangeListeners!=null){//保证响应其他lisnter
                    for(OnPageChangeListener listener:mPageChangeListeners){
                        if (listener != null) {
                            if (realPosition != mAdapter.getRealCount() - 1) {
                                listener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
                            } else {
                                if (positionOffset > 0.5) {
                                    listener.onPageScrolled(0, 0, 0);
                                } else {
                                    listener.onPageScrolled(realPosition, 0, 0);
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            int realPosition = mAdapter.toRealPosition(position);
            if(position!=prePagerIndex){//避免循环调用
                prePagerIndex = position;
            }
            if(mPageChangeListeners!=null){ //保证响应其他lisnter
                for(OnPageChangeListener listener:mPageChangeListeners){
                    listener.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapter != null) {
                int position = CycleViewpager.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE && (position == 0
                        || position == mAdapter.getCount() - 1)) { //处理滑动静止时的
                    setCurrentItem(realPosition, false);
                }
            }
            if(mPageChangeListeners!=null){//保证响应其他lisnter
                for(OnPageChangeListener listener:mPageChangeListeners){
                    listener.onPageScrollStateChanged(state);
                }
            }
        }
    };

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        addOnPageChangeListener(listener);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (mPageChangeListeners == null) {
            mPageChangeListeners = new ArrayList<>();
        }
        mPageChangeListeners.add(listener);
    }

    @Override
    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (mPageChangeListeners != null) {
            mPageChangeListeners.remove(listener);
        }
    }

    @Override
    public void clearOnPageChangeListeners() {
        if (mPageChangeListeners != null) {
            mPageChangeListeners.clear();
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = new CyclePagerAdapterWrapper(adapter);
        super.setAdapter(mAdapter);
        setCurrentItem(0, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = mAdapter.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    public void scrollOnce() {
        if(mAdapter.getRealCount()>0){
            int position = getCurrentItem() ;
            setCurrentItem(position,true);
        }
    }

}
