package net.yuzumone.raid.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import java.util.*

class TimePickerDialogFragment : DialogFragment() {

    lateinit private var listener: TimePickerDialog.OnTimeSetListener

    companion object {
        fun newInstance(fragment: Fragment) : TimePickerDialogFragment {
            return TimePickerDialogFragment().apply {
                setTargetFragment(fragment, 0)
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            listener = targetFragment as TimePickerDialog.OnTimeSetListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Don't implement Listener.")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(activity, listener, hour, minute, true)
        return timePickerDialog
    }
}