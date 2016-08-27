package com.youdu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youdu.R;
import com.youdu.activity.AdBrowserActivity;
import com.youdu.constant.Constant;
import com.youdu.core.AdContextInterface;
import com.youdu.core.context.AdContext;
import com.youdu.share.ShareDialog;
import com.youdu.util.ImageLoaderManager;
import com.youdu.util.Utils;

import java.util.ArrayList;

import cn.sharesdk.framework.Platform;

/**
 * @author: qndroid
 * @function:
 * @date: 16/6/15
 */
public class AdAdapter extends BaseAdapter {
    /**
     * Common
     */
    private static final int CARD_COUNT = 3;
    private static final int VIDOE_TYPE = 0x00;
    private static final int CARD_TYPE_ONE = 0x01;
    private static final int CARD_TYPE_TWO = 0x02;

    private LayoutInflater mInflate;
    private Context mContext;
    private ArrayList<String> mData;
    private ViewHolder mViewHolder;
    private AdContext mAdsdkContext;
    private ImageLoaderManager mImagerLoader;

    public AdAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
        mInflate = LayoutInflater.from(mContext);
        mImagerLoader = ImageLoaderManager.getInstance(mContext);
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
        return CARD_COUNT;
    }

    @Override
    public int getItemViewType(int position) {

        String tag = (String) getItem(position);
        if (tag.equals("true")) {
            return VIDOE_TYPE;
        } else {
            return CARD_TYPE_ONE;
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        //无tag时
        if (convertView == null) {
            switch (type) {
                case VIDOE_TYPE:
                    //显示video卡片
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_video_layout, parent, false);
                    mViewHolder.mVieoContentLayout = (RelativeLayout)
                        convertView.findViewById(R.id.video_ad_layout);
                    mViewHolder.mShareView = (ImageView) convertView.findViewById(R.id.item_share_view);
                    //为对应布局创建播放器
                    mAdsdkContext = new AdContext(mViewHolder.mVieoContentLayout,
                        Constant.TES_JSON, null);
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
                    break;
                case CARD_TYPE_ONE:
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_one_layout, parent, false);
                    mViewHolder.mProductPhotoLayout = (LinearLayout) convertView.findViewById(R.id.product_photo_layout);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    break;
                case CARD_TYPE_TWO:
                    mViewHolder = new ViewHolder();
                    convertView = mInflate.inflate(R.layout.item_product_card_two_layout, parent, false);
                    mViewHolder.mProductView = (ImageView) convertView.findViewById(R.id.product_photo_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    break;
            }
            mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
            mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
            mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
            convertView.setTag(mViewHolder);
        }//有tag时
        else {
            switch (type) {
                case VIDOE_TYPE:
                    mViewHolder = (ViewHolder) convertView.getTag();
                    break;
                case CARD_TYPE_ONE:
                    mViewHolder = (ViewHolder) convertView.getTag();
                    break;
                case CARD_TYPE_TWO:
                    mViewHolder = (ViewHolder) convertView.getTag();
                    break;
            }
        }
        //填充item的数据
        switch (type) {
            case VIDOE_TYPE:
                mViewHolder.mShareView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareDialog dialog = new ShareDialog(mContext);
                        dialog.setShareType(Platform.SHARE_IMAGE);
                        dialog.setShareTitle("周杰伦");
                        dialog.setShareText("我很忙");
                        dialog.setImagePhoto(Environment.getExternalStorageDirectory() + "/test2.jpg");
                        dialog.show();
                    }
                });
                break;
            case CARD_TYPE_ONE:
                //主要是往linearlaout中去添加创建好的ImageView.
                break;
            case CARD_TYPE_TWO:
                //为ImageView加载远程图片
                mImagerLoader.displayImage(mViewHolder.mProductView, "");
                break;
        }
        return convertView;
    }

    private ImageView createImageView(String url) {
        ImageView photoView = new ImageView(mContext);
        ViewGroup.MarginLayoutParams params = new MarginLayoutParams(Utils.dip2px(mContext, 80), LayoutParams.MATCH_PARENT);
        params.rightMargin = Utils.dip2px(mContext, 5);
        photoView.setLayoutParams(params);
        mImagerLoader.displayImage(photoView, url);
        return photoView;
    }

    //自动播放方法
    public void updateAdInScrollView() {
        if (mAdsdkContext != null) {
            mAdsdkContext.updateAdInScrollView();
        }
    }

    private static class ViewHolder {
        //所有Card共有属性
        private TextView mTitleView;
        private TextView mInfoView;
        private TextView mFooterView;
        //Video Card特有属性
        private RelativeLayout mVieoContentLayout;
        private ImageView mShareView;

        //Video Card外有Card具有属性
        private TextView mFromView;
        private TextView mZanView;
        //Card One特有属性
        private LinearLayout mProductPhotoLayout;
        //Card Two特有属性
        private ImageView mProductView;
    }


}
