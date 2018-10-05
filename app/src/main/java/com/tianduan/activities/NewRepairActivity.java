package com.tianduan.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.tianduan.model.User;
import com.tianduan.net.MyJsonRequest;
import com.tianduan.record.adapter.RecordAdapter;
import com.tianduan.record.entity.RecordBean;
import com.tianduan.record.utils.MediaManager;
import com.tianduan.record.utils.TimeUtil;
import com.tianduan.record.view.RecordButton;
import com.tianduan.util.DimensUtil;
import com.tianduan.util.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class NewRepairActivity extends Activity implements
        RecordButton.AudioFinishRecordListener {

    private static final String TAG = "NewRepairActivity";

    private static final int MAX_RECORD_DURATION = Integer.MAX_VALUE;

    private final int REQUEST_CODE_GALLERY = 1001;
    public static final int RECORD_SYSTEM_VIDEO = 1002;

    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;
    private ImageView iv_back;

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

    private RecordButton btnRecord;

    private View animView;

    private TextView tvDuration;
    private ImageView ivRecord;
    private LinearLayout llRecord;
    private RecordBean recordBean;

    private String dir = Environment.getExternalStorageDirectory() + "/wilson_record_audios";

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
        iv_back = findViewById(R.id.iv_activity_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, 9, mOnHanlderResultCallback);
            }
        });

        ivRecord = findViewById(R.id.iv_record);
        llRecord = findViewById(R.id.ll_record);
        tvDuration = findViewById(R.id.tv_duration);


        btnRecord = findViewById(R.id.btn_record);
        User user = MyApplication.newInstance().getUser();
        String fileDir = MyApplication.newInstance().getContext().getFilesDir().getAbsolutePath() + "/" + user.getObjectId() + "/repairs/" + repair.getObjectId() + "/audio";
        btnRecord.setFIleInfo(fileDir, "audio.amr");
        btnRecord.setmAudioFinishRecordListener(this);
        ivRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animView != null) {
                    animView.setBackgroundResource(R.drawable.adj);
                    animView = null;
                }
                animView = findViewById(R.id.iv_record);
                animView.setBackgroundResource(R.drawable.record_anim);
                AnimationDrawable animationDrawable = (AnimationDrawable) animView.getBackground();
                animationDrawable.start();
                MediaManager.playSound(recordBean.getRecordPath(), new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        animView.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });

//        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (animView != null) {
//                    animView.setBackgroundResource(R.drawable.adj);
//                    animView = null;
//                }
//                animView = view.findViewById(R.id.iv_record);
//                animView.setBackgroundResource(R.drawable.record_anim);
//                AnimationDrawable animationDrawable = (AnimationDrawable) animView.getBackground();
//                animationDrawable.start();
//
//                RecordBean recordBean = recordBeans.get(i);
//                MediaManager.playSound(recordBean.getRecordPath(), new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        animView.setBackgroundResource(R.drawable.adj);
//                    }
//                });
//
//            }
//        });

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
                                    if (!map.isEmpty()) {
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
                    Log.e(TAG, videoPath);
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
                    Button play = new Button(NewRepairActivity.this);
                    RelativeLayout.LayoutParams playParams = new RelativeLayout.LayoutParams(DimensUtil.dip2px(40), DimensUtil.dip2px(40));
                    playParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    play.setLayoutParams(playParams);
                    play.setBackgroundResource(R.mipmap.ic_video_play);
                    layout.addView(play);
                    ll_new_repair_video.addView(layout);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout.setVisibility(View.GONE);
                            bn_new_repair_add_video.setVisibility(View.VISIBLE);
                            videoPath = null;
                        }
                    });
                    final String uri = data.getData().toString();
                    play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(NewRepairActivity.this, VideoPlayActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("videoPath", uri);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    private void initPermission() {
        List<PermissionItem> permissions = new ArrayList<PermissionItem>();
        permissions.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, getString(R.string.permission_cus_item_audio), R.drawable.permission_ic_micro_phone));
        permissions.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.permission_cus_item_read), R.drawable.permission_ic_storage));
        permissions.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.permission_cus_item_write), R.drawable.permission_ic_storage));

        HiPermission.create(this)
                .permissions(permissions)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onDeny(String permisson, int position) {
                    }

                    @Override
                    public void onGuarantee(String permisson, int position) {
                    }
                });
    }

    @Override
    public void onFinishRecord(float seconds, String filePath) {
        recordBean = new RecordBean();
        recordBean.setRecordPath(filePath);
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(recordBean.getRecordPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int duration = mediaPlayer.getDuration();

        //根据语音时长来重置UI长度显示
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int maxWidth = (int) (dm.widthPixels * 0.6);
        int minWidth = (int) (dm.widthPixels * 0.2);
        ViewGroup.LayoutParams layoutParams = llRecord.getLayoutParams();
        layoutParams.width = (int) (minWidth + (maxWidth / MAX_RECORD_DURATION * TimeUtil.convertToSecond(Long.valueOf(duration))));

        tvDuration.setText(TimeUtil.formatDuring(duration));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
        File file = new File(dir);
        if (file.isDirectory()) {
            com.tianduan.record.utils.FileUtil.deleteDir(file);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }
}
