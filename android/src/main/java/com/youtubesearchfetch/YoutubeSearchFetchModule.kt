package com.youtubesearchfetch

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class YoutubeSearchFetchModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return NAME
    }

    @ReactMethod
    fun fetchData(url: String, headers: Map<String, String>, body: String, promise: Promise) {
        Thread {
            try {
                val urlObj = URL(url)
                val conn = urlObj.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"

                // Set headers
                headers.forEach { (key, value) ->
                    conn.setRequestProperty(key, value)
                }

                // Set body
                conn.doOutput = true
                val writer = OutputStreamWriter(conn.outputStream)
                writer.write(body)
                writer.flush()
                writer.close()

                val responseCode = conn.responseCode
                val response = StringBuilder()
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()

                promise.resolve(response.toString())
            } catch (e: Exception) {
                promise.reject("ERROR", e)
            }
        }.start()
    }
}
