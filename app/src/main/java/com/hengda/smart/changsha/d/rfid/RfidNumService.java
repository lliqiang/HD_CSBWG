package com.hengda.smart.changsha.d.rfid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

public abstract class RfidNumService extends Service {

    public ReadThread mReadThread;
    public InputStream mInputStream;
    public OutputStream mOutputStream;
    private byte[] tem1 = new byte[4];
    private byte[] tem2 = new byte[4];

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            mInputStream = SerialPort.getInstance().getInputStream();
            mOutputStream = SerialPort.getInstance().getOutputStream();
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                int size;
                byte[] buffer = new byte[16];
                try {
                    if (mInputStream == null) {
                        return;
                    }
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        if (size == 4) {
                            if (buffer[0] == (byte) 0xAA) {
                                tem1 = subBytes(buffer, 0, 4);
                            }
                        } else if (size == 3) {
                            tem2 = subBytes(buffer, 0, 4);
                            if (tem1 != null) {
                                onDataReceived(byteMerger(tem1, tem2), 8);
                            }
                        } else {
                            onDataReceived(buffer, size);
                        }
                    }
                    sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    private byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i = begin; i < begin + count; i++)
            bs[i - begin] = src[i];
        return bs;
    }

    /**
     * 抽象方法，处理收到的数据，在子类中实现
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/1/21 13:21
     */
    public abstract void onDataReceived(final byte[] buffer, final int size);

}
