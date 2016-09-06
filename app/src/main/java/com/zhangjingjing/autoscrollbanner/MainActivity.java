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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CycleViewpager mAutoVp;
    private List<String> imgUrls;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutoVp = (CycleViewpager) findViewById(R.id.vp_auto);
        imgUrls = new ArrayList<>();
        imgUrls.add("http://www.2cto.com/uploadfile/Collfiles/20160525/20160525090655250.jpg");
        imgUrls.add("http://img.my.csdn.net/uploads/201211/10/1352550609_8872.png");
        imgUrls.add("http://img.my.csdn.net/uploads/201211/10/1352553548_4447.png");
        final MyPagerAdapter adapter = new MyPagerAdapter(this, imgUrls);
        mAutoVp.setAdapter(adapter);
        imageView = (ImageView) findViewById(R.id.img);
        mAutoVp.startAutoScroll();
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
