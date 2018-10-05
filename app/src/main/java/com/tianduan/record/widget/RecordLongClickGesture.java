package com.tianduan.record.widget;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.tianduan.record.interfaces.OnLongPressListener;

/**
 * Created by Administrator on 2018/9/12.
 */
public class RecordLongClickGesture extends GestureDetector.SimpleOnGestureListener {
    private OnLongPressListener mListener;

    @Override
    public void onLongPress(MotionEvent e) {
        if (null != mListener) {
            mListener.startRecord();
        }
        Log.e("tag", "onLongPress!");
    }

    public RecordLongClickGesture setLongPressListener(OnLongPressListener listener) {
        mListener = listener;
        return this;
    }
}
