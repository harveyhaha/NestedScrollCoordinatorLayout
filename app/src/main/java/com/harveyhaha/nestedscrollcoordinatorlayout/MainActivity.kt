package com.harveyhaha.nestedscrollcoordinatorlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var tabLayout: TabLayout
    private lateinit var viewpager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appBarLayout = findViewById(R.id.appbar_layout)
        tabLayout = findViewById(R.id.tab_layout)
        viewpager = findViewById(R.id.view_page)
        val viewPagerAdapter = MainViewPagerAdapter(this, getPageFragments())
        for (tabName in getPagerName()) {
            tabLayout.addTab(tabLayout.newTab().setText(tabName))
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }

        })
        viewpager.adapter = viewPagerAdapter
        viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.setScrollPosition(position, 0f, true)
            }
        })
    }

    private fun getPageFragments(): List<Fragment> {
        val data: MutableList<Fragment> = ArrayList()
        data.add(RecyclerViewFragment())
        data.add(RecyclerViewFragment())
        data.add(RecyclerViewFragment())
        return data
    }

    private fun getPagerName(): List<String> {
        val data: MutableList<String> = ArrayList()
        data.add("热门")
        data.add("推荐")
        data.add("最新")
        return data
    }
}