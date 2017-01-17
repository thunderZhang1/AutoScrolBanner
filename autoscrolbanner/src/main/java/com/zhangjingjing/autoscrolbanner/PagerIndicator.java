package com.zhangjingjing.autoscrolbanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zhangjingjing on 16/9/6.
 * 圆形指示器
 *
 */
public class PagerIndicator extends LinearLayout{
    private static final int CIRCLE =0; //数字
    private static final int NUMBER =1; //圆点

    private Context mContext;
    //指示器间隔
    private int mIndicatorMargin = 10;
    //指示器宽度
    private int mIndicatorWidth = 30;
    //指示器高度
    private int mIndicatorHeight = 30;
    //指示器字体大小
    private float mIndicatorFontSize = 16;
    //指示器字体颜色
    private int mIndicatorFontColor = Color.WHITE;
    private int mSelectResId;
    private int mUnSelectResId;
    private int indicatorType = NUMBER;
    private ViewPager mPager; //绑定的viewpager

    private TextView mTextIndicator;
    public PagerIndicator(Context context) {
        super(context);
        init(context,null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public PagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    /**
     * 初始化属性
     * @param context
     * @param attrs
     */
    private void init(Context context,AttributeSet attrs){
        if (attrs == null) {
            return;
        }
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);

        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.PagerIndicator_indicator_margin,10);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.PagerIndicator_indicator_width,20);
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.PagerIndicator_indicator_height,20);
        indicatorType = typedArray.getInt(R.styleable.PagerIndicator_indicator_type,CIRCLE);

        mSelectResId = typedArray.getResourceId(R.styleable.PagerIndicator_select_drawable,R.drawable.black_radius);
        mUnSelectResId =typedArray.getResourceId(R.styleable.PagerIndicator_select_drawable_unselect,R.drawable.white_radius);

        mIndicatorFontColor  = typedArray.getColor(R.styleable.PagerIndicator_indicator_font_color,Color.WHITE);
        mIndicatorFontSize = typedArray.getDimension(R.styleable.PagerIndicator_indicator_font_size,16);
        typedArray.recycle();
    }

    /**
     * 为indicator绑定viewpager
     * @param pager
     */
    public void setViewpager(ViewPager pager){
        mPager = pager;
        initIndicator();
        mPager.removeOnPageChangeListener(mPageChangeListener);
        mPager.addOnPageChangeListener(mPageChangeListener);
        mPageChangeListener.onPageSelected(mPager.getCurrentItem());

    }

    /**
     * 初始化指示器
     * 对指示器类型做分发
     */
    private void initIndicator(){
        switch (indicatorType){
            case NUMBER:
                if(mPager!=null) {
                    initNumIndicator();
                }
                break;
            case CIRCLE:
                if(mPager!=null) {
                    initCircleIndicator();
                }
                break;
            default:
                if(mPager!=null) {
                    initCircleIndicator();
                }
                break;
        }
    }

    /**
     * 初始化数字指示器
     */
    private void initNumIndicator(){
        if(mPager.getAdapter()!=null&&mPager.getAdapter().getCount()!=0) {
            mTextIndicator = new TextView(mContext);
            mTextIndicator.setText(String.format(mContext.getResources().getString(R.string.str_indicator),mPager.getCurrentItem()+1,mPager.getAdapter().getCount()));
            mTextIndicator.setTextColor(mIndicatorFontColor);
            mTextIndicator.setTextSize(TypedValue.COMPLEX_UNIT_SP,mIndicatorFontSize);
            addView(mTextIndicator);
        }
    }

    /**
     * 构造圆点指示器
     */
    private void initCircleIndicator(){
        if(mPager.getAdapter()!=null){
            createIndicators();
        }
    }

    /**
     * 构造指示器
     */
    private void createIndicators() {
        removeAllViews();
        int count = mPager.getAdapter().getCount();
        if (count <= 0) {
            return;
        }
        int currentItem = mPager.getCurrentItem();
        for (int i = 0; i < count; i++) {
            if (currentItem == i) {
                addIndicator(mSelectResId);
            } else {
                addIndicator(mUnSelectResId);
            }
        }
    }

    /**
     * @param resId
     * 构造单个圆点
     */
    private void addIndicator(int resId){
        View Indicator = new View(mContext);
        Indicator.setBackgroundResource(resId);
        addView(Indicator, mIndicatorWidth, mIndicatorHeight);
        LayoutParams lp = (LayoutParams) Indicator.getLayoutParams();
        lp.leftMargin = mIndicatorMargin;
        lp.rightMargin = mIndicatorMargin;
        Indicator.setLayoutParams(lp);
    }



    private int lastPager = -1;
    //页码变化监听器
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(lastPager == position){
                return;
            }
            switch (indicatorType){
                case NUMBER:
                    updateNumIndicator(position);
                    break;
                case CIRCLE:
                    updateCircleIndicator(position);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 更新数字指示器
     */
    private void updateNumIndicator(int position){
        if(mTextIndicator!=null) {
            String text = String.format(mContext.getResources().getString(R.string.str_indicator),position+1, mPager.getAdapter().getCount());
            mTextIndicator.setText(text);
        }
    }
    /**
     * 更新圆点指示器
     */
    private void updateCircleIndicator(int position){
        View last =null;
        if(lastPager >= 0 && mPager.getChildAt(lastPager)!=null){
            last = this.getChildAt(lastPager);
            last.setBackgroundResource(mUnSelectResId);
        }
        View curView = this.getChildAt(position);
        if(curView!=null) {
            curView.setBackgroundResource(mSelectResId);
        }
        lastPager = position;
    }
}
