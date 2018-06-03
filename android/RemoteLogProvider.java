package com.example.zhaoyaojun.test.RemoteLog;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by zhaoyaojun on 2018/6/2.
 */

public class RemoteLogProvider extends ContentProvider {

    private static boolean isInited = false;
    private static TcpUtil tcp = new TcpUtil();
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        String type = (String) values.get("type");
        if (type.equals("init")) {
            if (!isInited) {
                String host = values.getAsString("ip");
                int port = values.getAsInteger("port");
                String cuid = values.getAsString("cuid");
                tcp.init(host, port, cuid, Utils.getCurrentTime());
                isInited = true;
            }
        } else if (type.equals("log")) {
            if (isInited) {
                String log = values.getAsString("logmsg");
                tcp.write(new LogPackage(log, LogPackage.Type_Log));
            }
        } else {

        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
