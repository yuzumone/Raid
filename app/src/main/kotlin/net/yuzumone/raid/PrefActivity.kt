package net.yuzumone.raid

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val fragment = PrefFragment()
        supportFragmentManager.beginTransaction().add(android.R.id.content, fragment).commit()
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
                activity.finish()
                // TODO
            }
            return binding.root
        }
    }
}