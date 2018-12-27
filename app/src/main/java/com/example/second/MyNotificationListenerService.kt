package com.example.second

import android.app.Notification
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider

private const val TAG = "MyNotificationService"

/**
 * NotificationListenerService
 */
class MyNotificationListenerService : NotificationListenerService() {

    /**
     * 생성
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")

        // TODO Anonymous Login to Firebase
        // Login Check
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInAnonymously().addOnCompleteListener {task ->
                if (task.isSuccessful)
                    Log.d(TAG, "Sign In Success")
                else
                    Log.d(TAG, "Sign In Fail")
            }
        }
    }

    /**
     * 제거
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }

    /**
     * 알림 수신
     */
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        // 패키지 이름
        val packageName = sbn?.packageName

        // 특정 패키지의 특정 id는 제외
        if (packageName.equals("com.kakao.talk") && sbn?.id == 1)
            return
        if (packageName.equals("com.sds.mysinglesquare") && sbn?.id == 1)
            return
        // 시스템UI 제외
        if (packageName.equals("com.android.systemui"))
            return

        // 필요한 정보를 얻는다.
        // Get AppName
        val pm = applicationContext.packageManager
        val ai = pm.getApplicationInfo(packageName, 0)
        val appName = pm.getApplicationLabel(ai).toString()

        val title = sbn?.notification?.extras?.getCharSequence(Notification.EXTRA_TITLE).toString()
        // val text = sbn?.notification?.extras?.getCharSequence(Notification.EXTRA_TEXT).toString()

        Log.d(TAG, "onNotificationPosted() [$appName,$packageName,$title]")

        // broadcast!
        val localIntent = Intent(MY_BROADCAST_ACTION).apply {
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(EXTRA_APP_NAME, appName)
            putExtra(EXTRA_POST_TIME, sbn?.postTime)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent)

        // Save to Firebase
        val db = FirebaseFirestore.getInstance()
        db.collection("noti").add(NotiData(appName, title, sbn?.postTime ?: System.currentTimeMillis()))
    }
}