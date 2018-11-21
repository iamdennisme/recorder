package com.dennisce.recorder.mvp.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dennisce.recorder.R;
import com.dennisce.recorder.mvp.contract.HistoryActivityContract;
import com.dennisce.recorder.mvp.model.RecorderInfo;
import com.dennisce.recorder.mvp.presenter.HistoryActivityPresenter;
import com.dennisce.recorder.mvp.view.adapter.RecordHistoryAdapter;
import com.dennisce.recorder.tools.recyclerview.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by dennis on 2018/11/21
 */
public class HistoryActivity extends AppCompatActivity implements HistoryActivityContract.View {


    private HistoryActivityPresenter presenter;
    private RecyclerView rcyHistory;
    private TextView tvEmpty;
    private AlertDialog deleteDialog;
    private RecordHistoryAdapter adapter;
    private List<RecorderInfo> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        initPresenter();
        initData();
    }

    private void initData() {
        presenter.getRecordHistory();
    }

    private void initPresenter() {
        presenter = new HistoryActivityPresenter(this);
    }

    private void initView() {
        setTitle(R.string.history);
        rcyHistory = findViewById(R.id.rcy_history);
        tvEmpty = findViewById(R.id.tv_empty);
        adapter = new RecordHistoryAdapter(data);
        rcyHistory.setAdapter(adapter);
        rcyHistory.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rcyHistory.addItemDecoration(new SpacesItemDecoration(18, RecyclerView.VERTICAL, R.color.grey));
        adapter.setOnLongClickListener(new RecordHistoryAdapter.OnLongClickListener() {
            @Override
            public void longClick(final int position) {
                new AlertDialog.Builder(HistoryActivity.this)
                        .setTitle(getString(R.string.delete))
                        .setMessage(getString(R.string.delete_sure))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                presenter.delete(data.get(position));//删除数据库和本地数据
                                //修改显示数据
                                data.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

    }

    @Override
    public void showRecordHistory(List<RecorderInfo> recorderInfoList) {
        tvEmpty.setVisibility(View.INVISIBLE);
        rcyHistory.setVisibility(View.VISIBLE);
        data.addAll(recorderInfoList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showEmpty() {
        tvEmpty.setVisibility(View.VISIBLE);
        rcyHistory.setVisibility(View.INVISIBLE);
    }

}
