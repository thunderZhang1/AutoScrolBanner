package com.zhangjingjing.autoscrolbanner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangjingjing on 16/9/5.
 * 无限循环viewpager包装类
 * 保证使用者可定制pagerAdapter
 */
public class CyclePagerAdapterWrapper extends PagerAdapter{
    private PagerAdapter outAdapter;//外部viewpager
    private static final boolean DEFAULT_BOUNDARY_LOOPING = true;

    private boolean mBoundaryLooping = DEFAULT_BOUNDARY_LOOPING;//是否无限循环

    public CyclePagerAdapterWrapper(PagerAdapter outAdapter) {
        this.outAdapter = outAdapter;
    }

    /**
     * @return 包装后的页数
     * 如果不循环可作为普通viewpageradapter使用
     */
    @Override
    public int getCount() {
        int count = getRealCount();
        return mBoundaryLooping?count+2:count; //添加左右一个
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 获取实际页数
     */
    public int getRealCount(){
        return outAdapter.getCount();
    }
    /**
     * 将包装后的页码转换成实际的页面
     */
    public int toRealPosition(int position){
        int realPosition = position;
        int realCount = getRealCount();
        if (realCount == 0) {
            return 0;
        }
        if (mBoundaryLooping) {
            realPosition = (position - 1)%realCount ;//求余,防止数组越界
            if (realPosition < 0) {
                realPosition += realCount;
            }
        }

        return realPosition;
    }

    /**
     *
     * @param container
     * @param position
     * @return
     * 调用外部实际adapter的方法
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = toRealPosition(position);
        return outAdapter.instantiateItem(container,realPosition);
    }

    /**
     *
     * @param container
     * @param position
     * @param object
     * 调用外部的adapter的方法
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realPosition = toRealPosition(position);
        outAdapter.destroyItem(container, realPosition, object);
    }

    /**
     * 讲实际position转为包装后的position
     * @param realPosition
     * @return
     */
    public int toInnerPosition(int realPosition) {
        int position = (realPosition + 1);
        return mBoundaryLooping ? position : realPosition;
    }
}

