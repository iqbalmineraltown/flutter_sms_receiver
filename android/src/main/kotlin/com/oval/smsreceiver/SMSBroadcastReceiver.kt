package com.oval.smsreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class SMSBroadcastReceiver: BroadcastReceiver() {

    private var listener: Listener? = null

    fun injectListener(listener: Listener?) {
        this.listener = listener
    }

    override fun onReceive(context: Context, intent: Intent) {
        print("HEYYYYY"+intent.toString())

        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras.get(SmsRetriever.EXTRA_STATUS) as Status
            print("HEYYYYY"+status.statusCode.toString())

            when (status.statusCode) {

                CommonStatusCodes.SUCCESS -> {

                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String

                    val pattern = Pattern.compile("\\d{6}")
                    val matcher = pattern.matcher(message)

                    if (matcher.find())
                        listener?.onSMSReceived(matcher.group(0))
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