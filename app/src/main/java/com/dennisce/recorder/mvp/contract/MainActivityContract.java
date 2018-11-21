package com.dennisce.recorder.mvp.contract;

/**
 * Create by dennis on 2018/11/21
 */
public class MainActivityContract {
    public interface View {
        void recordStarted();

        void recordStopped();

        void recordFailed();
    }

    public interface Presenter {
        void startRecord();

        void stopRecord();

        void onDestroy();
    }
}
