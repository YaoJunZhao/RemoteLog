package com.example.zhaoyaojun.test.RemoteLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhaoyaojun on 2018/6/2.
 */

public class Utils {

    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
        return formatter.format(new Date());
    }
}
