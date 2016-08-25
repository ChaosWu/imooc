package com.youdu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.youdu.R;
import com.youdu.activity.AdBrowserActivity;
import com.youdu.constant.Constant;
import com.youdu.core.AdContextInterface;
import com.youdu.core.context.AdContext;
import com.youdu.widget.CustomVideoView.ADFrameImageLoadListener;
import com.youdu.widget.CustomVideoView.ImageLoaderListener;

import java.util.ArrayList;

/**
 * @author: qndroid
 * @function:
 * @date: 16/6/15
 */
public class AdAdapter extends BaseAdapter {

    private LayoutInflater mInflate;
    private Context mContext;
    private ArrayList<String> mData;
    private ViewHolder mImageHoler, mVideoHolder;
    private AdContext mAdsdkContext;

    public AdAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
        mInflate = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        String tag = (String) getItem(position);
        if (tag.equals("true")) {
            return 0;
        } else {
            return 1;
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        //无tag时
        if (convertView == null) {
            switch (type) {
                case 0:
                    //显示video
                    mVideoHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_video_layout, parent, false);
                    mVideoHolder.vieoContentLayout = (RelativeLayout) convertView.findViewById(R.id.content_layout);
                    //为对应布局创建播放器
                    mAdsdkContext = new AdContext(mVideoHolder.vieoContentLayout,
                        Constant.TES_JSON, frameImageLoadListener);
                    /**
                     *  添加闲鱼关心的事件处理
                     */
                    mAdsdkContext.setAdResultListener(new AdContextInterface() {
                        @Override
                        public void onAdSuccess() {

                        }

                        @Override
                        public void onAdFailed() {

                        }

                        @Override
                        public void onClickVideo(String url) {
                            Intent intent = new Intent(mContext, AdBrowserActivity.class);
                            intent.putExtra(AdBrowserActivity.KEY_URL, url);
                            mContext.startActivity(intent);
                        }
                    });
                    convertView.setTag(mVideoHolder);
                    break;
                case 1:
                    mImageHoler = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_normal_layout, parent, false);
                    mImageHoler.imageView = (ImageView) convertView.findViewById(R.id.image_one);
                    convertView.setTag(mImageHoler);
                    break;
            }
        }//有tag时
        else {
            switch (type) {
                case 0:
                    mVideoHolder = (ViewHolder) convertView.getTag();
                    break;
                case 1:
                    mImageHoler = (ViewHolder) convertView.getTag();
                    break;
            }
        }

        //填充item的数据
        switch (type) {
            case 0:
                break;
            case 1:
                break;
        }
        return convertView;
    }

    //自动播放方法
    public void updateAdInScrollView() {
        if (mAdsdkContext != null) {
            mAdsdkContext.updateAdInScrollView();
        }
    }

    private static class ViewHolder {
        private RelativeLayout vieoContentLayout;
        private ImageView imageView;
    }

    private ADFrameImageLoadListener frameImageLoadListener = new ADFrameImageLoadListener() {

        @Override
        public void onStartFrameLoad(String url, ImageLoaderListener listener) {
            listener.onLoadingComplete(null);
        }
    };
}
