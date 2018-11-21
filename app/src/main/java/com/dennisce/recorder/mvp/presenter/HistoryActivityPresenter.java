package com.dennisce.recorder.mvp.presenter;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.dennisce.recorder.R;
import com.dennisce.recorder.mvp.contract.HistoryActivityContract;
import com.dennisce.recorder.mvp.model.PlayState;
import com.dennisce.recorder.mvp.model.RecorderInfo;
import com.dennisce.recorder.mvp.view.activity.HistoryActivity;
import com.dennisce.recorder.tools.io.FileTools;
import com.dennisce.recorder.tools.io.RecorderDbHelper;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Create by dennis on 2018/11/21
 */
public class HistoryActivityPresenter implements HistoryActivityContract.Presenter {

    private HistoryActivity activity;
    private RecorderDbHelper db;

    private MediaPlayer mediaPlayer = new MediaPlayer();//简单播放一下语音

    private int currentPlayId = -1;//初始化-1


    private RecordHistoryHandler recordHistoryHandler;

    private final static int recordHandlerWhat = 2;

    public HistoryActivityPresenter(HistoryActivityContract.View view) {
        activity = (HistoryActivity) view;
        db = RecorderDbHelper.getInstance(activity.getApplicationContext());
        recordHistoryHandler = new RecordHistoryHandler(activity);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                activity.changePlayState(currentPlayId, PlayState.STOP);
            }
        });
    }


    @Override
    public void getRecordHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //从数据库取出信息和本地文件做比照，移除已经在本地被删除的文件记录
                List<RecorderInfo> dbInfo = db.getAllItem();
                List<String> allRealFile = FileTools.getAllRecorderInfo();
                ArrayList data = new ArrayList();
                for (RecorderInfo recorderInfo : dbInfo) {
                    for (String path : allRealFile) {
                        if (path.equals(recorderInfo.filePath)) {
                            data.add(recorderInfo);
                        }
                    }
                    if (!data.contains(recorderInfo)) {
                        db.removeRecorderInfoInfoWithId(recorderInfo.id);//移出数据库记录
                    }
                }
                Message msg = new Message();
                msg.what = recordHandlerWhat;
                msg.obj = data;
                recordHistoryHandler.sendMessage(msg);
            }
        }).run();
    }

    @Override
    public void delete(final RecorderInfo recorderInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.removeRecorderInfoInfoWithId(recorderInfo.id);//移出数据库记录
                FileTools.deleteFile(recorderInfo.filePath);
            }
        }).run();
    }

    @Override
    public void play(RecorderInfo recorderInfo) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(recorderInfo.filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            activity.changePlayState(recorderInfo.id, PlayState.PLAY);
            currentPlayId = recorderInfo.id;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, activity.getString(R.string.play_err), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    @Override
    public void stop(RecorderInfo recorderInfo) {
        if (mediaPlayer.isPlaying()) {
            activity.changePlayState(recorderInfo.id, PlayState.STOP);
            mediaPlayer.stop();
        }

    }

    private static class RecordHistoryHandler extends Handler {
        //弱引用
        private final WeakReference<HistoryActivity> mActivity;

        RecordHistoryHandler(HistoryActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case recordHandlerWhat: {
                    List<RecorderInfo> result = (List<RecorderInfo>) (msg.obj);
                    HistoryActivity activity = mActivity.get();
                    if (result.size() == 0) {
                        if (activity != null) {
                            activity.showEmpty();
                        }
                    } else {
                        if (activity != null) {
                            activity.showRecordHistory(result);
                        }
                    }
                }
            }

        }
    }

}
