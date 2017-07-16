package myandroid.pigrobot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by quxia on 2017/7/9.
 */
public class FragmentLeft extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.meun,container,false);
        TextView tv1,tv2,tv3,tv4;
        tv1 = (TextView) view.findViewById(R.id.id_changeBackground);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Log.e("TAG","back");
            }
        });
        tv2 = (TextView) view.findViewById(R.id.id_changeImage);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv3 = (TextView) view.findViewById(R.id.id_new);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv4 = (TextView) view.findViewById(R.id.id_weather);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}