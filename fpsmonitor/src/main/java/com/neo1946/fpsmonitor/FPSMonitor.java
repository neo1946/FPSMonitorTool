package com.neo1946.fpsmonitor;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;
import android.view.Choreographer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;


/**
 * @auther ouyangzhaoxian
 * @data 2018/10/15
 *
 * 每秒log帧率，帧率小于50的时候log出掉帧2帧以上的堆栈信息
 *
 */
public class FPSMonitor implements Choreographer.FrameCallback {
    private boolean isDetail;
    private static final String TAG = "FPSMonitor";
    private static long mMarkTime = 0;
    private static int mFPS = 0;
    private static FPSMonitor instance = new FPSMonitor();
    private static boolean isStart = true;
    private static Application application;

    private FPSMonitor() {
    }

    public static boolean isIsStart() {
        return isStart;
    }

    public static void start(Application context) {
        if(context != null){
            application = context;
        }
        if(application == null){
            Log.e(TAG,"application is null");
        }
        FileManager.getInstance().init(application);
        FloatWindowManager.createFloatWindow(context);
        BlockDetectByPrinter.start();
        isStart = true;
        Choreographer.getInstance().postFrameCallback(instance);
    }

    public static void stop() {
        FloatWindowManager.removeFloatWindowManager();
        isStart = false;
    }

    public static FPSMonitor getInstance() {
        return instance;
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        long now = System.currentTimeMillis();
        if (now - mMarkTime > 999) {
//            KLog.debug("kayzing FPS", "" + mFPS);
            FloatWindowManager.setFrameNum(mFPS);
            if(mFPS < 50){
                LogMonitor.getInstance().print(mFPS);
            }
            LogMonitor.clean();
            mMarkTime = now;
            mFPS = 0;
        }
        mFPS++;
        if (isStart) {
            Choreographer.getInstance().postFrameCallback(instance);
        }
    }

    public static class LogMonitor {

        private static LogMonitor sInstance = new LogMonitor();
        private HandlerThread mLogThread = new HandlerThread("log");
        private Handler mIoHandler;
        private static final long TIME_BLOCK = 33L;
        private static StringBuffer sb = new StringBuffer();
        private static String output;
        private static boolean hasJunk;
        private static int mFPS;
        private LogMonitor() {
            mLogThread.start();
            mIoHandler = new Handler(mLogThread.getLooper());
        }

        private static Runnable mLogRunnable = new Runnable() {
            @Override
            public void run() {
                hasJunk = true;
                StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
                for (StackTraceElement s : stackTrace) {
                    sb.append(s.toString() + "\n");
                }
            }
        };
        private static Runnable mIORunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(FileManager.getInstance().checkLogDir(), "FPS:"+mFPS+"|"+formatDate(new Date()));
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    final PrintWriter printer = new PrintWriter(new FileOutputStream(file), true);
                    printer.append("FPS:"+mFPS+"|"+output);
                    printer.flush();
                    printer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss MM-dd");
        public static String formatDate(Date date) {
            return sDateFormat.format(date);
        }
        public static LogMonitor getInstance() {
            return sInstance;
        }

        public void startMonitor() {
            mIoHandler.postDelayed(mLogRunnable, TIME_BLOCK);
        }

        public void removeMonitor(long temTime) {
            if(hasJunk){
                sb.append("total time:"+temTime+"\n\n");
            }
            hasJunk = false;
            mIoHandler.removeCallbacks(mLogRunnable);
        }

        public static String getPrint() {
            return sb.toString();
        }

        public void print(int frameCount) {
            if(!sb.toString().isEmpty()){
                mFPS = frameCount;
                output = formatDate(new Date())+"\n"+sb.toString();
                mIoHandler.post(mIORunnable);
            }
        }

        public static void clean() {
            sb = new StringBuffer();
        }
    }

    public static class BlockDetectByPrinter {
        private static long startTime = 0;
        public static void start() {

            Looper.getMainLooper().setMessageLogging(new Printer() {

                private static final String START = ">>>>> Dispatching";
                private static final String END = "<<<<< Finished";

                @Override
                public void println(String x) {
                    if (x.startsWith(START)) {
                        startTime = System.currentTimeMillis();
                        LogMonitor.getInstance().startMonitor();
                    }
                    if (x.startsWith(END)) {
                        LogMonitor.getInstance().removeMonitor(System.currentTimeMillis() - startTime);
                    }
                }
            });

        }
    }
}
