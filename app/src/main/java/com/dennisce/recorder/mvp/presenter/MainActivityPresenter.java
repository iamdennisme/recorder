package com.dennisce.recorder.mvp.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.dennisce.recorder.base.Const;
import com.dennisce.recorder.mvp.contract.MainActivityContract;
import com.dennisce.recorder.receiver.RecordReceiverEnum;
import com.dennisce.recorder.service.RecorderService;

import static com.dennisce.recorder.base.Const.RECORD_RECEIVER;

/**
 * Create by dennis on 2018/11/21
 */
public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View mView;
    private Activity activity;
    private Intent recordIntent;
    private RecordReceiver recordReceiver;

    public MainActivityPresenter(MainActivityContract.View view) {
        mView = view;
        activity = (Activity) view;
        recordIntent = new Intent(activity, RecorderService.class);
        recordReceiver = new RecordReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECORD_RECEIVER);
        activity.registerReceiver(recordReceiver, intentFilter);
    }


    @Override
    public void startRecord() {
        activity.startService(recordIntent);
    }

    @Override
    public void stopRecord() {
        activity.stopService(recordIntent);
    }

    @Override
    public void onDestroy() {
        stopRecord();
        activity.unregisterReceiver(recordReceiver);
    }

    public class RecordReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            RecordReceiverEnum recordReceiverEnum = (RecordReceiverEnum) intent.getSerializableExtra(Const.RECORD_STATE);
            switch (recordReceiverEnum) {
                case FAIL: {
                    mView.recordFailed();
                    break;
                }
                case STOP: {
                    mView.recordStopped();
                    break;
                }
                case START: {
                    mView.recordStarted();
                    break;
                }
            }
        }

    }

}
