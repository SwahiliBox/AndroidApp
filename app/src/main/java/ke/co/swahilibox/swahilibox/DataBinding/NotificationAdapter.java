package ke.co.swahilibox.swahilibox.DataBinding;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import ke.co.swahilibox.swahilibox.R;
import ke.co.swahilibox.swahilibox.model.Messages;
import ke.co.swahilibox.swahilibox.model.Notification;

public class NotificationAdapter extends ListAdapter<Notification, NotificationAdapter.MessageViewHolder> {


    public NotificationAdapter() {
        super(diffCallback);
    }
    private static final DiffUtil.ItemCallback<Notification> diffCallback = new DiffUtil.ItemCallback<Notification>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notification notification, @NonNull Notification t1) {
            return notification.getNotification_id() == t1.getNotification_id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notification notification, @NonNull Notification t1) {
            return notification.getNotification_message().equals(t1.getNotification_message()) &&
                    notification.getNotification_title().equals(t1.getNotification_title());
        }
    };


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MessageViewHolder(view);
    }

    public Notification getNotificationAt(int position){return getItem(position);}

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Notification notification = getItem(position);
        holder.title.setText(notification.getNotification_title());
        holder.message.setText(notification.getNotification_message());
    }
    //caches data
    class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView message;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
        }
    }
}
