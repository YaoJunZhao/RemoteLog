package com.example.remotelog;

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

    private static boolean logEnable = false;
    private static boolean isInited = false;

    private static TcpUtil tcp = new TcpUtil();

    public static void init(String host, int port, String cuid) {
        if (!isInited) {
            tcp.init(host, port, cuid);
            isInited = true;
        }
    }

    public static void destory() {
        if (isInited && tcp != null) {
            tcp.destory();
        }
    }

    public static void setRemoteLogEnable(boolean enable) {
        logEnable = enable;
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

    static int println_native(int priority, String tag, String msgs) {
        if (!isInited || !logEnable) {
            return -1;
        }
        String prefix = priorityChar(priority) + "/" + tag + ": ";
        for (String msg : msgs.split("\n")) {
            tcp.write(new LogPackage(prefix + msg, LogPackage.Type_Log));
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
    public static int v(String tag, String msg) {
        return println_native(VERBOSE, tag, msg);
    }

    /**
     * Send a {@link #DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
        return println_native(DEBUG, tag, msg);
    }

    /**
     * Send an {@link #INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) {
        return println_native(INFO, tag, msg);
    }

    /**
     * Send a {@link #WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
        return println_native(WARN, tag, msg);
    }

    /**
     * Send an {@link #ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
        return println_native(ERROR, tag, msg);
    }
}
