package com.harveyhaha.nestedscrollcoordinatorlayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = RecyclerAdapter(data)
        recyclerView.adapter = adapter
        return view
    }

    private var i = 0
    private val data: List<String>
        get() {
            val data: MutableList<String> = ArrayList()
            while (i < 60) {
                data.add("ChildView item $i")
                i++
            }
            return data
        }
}
