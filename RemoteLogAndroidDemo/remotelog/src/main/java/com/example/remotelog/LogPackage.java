package com.example.remotelog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhaoyaojun on 2018/5/14 0014.
 */

public class LogPackage {
    static class LogHeader {
        final int length;

        LogHeader(int length) {
            this.length = length;
        }

        public byte[] toBytes() {
            byte[] bytes = new byte[4];
            System.arraycopy(intToBytes2(length), 0, bytes, 0, 4);
            return bytes;
        }

        private byte[] intToBytes2(int value) {
            byte[] src = new byte[4];
            src[0] = (byte) ((value >> 24) & 0xFF);
            src[1] = (byte) ((value >> 16) & 0xFF);
            src[2] = (byte) ((value >> 8) & 0xFF);
            src[3] = (byte) (value & 0xFF);
            return src;
        }
    }

    public static final int Type_Log = 1;
    public static final int Type_Cuid = 2;

    private static String formatMsg(String msg, int type) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("msgtype", String.valueOf(type));
            obj.put("msginfo", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static byte[] packageMsg(String m, int type) {
        String msg = formatMsg(m, type);
        LogHeader header = new LogHeader(msg.getBytes().length);
        byte[] bytes = new byte[4 + msg.getBytes().length];
        System.arraycopy(header.toBytes(), 0, bytes, 0, 4);
        System.arraycopy(msg.getBytes(), 0, bytes, 4, msg.getBytes().length);
        return bytes;
    }
}
