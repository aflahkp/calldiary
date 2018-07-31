package softfruit.solutions.calldiary.listener

import android.telephony.TelephonyManager
import android.telephony.PhoneStateListener
import android.util.Log


public class PhoneStateChangeListener(showAction:ShowAction) : PhoneStateListener() {
    var showAction = showAction
    override fun onCallStateChanged(state: Int, incomingNumber: String) {
        Log.e("call diary", state.toString())
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {


            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {


            }
            TelephonyManager.CALL_STATE_IDLE -> {
                showAction.showToast("Call End, Now show the popup")
            }
        }
    }

    companion object {
        var wasRinging: Boolean = false
    }
}