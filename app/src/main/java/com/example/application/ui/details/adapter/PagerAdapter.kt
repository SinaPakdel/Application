package com.example.application.ui.details.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.lifecycle.Lifecycle
import com.example.application.ui.details.fragments.IngredientFragment
import com.example.application.ui.details.fragments.InstructionsFragment
import com.example.application.ui.details.fragments.OverviewFragment

class PagerAdapter(
    private val resultBundle: Bundle,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment().apply { arguments = resultBundle }
            1 -> IngredientFragment().apply { arguments = resultBundle }
            2 -> InstructionsFragment().apply { arguments = resultBundle }
            else -> OverviewFragment().apply { arguments = resultBundle }
        }
    }
}