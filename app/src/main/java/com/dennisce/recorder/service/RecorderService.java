package com.dennisce.recorder.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.dennisce.recorder.base.Const;
import com.dennisce.recorder.R;
import com.dennisce.recorder.receiver.RecordReceiverEnum;
import com.dennisce.recorder.tools.IO.RecorderDbHelper;


import java.io.File;
import java.io.IOException;

/**
 * Create by dennis on 2018/11/20
 */

public class RecorderService extends Service {

    private static final String RECORDER_SERVICE_TAG = "RECORDER_SERVICE_TAG";

    private String mFileName = null;
    private String mFilePath = null;
    private static final String TYPE = ".mp4";
    private static final String RECORD_FILE = "CoolRecorder";

    private MediaRecorder mRecorder = null;

    private RecorderDbHelper mDatabase;

    private long mStartingTimeMillis = 0;

    private long mRecordTimeMillis = 0;

    private TelephonyManager mTeleManager;

    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state != TelephonyManager.CALL_STATE_IDLE) {
                stopRecording();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //创建通话监听
        mTeleManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTeleManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        //创建数据库工具
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                RECORD_FILE;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        mDatabase = RecorderDbHelper.getInstance(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();//服务启动开始录音
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            stopRecording();
            mTeleManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);//销毁监听
        }
        super.onDestroy();
    }

    public void startRecording() {
        Log.d(RECORDER_SERVICE_TAG, "startRecording");
        //每一次启动都要生成一次当前录音路径
        initFileNameAndPath();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);
        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();
            postRecordState(RecordReceiverEnum.START);
        } catch (IOException e) {
            Log.d(RECORDER_SERVICE_TAG, "prepare failed" + e.getMessage());
            postRecordState(RecordReceiverEnum.FAIL);
        }
    }

    public void initFileNameAndPath() {
        //通过循环来确定当前文件的名字（路径）
        int count = 0;
        File file;
        do {
            count++;
            mFileName = getString(R.string.file_name_format, mDatabase.getCount() + count) +
                    TYPE;
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath = mFilePath +
                    "/" +
                    RECORD_FILE +
                    "/" +
                    mFileName;
            file = new File(mFilePath);
        } while (file.exists() && !file.isDirectory());
    }

    public void stopRecording() {
        Log.d(RECORDER_SERVICE_TAG, "stopRecording");
        mRecorder.stop();
        mRecordTimeMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        mRecorder.release();
        //回收资源
        mRecorder = null;
        //添加到数据库
        try {
            mDatabase.addRecorderInfo(mFileName, mFilePath, mRecordTimeMillis);
            Toast.makeText(this, getString(R.string.record_success_tips, mFilePath, mRecordTimeMillis), Toast.LENGTH_SHORT).show();
            postRecordState(RecordReceiverEnum.STOP);
        } catch (Exception e) {
            Log.d(RECORDER_SERVICE_TAG, "exception", e);
            postRecordState(RecordReceiverEnum.FAIL);
            Toast.makeText(this, getString(R.string.record_fail), Toast.LENGTH_SHORT).show();
        }
    }

    //发送当前状态
    private void postRecordState(RecordReceiverEnum recordReceiverEnum) {
        Intent recordStateIntent = new Intent(Const.RECORD_RECEIVER).putExtra(Const.RECORD_STATE, recordReceiverEnum);
        sendBroadcast(recordStateIntent);
    }
}
