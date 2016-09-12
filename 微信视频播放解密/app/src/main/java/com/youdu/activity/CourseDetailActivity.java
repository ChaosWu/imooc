package com.youdu.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youdu.R;
import com.youdu.activity.base.BaseActivity;
import com.youdu.adapter.CourseCommentAdapter;
import com.youdu.manager.UserManager;
import com.youdu.module.course.BaseCourseModel;
import com.youdu.module.course.CourseCommentValue;
import com.youdu.module.user.User;
import com.youdu.network.http.RequestCenter;
import com.youdu.okhttp.listener.DisposeDataListener;
import com.youdu.util.Util;
import com.youdu.view.course.CourseDetailFooterView;
import com.youdu.view.course.CourseDetailHeaderView;

/**
 * @author: vision
 * @function: 课程详情Activity, 展示课程详情,这个页面要用signalTop模式
 * @date: 16/9/7
 */
public class CourseDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static String COURSE_ID = "courseID";

    /**
     * UI
     */
    private ImageView mBackView;
    private ListView mListView;
    private CourseDetailHeaderView headerView;
    private CourseDetailFooterView footerView;
    private ImageView mLoadingView;
    private CourseCommentAdapter mAdapter;
    private RelativeLayout mBottomLayout;
    private ImageView mJianPanView;
    private EditText mInputEditView;
    private TextView mSendView;

    /**
     * Data
     */
    private String mCourseID;
    private BaseCourseModel mData;
    private String tempHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_layout);
        initData();
        initView();
        requestDeatil();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
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
        mListView.setOnItemClickListener(this);
        mListView.setVisibility(View.GONE);
        mLoadingView = (ImageView) findViewById(R.id.loading_view);
        mLoadingView.setVisibility(View.VISIBLE);
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();

        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mJianPanView = (ImageView) findViewById(R.id.jianpan_view);
        mJianPanView.setOnClickListener(this);
        mInputEditView = (EditText) findViewById(R.id.comment_edit_view);
        mSendView = (TextView) findViewById(R.id.send_view);
        mSendView.setOnClickListener(this);
        mBottomLayout.setVisibility(View.GONE);
        intoEmptyState();
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

        if (headerView != null) {
            mListView.removeHeaderView(headerView);
        }
        headerView = new CourseDetailHeaderView(this, mData.data.head);
        mListView.addHeaderView(headerView);
        if (footerView != null) {
            mListView.removeFooterView(footerView);
        }
        footerView = new CourseDetailFooterView(this, mData.data.footer);
        mListView.addFooterView(footerView);

        mBottomLayout.setVisibility(View.VISIBLE);
        mInputEditView.requestFocus();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int cursor = position - mListView.getHeaderViewsCount();
        if (cursor < mAdapter.getCommentCount()) {
            CourseCommentValue value = (CourseCommentValue) mAdapter.getItem(
                position - mListView.getHeaderViewsCount());
            tempHint = getString(R.string.comment_hint_head).concat(value.name).
                concat(getString(R.string.comment_hint_footer));
            intoEditState(tempHint);
        }
    }

    /**
     * EditText进入编辑状态
     */
    private void intoEditState(String hint) {
        mInputEditView.setHint(tempHint);
        Util.showSoftInputMethod(this, mInputEditView);
    }

    public void intoEmptyState() {
        mInputEditView.setText("");
        mInputEditView.setHint(getString(R.string.input_comment));
        Util.hideSoftInputMethod(this, mInputEditView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_view:
                finish();
                break;
            case R.id.send_view:
                String comment = mInputEditView.getText().toString().trim();
                if (UserManager.getInstance().hasLogined()) {
                    if (!TextUtils.isEmpty(comment)) {
                        mAdapter.addComment(assembleCommentValue(comment));
                        intoEmptyState();
                    }
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }

                break;
            case R.id.jianpan_view:
                Util.showSoftInputMethod(this, mInputEditView);
                break;
        }
    }

    /**
     * 组装CommentValue对象
     *
     * @return
     */
    private CourseCommentValue assembleCommentValue(String comment) {
        User user = UserManager.getInstance().getUser();
        CourseCommentValue value = new CourseCommentValue();
        value.name = user.data.name;
        value.logo = user.data.photoUrl;
        value.type = 1;
        value.text = tempHint + comment;
        return value;
    }
}
