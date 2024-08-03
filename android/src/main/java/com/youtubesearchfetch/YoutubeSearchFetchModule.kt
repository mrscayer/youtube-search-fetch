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
com.facebook.react.bridge.ReadableMap

class YoutubeSearchFetchModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "YoutubeSearchFetch"
    }
    
    fun fetchData(url: String, headers: ReadableMap, body: String, promise: Promise) {
        Thread {
            try {
                val urlObj = URL(url)
                val conn = urlObj.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"

                // Set headers
                // headers parametresi artık ReadableMap türünde.
                // ReadableMap kullanılarak JavaScript'ten gelen verileri işliyoruz.
                headers.keySetIterator().forEachRemaining { key ->
                    conn.setRequestProperty(key, headers.getString(key))
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
