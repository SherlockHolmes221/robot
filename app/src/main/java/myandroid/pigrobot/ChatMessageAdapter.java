package myandroid.pigrobot;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by quxia on 2017/7/3.
 */
public class ChatMessageAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<ChatMessage> mDatas;


    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        mInflater = LayoutInflater.from(context);
        mDatas = data;
    }


    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mDatas.get(position);
        if(chatMessage.getType() == ChatMessage.Type.INCOMING){
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatMessage chatMessage = mDatas.get(i);
        ViewHolder viewHolder =null;
        if(view == null){
            if(getItemViewType(i) == 0){
                view= mInflater.inflate(R.layout.item_from_msg,viewGroup,false);
             viewHolder = new ViewHolder();
            viewHolder.mDate = (TextView) view.findViewById(R.id.id_from_msg_date);
                if(chatMessage.getUrl() == null){
                    viewHolder.mMessage = (TextView) view.findViewById(R.id.id_from_msg_info);
                }

            }else {
                view= mInflater.inflate(R.layout.item_to_msg,viewGroup,false);
                viewHolder = new ViewHolder();
                viewHolder.mDate = (TextView) view.findViewById(R.id.id_to_msg_date);
                viewHolder.mMessage = (TextView) view.findViewById(R.id.id_to_msg_info);
            }
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

       viewHolder.mDate.setText(simpleDateFormat.format(chatMessage.getDate()));
        if(chatMessage.getUrl() == null){
            viewHolder.mMessage.setText(chatMessage.getMsg());
        }else {
            String url  = String.valueOf(chatMessage.getUrl());
            Spanned textView = Html.fromHtml(url);
            viewHolder.mMessage.setText(chatMessage.getMsg()+textView);
        }
        return view;
    }

    private final class ViewHolder{
        TextView mDate ;
        TextView mMessage;
    }


}
