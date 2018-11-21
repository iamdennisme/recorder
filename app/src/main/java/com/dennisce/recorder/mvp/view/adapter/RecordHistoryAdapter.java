package com.dennisce.recorder.mvp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dennisce.recorder.R;
import com.dennisce.recorder.mvp.model.RecorderInfo;


import java.util.List;

/**
 * Create by dennis on 2018/11/21
 */
public class RecordHistoryAdapter extends RecyclerView.Adapter<RecordHistoryAdapter.RecordHistoryHolder> {

    private List<RecorderInfo> data;

    public RecordHistoryAdapter(List<RecorderInfo> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecordHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new RecordHistoryHolder(layoutInflater.inflate(R.layout.item_record_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHistoryHolder holder, int position) {
        RecorderInfo item = data.get(position);
        holder.tvName.setText(item.name + " " + item.length);
        holder.tvTime.setText(item.time+"");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecordHistoryHolder extends RecyclerView.ViewHolder {
        private ImageView ivPlay;
        private TextView tvName;
        private TextView tvTime;

        RecordHistoryHolder(@NonNull View itemView) {
            super(itemView);
            ivPlay = itemView.findViewById(R.id.iv_play);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}

