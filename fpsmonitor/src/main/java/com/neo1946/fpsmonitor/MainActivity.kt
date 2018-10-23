package com.neo1946.fpsmonitor

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private val ISDETAIL = "isdetail"
    private val DATA = "data"
    private var isDetail: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isDetail = intent.getBooleanExtra(ISDETAIL,false)
        setContentView(R.layout.activity_detail)
        button.setText("delete")
        if(isDetail){
            var data = intent.getStringExtra(DATA)
            var packageName = this.getPackageName()
            lv_main.visibility = View.GONE
            tv_main.visibility = View.VISIBLE
            button.visibility = View.GONE
            val result = SpannableStringBuilder()
            result.append(data)
            var index = 0
            while (true) {
                index = data.indexOf(packageName, index)
                if (index >= 0) {
                    result.setSpan(object : CustomClickableSpan(Color.BLUE,Color.WHITE,false) {
                        override fun onClick(widget: View) {
                        }
                    }, index, index + packageName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    index++
                } else {
                    break
                }
            }
            tv_main.text = result
        }else{
            tv_main.visibility = View.GONE
            lv_main.visibility = View.VISIBLE
            GetFileTask().execute()
            button.setOnClickListener(View.OnClickListener {
                FileManager.getInstance().clean()
                GetFileTask().execute()
            })
        }
    }

    override fun finish() {
        super.finish()
//        FPSMonitor.start(null)
    }

    inner class Info(internal var title: String, internal var info: String)

    @Throws(IOException::class)
    fun readFile(file: File): String {
        val res = ""
        try {
            val fin = openFileInput(file.name)
            val length = fin.available()
            val buffer = ByteArray(length)
            fin.read(buffer)
            fin.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return res
    }

    inner class GetFileTask : AsyncTask<Void, Int, List<Info>>() {

        override fun onPreExecute() {}

        override fun doInBackground(vararg params: Void): List<Info> {
            val files = FileManager.getInstance().allFile
            val infos = ArrayList<Info>()
            for (file in files!!) {
                try {
                    var input: InputStream = FileInputStream(file)
                    //根据文件大小来创建字节数组
                    var bytes = ByteArray(file.length().toInt())
                    input.read(bytes)//返回读取字节的长度
                    infos.add(Info(file.name, String(bytes, 0, bytes.size)))
                    input.close();
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return infos
        }

        override fun onPostExecute(infos: List<Info>) {
            val strings = ArrayList<String>()
            for (info in infos) {
                strings.add(info.title)
            }
            val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_expandable_list_item_1, strings)
            lv_main.adapter = adapter
            lv_main.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val temIntent = Intent(this@MainActivity, MainActivity::class.java)
                temIntent.putExtra(DATA,infos.get(position).info)
                temIntent.putExtra(ISDETAIL,true)
                this@MainActivity.startActivity(temIntent)
            }
        }
    }

}
