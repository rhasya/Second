package com.example.second

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SendTextTask: AsyncTask<String, String, Long>() {

    companion object {
        private const val SLACK_URL = "https://hooks.slack.com/services/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"
    }

    override fun doInBackground(vararg params: String?): Long {
        // make json object
        val json = JSONObject().apply {
            put("text", params[0])
        }

        val url = URL(SLACK_URL)
        var conn: HttpURLConnection? = null
        try {
            conn = url.openConnection() as? HttpURLConnection
            conn?.run {
                connectTimeout = CONN_TIMEOUT * 1000
                readTimeout = READ_TIMEOUT * 1000
                requestMethod = "POST"
                setRequestProperty("Cache-Control", "no-cache")
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Accept", "application/json")
                doInput = true
                doOutput = true
                connect()

                outputStream?.let { stream ->
                    stream.write(json.toString().toByteArray())
                    stream.close()
                }

                BufferedReader(InputStreamReader(inputStream))?.let { reader ->
                    var line: String? = reader.readLine()
                    while (line != null) {
                        Log.d("SendTextTask", line)
                        line = reader.readLine()
                    }
                }
            }
        }
        finally {
            conn?.disconnect()
        }

        return 0
    }
}