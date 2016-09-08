package com.youdu.view.course;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youdu.R;
import com.youdu.module.course.CourseHeaderValue;
import com.youdu.util.ImageLoaderManager;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by renzhiqiang on 16/9/8.
 */
public class CourseDetailHeaderView extends RelativeLayout {

    private Context mContext;
    /**
     * UI
     */
    private RelativeLayout mRootView;
    private CircleImageView mPhotoView;
    private TextView mNameView;
    private TextView mDayView;
    private TextView mOldValueView;
    private TextView mNewValueView;
    private TextView mIntroductView;
    private TextView mFromView;
    private TextView mZanView;
    private TextView mScanView;
    private LinearLayout mContentLayout;
    /**
     * data
     */
    private CourseHeaderValue mData;
    private ImageLoaderManager mImageLoader;

    public CourseDetailHeaderView(Context context, CourseHeaderValue value) {
        this(context, null, value);
    }

    public CourseDetailHeaderView(Context context, AttributeSet attrs, CourseHeaderValue value) {
        super(context, attrs);
        mContext = context;
        mData = value;
        mImageLoader = ImageLoaderManager.getInstance(mContext);
        initView();
    }

    private void initView() {
        mRootView = (RelativeLayout) LayoutInflater.from(mContext).
                inflate(R.layout.listview_course_comment_head_layout, this);
        mPhotoView = (CircleImageView) mRootView.findViewById(R.id.photo_view);
        mNameView = (TextView) mRootView.findViewById(R.id.name_view);
        mDayView = (TextView) mRootView.findViewById(R.id.day_view);
        mOldValueView = (TextView) mRootView.findViewById(R.id.old_value_view);
        mNewValueView = (TextView) mRootView.findViewById(R.id.new_value_view);
        mIntroductView = (TextView) mRootView.findViewById(R.id.introduct_view);
        mFromView = (TextView) mRootView.findViewById(R.id.from_view);
        mContentLayout = (LinearLayout) mRootView.findViewById(R.id.picture_layout);
        mZanView = (TextView) mRootView.findViewById(R.id.zan_view);
        mScanView = (TextView) mRootView.findViewById(R.id.scan_view);

        mImageLoader.displayImage(mPhotoView, mData.logo);
        mNameView.setText(mData.name);
        mDayView.setText(mData.dayTime);
        mOldValueView.setText(mData.oldPrice);
        mNewValueView.setText(mData.newPrice);
        mIntroductView.setText(mData.text);
        mFromView.setText(mData.from);
        mZanView.setText(mData.zan);
        mScanView.setText(mData.scan);
    }
}
