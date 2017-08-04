package net.yuzumone.raid

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import net.yuzumone.raid.databinding.ActivityMainBinding
import net.yuzumone.raid.fragment.MoriokaRaidFragment
import net.yuzumone.raid.fragment.TakizawaRaidFragment

class MainActivity : AppCompatActivity() {

    lateinit private var binding: ActivityMainBinding
    lateinit private var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        analytics = FirebaseAnalytics.getInstance(this)
        if (savedInstanceState == null) {
            if (isDeviceOnline()) {
                val adapter = ViewPagerAdapter(supportFragmentManager)
                binding.pager.adapter = adapter
                binding.tab.setupWithViewPager(binding.pager)
                val morioka = MoriokaRaidFragment()
                adapter.add(getString(R.string.morioka), morioka)
                val takizawa = TakizawaRaidFragment()
                adapter.add(getString(R.string.takizawa), takizawa)
            } else {
                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isDeviceOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragments = ArrayList<Fragment>()
        private val titles = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }

        fun add(title: String, fragment: Fragment) {
            fragments.add(fragment)
            titles.add(title)
            notifyDataSetChanged()
        }
    }
}
