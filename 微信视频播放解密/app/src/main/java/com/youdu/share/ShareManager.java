package com.youdu.share;

import android.content.Context;

import com.youdu.util.LogUtils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author vision
 * @function 分享功能统一入口，负责发送数据到指定平台,可以优化为一个单例模式
 */

public class ShareManager {

    private static ShareManager mShareManager = null;
    /**
     * 要分享到的平台
     */
    private Platform mCurrentPlatform;

    /**
     * 线程安全的单例模式
     */
    public static ShareManager getInstance() {
        if (mShareManager == null) {
            synchronized (ShareManager.class) {
                if (mShareManager == null) {
                    mShareManager = new ShareManager();
                }
            }
        }
        return mShareManager;
    }

    private ShareManager() {
    }

    /**
     * 第一个执行的方法,最好在程序入口入执行
     *
     * @param context
     */
    public static void initSDK(Context context) {

        ShareSDK.initSDK(context);
    }

    /**
     * 分享数据到不同平台
     */
    public void shareData(ShareData shareData, PlatformActionListener listener) {
        switch (shareData.mPlatformType) {
            case QQ:
                mCurrentPlatform = ShareSDK.getPlatform(QQ.NAME);
                break;
            case QZone:
                mCurrentPlatform = ShareSDK.getPlatform(QZone.NAME);
                break;
            case WeChat:
                mCurrentPlatform = ShareSDK.getPlatform(Wechat.NAME);
                break;
            case WechatMoments:
                mCurrentPlatform = ShareSDK.getPlatform(WechatMoments.NAME);
                break;
            default:
                break;
        }
        mCurrentPlatform.setPlatformActionListener(listener); //由应用层去处理回调,分享平台不关心。
        mCurrentPlatform.share(shareData.mShareParams);
    }

    /**
     * 第三方用户登陆应用统一入口，
     *
     * @param type     第三方类型
     * @param listener 事件回调处理
     */
    public void loginEntry(PlatofrmType type, PlatformActionListener listener) {
        switch (type) {
            case QQ:
            case QZone:
                mCurrentPlatform = ShareSDK.getPlatform(QZone.NAME);
                break;
            default:
                break;
        }

        mCurrentPlatform.setPlatformActionListener(listener);
        mCurrentPlatform.SSOSetting(false);
        mCurrentPlatform.showUser(null); // 请求用户信息
        LogUtils.e("ShareManager", "go here");
    }

    /**
     * @author 应用程序需要的平台
     */
    public enum PlatofrmType {
        QQ, QZone, WeChat, WechatMoments;
    }
}