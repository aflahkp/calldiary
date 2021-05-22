package com.muod.calldiary.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.muod.calldiary.activity.AddNotes

class CallHangupReceiver : BroadcastReceiver() {

    lateinit var context:Context

    override fun onReceive(context: Context, intent: Intent) {
        this.context=context

        val state = intent.extras[TelephonyManager.EXTRA_STATE]
        val incoming_number = intent.extras[TelephonyManager.EXTRA_INCOMING_NUMBER] as String

        when (state) {
            TelephonyManager.EXTRA_STATE_RINGING -> {

            }
            TelephonyManager.EXTRA_STATE_OFFHOOK -> {

            }
            TelephonyManager.EXTRA_STATE_IDLE -> {
                val intent = Intent(context,AddNotes::class.java)
                intent.putExtra("number",incoming_number);
                intent.putExtra("do","add")
                context.startActivity(intent)
            }
        }
    }

}
