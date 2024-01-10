package com.oval.sms_receiver

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.auth.api.phone.SmsRetriever
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class SmsReceiverPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {

  private lateinit var channel : MethodChannel
  private lateinit var activity: Activity

  companion object {
    const val TAG: String = "FLUTTER-SMS-RECEIVER"
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "com.oval.sms_receiver")
    channel.setMethodCallHandler(this)
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
  }

  private val smsBroadcastReceiver by lazy { SMSBroadcastReceiver() }
  private var isListening: Boolean = false


  override fun onMethodCall(call: MethodCall, result: Result) {
    Log.d(TAG, "calling ${call.method}")
    when(call.method) {
      "getPlatformVersion" -> {
        result.success("Android ${android.os.Build.VERSION.RELEASE}")
      }
      "startListening" -> {
        synchronized(this){
          if(!isListening) {
            isListening = true
            startListening()
            Log.d(TAG, "SMS Receiver Registered")
            result.success(true)
          }
          else{
            Log.d(TAG, "Preventing register multiple SMS receiver")
            result.success(false)
          }
        }
      }
      "stopListening" -> {
        if(isListening) {
          Log.d(TAG, "SMS Receiver listener stopped manually")
          stopListening()
          result.success(false)
        }
      }
      else -> {
        result.notImplemented()
      }
    }
  }

  @SuppressLint("UnspecifiedRegisterReceiverFlag")
  private fun startListening() {
    val client = SmsRetriever.getClient(activity)
    val retriever = client.startSmsRetriever()
    retriever.addOnSuccessListener {
      val listener = object:SMSBroadcastReceiver.Listener {
        override fun onSMSReceived(message: String) {
          channel.invokeMethod("onSmsReceived", message)
          stopListening()
        }
        override fun onTimeout() {
          channel.invokeMethod("onTimeout", null)
          stopListening()
        }
      }
      smsBroadcastReceiver.injectListener(listener)
      if (Build.VERSION.SDK_INT >= 33) {
        activity.registerReceiver(smsBroadcastReceiver,
          IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION),
          Context.RECEIVER_EXPORTED)
      } else {
        activity.registerReceiver(smsBroadcastReceiver,
          IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
      }
    }
    retriever.addOnFailureListener {
      channel.invokeMethod("onFailureListener", null)
      stopListening()
    }
  }

  private fun stopListening() {
    try {
      activity.unregisterReceiver(smsBroadcastReceiver)
    } catch (e: Exception) {
      // Ignored
    } finally {
      isListening = false
    }
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivity() {
  }
}