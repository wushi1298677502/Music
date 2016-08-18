package com.example.scxh.mymusic;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    int mStartMode;// indicates how to behave if the service is killed
    IBinder mBinder;// interface for clients that bind
    boolean mAllowRebind;//intdicates Where onRebind should be used
    public MyService() {
        Logs.e("MyService 构造方法");
    }
    public void onCreate(){
        // The service is being created
        Logs.e("MyService onCreate");
    }
    public int onStartCommand(Intent intent,int flags,int starId){
        // The service is starting, due to a call to startService()
        Logs.e("MyService onStartCommand");

        return mStartMode;
    }

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Logs.e("MyService onBind");

//        return mBinder;
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public boolean onUnbind(Intent intent){
        //All clients have unbound with unbindService()
        Logs.e("MyService onUnbind");

        return mAllowRebind;
    }
    public void onRebind(Intent intent){
        // A client is binding to the service with bindService()
        // after onUnbind() has already been called
        Logs.e("MyService onRebind");

    }
    public void onDestroy(){
        // The service is no longer used and is being destroyed
        Logs.e("MyService onDestroy");

    }
}
