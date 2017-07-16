package myandroid.pigrobot;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myandroid.pigrobot.utils.HttpUtils;

public class ChatFragment extends Fragment
{
	private Bundle savedState = null;
	private final static String KEY = "LIST_VIEW";
	private ListView mMsgs;
	private ChatMessageAdapter mAdapter = null;
	private List<ChatMessage> mDatas = null;

	private RelativeLayout leftLayout;
	private RelativeLayout rightLayout;

	private EditText mInputMsg;
	private Button mSendButton;

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			ChatMessage fromMessage = (ChatMessage) msg.obj;
			mDatas.add(fromMessage);
			mAdapter.notifyDataSetChanged();
			mMsgs.setSelection(mDatas.size()-1);
		}
	};


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.chat_layout,container,false);


		mMsgs = (ListView) view.findViewById(R.id.id_listview_msg);
		mMsgs.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		mInputMsg = (EditText)view.findViewById(R.id.id_input_message);
		mInputMsg.clearFocus();
		mSendButton = (Button) view.findViewById(R.id.id_send_message);
		initDatas();
		initListener();
		return view;
	}

	private void initListener() {
		mSendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final String toMsg = mInputMsg.getText().toString();
				if(TextUtils.isEmpty(toMsg)){
					Toast.makeText(getContext(),"发送消息不能为空！",Toast.LENGTH_LONG).show();
					return;
				}

				ChatMessage toMessage = new ChatMessage(toMsg, ChatMessage.Type.OUTCOMING,new Date());
				if(!TextUtils.isEmpty(toMsg)){
					mDatas.add(toMessage);
				}
				mAdapter.notifyDataSetChanged();
				mMsgs.setSelection(mDatas.size()-1);
				mInputMsg.setText("");

				new Thread(){
					@Override
					public void run() {
						ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
						Message m = Message.obtain();
						m.obj = fromMessage;
						mHandler.sendMessage(m);
					};
				}.start();

			}
		});
	}

	private void initDatas() {
		if(mDatas == null){
			mDatas = new ArrayList<ChatMessage>();
			mDatas.add(new ChatMessage("你好，猪宝宝为你服务", ChatMessage.Type.INCOMING,new Date()));
		}
		mAdapter = new ChatMessageAdapter(getContext(),mDatas);
		mMsgs.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		if(savedInstanceState != null) {
//			mDatas.clear();
//			Gson gson = new Gson();
//			ChatMessage chatMessage;
//			String jsonString;
//			for (int i = 0; i < 50; i++) {
//				String s = KEY + i;
//				jsonString = savedInstanceState.getString(s, "");
//				if (!jsonString.equals("")) {
//					chatMessage = gson.fromJson(jsonString, ChatMessage.class);
//					mDatas.add(chatMessage);
//				}
//			}
//			Log.e("TAG", "get");
//			mAdapter.notifyDataSetChanged();
//		}
//		if (mDatas != null && mDatas.size() >= 0 && (!mDatas.get(mDatas.size() - 1).getMsg().equals("你好，猪宝宝为你服务"))) {
//			mDatas.add(new ChatMessage("你好，猪宝宝为你服务", ChatMessage.Type.INCOMING, new Date()));
//			mAdapter.notifyDataSetChanged();
//		}
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		int len = mDatas.size();
		String s  ;
		Gson gson = new Gson();
		String jsonString ;
		if(mDatas.size()<=50){
			Log.e("TAG","save");
			for(int i = 0;i<len;i++){
				s = KEY +i;
				jsonString = gson.toJson(mDatas.get(i));
				outState.putString(s,jsonString);
			}
		}else {
			for(int j = 0;j<50;j++){
				s = KEY +j;
				jsonString = gson.toJson(mDatas.get(j+mDatas.size()-50));
				outState.putString(s,jsonString);
			}
		}
}
}
