package com.tianduan.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;
import me.shaohui.advancedluban.OnMultiCompressListener;

public class CompressUtil {

    private static String TAG = CompressUtil.class.getName();
    private static CompressUtil mInstance;

    public static synchronized CompressUtil getInstance() {
        if (mInstance == null) {
            mInstance = new CompressUtil();
        }
        return mInstance;
    }

    private CompressUtil() {
    }

    /**
     * @param context
     * @param maxSize       单位kb
     * @param photoPathList
     * @param callBack
     */
    public void compress(final Context context, int maxSize, final List<File> photoPathList, final CompressFinishCallBack callBack) {
        if ((photoPathList == null || photoPathList.size() == 0) && callBack != null) {
            callBack.compressFinish(null);
            return;
        }
        final long startTime = System.currentTimeMillis();
        if (photoPathList != null && photoPathList.size() > 0) {
            List<File> tempFiles = new ArrayList<>();
            for (int i = 0; i < photoPathList.size(); i++) {
                tempFiles.add(photoPathList.get(i));
            }
            Luban.compress(context, tempFiles)
                    .putGear(Luban.CUSTOM_GEAR)
                    .setMaxSize(maxSize)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .launch(new OnMultiCompressListener() {
                        @Override
                        public void onStart() {
                            if (callBack != null)
                                callBack.compressStart("开始压缩图片");
                        }

                        @Override
                        public void onSuccess(List<File> fileList) {
                            if (callBack != null)
                                callBack.compressFinish(fileList);
                            final long finishTime = System.currentTimeMillis();
                            Log.e(TAG, "压缩时间:" + (finishTime - startTime) / 1000);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (callBack != null)
                                callBack.compressError("部分文件不存在或已被删除");
                        }
                    });
        }
    }

    /**
     * 压缩单张图片
     *
     * @param context
     * @param maxSize  单位kb
     * @param file
     * @param callBack
     */
    public void compress(final Context context, int maxSize, final File file, final CompressFinishCallBack callBack) {
        compress(context, maxSize, file.getAbsolutePath(), callBack);
    }

    /**
     * 压缩单张图片
     *
     * @param context
     * @param maxSize   单位kb
     * @param photoPath
     * @param callBack
     */
    public void compress(final Context context, int maxSize, final String photoPath, final CompressFinishCallBack callBack) {
        if (TextUtils.isEmpty(photoPath)) {
            callBack.compressFinish(null);
            return;
        }
        final long startTime = System.currentTimeMillis();
        Luban.compress(context, new File(photoPath))
                .putGear(Luban.CUSTOM_GEAR)
                .setMaxSize(maxSize)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .launch(new OnCompressListener() {

                    @Override
                    public void onStart() {
                        if (callBack != null)
                            callBack.compressStart("开始压缩图片");
                    }

                    @Override
                    public void onSuccess(File fileList) {
                        callBack.compressFinish(fileList);
                        final long finishTime = System.currentTimeMillis();
                        Log.e(TAG, "压缩时间:" + (finishTime - startTime) / 1000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callBack != null)
                            callBack.compressError("压缩图片失败");
                    }
                });
    }

    public interface CompressFinishCallBack<T> {
        /**
         * 开始压缩图片
         *
         * @param msg 提示
         */
        void compressStart(String msg);

        /**
         * 图片压缩完成
         *
         * @param files
         */
        void compressFinish(T files);

        /**
         * 图片压缩失败
         *
         * @param msg 提示
         */
        void compressError(String msg);
    }
}

