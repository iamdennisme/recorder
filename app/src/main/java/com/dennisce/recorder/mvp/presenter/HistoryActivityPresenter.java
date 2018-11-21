package com.dennisce.recorder.mvp.presenter;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.dennisce.recorder.mvp.contract.HistoryActivityContract;
import com.dennisce.recorder.mvp.model.RecorderInfo;
import com.dennisce.recorder.mvp.view.activity.HistoryActivity;
import com.dennisce.recorder.tools.io.RecorderDbHelper;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.dennisce.recorder.base.Const.RECORD_FILE;

/**
 * Create by dennis on 2018/11/21
 */
public class HistoryActivityPresenter implements HistoryActivityContract.Presenter {

    private HistoryActivity activity;
    private RecorderDbHelper db;

    private RecordHistoryHandler recordHistoryHandler;

    private final static int recordHandlerWhat = 2;

    public HistoryActivityPresenter(HistoryActivityContract.View view) {
        activity = (HistoryActivity) view;
        db = RecorderDbHelper.getInstance(activity.getApplicationContext());
        recordHistoryHandler = new RecordHistoryHandler(activity);
    }


    @Override
    public void getRecordHistory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //从数据库取出信息和本地文件做比照，移除已经在本地被删除的文件记录
                List<RecorderInfo> dbInfo = db.getAllItem();
                List<String> allRealFile = getAllFileName();
                ArrayList data = new ArrayList();
                for (RecorderInfo recorderInfo : dbInfo) {
                    for (String path : allRealFile) {
                        if (path.equals(recorderInfo.filePath)) {
                            data.add(recorderInfo);
                            break;
                        }
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
    public void delete(RecorderInfo recorderInfo) {

    }

    private List<String> getAllFileName() {//获取本地有的路径
        List<String> allFile = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + RECORD_FILE;
        File rootFile = new File(path);
        File[] files = rootFile.listFiles();
        if (files == null) {
            return allFile;
        }
        for (File file : files) {
            allFile.add(file.getAbsolutePath());
        }
        return allFile;
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
