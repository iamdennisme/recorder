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
    public static List<String> getAllRecorderInfo() {
        List<String> allFile = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + RECORD_FILE;
        File rootFile = new File(path);
        File[] files = rootFile.listFiles();
        if (!rootFile.exists()) {
            return allFile;
        }
        if (files == null) {
            return allFile;
        }
        for (File file : files) {
            allFile.add(file.getAbsolutePath());
        }
        return allFile;
    }

    public static boolean deleteFile(String path) {
        return new File(path).delete();
    }
}
