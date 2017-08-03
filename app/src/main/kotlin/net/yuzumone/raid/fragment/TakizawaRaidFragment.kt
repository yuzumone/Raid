package net.yuzumone.raid.fragment

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query

class TakizawaRaidFragment : RaidListFragment() {

    override fun getQuery(reference: DatabaseReference): Query {
        return reference.child("raids").orderByChild("address").equalTo("takizawa")
    }
}