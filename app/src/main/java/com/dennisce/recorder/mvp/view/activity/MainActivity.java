package com.dennisce.recorder.mvp.view.activity;

import android.Manifest;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.dennisce.recorder.R;
import com.dennisce.recorder.mvp.contract.MainActivityContract;
import com.dennisce.recorder.mvp.presenter.MainActivityPresenter;


/**
 * Create by dennis on 2018/11/20
 */
public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    public static final int REQUEST_PERMISSION_CODE = 100;

    private ImageView mIvRecord;

    private Chronometer mChronometer;

    private ProgressDialog loadingDialog;

    private boolean isRecording = false;

    private MainActivityContract.Presenter presenter;

    private boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPresenter();
    }

    private void initPresenter() {
        presenter = new MainActivityPresenter(this);
    }

    private void initView() {
        mIvRecord = findViewById(R.id.iv_record);
        mIvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                if (!isRecording) {
                    if (!checkAudioAndExternalPermission()) {
                        //没有权限就请求权限
                        requestPermission();
                        return;
                    }
                    presenter.startRecord();
                    return;
                }
                presenter.stopRecord();
            }
        });
        mChronometer = findViewById(R.id.chronometer);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void showDialog() {
        isLoading = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoading && !loadingDialog.isShowing()) {
                    loadingDialog.show();
                }
            }
        }, 500);
    }

    private void dismissDialog() {
        isLoading = false;
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void recordStarted() {
        isRecording = true;
        mIvRecord.setImageResource(R.drawable.icon_complete);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        dismissDialog();
    }

    @Override
    public void recordStopped() {
        isRecording = false;
        mIvRecord.setImageResource(R.drawable.icon_start);
        mChronometer.stop();
        mChronometer.setBase(SystemClock.elapsedRealtime());
        dismissDialog();
    }

    @Override
    public void recordFailed() {
        isRecording = false;
        dismissDialog();
        Toast.makeText(this, getString(R.string.record_fail), Toast.LENGTH_SHORT).show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_CODE);

    }

    private boolean checkAudioAndExternalPermission() {
        //判断是否有音频和文件读取权限，缺1则请求
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                && PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int code : grantResults) {
                if (code != PackageManager.PERMISSION_GRANTED) {
                    //权限授权不完整
                    Toast.makeText(MainActivity.this, getString(R.string.authorization_failure), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this, getString(R.string.authorization_success), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
