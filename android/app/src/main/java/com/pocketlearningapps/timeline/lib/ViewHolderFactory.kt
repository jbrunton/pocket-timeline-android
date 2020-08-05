package com.pocketlearningapps.timeline.lib

import android.view.View

interface ViewHolderFactory<D, H : androidx.recyclerview.widget.RecyclerView.ViewHolder> {
    fun createViewHolder(view: View): H
    fun bindHolder(holder: H, item: D, position: Int)
}
