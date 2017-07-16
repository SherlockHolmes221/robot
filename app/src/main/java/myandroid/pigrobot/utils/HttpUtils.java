package myandroid.pigrobot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import myandroid.pigrobot.ChatMessage;
import myandroid.pigrobot.TextResult;
import myandroid.pigrobot.codeResult;
import myandroid.pigrobot.urlResult;

/**
 * Created by quxia on 2017/7/1.
 */
public class HttpUtils {
    private static final  String URL = "http://www.tuling123.com/openapi/api";
    private static final  String API_KEY = "66f43a43c28f407facae6719d5f6cac4";

    public static  ChatMessage sendMessage(String msg){
        ChatMessage chatMessage = new ChatMessage(null,null,null);
        try {
            String jsonRes = doGet(msg);

            Gson gson = new Gson();

            codeResult codeResult = null;
            codeResult = gson.fromJson(jsonRes,codeResult.class);
            int code;
            code = codeResult.getCode();
            switch (code){
                case 100000:
                    TextResult textResult = null;
                    try {
                        textResult = gson.fromJson(jsonRes,TextResult.class);
                        chatMessage.setMsg(textResult.getText());
                        chatMessage.setUrl(null);
                    }catch (JsonIOException e){
                        chatMessage.setMsg("服务器繁忙，请稍后再试");
                    }
                    break;
                case 200000:
                    try {
                        urlResult urlResult = null;
                        urlResult = gson.fromJson(jsonRes, urlResult.class);
                        chatMessage.setMsg(urlResult.getText());
                        chatMessage.setUrl(urlResult.getUrl());
                    }catch (JsonIOException e){
                        chatMessage.setMsg("服务器繁忙，请稍后再试");
                    }
                   break;
                default:
                    break;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        chatMessage.setDate(new Date());
        chatMessage.setType(ChatMessage.Type.INCOMING);
        return chatMessage;
    }

    public static String doGet(String msg) throws MalformedURLException {
        String result = "";
        String url = setParams(msg);
        InputStream input = null;
        ByteArrayOutputStream baos = null;
        try {
            java.net.URL urlNet= new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlNet.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            input = conn.getInputStream();
            int len = -1;
            byte[] buf = new byte[128];
            baos = new ByteArrayOutputStream();

            while((len = input.read(buf))!=-1){
                baos.write(buf,0,len);
            }
            baos.flush();
            result = new String(baos.toByteArray());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static String setParams(String msg) {
        String url = null;
        try {
            url = URL+ "?key="+API_KEY+"&info="+ URLEncoder.encode(msg,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
