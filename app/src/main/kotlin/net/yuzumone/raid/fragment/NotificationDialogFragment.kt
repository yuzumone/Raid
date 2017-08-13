package net.yuzumone.raid.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import net.yuzumone.raid.BuildConfig
import net.yuzumone.raid.R
import org.json.JSONException
import org.json.JSONObject

class NotificationDialogFragment : DialogFragment() {

    val place: String by lazy {
        arguments.getString(ARG_PLACE)
    }
    val pokemon: String by lazy {
        arguments.getString(ARG_POKEMON)
    }
    val address: String by lazy {
        arguments.getString(ARG_ADDRESS)
    }

    companion object {
        val ARG_PLACE = "place"
        val ARG_POKEMON = "pokemon"
        val ARG_ADDRESS = "address"
        fun newInstance(place: String, pokemon: String, address: String): NotificationDialogFragment {
            return NotificationDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PLACE, place)
                    putString(ARG_POKEMON, pokemon)
                    putString(ARG_ADDRESS, address)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity).apply {
            setTitle(getString(R.string.fragment_notification_dialog_title))
            setPositiveButton(getString(R.string.ok), { _, _ -> sendNotification()})
            setNegativeButton(getString(R.string.close), { _, _ -> dismiss()})
        }
        return builder.create()
    }

    private fun sendNotification() {
        val key = "key=" + BuildConfig.KEY
        val json = JSONObject()
        try {
            val notification = JSONObject()
            val title = getString(R.string.raid_information)
            notification.put("title", title)
            val body = getString(R.string.notification_body, place, pokemon)
            notification.put("body", body)
            notification.put("sound", "default")
            json.put("to", "/topics/$address")
            json.put("notification", notification)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        AndroidNetworking.post("https://fcm.googleapis.com/fcm/send")
                .setContentType("application/json")
                .addHeaders("Authorization", key)
                .addJSONObjectBody(json)
                .build()
                .getAsJSONObject(object: JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d("Notification", response.toString())
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("Notification", anError.toString())
                    }
                })
    }
}