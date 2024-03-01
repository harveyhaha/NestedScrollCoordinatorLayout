package com.harveyhaha.nestedscrollcoordinatorlayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class RecyclerAdapter(var data: List<String>) :
    RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        return RecyclerHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_recycler_view_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.nameTv.text = data[position]
    }

    class RecyclerHolder(itemView: View) : ViewHolder(itemView) {
        var nameTv: TextView

        init {
            nameTv = itemView.findViewById(R.id.name_tv)
        }
    }
}
