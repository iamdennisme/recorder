package com.dennisce.recorder.mvp.contract;

import com.dennisce.recorder.mvp.model.PlayState;
import com.dennisce.recorder.mvp.model.RecorderInfo;

import java.util.List;

/**
 * Create by dennis on 2018/11/21
 */
public interface HistoryActivityContract {
    interface View {
        void showRecordHistory(List<RecorderInfo> recorderInfoList);

        void showEmpty();

        void changePlayState(int id, PlayState playState);
    }

    interface Presenter {
        void getRecordHistory();

        void delete(RecorderInfo recorderInfo);

        void play(RecorderInfo path);

        void onDestroy();

        void stop(RecorderInfo recorderInfo);
    }
}
