package myandroid.pigrobot;

import java.net.URL;

/**
 * Created by quxia on 2017/7/6.
 */
public class urlResult {
    private int code;
    private String text;
    private URL url;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
