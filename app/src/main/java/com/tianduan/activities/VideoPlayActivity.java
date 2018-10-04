package com.tianduan.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayActivity extends Activity {

    private VideoView videoView;
    private TextView tv_top_bar_title;
    private TextView tv_top_bar_right;
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_play);
        tv_top_bar_title = findViewById(R.id.tv_top_bar_title);
        tv_top_bar_right = findViewById(R.id.tv_top_bar_right);
        tv_top_bar_title.setText("视频预览");
        tv_top_bar_right.setVisibility(View.GONE);
        iv_back = findViewById(R.id.iv_activity_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Uri uri = Uri.parse(getIntent().getExtras().getString("videoPath"));
        videoView = findViewById(R.id.vv_video_play);
        MediaController mediaController = new MediaController(this);
        mediaController.setVisibility(View.VISIBLE);
        //隐藏进度条
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }
}
