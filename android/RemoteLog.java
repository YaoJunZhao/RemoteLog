package com.example.zhaoyaojun.test.RemoteLog;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class RemoteLog {
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    private static boolean isInited = false;

    public static void init(Context ctx, String host, int port, String cuid) {
        if (!isInited) {
            ContentValues values = new ContentValues();
            values.put("type", "init");
            values.put("ip", host);
            values.put("port", port);
            values.put("cuid", cuid);
            insertContent(ctx, values);
            isInited = true;
        }
    }

    private static void insertContent(Context ctx, ContentValues values) {
        ContentResolver resolver = ctx.getContentResolver();
        Uri uri = Uri.parse("content://org.Randy.RemoteLogProvider");
        resolver.insert(uri, values);
    }

    public static void destory() {
        if (isInited) {

        }
    }

    private static char priorityChar(int priority) {
        switch (priority) {
            case RemoteLog.VERBOSE:
                return 'V';
            case RemoteLog.DEBUG:
                return 'D';
            case RemoteLog.INFO:
                return 'I';
            case RemoteLog.WARN:
                return 'W';
            case RemoteLog.ERROR:
                return 'E';
            case RemoteLog.ASSERT:
                return 'A';
            default:
                return '?';
        }
    }

    static int println_native(Context ctx, int priority, String tag, String msgs) {
        String prefix = priorityChar(priority) + "/" + tag + ": ";
        for (String msg : msgs.split("\n")) {
            ContentValues values = new ContentValues();
            values.put("type", "log");
            values.put("logmsg", Utils.getCurrentTime() + " " + prefix + msg);
            insertContent(ctx, values);
        }
        return 0;
    }

    /**
     * Send a {@link #VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(Context ctx, String tag, String msg) {
        return println_native(ctx, VERBOSE, tag, msg);
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(Context ctx, String tag, String msg) {
        return println_native(ctx, DEBUG, tag, msg);
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(Context ctx, String tag, String msg) {
        return println_native(ctx, INFO, tag, msg);
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(Context ctx, String tag, String msg) {
        return println_native(ctx, WARN, tag, msg);
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(Context ctx, String tag, String msg) {
        return println_native(ctx, ERROR, tag, msg);
    }

}
