package com.youdu.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.youdu.util.ImageLoaderManager;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by renzhiqiang on 16/8/31.
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;

    private ArrayList<String> mData;
    private ImageLoaderManager mLoader;

    public PhotoPagerAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        mData = list;
        mLoader = ImageLoaderManager.getInstance(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mContext);
        mLoader.displayImage(photoView, mData.get(position));
        container.addView(photoView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
