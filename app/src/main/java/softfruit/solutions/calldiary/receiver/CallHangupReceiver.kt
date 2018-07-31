package softfruit.solutions.calldiary.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import softfruit.solutions.calldiary.activity.AddNotes
import softfruit.solutions.calldiary.listener.PhoneStateChangeListener
import softfruit.solutions.calldiary.listener.ShowAction
import android.R.id.content

class CallHangupReceiver : BroadcastReceiver(),ShowAction {

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
                context.startActivity(intent)
            }
        }
    }

    override fun showToast(msg: String) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }


}
