package com.example.application.ui.details.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.lifecycle.Lifecycle
import com.example.application.ui.details.fragments.IngredientFragment
import com.example.application.ui.details.fragments.InstructionsFragment
import com.example.application.ui.details.fragments.OverviewFragment

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment()
            1 -> IngredientFragment()
            2 -> InstructionsFragment()
            else -> OverviewFragment()
        }
    }
}