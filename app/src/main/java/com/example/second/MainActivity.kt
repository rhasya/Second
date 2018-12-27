package com.example.second

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewAdapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    private val myDataset = ArrayList<NotiData>()

    private lateinit var myBroadcastManger: MyBroadcastReceiver

    /**
     * Main Activity 생성
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(myDataset)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // Register LocalBroadcastReceiver
        val myIntentFilter = IntentFilter(MY_BROADCAST_ACTION)
        myBroadcastManger = MyBroadcastReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcastManger, myIntentFilter)
    }

    /**
     * Main Activity 제거
     */
    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadcastManger)

        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_pre -> {
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * 자료 수신 처리!
     */
    private fun receive(intent: Intent?) {
        val appName = intent?.getCharSequenceExtra(EXTRA_APP_NAME).toString()
        val title = intent?.getCharSequenceExtra(Intent.EXTRA_TITLE).toString()
        val postTime = intent?.getLongExtra(EXTRA_POST_TIME, 0) ?: System.currentTimeMillis()

        // add to recycler View
        myDataset.add(NotiData(appName, title, postTime))
        recyclerView.adapter?.notifyItemInserted(0)

        // scroll to top
        recyclerView.smoothScrollToPosition(0)
    }

    /**
     * Broadcast Receiver클래스
     */
    private inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            this@MainActivity.receive(intent)
        }
    }
}
