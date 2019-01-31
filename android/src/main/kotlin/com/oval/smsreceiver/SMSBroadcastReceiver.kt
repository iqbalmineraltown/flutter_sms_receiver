package com.oval.smsreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SMSBroadcastReceiver: BroadcastReceiver() {

    private var listener: Listener? = null

    fun injectListener(listener: Listener?) {
        this.listener = listener
    }

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
            val extras = intent.extras
            val status = extras.get(SmsRetriever.EXTRA_STATUS) as Status
            Log.d("FLUTTER-SMS-RECEIVER", status.toString())
            when (status.statusCode) {

                CommonStatusCodes.SUCCESS -> {

                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    listener?.onSMSReceived(message)
                }
                CommonStatusCodes.TIMEOUT -> listener?.onTimeout()
            }
        }
    }

    interface Listener {
        fun onSMSReceived(otp: String)
        fun onTimeout()
    }
}