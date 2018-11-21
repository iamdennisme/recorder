package com.dennisce.recorder.mvp.contract;

import com.dennisce.recorder.mvp.model.RecorderInfo;

import java.util.List;

/**
 * Create by dennis on 2018/11/21
 */
public interface HistoryActivityContract {
    interface View {
        void showRecordHistory(List<RecorderInfo> recorderInfoList);

        void showEmpty();
    }

    interface Presenter {
        void getRecordHistory();

        void delete(RecorderInfo recorderInfo);
    }
}
