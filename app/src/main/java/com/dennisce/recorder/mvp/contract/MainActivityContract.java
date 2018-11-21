package com.dennisce.recorder.mvp.contract;

/**
 * Create by dennis on 2018/11/21
 */
public interface MainActivityContract {
    interface View {
        void recordStarted();

        void recordStopped();

        void recordFailed();
    }

    interface Presenter {
        void startRecord();

        void stopRecord();

        void onDestroy();
    }
}
