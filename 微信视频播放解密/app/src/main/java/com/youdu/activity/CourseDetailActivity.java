package com.youdu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.youdu.R;
import com.youdu.activity.base.BaseActivity;

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
    /**
     * Data
     */
    private String mCourseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_layout);
        initData();
        initView();
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
