package com.example.zhaoyaojun.RemoteLogDemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.remotelog.RemoteLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RemoteLog.init("192.168.1.113", 8752, "baidutest");
        try {
            // RemoteLog.init need time to connect to server
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RemoteLog.setRemoteLogEnable(true);
        for (int i = 0; i < 40; i++) {
            RemoteLog.i("MainActivity", "hello " + String.valueOf(i));
        }

        RemoteLog.destory();
    }
}
