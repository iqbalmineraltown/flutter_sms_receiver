package com.oval.smsreceiverexample

import android.os.Bundle
import android.util.Log
import com.oval.smsreceiver.AppSignatureHelper

import io.flutter.app.FlutterActivity
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)
    val appSig = AppSignatureHelper(this)
    val appSignatures = appSig.getAppSignatures()
    for( sig in appSignatures){
      Log.d("Signature", sig)
    }
  }
}
