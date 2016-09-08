package com.youdu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.youdu.R;
import com.youdu.activity.base.BaseActivity;
import com.youdu.adapter.CourseCommentAdapter;
import com.youdu.module.course.BaseCourseModel;
import com.youdu.network.http.RequestCenter;
import com.youdu.okhttp.listener.DisposeDataListener;
import com.youdu.view.course.CourseDetailFooterView;
import com.youdu.view.course.CourseDetailHeaderView;

/**
 * @author: vision
 * @function: 课程详情Activity, 展示课程详情,这个页面要用signalTop模式
 * @date: 16/9/7
 */
public class CourseDetailActivity extends BaseActivity implements View.OnClickListener {

    public static String COURSE_ID = "courseID";

    /**
     * UI
     */
    private ImageView mBackView;
    private ListView mListView;
    private ImageView mLoadingView;
    private CourseCommentAdapter mAdapter;
    /**
     * Data
     */
    private String mCourseID;
    private BaseCourseModel mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_layout);
        initData();
        initView();
        requestDeatil();
    }

    //初始化数据
    private void initData() {
        Intent intent = getIntent();
        mCourseID = intent.getStringExtra(COURSE_ID);
    }

    //初始化数据
    private void initView() {
        mBackView = (ImageView) findViewById(R.id.back_view);
        mBackView.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.comment_list_view);
        mLoadingView = (ImageView) findViewById(R.id.loading_view);
    }

    private void requestDeatil() {

        RequestCenter.requestCourseDetail(mCourseID, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mData = (BaseCourseModel) responseObj;
                updateUI();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    //根据数据填充UI
    private void updateUI() {
        mLoadingView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        mAdapter = new CourseCommentAdapter(this, mData.data.body);
        mListView.setAdapter(mAdapter);
        mListView.addHeaderView(new CourseDetailHeaderView(this, mData.data.head));
        mListView.addFooterView(new CourseDetailFooterView(this, mData.data.footer));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_view:
                finish();
                break;
        }
    }
}
