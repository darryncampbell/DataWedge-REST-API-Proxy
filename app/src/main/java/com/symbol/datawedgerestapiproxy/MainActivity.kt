package com.symbol.datawedgerestapiproxy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    //  todo
    //  Create service which listens for DataWedge Intents
    //  QUESTION: How are they using DW at the moment??
    //            The familiar problem on Oreo devices that you can't receive broadcast intents in the background
    //            Maybe this would require a change to DataWedge - or another output plugin.
    //            Could forward the barcode on to another endpoint but that is a bit of a fudge.
    //  Service is foreground on O+ or just background before O
    //  There will need to be some processing of the barcode to determine WHICH endpoint to call
    //  Service makes REST API call
    //  Configuration: REST API endpoint.  How to configure?
    //  Add additional information to REST API call
    //  Remove main activity and remove the app from the launcher
    //  ReadMe including DataWedge configuration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
