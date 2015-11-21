package ke.co.swahilibox.swahilibox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ke.co.swahilibox.swahilibox.R;
import ke.co.swahilibox.swahilibox.model.Message;

/**
 * Created by japheth on 11/17/15.
 */
public class MessageAdapter extends ArrayAdapter<Message> {

    private static final String TAG = MessageAdapter.class.getSimpleName();
    Context context;
    List<Message> messages;

    public MessageAdapter(Context context, List<Message> messageList) {
        super(context, 0, messageList);
        this.context = context;
        messages = messageList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = View.inflate(context, R.layout.list_item, null);

        Message message = getItem(position);
        TextView mess = (TextView) view.findViewById(R.id.message);
        TextView title = (TextView) view.findViewById(R.id.title);

        title.setText(message.getTitle());
        mess.setText(message.getMessage());

        return view;
    }
}
