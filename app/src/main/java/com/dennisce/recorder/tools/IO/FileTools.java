package com.dennisce.recorder.tools.io;

import android.os.Environment;
import com.dennisce.recorder.mvp.model.RecorderInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dennisce.recorder.base.Const.RECORD_FILE;

/**
 * Create by dennis on 2018/11/21
 */
public class FileTools {
    public static List<RecorderInfo> getAllRecorderInfo() {
        List<RecorderInfo> allFile = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + RECORD_FILE;
        File rootFile = new File(path);
        File[] files = rootFile.listFiles();
        if (files == null) {
            return allFile;
        }
        for (File file : files) {
            RecorderInfo recorderInfo = new RecorderInfo();
            recorderInfo.name = file.getAbsolutePath();
            recorderInfo.filePath = file.getPath();
            allFile.add(recorderInfo);
        }
        return allFile;
    }

    public static int getFileCount() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + RECORD_FILE;
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            return 0;
        }
        File[] files = rootFile.listFiles();
        if (files == null) {
            return 0;
        }
        return files.length;
    }
}
