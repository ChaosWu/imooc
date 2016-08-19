package com.youdu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.youdu.R;
import com.youdu.activity.base.BaseActivity;
import com.youdu.manager.DialogManager;
import com.youdu.manager.UserManager;
import com.youdu.module.user.User;
import com.youdu.network.RequestCenter;
import com.youdu.network.mina.MinaService;
import com.youdu.okhttp.listener.DisposeDataListener;
import com.youdu.view.associatemail.MailBoxAssociateTokenizer;
import com.youdu.view.associatemail.MailBoxAssociateView;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    /**
     * UI
     */
    private MailBoxAssociateView mUserNameAssociateView;
    private EditText mPasswordView;
    private TextView mLoginView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        initView();
    }

    private void initView() {

        mUserNameAssociateView = (MailBoxAssociateView) findViewById(R.id.associate_email_input);
        mPasswordView = (EditText) findViewById(R.id.login_input_password);
        mLoginView = (TextView) findViewById(R.id.login_button);
        mLoginView.setOnClickListener(this);

        mUserNameAssociateView = (MailBoxAssociateView) findViewById(R.id.associate_email_input);
        String[] recommendMailBox = getResources().getStringArray(R.array.recommend_mailbox);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.associate_mail_list_item,
                R.id.tv_recommend_mail, recommendMailBox);
        mUserNameAssociateView.setAdapter(adapter);
        mUserNameAssociateView.setTokenizer(new MailBoxAssociateTokenizer());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_button:
                login();
                break;
        }
    }

    private void login() {

        String userName = mUserNameAssociateView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            return;
        }

        if (TextUtils.isEmpty(password)) {
            return;
        }

        DialogManager.getInstnce().showProgressDialog(this);
        RequestCenter.login(userName, password, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                DialogManager.getInstnce().dismissProgressDialog();
                User user = (User) responseObj;
                UserManager.getInstance().setUser(user);//保存当前用户对象

                //启动长连接
                startService(new Intent(LoginActivity.this, MinaService.class));
                finish();//销毁当前登陆页面
            }

            @Override
            public void onFailure(Object reasonObj) {
                DialogManager.getInstnce().dismissProgressDialog();
            }
        });
    }
}
