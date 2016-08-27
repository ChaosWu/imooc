package com.youdu.util;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.youdu.vuandroidadsdk.R;

/**
 * @author: qndroid
 * @function: 初始化UniverImageLoader, 并用来加载网络图片
 * @date: 16/6/27
 */
public class ImageLoaderManager {

    private static final int THREAD_COUNT = 3;
    private static final int PRIORITY = 2;
    private static final int MEMORY_CACHE_SIZE = 2 * 1024 * 1024;
    private static final int CONNECTION_TIME_OUT = 5 * 1000;
    private static final int READ_TIME_OUT = 30 * 1000;

    private static ImageLoaderManager mInstance = null;
    private static ImageLoader mLoader = null;


    public static ImageLoaderManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ImageLoaderManager.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 私有构造方法完成初始化工作
     *
     * @param context
     */
    private ImageLoaderManager(Context context) {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
            .Builder(context)
            .threadPoolSize(THREAD_COUNT)
            .threadPriority(Thread.NORM_PRIORITY - PRIORITY)
            .denyCacheImageMultipleSizesInMemory()
            .memoryCache(new UsingFreqLimitedMemoryCache(MEMORY_CACHE_SIZE))
            .tasksProcessingOrder(QueueProcessingType.LIFO)
            .defaultDisplayImageOptions(getDefaultOptions())
            .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT))
            .writeDebugLogs()
            .build();

        ImageLoader.getInstance().init(config);
        mLoader = ImageLoader.getInstance();
    }

    //load the image
    public void displayImage(ImageView imageView, String path, ImageLoadingListener listener) {
        if (mLoader != null) {
            mLoader.displayImage(path, imageView, listener);
        }
    }

    public void displayImage(ImageView imageView, String path) {
        displayImage(imageView, path, null);
    }

    private DisplayImageOptions getDefaultOptions() {

        DisplayImageOptions options = new
            DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.xadsdk_img_error)
            .showImageOnFail(R.drawable.xadsdk_img_error)
            .build();
        return options;
    }
}
