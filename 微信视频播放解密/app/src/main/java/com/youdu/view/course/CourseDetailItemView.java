package com.youdu.view.course;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youdu.R;
import com.youdu.module.course.CourseFooterValue;
import com.youdu.util.ImageLoaderManager;

/**
 * @author: vision
 * @function:
 * @date: 16/9/8
 */
public class CourseDetailItemView extends RelativeLayout {

    private Context mContext;
    /**
     * UI
     */
    private RelativeLayout mRootView;
    private ImageView mPhotoView;
    private TextView mNameView;
    private TextView mPriceView;
    private TextView mZanView;
    /**
     * data
     */
    private CourseFooterValue mData;
    private ImageLoaderManager mImageLoader;

    public CourseDetailItemView(Context context, CourseFooterValue value) {
        this(context, null, value);
    }

    public CourseDetailItemView(Context context, AttributeSet attrs, CourseFooterValue value) {
        super(context, attrs);
        mContext = context;
        mData = value;
        mImageLoader = ImageLoaderManager.getInstance(mContext);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.item_recommand_course_layout, this);
        mPhotoView = (ImageView) mRootView.findViewById(R.id.image_two_view);
        mNameView = (TextView) mRootView.findViewById(R.id.name_two_view);
        mPriceView = (TextView) mRootView.findViewById(R.id.price_two_view);
        mZanView = (TextView) mRootView.findViewById(R.id.zan_two_view);

        mImageLoader.displayImage(mPhotoView, mData.photoUrl);
        mNameView.setText(mData.name);
        mPriceView.setText(mData.price);
        mZanView.setText(mData.zan);
    }
}
