package com.youdu.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.youdu.R;

/**
 * Created by renzhiqiang on 16/9/5.
 */
public class HotSalePagerAdapter extends PagerAdapter {
    int[] imgIdArray;
    ImageView[] mImageViews;
    private Context mContext;

    public HotSalePagerAdapter(Context context) {

        mContext = context;

        imgIdArray = new int[]{R.drawable.bg_1_a_01, R.drawable.bg_1_b_01, R.drawable.bg_1_c_01, R.drawable.bg_2_a_01,
                R.drawable.bg_2_b_01};
        //将图片装载到数组中
        mImageViews = new ImageView[imgIdArray.length];
        for (int i = 0; i < mImageViews.length; i++) {
            ImageView imageView = new ImageView(mContext);
            mImageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdArray[i]);
        }
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(mImageViews[position % mImageViews.length]);

    }

    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(mImageViews[position % mImageViews.length], 0);
        return mImageViews[position % mImageViews.length];
    }
}
