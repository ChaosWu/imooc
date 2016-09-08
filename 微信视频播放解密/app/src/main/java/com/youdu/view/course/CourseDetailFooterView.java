package com.youdu.view.course;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.youdu.R;
import com.youdu.module.course.CourseFooterValue;
import com.youdu.util.Utils;

import java.util.ArrayList;

/**
 * @author: vision
 * @function:
 * @date: 16/9/8
 */
public class CourseDetailFooterView extends RelativeLayout {

    private Context mContext;
    /**
     * UI
     */
    private RelativeLayout mRootView;
    private LinearLayout mContentLayout;

    /**
     * data
     */
    private ArrayList<CourseFooterValue> mFooterValues;

    public CourseDetailFooterView(Context context, ArrayList<CourseFooterValue> footerValues) {
        this(context, null, footerValues);
    }

    public CourseDetailFooterView(Context context, AttributeSet attrs, ArrayList<CourseFooterValue> footerValues) {
        super(context, attrs);
        mContext = context;
        mFooterValues = footerValues;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.listview_course_comment_footer_layout, this);
        mContentLayout = (LinearLayout) mRootView.findViewById(R.id.content_layout);

        for (CourseFooterValue item : mFooterValues) {
            mContentLayout.addView(createItem(item));
        }
    }

    private CourseDetailItemView createItem(CourseFooterValue item) {

        CourseDetailItemView view = new CourseDetailItemView(mContext, item);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = Utils.dip2px(mContext, 10);
        view.setLayoutParams(params);
        return view;
    }
}
