package com.dennisce.recorder.mvp.view.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dennisce.recorder.R;
import com.dennisce.recorder.mvp.model.PlayState;
import com.dennisce.recorder.mvp.model.RecorderInfo;
import com.dennisce.recorder.tools.FormatDateTools;


import java.util.List;

/**
 * Create by dennis on 2018/11/21
 */
public class RecordHistoryAdapter extends RecyclerView.Adapter<RecordHistoryAdapter.RecordHistoryHolder> {

    private List<RecorderInfo> data;

    public RecordHistoryAdapter(List<RecorderInfo> data) {
        this.data = data;
    }

    private OnLongClickListener mOnLongClickListener = null;
    private OnLPlayListener mOnLPlayListener = null;

    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    public void setOnLPlayListener(OnLPlayListener listener) {
        mOnLPlayListener = listener;
    }

    @NonNull
    @Override
    public RecordHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new RecordHistoryHolder(layoutInflater.inflate(R.layout.item_record_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordHistoryHolder holder, final int position) {
        final RecorderInfo item = data.get(position);
        holder.tvName.setText(item.name + "   " + FormatDateTools.formatDate(item.length, "mm分:ss秒"));
        holder.tvTime.setText(FormatDateTools.formatDate(item.time, "yyyy-mm-dd hh:mm:ss"));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongClickListener != null) {
                    mOnLongClickListener.longClick(position);//长按删除
                }
                return true;
            }
        });
        holder.ivPlay.setImageResource(item.playState == PlayState.PLAY ? R.drawable.icon_complete : R.drawable.icon_start);
        holder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLPlayListener != null) {
                    if (item.playState != PlayState.PLAY) {
                        mOnLPlayListener.play(item);//播放
                    } else {
                        mOnLPlayListener.stop(item);//停止
                    }
                }
            }
        });
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

    public interface OnLongClickListener {
        void longClick(int position);
    }

    public interface OnLPlayListener {
        void play(RecorderInfo recorderInfo);

        void stop(RecorderInfo recorderInfo);
    }
}

