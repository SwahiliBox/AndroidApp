package com.sam.swahilibox.adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam.swahilibox.R;
import com.sam.swahilibox.model.Notification;

public class NotificationAdapter extends ListAdapter<Notification, NotificationAdapter.NoteHolder> {
    private OnItemclicklistener onItemclicklistener;

    public NotificationAdapter() {
        super(DIFF_CALLBACK);
    }

    //more efficient than notifyDatasetChanged
    private static final DiffUtil.ItemCallback<Notification> DIFF_CALLBACK = new DiffUtil.ItemCallback<Notification>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notification notification, @NonNull Notification t1) {
            return notification.getId() == t1.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notification notification, @NonNull Notification t1) {
            return notification.getTitle().equals(t1.getTitle())&&
                    notification.getMessage().equals(t1.getMessage())&&
                    notification.getDate().equals(t1.getDate());
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_item, parent, false);

        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Notification notification = getItem(position);
        holder.tv_title.setText(notification.getTitle());
        holder.tv_date.setText(String.valueOf(notification.getDate()));
        holder.tv_message.setText(notification.getMessage());
    }


    public Notification getNotificationMessageAt(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_date;
        private TextView tv_message;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_date = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (onItemclicklistener != null && position != RecyclerView.NO_POSITION) {
                        onItemclicklistener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }
    public interface OnItemclicklistener {
        void onItemClick(Notification notification);
    }
    //future objectives app version2.0
    public void setOnItemclicklistener(OnItemclicklistener onItemclicklistener) {
        this.onItemclicklistener = onItemclicklistener;
    }
}

