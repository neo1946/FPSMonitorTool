package com.neo1946.test1022;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.neo1946.fpsmonitor.FPSMonitor;
import com.neo1946.fpsmonitor.FileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    public static  boolean isTest = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textview);
        tv.setText("1");
        final ImageView imageView = findViewById(R.id.imageview);
        ValueAnimator mValueAnimator;
        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setDuration(4000);
        mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isTest) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                imageView.setImageAlpha((int) (255 * animation.getAnimatedFraction()));
            }
        });
        mValueAnimator.start();
    }
    public void show(android.view.View view){
        FPSMonitor.start(getApplication());
    }
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                Thread.sleep(random);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    };
    public void start(android.view.View view){
        if(isTest){
            isTest = false;
        }else{
            isTest = true;
        }
//        try {
//            Thread.sleep(random);
//            tv.post(runnable);
//            tv.post(runnable);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Intent intent = new Intent(this,SecondActivity.class);
//        startActivity(intent);
    }
//    private  class Info{
//        String title;
//        String info;
//
//        public Info(String title, String info) {
//            this.title = title;
//            this.info = info;
//        }
//    }
//    public String readFile(File file) throws IOException {
//        String res="";
//        try{
//            FileInputStream fin = openFileInput(file.getName());
//            int length = fin.available();
//            byte [] buffer = new byte[length];
//            fin.read(buffer);
//            fin.close();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return res;
//    }
//    public class DownloadTask extends AsyncTask<Void, Integer, List<Info>> {
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected  List<Info> doInBackground(Void... params) {
//            File[] files = FileManager.getInstance().getAllFile();
//            List<Info> infos = new ArrayList<>();
//            for(File file:files){
//                try{
//                    FileInputStream fin = openFileInput(file.getName());
//                    int length = fin.available();
//                    byte [] buffer = new byte[length];
//                    fin.read(buffer);
//                    infos.add(new Info(file.getName(),new String(buffer, 0, buffer.length)));
//                    fin.close();
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//            return infos;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//        }
//
//        @Override
//        protected void onPostExecute(List<Info> infos) {
//            List<String> strings = new ArrayList<>();
//            for(Info info: infos){
//                strings.add(new String(info.title));
//            }
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,strings);
//            setListAdapter(adapter);
//        }
//    }

}
