package com.tianduan.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tianduan.MyApplication;
import com.tianduan.model.Repair;
import com.tianduan.net.MyJsonRequest;
import com.tianduan.util.DimensUtil;
import com.tianduan.util.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cz.msebera.android.httpclient.Header;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class NewRepairActivity extends Activity {

    private static final String TAG = "NewRepairActivity";

    private final int REQUEST_CODE_GALLERY = 1001;
    public static final int RECORD_SYSTEM_VIDEO = 1002;

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;

    private EditText et_new_repair_name;
    private EditText et_new_repair_code;
    private EditText et_new_repair_question_description;
    private EditText et_new_repair_fault_part;

    private Button bn_new_repair_commit;
    private Button bn_new_repair_add_picture;
    private Button bn_new_repair_add_video;

    private LinearLayout ll_new_repair_images;
    private LinearLayout ll_new_repair_video;

    private Repair repair;
    private List<String> imagePaths;
    private List<RelativeLayout> imagesList;
    private Map<View, RelativeLayout> image_layout;
    private Map<View, String> image_path;

    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_repair);
        init();
    }

    /**
     * 选取图片后的回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                Log.e(TAG, resultList.get(0).getPhotoPath());
                for (PhotoInfo info : resultList) {
                    String path = info.getPhotoPath();
                    if (imagePaths.contains(path)) {
                        continue;
                    }
                    imagePaths.add(path);
                    RelativeLayout layout = new RelativeLayout(NewRepairActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DimensUtil.dip2px(100), DimensUtil.dip2px(100));
                    params.setMargins(DimensUtil.dip2px(5), 0, DimensUtil.dip2px(5), 0);
                    layout.setLayoutParams(params);
                    ImageView imageView = new ImageView(NewRepairActivity.this);
                    RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    imageView.setImageURI(Uri.fromFile(new File(path)));
                    imageView.setLayoutParams(imageViewParams);
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    layout.addView(imageView);
                    ImageButton imageButton = new ImageButton(NewRepairActivity.this);
                    RelativeLayout.LayoutParams imageButtonParams = new RelativeLayout.LayoutParams(DimensUtil.dip2px(20), DimensUtil.dip2px(20));
                    imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    imageButton.setLayoutParams(imageButtonParams);
                    imageButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_delete));
                    imageButton.setScaleType(ImageView.ScaleType.CENTER);
                    layout.addView(imageButton);
                    ll_new_repair_images.addView(layout);
                    imagesList.add(layout);
                    image_layout.put(imageButton, layout);
                    image_path.put(imageButton, path);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagePaths.remove(image_path.get(v));
                            imagesList.remove(image_layout.get(v));
                            image_layout.get(v).setVisibility(View.GONE);
                            image_path.remove(v);
                            image_layout.remove(v);
                        }
                    });
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Log.e(TAG, errorMsg);
        }
    };

    /**
     * 启用系统相机录制
     */
    public void reconverIntent() {
        Uri fileUri = Uri.fromFile(getOutputMediaFile());
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);     //限制的录制时长 以秒为单位
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024);        //限制视频文件大小 以字节为单位
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);      //设置拍摄的质量0~1
//        intent.putExtra(MediaStore.EXTRA_FULL_SCREEN, false);        // 全屏设置
        startActivityForResult(intent, RECORD_SYSTEM_VIDEO);

    }

    /**
     * Create a File for saving an video
     */
    private File getOutputMediaFile() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "请检查SDCard！", Toast.LENGTH_SHORT).show();
            return null;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }

    private void init() {
        tv_top_bar_title = findViewById(R.id.tv_top_bar_title);
        tv_top_bar_right = findViewById(R.id.tv_top_bar_right);
        tv_top_bar_title.setText("新建报修");
        tv_top_bar_right.setText("保存");

        et_new_repair_name = findViewById(R.id.et_new_repair_name);
        et_new_repair_code = findViewById(R.id.et_new_repair_code);
        et_new_repair_question_description = findViewById(R.id.et_new_repair_question_description);
        et_new_repair_fault_part = findViewById(R.id.et_new_repair_fault_part);
        bn_new_repair_commit = findViewById(R.id.bn_new_repair_commit);
        bn_new_repair_add_picture = findViewById(R.id.bn_new_repair_add_picture);
        ll_new_repair_images = findViewById(R.id.ll_new_repair_images);
        ll_new_repair_video = findViewById(R.id.ll_new_repair_video);
        bn_new_repair_add_video = findViewById(R.id.bn_new_repair_add_video);

        imagePaths = new ArrayList<>();
        imagesList = new ArrayList<>();
        image_layout = new HashMap<>();
        image_path = new HashMap<>();

        bn_new_repair_add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reconverIntent();
            }
        });

        bn_new_repair_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MultiImageSelector.create(NewRepairActivity.this)
