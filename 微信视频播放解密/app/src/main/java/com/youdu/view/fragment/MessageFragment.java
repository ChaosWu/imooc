package com.youdu.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youdu.R;

/**
 * @author: vision
 * @function:
 * @date: 16/7/14
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener {

    /**
     * UI
     */
    private View mContentView;
    private TextView mMessageView;
    private TextView mZanView;
    private TextView mImoocView;
    private TextView mTipView;

    public MessageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_message_layout, null, false);
        initView();
        return mContentView;
    }

    private void initView() {
        mMessageView = (TextView) mContentView.findViewById(R.id.tip_message_view);
        mImoocView = (TextView) mContentView.findViewById(R.id.tip_imooc_view);
        mZanView = (TextView) mContentView.findViewById(R.id.zan_message_info_view);
        mTipView = (TextView) mContentView.findViewById(R.id.tip_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
