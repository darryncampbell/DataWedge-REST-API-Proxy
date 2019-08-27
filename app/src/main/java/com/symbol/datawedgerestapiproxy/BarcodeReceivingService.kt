package com.symbol.datawedgerestapiproxy

import android.content.Intent
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import androidx.core.app.NotificationCompat
import android.R
import android.app.*
import android.widget.RemoteViews
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.app.NotificationManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.core.content.ContextCompat


private const val ACTION_BARCODE = "com.zebra.dwrestproxy.ACTION"
private const val EXTRA_BARCODE = "com.symbol.datawedge.data_string"
private const val EXTRA_SYMBOLOGY = "com.symbol.datawedge.label_type"
private const val FOREGROUND_CHANNEL_ID = "REST API"
private const val NOTIFICATION_ID_FOREGROUND_SERVICE = 100
private const val URL = "http://www.google.co.uk"

class BarcodeReceivingService : IntentService("BarcodeReceivingService") {

    private val TAG = "DWRESTProxy"

    override fun onHandleIntent(intent: Intent?) {
        //  In practice the REST API call should be too quick to see the notification
        startForeground(NOTIFICATION_ID_FOREGROUND_SERVICE, prepareNotification())
        //Thread.sleep(10000)
        when (intent?.action) {
            ACTION_BARCODE -> {
                val barcodeData = intent.getStringExtra(EXTRA_BARCODE)
                val symbology = intent.getStringExtra(EXTRA_SYMBOLOGY)
                makeRestfulCall(barcodeData, symbology)
            }
        }
    }

    private fun prepareNotification(): Notification {
        // handle build version above android oreo
        val mNotificationManager: NotificationManager
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mNotificationManager.getNotificationChannel(
                FOREGROUND_CHANNEL_ID
            ) == null
        ) {
            val name = "DW REST API"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(FOREGROUND_CHANNEL_ID, name, importance)
            channel.enableVibration(false)
            mNotificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // notification builder
        val notificationBuilder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = NotificationCompat.Builder(this, FOREGROUND_CHANNEL_ID)
        } else {
            notificationBuilder = NotificationCompat.Builder(this)
        }
        notificationBuilder
            //.setSmallIcon(R.mipmap.)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(PRIORITY_MIN)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        }

        return notificationBuilder.build()
    }

    private fun makeRestfulCall(barcodeData: String?, symbology: String?) {
        val payload = JSONObject()
        payload.put("data", barcodeData)
        payload.put("symbology", symbology)

        val volleyRequestQueue = Volley.newRequestQueue(this)
        val volleyJsonRequest = JsonObjectRequest(Request.Method.GET, URL, payload, Response.Listener { response ->
            val str = response.toString()
            Log.d(TAG,"(Success) Response from server: $str")
        }, Response.ErrorListener {
                error ->
            Log.w(TAG,"(Error) Response from server: ${error.message}")
        })
        volleyRequestQueue.add(volleyJsonRequest)
    }


}
