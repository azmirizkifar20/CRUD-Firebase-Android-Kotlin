package org.d3if4055.belajarfirebase.recyclerview

import android.view.View
import org.d3if4055.belajarfirebase.data.Author

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view: View, author: Author)
}