//                        .showCamera(true) // show camera or not. true by default
//                        //.count(9) // max select image size, 9 by default. used width #.multi()
//                        .multi() // multi mode, default mode;
//                        //.origin(images) // original select data set, used width #.multi()
//                        .start(NewRepairActivity.this, 1);
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, 9, mOnHanlderResultCallback);
            }
        });

        bn_new_repair_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIsEmptyField()) {
                    repair = new Repair();
                    repair.setName(et_new_repair_name.getText().toString());
                    repair.setCode(et_new_repair_code.getText().toString());
                    repair.setDescription(et_new_repair_question_description.getText().toString());
                    repair.setFaultpart(et_new_repair_fault_part.getText().toString());
                    Log.d(TAG, repair.toStringNotNullField());
                    MyJsonRequest jsonRequest = null;
                    try {
                        jsonRequest = new MyJsonRequest(Request.Method.PUT
                                , MyApplication.buildURL("/repair/new")
                                , new JSONObject(repair.toStringNotNullField())
                                , new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("hefvcjm-1", response.toString());
                                try {
                                    repair = new Gson().fromJson(response.getString("data"), Repair.class);
                                    Map<String, String[]> map = new HashMap<>();
                                    if (imagePaths.size() > 0) {
                                        String[] images = new String[imagePaths.size()];
                                        imagePaths.toArray(images);
                                        map.put("pictures", images);
                                    }
                                    if (videoPath != null) {
                                        map.put("videos", new String[]{videoPath});
                                    }
                                    if (!map.isEmpty()){
                                        postFiles(repair.getObjectId(), map);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        MyApplication.newInstance().getRequestQueue().add(jsonRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 老版本的使用方法
     */
    private void selectSingleImage() {
        Intent intent = new Intent();
        intent.setClass(this, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);//显示可拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);//最多可选9张
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);//单选模式
//        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);//多选模式
        startActivityForResult(intent, 1);//启动MultiImageSelectorActivity
    }


    private void postFiles(String repairObjectId, Map<String, String[]> pathMap) throws
            Exception {
        RequestParams params = new RequestParams();
        Set<String> keys = pathMap.keySet();
        for (String key : keys) {
            String[] paths = pathMap.get(key);
            List<File> files = new ArrayList<>();
            for (String path : paths) {
                File file = new File(path);
                if (file.exists() && file.length() > 0) {
                    files.add(file);
                }
            }
            if (files.size() > 0) {
                File[] postFiles = new File[files.size()];
                files.toArray(postFiles);
                params.put(key, postFiles);
            }
        }
        MyApplication.newInstance().getAsyncHttpClient().post(MyApplication.buildURL("/repair/file/" + repairObjectId)
                , params
                , new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d(TAG, new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d(TAG, new String(responseBody));
                    }
                });
    }

    private boolean checkIsEmptyField() {
        if (replaceBlank(et_new_repair_name.getText().toString()) == ""
                || replaceBlank(et_new_repair_code.getText().toString()) == ""
                || replaceBlank(et_new_repair_question_description.getText().toString()) == ""
                || replaceBlank(et_new_repair_fault_part.getText().toString()) == "") {
            return true;
        }
        return false;
    }

    protected String replaceBlank(String str) {
        String dest = null;
        if (str == null) {
            return dest;
        } else {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            return dest;
        }
    }

    private RelativeLayout layout;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "选择图片");
            switch (requestCode) {
                case 0:

                    break;
                case 1:
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    Log.d(TAG, path.toString());
//                    String path = FileUtils.getPath(this, uri);//获取图片真实保存位置
//                    images.add(path);//记录文件路径
                    //Bitmap bitmap2 = BitMapUtils.getSmallBitmap(path2);//获取压缩图像
                    //image2.setImageBitmap(bitmap2);
                    break;
                case RECORD_SYSTEM_VIDEO:
                    Log.e(TAG, data.getData().toString());
                    videoPath = FileUtil.getRealFilePath(data.getData());
                    bn_new_repair_add_video.setVisibility(View.INVISIBLE);
                    layout = new RelativeLayout(NewRepairActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DimensUtil.dip2px(100), DimensUtil.dip2px(100));
                    params.setMargins(DimensUtil.dip2px(5), 0, DimensUtil.dip2px(5), 0);
                    layout.setLayoutParams(params);
                    VideoView videoView = new VideoView(NewRepairActivity.this);
                    RelativeLayout.LayoutParams videoViewParams = new RelativeLayout.LayoutParams(DimensUtil.dip2px(100), DimensUtil.dip2px(100));
                    videoView.setLayoutParams(videoViewParams);
                    videoView.setVideoURI(data.getData());
                    videoView.seekTo(1);
//                    videoView.start();
                    layout.addView(videoView);
                    ImageButton imageButton = new ImageButton(NewRepairActivity.this);
                    RelativeLayout.LayoutParams imageButtonParams = new RelativeLayout.LayoutParams(DimensUtil.dip2px(20), DimensUtil.dip2px(20));
                    imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    imageButton.setLayoutParams(imageButtonParams);
                    imageButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_delete));
                    imageButton.setScaleType(ImageView.ScaleType.CENTER);
                    layout.addView(imageButton);
                    ll_new_repair_video.addView(layout);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout.setVisibility(View.GONE);
                            bn_new_repair_add_video.setVisibility(View.VISIBLE);
                            videoPath = null;
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }
}
