package com.example.zhaoyaojun.test.RemoteLog;

import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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

    LinkedBlockingQueue logQueue = new LinkedBlockingQueue();

    public void init(final String host, final int port, String cuid, String time) {
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

                    while(true) {
                        try {
                            LogPackage logPack = (LogPackage) logQueue.take();
                            write(logPack);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        write(new LogPackage(time + "-" + cuid, LogPackage.Type_Cuid));
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

    public void write(final LogPackage logPackage) {
        if (!isInited) {
            logQueue.add(logPackage);
            return;
        }
        write(logPackage.toBytes());
    }

    private void write(final byte[] bytes) {
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
