package net.yuzumone.raid.fragment

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import net.yuzumone.raid.R
import net.yuzumone.raid.databinding.FragmentRaidListBinding
import net.yuzumone.raid.model.Raid
import net.yuzumone.raid.viewholder.RaidViewHolder


abstract class RaidListFragment : Fragment() {

    lateinit private var binding:  FragmentRaidListBinding
    lateinit private var database: DatabaseReference
    lateinit private var adapter: RaidAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_raid_list, container, false)
        binding.progress.visibility = View.VISIBLE
        database = FirebaseDatabase.getInstance().reference
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val query = getQuery(database)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                binding.progress.visibility = View.GONE
            }

            override fun onCancelled(e: DatabaseError?) {
            }
        })
        adapter = RaidAdapter(activity ,query)
        binding.listRaid.layoutManager = LinearLayoutManager(activity)
        binding.listRaid.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.cleanup()
    }

    class RaidAdapter(val context: Context, query: Query) : FirebaseRecyclerAdapter<Raid, RaidViewHolder>
    (Raid::class.java, R.layout.item_raid, RaidViewHolder::class.java, query) {
        override fun populateViewHolder(viewHolder: RaidViewHolder?, model: Raid?, position: Int) {
            val binding = viewHolder!!.binding
            if (model != null) {
                binding.raid = model
                binding.container.setOnClickListener {
                    val key = getRef(position).key
                    val fragment = UpdateDialogFragment.newInstance(key)
                    val manager = (context as AppCompatActivity).supportFragmentManager
                    fragment.show(manager, "update")
                }
                binding.container.setOnLongClickListener {
                    if (model.lat != "" && model.lng != "") {
                        val place = model.place
                        val lat = model.lat
                        val lng = model.lng
                        val uri = Uri.parse("geo:0,0?q=$lat,$lng($place)")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.`package` = "com.google.android.apps.maps"
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, context.getString(R.string.no_location),
                                Toast.LENGTH_SHORT).show()
                    }
                    true
                }
            }
            binding.executePendingBindings()
        }
    }

    abstract fun getQuery(reference: DatabaseReference): Query
}