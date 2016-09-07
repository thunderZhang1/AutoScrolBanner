package com.zhangjingjing.autoscrollbanner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhangjingjing.autoscrolbanner.CycleViewpager;
import com.zhangjingjing.autoscrolbanner.PagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CycleViewpager mAutoVp;
    private List<String> imgUrls;
    private ImageView imageView;
    private PagerIndicator mIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutoVp = (CycleViewpager) findViewById(R.id.vp_auto);
        mIndicator = (PagerIndicator) findViewById(R.id.pi_indicator);
        imgUrls = new ArrayList<>();
        imgUrls.add("http://img1.imgtn.bdimg.com/it/u=3769085312,4099151823&fm=11&gp=0.jpg");
        imgUrls.add("http://pic84.huitu.com/res/20160811/872881_20160811165054507200_1.jpg");
        imgUrls.add("http://pic84.huitu.com/res/20160819/519224_20160819161432929319_1.jpg");
        final MyPagerAdapter adapter = new MyPagerAdapter(this, imgUrls);
        mAutoVp.setAdapter(adapter);
        mAutoVp.startAutoScroll();
        mIndicator.setViewpager(mAutoVp);
    }
    private class MyPagerAdapter extends PagerAdapter{
        private Context mContext;
        private List<String> mUrls;
        public MyPagerAdapter(Context context,List<String> imgUrls) {
            this.mContext = context;
            this.mUrls = imgUrls;
        }


        @Override
        public int getCount() {
            return mUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mContext).load(mUrls.get(position)).into(imageView);
            ViewGroup par = (ViewGroup) imageView.getParent();
            if(par!=null){
                par.removeView(imageView);
            }
            container.addView(imageView, lp);
            return imageView;
        }

    }
}
