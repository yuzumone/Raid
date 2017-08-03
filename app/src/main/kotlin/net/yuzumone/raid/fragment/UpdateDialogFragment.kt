package net.yuzumone.raid.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import com.google.firebase.database.*
import net.yuzumone.raid.R
import net.yuzumone.raid.databinding.FragmentUpdateDialogBinding
import net.yuzumone.raid.model.Raid

class UpdateDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    lateinit private var binding: FragmentUpdateDialogBinding
    lateinit private var reference: DatabaseReference
    val key: String by lazy {
        arguments.getString(ARG_RAID_KEY)
    }

    companion object {
        val ARG_RAID_KEY = "key"
        fun newInstance(key: String) : UpdateDialogFragment {
            return UpdateDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_RAID_KEY, key)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.fragment_update_dialog, null, false)
        binding.progress.visibility = View.VISIBLE
        reference = FirebaseDatabase.getInstance().reference.child("raids").child(key)
        val builder = AlertDialog.Builder(activity).apply {
            setView(binding.root)
            setTitle(" ")
            setPositiveButton(getString(R.string.ok), { _, _ ->
                if (binding.editPokemon.text.isBlank() || binding.textTime.text == "00:00") {
                    Toast.makeText(activity, getString(R.string.fragment_update_blank_error),
                            Toast.LENGTH_SHORT).show()
                } else {
                    onUpdate(reference)
                }
            })
            setNegativeButton(getString(R.string.close), { _, _ -> dismiss()})
        }
        return builder.create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                binding.progress.visibility = View.GONE
                if (snapshot != null) {
                    val raid = snapshot.getValue(Raid::class.java)
                    dialog.setTitle(raid?.place ?: "")
                    binding.raid = raid
                    binding.textTime.setOnClickListener {
                        val fragment = TimePickerDialogFragment.newInstance(this@UpdateDialogFragment)
                        fragment.show(fragmentManager, "time_picker")
                    }
                }
            }

            override fun onCancelled(e: DatabaseError?) {
            }
        })
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        val h = if (hour < 10) "0" + hour.toString() else hour.toString()
        val m = if (minute < 10) "0" + minute.toString() else minute.toString()
        binding.textTime.text = "$h:$m"
    }

    private fun onUpdate(reference: DatabaseReference) {
        reference.runTransaction(object : Transaction.Handler {
            override fun doTransaction(data: MutableData?): Transaction.Result {
                val raid = data?.getValue(Raid::class.java) ?: return Transaction.success(data)
                raid.pokemon = binding.editPokemon.text.toString()
                raid.time = binding.textTime.text.toString()
                raid.player = binding.editPlayer.text.toString().toInt()
                data.value = raid
                return Transaction.success(data)
            }

            override fun onComplete(e: DatabaseError?, b: Boolean, snapshot: DataSnapshot?) {

            }
        })
    }
}