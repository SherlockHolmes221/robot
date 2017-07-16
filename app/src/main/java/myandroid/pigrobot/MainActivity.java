package myandroid.pigrobot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private final static String KEY = "LIST_VIEW";
    private ListView mMsgs;
    private ChatMessageAdapter mAdapter = null;
    private List<ChatMessage> mDatas = null;


    private Handler mHandler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
        ChatMessage fromMessage = (ChatMessage) msg.obj;
          mDatas.add(fromMessage);
          mAdapter.notifyDataSetChanged();
          mMsgs.setSelection(mDatas.size()-1);
      }
  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);


    }

    @Override
    protected void onStart() {
        super.onStart();
//        getView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveView();
    }

    @Override
    protected void onResume() {
        super.onResume();
      getView();
    }

    //用ShanredPerference进行数据的存储
    public void saveView() {
        SharedPreferences preferences = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
       int len = mDatas.size();
        String s  =null;
        Gson gson = new Gson();
        String jsonString = null;
        if(mDatas.size()<=50){
            for(int i = 0;i<len;i++){
                s = KEY +i;
                jsonString = gson.toJson(mDatas.get(i));
                editor.putString(s,jsonString);
            }
        }else {
            for(int j = 0;j<50;j++){
                s = KEY +j;
                jsonString = gson.toJson(mDatas.get(j+mDatas.size()-50));
                editor.putString(s,jsonString);
            }
        }
        Log.e("TAG",jsonString);

        editor.commit();
    }

    //用ShanredPerference进行数据的提取
    public void getView() {
       mDatas.clear();
        Gson gson = new Gson();
        ChatMessage chatMessage = null;

        SharedPreferences preferences = getSharedPreferences("test", MODE_PRIVATE);
        String jsonString =null;
        for(int i = 0;i<50;i++) {
            String s = KEY + i;
            jsonString = preferences.getString(s, "");
            if (!jsonString.equals("")) {
                chatMessage = gson.fromJson(jsonString, ChatMessage.class);
                mDatas.add(chatMessage);
            }
        }
        if(mDatas.size()>=0 &&(!mDatas.get(mDatas.size()-1).getMsg().equals("你好，猪宝宝为你服务"))){
            mDatas.add(new ChatMessage("你好，猪宝宝为你服务", ChatMessage.Type.INCOMING,new Date()));
        }
            mAdapter.notifyDataSetChanged();
        }

}
