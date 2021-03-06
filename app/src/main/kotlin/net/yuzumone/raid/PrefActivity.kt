package net.yuzumone.raid

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.firebase.messaging.FirebaseMessaging
import net.yuzumone.raid.databinding.FragmentPrefBinding
import net.yuzumone.raid.util.PrefUtil

class PrefActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) {
            val intent = Intent(context, PrefActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val fragment = PrefFragment()
        supportFragmentManager.beginTransaction().add(android.R.id.content, fragment).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class PrefFragment : Fragment() {

        lateinit private var binding: FragmentPrefBinding

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pref, container, false)
            val prefs = PrefUtil(activity)
            binding.morioka.title.text = getString(R.string.morioka)
            binding.morioka.checkbox.isChecked = prefs.isMoriokaNotification
            binding.takizawa.title.text = getString(R.string.takizawa)
            binding.takizawa.checkbox.isChecked = prefs.isTakizawaNotification
            binding.buttonSave.setOnClickListener {
                prefs.isMoriokaNotification = binding.morioka.checkbox.isChecked
                prefs.isTakizawaNotification = binding.takizawa.checkbox.isChecked
                if (binding.morioka.checkbox.isChecked) {
                    FirebaseMessaging.getInstance().subscribeToTopic("morioka")
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("morioka")
                }
                if (binding.takizawa.checkbox.isChecked) {
                    FirebaseMessaging.getInstance().subscribeToTopic("takizawa")
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("takizawa")
                }
                activity.finish()
            }
            return binding.root
        }
    }
}