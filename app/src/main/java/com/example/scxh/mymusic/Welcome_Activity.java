package com.example.scxh.mymusic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome_Activity extends AppCompatActivity {

   public Handler mhandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_layout);



        mhandler = new Handler(){
            public void handleMessage(Message message){
                int type = message.arg1;
                switch (type){
                    case 0:
                        Intent intent = new Intent(Welcome_Activity.this,MainActivity.class);
                        startActivity(intent);
                        finish();// TODO: 2016/6/12 finish()方法保证退出时不再进入当前页面
                        break;
                    case 1:
                        String count = (String) message.obj;
                        Toast toast = new Toast(Welcome_Activity.this);

                        LayoutInflater layoutInflater = LayoutInflater.from(Welcome_Activity.this);
                        View view1 = layoutInflater.inflate(R.layout.acticity_welcomedemo_layout,null);
                        TextView textView = (TextView) view1.findViewById(R.id.toast_text);
                        textView.setText(count);

                        toast.setView(view1);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();


                        break;
                }
            }
        };

        Message msg = Message.obtain();
        msg.arg1 = 0;
        mhandler.sendMessageDelayed(msg,3000);// TODO: 2016/6/12 以延迟的方式传递内容，（内容，延迟时间）

        // TODO: 2016/6/12 可以实现倒计时
        new Thread(new Runnable() {

            public void run() {

                    Message msg = Message.obtain();
                    msg.arg1 = 1; // TODO: 2016/6/12 arg1 传递整数，用来判断类型
                    msg.obj = "只为最好的你";
                    mhandler.sendMessage(msg);



            }
        }).start();
    }
}

