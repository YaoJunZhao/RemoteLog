package com.example.remotelog;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaoyaojun on 2018/5/14 0014.
 */

public class TcpUtil {

    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1,
            Runtime.getRuntime().availableProcessors() * 2 + 1, 5,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(128));

    private static Socket s = null;
    private static OutputStream os = null;

    private static boolean isInited = false;

    public void init(final String host, final int port, final String cuid) {
        if (isInited) {
            return;
        }
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    s = new Socket(host, port);
                    os = s.getOutputStream();
                    isInited = true;

                    write(LogPackage.packageMsg(cuid, LogPackage.Type_Cuid));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void destory() {
        try {
            if (os != null) {
                os.close();
            }
            if (s != null) {
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }

    public void write(final byte[] bytes) {
        if (!isInited) {
            return;
        }
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (os != null) {
                        os.write(bytes);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
