package com.tianduan.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tianduan.MyApplication;
import com.tianduan.model.User;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class InitService extends Service {

    private static final String TAG = "InitService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initUserPic();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initUserPic() {
        final User user = MyApplication.newInstance().getUser();
        if (user != null) {
            final String picture_path = user.getPicture();
            if (picture_path != null) {
                MyApplication.newInstance().getAsyncHttpClient().get(MyApplication.BASE_URL + picture_path.replace("\\", "/")
                        , new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                                if (bitmap != null) {
                                    String path = MyApplication.newInstance().getContext().getFilesDir().getAbsolutePath() + "/" + user.getObjectId() + "/user/head_pic";
                                    File file = new File(path);
                                    if (!file.exists()) {
                                        file.mkdirs();
                                    }
                                    String[] path_split = picture_path.replace("\\", "/").split("/");
                                    file = new File(path, path_split[path_split.length - 1]);
                                    if (!file.exists()) {
                                        try {
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    FileOutputStream fos = null;
                                    BufferedOutputStream bos = null;
                                    try {
                                        fos = new FileOutputStream(file);
                                        bos = new BufferedOutputStream(fos);
                                        bos.write(responseBody);
                                        bos.flush();
                                        Log.e(TAG, file.getAbsolutePath());
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (bos != null) {
                                            try {
                                                bos.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        if (fos != null) {
                                            try {
                                                fos.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
            }
        }
    }
}
