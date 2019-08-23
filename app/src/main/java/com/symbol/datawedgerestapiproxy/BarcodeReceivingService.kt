package com.symbol.datawedgerestapiproxy

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


private const val ACTION_BARCODE = "com.zebra.dwrestproxy.ACTION"

private const val EXTRA_BARCODE = "com.symbol.datawedge.data_string"
private const val EXTRA_SYMBOLOGY = "com.symbol.datawedge.label_type"

class BarcodeReceivingService : IntentService("BarcodeReceivingService") {

    private val TAG = "DWRESTProxy"

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_BARCODE -> {
                val barcodeData = intent.getStringExtra(EXTRA_BARCODE)
                val symbology = intent.getStringExtra(EXTRA_SYMBOLOGY)
                makeRestfulCall(barcodeData, symbology)
            }
        }
    }

    private fun makeRestfulCall(barcodeData: String?, symbology: String?) {
        //  todo
        val url = "http://www.google.co.uk"
        val ahOBJ = JSONObject()
        ahOBJ.put("dd", 2)

        Log.d(TAG,"kotJson")
        val queu = Volley.newRequestQueue(this)
        val ahReq = JsonObjectRequest(Request.Method.GET, url, ahOBJ, Response.Listener { response ->
            val str = response.toString()
            Log.d(TAG,"response: $str")
        }, Response.ErrorListener {
                error ->
            Log.d(TAG,"response: ${error.message}")
        })
        queu.add(ahReq)
    }


}
