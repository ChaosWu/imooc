package com.youdu.activity;

import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.youdu.R;
import com.youdu.activity.base.BaseActivity;
import com.youdu.adapter.SearchAdapter;
import com.youdu.db.database.DBDataHelper;
import com.youdu.db.database.DBHelper;
import com.youdu.module.BaseModel;
import com.youdu.module.search.ProductModel;
import com.youdu.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author renzhiqiang
 * @description 基金收索页面
 * @date 2015年8月19日
 */
public class SearchActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private static final int MAX_HISTORY_RECORD = 10;
    /**
     * 公共UI
     */
    private EditText inputEditText;
    private TextView cancelView;
    /**
     * 历史记录相关UI
     */
    private TextView clearHistoryView;
    private LinearLayout historyLayout;
    private ListView historyListView;
    private SearchAdapter historyAdapter;
    /**
     * 既没有历史纪绿，也没有开始搜索，空间面
     */
    private LinearLayout emptyLayout;
    /**
     * 正在搜索界面
     */
    private LinearLayout searchingLayout;
    private ListView searchingListView;
    private SearchAdapter searchingAdapter;
    private LinearLayout searchingEmptyLayout;
    private TextView searchNoView;
    /**
     * data
     */
    private ArrayList<BaseModel> historyListData;
    private ArrayList<BaseModel> searchingListData;
    private ProductModel fundModel;

    /**
     * speech
     */
    private SpeechRecognizer mIat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        initView();
        initSpeech();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initSpeech() {
        mIat = SpeechRecognizer.createRecognizer(this, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    LogUtils.e(mClassName, "初始化失败，错误码：" + code);
                }
                LogUtils.e(mClassName, code + "'");
            }
        });
        LogUtils.e("search", mIat.toString());
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "10000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "10000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
        //开始听写
        int ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            LogUtils.e(mClassName, "听写失败,错误码：" + ret);
        }
        LogUtils.e(mClassName, ret + "");
    }

    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean isLast) {
            if (isLast) {
                // TODO 最后的结果
                String sn = null;
                // 读取json结果中的sn字段
                try {
                    JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
                    sn = resultJson.optString("sn");
                    inputEditText.setText(sn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onError(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private void initView() {
        inputEditText = (EditText) findViewById(R.id.fund_search_view);
        cancelView = (TextView) findViewById(R.id.cancel_view);
        historyLayout = (LinearLayout) findViewById(R.id.fund_history_layout);
        clearHistoryView = (TextView) historyLayout
                .findViewById(R.id.delect_histroy_view);
        historyListView = (ListView) historyLayout
                .findViewById(R.id.history_list_view);
        historyListView.setOnItemClickListener(this);
        // 空间面View
        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
        // 正在搜索View
        searchingLayout = (LinearLayout) findViewById(R.id.fund_search_layout);
        searchingListView = (ListView) searchingLayout
                .findViewById(R.id.fund_list_view);
        searchingEmptyLayout = (LinearLayout) searchingLayout
                .findViewById(R.id.fund_search_empty_layout);
        searchNoView = (TextView) searchingEmptyLayout
                .findViewById(R.id.seach_no_fund_info);
        searchingListView.setEmptyView(searchingEmptyLayout);
        searchingListView.setOnItemClickListener(this);
        cancelView.setOnClickListener(this);
        clearHistoryView.setOnClickListener(this);
        inputEditText.addTextChangedListener(watcher);

        decideWhichMode();
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String selections = inputEditText.getText().toString();
            if (selections != null && !selections.equals("")) {
                entrySearchMode();
                /**
                 * 重数据库中模糊查询匹赔基金
                 */
                searchingListData = DBDataHelper
                        .getInstance()
                        .select(DBHelper.FUND_LIST_TABLE,
                                "fdcode like ? or abbrev like ? or spell like ?",
                                new String[]{"%" + selections + "%",
                                        "%" + selections + "%",
                                        "%" + selections + "%"}, null,
                                ProductModel.class);

                if (searchingAdapter == null) {
                    searchingAdapter = new SearchAdapter(
                            SearchActivity.this, searchingListData);
                    searchingListView.setAdapter(searchingAdapter);
                } else {
                    searchingAdapter.updateData(searchingListData);
                }
                /**
                 * 滚动到第零项
                 */
                searchingListView.smoothScrollToPosition(0);
                searchNoView.setText(Html.fromHtml(getNoFundInfo(selections)));
            } else {
                decideWhichMode();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_view:
                finish();
                break;
            case R.id.delect_histroy_view:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        switch (parent.getId()) {
            case R.id.history_list_view:
                fundModel = (ProductModel) historyAdapter.getItem(position);
                break;
            case R.id.fund_list_view:
                fundModel = (ProductModel) searchingAdapter
                        .getItem(position);
                insertHistoryTable();
                break;
        }
        //intent.putExtra("fdcode", fundModel.fdcode);
        //intent.putExtra("from_asemble", false);
        //startActivity(intent);
    }

    /**
     * decide to which mode
     */
    private void decideWhichMode() {
        if (getHistoryData() == 0) {
            entryEmptyMode();
        } else {
            entryHistoryMode();
        }
    }

    private void insertHistoryTable() {
        if (DBDataHelper
                .getInstance()
                .select(DBHelper.FUND_BROWSE_TABLE, "fdcode = ?",
                        new String[]{fundModel.fdcode}, null,
                        ProductModel.class).size() == 0) {
            if (getHistoryData() < MAX_HISTORY_RECORD) {
                fundModel.time = String.valueOf(System.currentTimeMillis());
                DBDataHelper.getInstance().insert(DBHelper.FUND_BROWSE_TABLE,
                        fundModel);
            } else {
                DBDataHelper.getInstance().delete(
                        DBHelper.FUND_BROWSE_TABLE,
                        "id = (select min(id) from "
                                + DBHelper.FUND_BROWSE_TABLE + ")", null);
                fundModel.time = String.valueOf(System.currentTimeMillis());
                DBDataHelper.getInstance().insert(DBHelper.FUND_BROWSE_TABLE,
                        fundModel);
            }
        } else {
            fundModel.time = String.valueOf(System.currentTimeMillis());
            DBDataHelper.getInstance().update(DBHelper.FUND_BROWSE_TABLE,
                    "fdcode = ?", new String[]{fundModel.fdcode}, fundModel);
        }
    }

    /**
     * 进入历史记录模式
     */
    private void entryHistoryMode() {
        emptyLayout.setVisibility(View.GONE);
        searchingLayout.setVisibility(View.GONE);
        historyLayout.setVisibility(View.VISIBLE);

        historyListData = DBDataHelper.getInstance().select(
                DBHelper.FUND_BROWSE_TABLE, null, null,
                DBHelper.TIME + DBHelper.DESC, ProductModel.class);
        if (historyAdapter == null) {
            historyAdapter = new SearchAdapter(this, historyListData);
            historyListView.setAdapter(historyAdapter);
        } else {
            historyAdapter.updateData(historyListData);
        }
    }

    /**
     * 进入空模式
     */
    private void entryEmptyMode() {
        DBDataHelper.getInstance().delete(DBHelper.FUND_BROWSE_TABLE, null,
                null);
        searchingLayout.setVisibility(View.GONE);
        historyLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 进入搜索模式
     */
    private void entrySearchMode() {
        historyLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        searchingLayout.setVisibility(View.VISIBLE);
    }

    private int getHistoryData() {
        return DBDataHelper
                .getInstance()
                .select(DBHelper.FUND_BROWSE_TABLE, null, null, null,
                        ProductModel.class).size();
    }

    private String getNoFundInfo(String info) {
        String sourceInfo = "<font color= #666666>"
                + getString(R.string.search_no_title) + "</font>"
                + "<font color= #ff3b3b>" + info + "</font>"
                + "<font color= #666666>" + getString(R.string.search_no_end)
                + "</font>";
        return sourceInfo;
    }
}