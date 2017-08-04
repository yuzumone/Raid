package net.yuzumone.raid.viewholder

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import net.yuzumone.raid.databinding.ItemRaidBinding

class RaidViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val binding: ItemRaidBinding = DataBindingUtil.bind<ItemRaidBinding>(itemView)
}