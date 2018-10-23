package com.neo1946.fpsmonitor;

import android.app.Application;
import android.util.Log;

import java.io.File;

/**
 * @auther ouyangzhaoxian
 * @data 2018/10/22
 */

public class FileManager {
    private static final String TAG = "FileManager";
    private static final String PARENT = "FPSMonitor";
    private static final String DIR_LOG = "kayzing";
    private static final String DIR_FLOW = "flow";
    private static final int TYPE_DIR_INTERNAL_LOG = 0;
    private static final int TYPE_DIR_ABORT = -1;
    private Application mApp;
    private static FileManager sInstance = new FileManager();

    public static FileManager getInstance() {
        return sInstance;
    }

    public void init(Application app) {
        mApp = app;
    }

    public File checkLogDir() {
        return checkDir(new File(getExternalLogDir()), TYPE_DIR_INTERNAL_LOG);
    }


    private String getExternalLogDir() {
        return new StringBuilder().append(mApp.getExternalCacheDir()).append(File.separator).append(PARENT).append(File.separator).append(DIR_LOG).toString();
    }

    private String getInternalLogDir() {
        return new StringBuilder().append(mApp.getCacheDir()).append(File.separator).append(PARENT).append(File.separator).append(DIR_LOG).toString();
    }

    private String getInternalFlowDir() {
        return new StringBuilder().append(mApp.getCacheDir()).append(File.separator).append(PARENT).append(File.separator).append(DIR_FLOW).toString();
    }

    private File checkDir(File file, int alter) {
        if (!file.exists()) {
            if (file.mkdirs()) {
                Log.i(TAG, "FrameMonitor log path=" + file.getAbsolutePath());
                return file;
            } else {
                String dir = getDir(alter);
                if (dir == null) {
                    return null;
                }
                return checkDir(new File(getDir(alter)), TYPE_DIR_ABORT);
            }
        } else {
            Log.i(TAG, "FrameMonitor log path=" + file.getAbsolutePath());
            return file;
        }
    }

    public void clean() {
        if (checkLogDir().exists()) {
            deleteDir(checkLogDir());
        }
    }

    public File[] getAllFile(){
        File file = checkLogDir();
        if (file.exists()) {
            return file.listFiles();
        }else{
            return null;
        }
    }

    public File findFile(String fileName) {
        File file = checkLogDir();
        return findFileInner(file,fileName);
    }

    private File findFileInner(File file,String fileName) {
        if (!file.exists()) {
            return null;
        } else {
            for(File tem:file.listFiles()){
                if(tem.isFile()){
                    if(tem.getName().equals(fileName)){
                        return tem;
                    }else{
                        continue;
                    }
                }else{
                    File temfile = findFileInner(tem,fileName);
                    if(temfile == null){
                        continue;
                    }else{
                        return temfile;
                    }
                }
            }
        }
        return null;
    }

    private void deleteDir(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteDir(f);
            }
            file.delete();
        }
    }

    private String getDir(int type) {
        String dir = null;
        switch (type) {
            case TYPE_DIR_INTERNAL_LOG:
                dir = getInternalLogDir();
                break;
            case TYPE_DIR_ABORT:
                dir = null;
            default:
                dir = getInternalFlowDir();
                break;
        }
        return dir;
    }
}