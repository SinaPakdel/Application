package com.example.application.ui.main.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.application.databinding.RecipesBottomSheetBinding
import com.example.application.ui.main.viewmodels.RecipesViewModel
import com.example.application.utils.consts.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.application.utils.consts.Constants.Companion.DEFAULT_DIET_TYPE_POSITION
import com.example.application.utils.consts.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.application.utils.consts.Constants.Companion.DEFAULT_MEAL_TYPE_POSITION
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class RecipesBottomSheet : BottomSheetDialogFragment() {
    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val recipesViewModel: RecipesViewModel by viewModels()
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = DEFAULT_MEAL_TYPE_POSITION
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = DEFAULT_DIET_TYPE_POSITION
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecipesBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
                val chip = group.findViewById<Chip>(checkedId)
                val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
                mealTypeChip = selectedMealType
                mealTypeChipId = checkedId
            }

            dietTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
                val chip = group.findViewById<Chip>(checkedId)
                val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
                dietTypeChip = selectedDietType
                dietTypeChipId = checkedId
            }

            btnApply.setOnClickListener {
                recipesViewModel.saveMealAndDietType(mealTypeChip, mealTypeChipId, dietTypeChip, dietTypeChipId)
                findNavController().navigate(
                    RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
                )
            }

            recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) {
                mealTypeChip = it.selectedMealType
                dietTypeChip = it.selectedDietType
                updateChips(it.selectedMealTypeId, mealTypeChipGroup)
                updateChips(it.selectedDietTypeId, dietTypeChipGroup)
            }
        }
    }

    private fun updateChips(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", "updateChips: ${e.message.toString()}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}